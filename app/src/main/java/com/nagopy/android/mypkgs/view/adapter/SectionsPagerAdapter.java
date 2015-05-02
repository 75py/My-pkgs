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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nagopy.android.mypkgs.ApplicationListFragment;
import com.nagopy.android.mypkgs.constants.FilterType;
import com.nagopy.android.mypkgs.util.DebugUtil;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    final Context context;
    final List<FilterType> filterTypeList;
    final Map<FilterType, WeakReference<ApplicationListFragment>> cache = new EnumMap<>(FilterType.class);

    public SectionsPagerAdapter(FragmentManager supportFragmentManager, Context context, List<FilterType> filterTypeList) {
        super(supportFragmentManager);
        this.context = context;
        this.filterTypeList = filterTypeList;
    }

    @Override
    public Fragment getItem(int position) {
        DebugUtil.verboseLog("SectionsPagerAdapter#getItem(" + position + ")");
        FilterType filterType = filterTypeList.get(position);
        WeakReference<ApplicationListFragment> weakReference = cache.get(filterType);
        if (weakReference == null || weakReference.get() == null) {
            DebugUtil.verboseLog("create new instance");
            ApplicationListFragment fragment = ApplicationListFragment.newInstance(filterType);
            cache.put(filterType, new WeakReference<>(fragment));
            return fragment;
        } else {
            DebugUtil.verboseLog("return from cache");
            return weakReference.get();
        }
    }

    @Override
    public int getCount() {
        return filterTypeList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        FilterType filterType = filterTypeList.get(position);
        return context.getString(filterType.titleResourceId);
    }
}
