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
package com.nagopy.android.mypkgs.constants;

import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.manager.CsvManager;

import java.util.List;

public enum ShareType {
    PACKAGE {
        @Override
        public String makeShareString(List<AppData> appList) {
            StringBuilder sb = new StringBuilder();
            for (AppData appData : appList) {
                sb.append(makeShareString(appData));
                sb.append(Constants.LINE_SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public String makeShareString(AppData appData) {
            return appData.packageName;
        }
    }, CSV {
        @Override
        public String makeShareString(List<AppData> appList) {
            StringBuilder sb = new StringBuilder(csv.getHeader());
            sb.append(Constants.LINE_SEPARATOR);
            for (AppData appData : appList) {
                sb.append(makeShareString(appData));
                sb.append(Constants.LINE_SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public String makeShareString(AppData appData) {
            return csv.toCSV(appData);
        }
    };

    public abstract String makeShareString(List<AppData> appList);

    public abstract String makeShareString(AppData appList);

    private static final CsvManager<AppData> csv = new CsvManager<>(AppData.class);
}