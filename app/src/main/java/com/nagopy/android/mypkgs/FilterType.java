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

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum FilterType {

    ALL(1, R.string.title_all) {
        @Override
        public boolean isTarget(AppData appData) {
            return true;
        }
    }, RUNNING(2, R.string.title_all_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.process != null && !appData.process.isEmpty();
        }
    }, SYSTEM(3, R.string.title_system) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isSystem;
        }
    }, SYSTEM_RUNNING(4, R.string.title_system_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, SYSTEM_UNDISABLABLE(5, R.string.title_system_undisablable) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && (appData.isThisASystemPackage || appData.hasActiveAdmins);
        }
    }, SYSTEM_UNDISABLABLE_RUNNING(6, R.string.title_system_undisablable_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM_UNDISABLABLE.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, SYSTEM_DISABLABLE(7, R.string.title_system_disablable) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && !SYSTEM_UNDISABLABLE.isTarget(appData);
        }
    }, SYSTEM_DISABLABLE_RUNNING(8, R.string.title_system_disablable_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM_DISABLABLE.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, DISABLED(9, R.string.title_disabled) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isEnabled;
        }
    }, DEFAULT(10, R.string.title_default) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isDefaultApp;
        }
    }, USER(11, R.string.title_user) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isSystem;
        }
    }, USER_RUNNING(12, R.string.title_user_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return USER.isTarget(appData) && RUNNING.isTarget(appData);
        }
    };

    public static final List<FilterType> DEFAULT_FILTERS
            = Collections.unmodifiableList(Arrays.asList(FilterType.ALL, FilterType.SYSTEM, FilterType.USER, FilterType.DISABLED));
    public static final Set<String> DEFAULT_FILTERS_NAME_SET;

    static {
        Set<String> defaultNameSet = new HashSet<>();
        for (FilterType filterType : DEFAULT_FILTERS) {
            defaultNameSet.add(filterType.name());
        }
        DEFAULT_FILTERS_NAME_SET = Collections.unmodifiableSet(defaultNameSet);
    }

    public final int titleId;
    public final int priority;

    private FilterType(int priority, int titleId) {
        this.priority = priority;
        this.titleId = titleId;
    }

    public abstract boolean isTarget(AppData appData);

    public static final Comparator<FilterType> COMPARATOR = new Comparator<FilterType>() {
        @Override
        public int compare(FilterType filterType, FilterType filterType2) {
            return filterType.priority - filterType2.priority;
        }
    };
}
