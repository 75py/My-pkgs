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
package com.nagopy.android.mypkgs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.nagopy.android.mypkgs.constants.FilterType;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.model.event.ListItemSelectedEvent;
import com.nagopy.android.mypkgs.model.event.MultiChoiceModeListenerEvent;
import com.nagopy.android.mypkgs.model.loader.ApplicationLoader;
import com.nagopy.android.mypkgs.util.EventBus;
import com.nagopy.android.mypkgs.view.adapter.ApplicationListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationListFragment extends ListFragment implements AbsListView.MultiChoiceModeListener {

    private static final String ARG_FILTER_TYPE = "filter_type";
    private final Handler handler = new Handler();

    public static ApplicationListFragment newInstance(FilterType filterType) {
        ApplicationListFragment fragment = new ApplicationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER_TYPE, filterType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity().getApplicationContext(), R.layout.fragment_application_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);

        Context context = getActivity().getApplicationContext();
        FilterType filterType = FilterType.valueOf(getArguments().getString(ARG_FILTER_TYPE));
        final ApplicationListAdapter applicationListAdapter = new ApplicationListAdapter(context, filterType);
        setListAdapter(applicationListAdapter);

        loadApplications();
    }

    public void loadApplications() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                Context context = getActivity().getApplicationContext();
                final ApplicationListAdapter applicationListAdapter = (ApplicationListAdapter) getListAdapter();

                final ApplicationLoader applicationLoader = ApplicationLoader.getInstance();
                applicationLoader.loadApplicationList(context, new ApplicationLoader.ApplicationLoadListener() {
                    @Override
                    public void onLoaded(final List<AppData> appList) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                applicationListAdapter.updateApplicationList(appList);
                                applicationListAdapter.doFilter();
                                hideProgressBar();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        AppData appData = (AppData) getListAdapter().getItem(position);
        EventBus.getDefault().post(new ListItemSelectedEvent(appData));
    }

    public ApplicationListFragment showProgressBar() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            getListView().setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public void hideProgressBar() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            getListView().setVisibility(View.VISIBLE);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressBar();
                }
            }, 3000);
        }
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
        MultiChoiceModeListenerEvent.ItemCheckedStateChangedEvent event = new MultiChoiceModeListenerEvent.ItemCheckedStateChangedEvent.Builder()
                .setActionMode(actionMode)
                .setPosition(i)
                .setId(l)
                .setChecked(b)
                .setCheckedItemCount(getListView().getCheckedItemCount())
                .build();
        EventBus.getDefault().post(event);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        EventBus.getDefault().post(new MultiChoiceModeListenerEvent.CreateActionModeEvent(actionMode, menu));
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        List<AppData> checkedItemList = getCheckedItemList(getListView());
        EventBus.getDefault().post(new MultiChoiceModeListenerEvent.ActionItemClickedEvent(actionMode, menuItem, checkedItemList));
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        EventBus.getDefault().post(new MultiChoiceModeListenerEvent.DestroyActionModeEvent(actionMode));
    }


    @SuppressWarnings("unchecked")
    @NonNull
    <T> List<T> getCheckedItemList(@NonNull ListView listView) {
        List<T> checkedItemList = new ArrayList<>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                checkedItemList.add((T) listView.getItemAtPosition(checkedItemPositions.keyAt(i)));
            }
        }
        return checkedItemList;
    }
}
