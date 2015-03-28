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

public enum FilterType {

    ALL(R.string.title_all) {
        @Override
        public boolean isTarget(AppData appData) {
            return true;
        }
    }, RUNNING(R.string.title_all_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.process != null && !appData.process.isEmpty();
        }
    }, SYSTEM(R.string.title_system) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isSystem;
        }
    }, SYSTEM_ACTIVE_ADMIN(R.string.title_system_active_admin) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && (appData.isThisASystemPackage || appData.hasActiveAdmins);
        }
    }, SYSTEM_INACTIVE_ADMIN(R.string.title_system_inactive_admin) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM.isTarget(appData) && !SYSTEM_ACTIVE_ADMIN.isTarget(appData);
        }
    }, SYSTEM_INACTIVE_ADMIN_RUNNING(R.string.title_system_inactive_admin_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return SYSTEM_INACTIVE_ADMIN.isTarget(appData) && RUNNING.isTarget(appData);
        }
    }, DISABLED(R.string.title_disabled) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isEnabled;
        }
    }, DEFAULT(R.string.title_default) {
        @Override
        public boolean isTarget(AppData appData) {
            return appData.isDefaultApp;
        }
    }, USER(R.string.title_user) {
        @Override
        public boolean isTarget(AppData appData) {
            return !appData.isSystem;
        }
    }, USER_RUNNING(R.string.title_user_running) {
        @Override
        public boolean isTarget(AppData appData) {
            return USER.isTarget(appData) && RUNNING.isTarget(appData);
        }
    };

    public final int titleId;

    private FilterType(int titleId) {
        this.titleId = titleId;
    }

    public static FilterType indexOf(int index) {
        return values()[index];
    }

    public abstract boolean isTarget(AppData appData);
}
