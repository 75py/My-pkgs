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

import android.content.Context;
import android.support.annotation.NonNull;

import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.view.preference.Selectable;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * アプリ毎に表示する詳細情報の定義クラス
 */
public enum AppInformation implements Selectable {

    /**
     * プロセス情報
     */
    PROCESS(R.string.title_info_process, R.string.summary_info_process) {
        @Override
        public boolean append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (appData.process == null || appData.process.isEmpty()) {
                return false;
            } else {
                for (String str : appData.process) {
                    sb.append(str);
                    sb.append(Constants.LINE_SEPARATOR);
                }
                sb.setLength(sb.length() - 1);
            }
            return true;
        }
    },
    /**
     * インストール状態
     */
    NOT_INSTALLED(R.string.title_info_not_installed, R.string.summary_info_not_installed) {
        @Override
        public boolean append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (!appData.isInstalled) {
                sb.append(context.getString(R.string.not_installed));
                return true;
            }
            return false;
        }
    },
    /**
     * 初回インストール日時
     */
    FIRST_INSTALL_TIME(R.string.title_info_first_install_time, R.string.summary_info_first_install_time) {
        @Override
        public boolean append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (appData.firstInstallTime < Constants.Y2K) {
                return false;
            }
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
            sb.append(context.getString(R.string.format_first_install_time, format.format(new Date(appData.firstInstallTime))));
            return true;
        }
    },
    /**
     * 最終更新日時
     */
    LAST_UPDATE_TIME(R.string.title_info_last_update_time, R.string.summary_info_last_update_time) {
        @Override
        public boolean append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData) {
            if (appData.lastUpdateTime < Constants.Y2K) {
                return false;
            }
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault());
            sb.append(context.getString(R.string.format_last_update_time, format.format(new Date(appData.lastUpdateTime))));
            return true;
        }
    };

    private final int titleResourceId;
    private final int summaryResourceId;

    /**
     * コンストラクタ
     *
     * @param titleId   設定画面で表示するタイトルの文字列リソースID
     * @param summaryId 設定画面で表示する説明文の文字列リソースID
     */
    AppInformation(int titleId, int summaryId) {
        this.titleResourceId = titleId;
        this.summaryResourceId = summaryId;
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

    /**
     * 定義された情報をStringBuilderに追加する。
     *
     * @param context Context
     * @param sb      StringBuilder
     * @param appData アプリ情報
     * @return 文字列を追加した場合はtrue、追加しなかった場合はfalse
     */
    public abstract boolean append(@NonNull Context context, @NonNull StringBuilder sb, @NonNull AppData appData);

    /**
     * デフォルトで使用する AppInformation のリスト
     */
    public static final List<AppInformation> DEFAULT_LIST
            = Collections.unmodifiableList(Collections.singletonList(AppInformation.PROCESS));
    /**
     * デフォルトで使用する AppInformation のリスト（SharedPreferencesのデフォルト文字列）
     */
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
