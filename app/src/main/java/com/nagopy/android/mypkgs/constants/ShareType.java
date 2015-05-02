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

import com.nagopy.android.mypkgs.manager.CsvManager;
import com.nagopy.android.mypkgs.model.AppData;

import java.util.List;

/**
 * 共有方法の定義クラス
 */
public enum ShareType {
    /**
     * アプリ名共有
     */
    LABEL {
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
            return appData.label;
        }
    },
    /**
     * パッケージ名
     */
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
    },
    /**
     * アプリ名とパッケージ名
     */
    LABEL_AND_PACKAGE {
        @Override
        public String makeShareString(List<AppData> appList) {
            StringBuilder sb = new StringBuilder();
            for (AppData appData : appList) {
                sb.append(makeShareString(appData));
                sb.append(Constants.LINE_SEPARATOR);
                sb.append(Constants.LINE_SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public String makeShareString(AppData appData) {
            return appData.label + Constants.LINE_SEPARATOR + appData.packageName;
        }
    },
    /**
     * CSV形式
     */
    CSV {
        @Override
        public String makeShareString(List<AppData> appList) {
            StringBuilder sb = new StringBuilder(CSV_MANAGER.getHeader());
            sb.append(Constants.LINE_SEPARATOR);
            for (AppData appData : appList) {
                sb.append(makeShareString(appData));
                sb.append(Constants.LINE_SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public String makeShareString(AppData appData) {
            return CSV_MANAGER.toCSV(appData);
        }
    };

    /**
     * 共有用の文字列を作成する
     *
     * @param appList 共有対象
     * @return 共有用の文字列
     */
    public abstract String makeShareString(List<AppData> appList);

    /**
     * 共有用の文字列を作成する
     *
     * @param appData 共有対象
     * @return 共有用の文字列
     */
    public abstract String makeShareString(AppData appData);

    private static final CsvManager<AppData> CSV_MANAGER = new CsvManager<>(AppData.class);
}
