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

import com.nagopy.android.mypkgs.preference.Selectable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum FilterType implements Selectable {

    ALL(1, R.string.title_all, R.string.summary_all) {
        @Override
        public boolean isTarget(AppData appData) {
            return true;
        }
    }, RUNNING(2, R.string.title_all_running, R.string.summary_all_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.process != null && !appData.process.isEmpty();
        }
    }, SYSTEM(3, R.string.title_system, R.string.summary_system) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isSystem;
        }
    }, SYSTEM_RUNNING(4, R.string.title_system_running, R.string.summary_system_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, SYSTEM_UNDISABLABLE(5, R.string.title_system_undisablable, R.string.summary_system_undisablable) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && (appData.isThisASystemPackage || appData.hasActiveAdmins);
        }
    }, SYSTEM_UNDISABLABLE_RUNNING(6, R.string.title_system_undisablable_running, R.string.summary_system_undisablable_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM_UNDISABLABLE.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, SYSTEM_DISABLABLE(7, R.string.title_system_disablable, R.string.summary_system_disablable) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && !SYSTEM_UNDISABLABLE.isTarget(appData);
        }
    }, SYSTEM_DISABLABLE_RUNNING(8, R.string.title_system_disablable_running, R.string.summary_system_disablable_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM_DISABLABLE.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, DISABLED(9, R.string.title_disabled, R.string.summary_disabled) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isEnabled;
        }
    }, DEFAULT(10, R.string.title_default, R.string.summary_default) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isDefaultApp;
        }
    }, USER(11, R.string.title_user, R.string.summary_user) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isSystem;
        }
    }, USER_RUNNING(12, R.string.title_user_running, R.string.summary_user_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return USER.isTarget(appData) && RUNNING.isTarget(appData);
        }
    };

    public static final List<FilterType> DEFAULT_FILTERS
            = Collections.unmodifiableList(Arrays.asList(FilterType.ALL, FilterType.SYSTEM, FilterType.USER, FilterType.DISABLED));
    public static final String DEFAULT_VALUE;

    static {
        StringBuilder sb = new StringBuilder();
        for (FilterType filterType : DEFAULT_FILTERS) {
            sb.append(filterType.name()).append(',');
        }
        sb.setLength(sb.length() - 1);
        DEFAULT_VALUE = sb.toString();
    }

    public final int titleResourceId;
    public final int summaryResourceId;
    public final int priority;

    FilterType(int priority, int titleResourceId, int summaryResourceId) {
        this.priority = priority;
        this.titleResourceId = titleResourceId;
        this.summaryResourceId = summaryResourceId;
    }

    public abstract boolean isTarget(AppData appData);

    public static final Comparator<FilterType> COMPARATOR = new Comparator<FilterType>() {
        @Override
        public int compare(FilterType filterType, FilterType filterType2) {
            return filterType.priority - filterType2.priority;
        }
    };


    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getTitleResourceId() {
        return titleResourceId;
    }

    @Override
    public int getSummaryResourceId() {
        return summaryResourceId;
    }
}
