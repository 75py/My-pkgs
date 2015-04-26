/*
 * Copyright (C) 2015 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nagopy.android.mypkgs.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nagopy.android.mypkgs.MyApplication;
import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.constants.AppComparator;
import com.nagopy.android.mypkgs.constants.AppInformation;
import com.nagopy.android.mypkgs.constants.Constants;
import com.nagopy.android.mypkgs.constants.FilterType;
import com.nagopy.android.mypkgs.manager.SettingManager;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.model.loader.ApplicationIconLoader;
import com.nagopy.android.mypkgs.util.DebugUtil;
import com.nagopy.android.mypkgs.view.AppLabelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ApplicationListAdapter extends BaseAdapter implements Filterable {

    @Inject
    SettingManager settingManager;
    @Inject
    Context context;

    public final FilterType filterType;
    public List<AppData> originalData = Collections.emptyList();
    public List<AppData> filteredData = Collections.emptyList();
    private LayoutInflater inflater;
    private ItemFilter mFilter;
    private final int COLOR_ENABLED;
    private final int COLOR_DISABLED;
    private List<AppInformation> appInformationList;
    private AppComparator appComparator;

    public ApplicationListAdapter(Context context, FilterType filterType) {
        ((MyApplication) context.getApplicationContext()).getComponent().inject(this);

        this.filterType = filterType;
        this.inflater = LayoutInflater.from(context);
        this.mFilter = new ItemFilter();
        this.COLOR_ENABLED = context.getResources().getColor(R.color.text_color);
        this.COLOR_DISABLED = context.getResources().getColor(R.color.textColorTertiary);

        this.appInformationList = settingManager.getAppInformation();
        this.appComparator = settingManager.getAppComparator();
    }

    public void updateApplicationList(List<AppData> data) {
        Collections.sort(data, appComparator);
        this.originalData = data;
        this.filteredData = data;
    }

    public void removeApplication(AppData appData) {
        this.originalData.remove(appData);
        this.filteredData.remove(appData);
    }

    public void doFilter() {
        mFilter.filter(filterType.name());
    }

    public int getCount() {
        return filteredData.size();
    }

    public AppData getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.app_list_row, null);

            holder = new ViewHolder(convertView);
            holder.title.setTag(new Object()); // 同期のために使用するオブジェクト

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppData appData = filteredData.get(position);
        synchronized (holder.title.getTag()) {
            holder.title.setText(appData.label);
            holder.title.setTag(R.id.tag_package_name, appData.packageName);

            Drawable icon = appData.icon == null ? null : appData.icon.get();
            if (icon == null) {
                DebugUtil.verboseLog("create loader :" + appData.packageName);
                holder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_transparent, 0, 0, 0);
                new ApplicationIconLoader(context, appData.packageName, holder.title).execute(appData);
            } else {
                DebugUtil.verboseLog("use cache onCreateView() :" + appData.packageName);
                holder.title.setIcon(icon);
            }

            StringBuilder sb = new StringBuilder();
            for (AppInformation appInformation : appInformationList) {
                if (appInformation.append(context, sb, appData)) {
                    sb.append(Constants.LINE_SEPARATOR);
                }
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
                String infoString = sb.toString().trim();
                infoString = infoString.replaceAll(Constants.LINE_SEPARATOR + "+", Constants.LINE_SEPARATOR);
                holder.info.setText(infoString);
                holder.info.setVisibility(View.VISIBLE);
            } else {
                holder.info.setVisibility(View.GONE);
            }

            holder.packageName.setText(appData.packageName);

            holder.title.setTextColor(appData.isEnabled ? COLOR_ENABLED : COLOR_DISABLED);
            holder.packageName.setTextColor(appData.isEnabled ? COLOR_ENABLED : COLOR_DISABLED);
            holder.info.setTextColor(appData.isEnabled ? COLOR_ENABLED : COLOR_DISABLED);
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.list_title)
        AppLabelView title;
        @InjectView(R.id.list_package_name)
        TextView packageName;
        @InjectView(R.id.list_info)
        TextView info;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterType filterType = FilterType.valueOf(constraint.toString());
            FilterResults results = new FilterResults();
            final List<AppData> list = originalData;

            int count = list.size();
            final List<AppData> filteredList = new ArrayList<>(count);

            for (AppData appData : list) {
                if (filterType.isTarget(appData)) {
                    filteredList.add(appData);
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<AppData>) results.values;
            notifyDataSetChanged();
        }
    }
}
