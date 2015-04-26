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
package com.nagopy.android.mypkgs.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.nagopy.android.mypkgs.MyApplication;
import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.constants.AppComparator;
import com.nagopy.android.mypkgs.constants.AppInformation;
import com.nagopy.android.mypkgs.constants.FilterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingManager {

    @Inject
    Context context;
    @Inject
    SharedPreferences sp;

    final String KEY_ADVANCED;
    final String KEY_SORT;
    final String KEY_INFORMATION;
    final String KEY_FILTERS;

    @Inject
    public SettingManager(Context context) {
        ((MyApplication) context.getApplicationContext()).getComponent().inject(this);
        KEY_ADVANCED = context.getString(R.string.pref_key_enabled_advanced);
        KEY_SORT = context.getString(R.string.pref_key_sort);
        KEY_INFORMATION = context.getString(R.string.pref_key_app_information);
        KEY_FILTERS = context.getString(R.string.pref_key_filters);
    }

    public AppComparator getAppComparator() {
        boolean advanced = sp.getBoolean(KEY_ADVANCED, false);
        if (!advanced) {
            return AppComparator.DEFAULT;
        }
        String name = sp.getString(KEY_SORT, AppComparator.DEFAULT.name());
        return AppComparator.valueOf(name);
    }

    public List<AppInformation> getAppInformation() {
        boolean advanced = sp.getBoolean(KEY_ADVANCED, false);
        if (!advanced) {
            return AppInformation.DEFAULT_LIST;
        }

        if (!sp.contains(KEY_INFORMATION)) {
            return AppInformation.DEFAULT_LIST;
        }

        String v = sp.getString(KEY_INFORMATION, AppInformation.DEFAULT_VALUE);
        if (v == null || v.isEmpty()) {
            return Collections.emptyList();
        }
        List<AppInformation> list = new ArrayList<>();
        for (String val : v.split(",")) {
            list.add(AppInformation.valueOf(val));
        }
        return list;
    }

    public List<FilterType> getFilters() {
        boolean advanced = sp.getBoolean(KEY_ADVANCED, false);
        if (!advanced) {
            return FilterType.DEFAULT_FILTERS;
        }

        String v = sp.getString(KEY_FILTERS, FilterType.DEFAULT_VALUE);
        if (v == null || v.isEmpty()) {
            return FilterType.DEFAULT_FILTERS;
        }
        List<FilterType> list = new ArrayList<>();
        for (String val : v.split(",")) {
            list.add(FilterType.valueOf(val));
        }
        Collections.sort(list, FilterType.COMPARATOR);
        return list;
    }
}
