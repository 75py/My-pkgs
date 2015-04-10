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
import android.support.annotation.NonNull;

import com.nagopy.android.mypkgs.preference.Selectable;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public enum AppInformation implements Selectable {

    PROCESS(R.string.title_info_process, R.string.summary_info_process) {
        @Override
        public void append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (!appData.process.isEmpty()) {
                for (String str : appData.process) {
                    sb.append(str);
                    sb.append(Constants.LINE_SEPARATOR);
                }
                sb.setLength(sb.length() - 1);
            }
        }
    }, NOT_INSTALLED(R.string.title_info_not_installed, R.string.summary_info_not_installed) {
        @Override
        public void append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (!appData.isInstalled) {
                sb.append(context.getString(R.string.not_installed));
            }
        }
    }, FIRST_INSTALL_TIME(R.string.title_info_first_install_time, R.string.summary_info_first_install_time) {
        @Override
        public void append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
            sb.append(context.getString(R.string.format_first_install_time, format.format(new Date(appData.firstInstallTime))));
        }
    }, LAST_UPDATE_TIME(R.string.title_info_last_update_time, R.string.summary_info_last_update_time) {
        @Override
        public void append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
            sb.append(context.getString(R.string.format_last_update_time, format.format(new Date(appData.lastUpdateTime))));
        }
    };

    public final int titleResourceId;
    public final int summaryResourceId;

    AppInformation(int titleResourceId, int summaryResourceId) {
        this.titleResourceId = titleResourceId;
        this.summaryResourceId = summaryResourceId;
    }

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

    public abstract void append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData);


    public static final List<AppInformation> DEFAULT_LIST
            = Collections.unmodifiableList(Collections.singletonList(AppInformation.PROCESS));
    public static final String DEFAULT_VALUE;

    static {
        StringBuilder sb = new StringBuilder();
        for (AppInformation info : DEFAULT_LIST) {
            sb.append(info.name()).append(',');
        }
        sb.setLength(sb.length() - 1);
        DEFAULT_VALUE = sb.toString();
    }
}
