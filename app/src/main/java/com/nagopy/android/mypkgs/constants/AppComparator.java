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

import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.view.preference.Selectable;

import java.util.Comparator;

/**
 * ソート順の定義クラス
 */
public enum AppComparator implements Comparator<AppData>, Selectable {

    /**
     * デフォルトのソート順。<br />
     * <ol>
     * <li>インストール状態が異なる場合、未インストールを後ろにする。</li>
     * <li>アプリ表示名の昇順に並べる。ただし、アプリ表示名が同一の場合はパッケージ名の昇順で並べる。</li>
     * </ol>
     */
    DEFAULT(R.string.pref_title_sort_default, R.string.pref_summary_sort_default) {
        @Override
        public int compare(AppData lhs, AppData rhs) {
            if (lhs.isInstalled != rhs.isInstalled) {
                // 「未インストール」は最後に
                return lhs.isInstalled ? -1 : 1;
            }

            String label0 = lhs.label;
            String label1 = rhs.label;

            int ret = label0.compareToIgnoreCase(label1);
            // ラベルで並び替え、同じラベルがあったらパッケージ名で
            if (ret == 0) {
                String pkgName0 = lhs.packageName;
                String pkgName1 = rhs.packageName;
                ret = pkgName0.compareToIgnoreCase(pkgName1);
            }
            return ret;
        }
    },
    /**
     * パッケージ名の昇順
     */
    PACKAGE_NAME(R.string.pref_title_sort_package_name, R.string.pref_summary_sort_package_name) {
        @Override
        public int compare(AppData lhs, AppData rhs) {
            if (lhs.isInstalled != rhs.isInstalled) {
                // 「未インストール」は最後に
                return lhs.isInstalled ? -1 : 1;
            }
            return lhs.packageName.compareToIgnoreCase(rhs.packageName);
        }
    },
    /**
     * 最終更新日時の降順
     */
    UPDATE_DATE_DESC(R.string.pref_title_sort_update_time_desc, R.string.pref_summary_sort_update_time_desc) {
        @Override
        public int compare(AppData lhs, AppData rhs) {
            if (lhs.lastUpdateTime != rhs.lastUpdateTime) {
                long l = lhs.lastUpdateTime - rhs.lastUpdateTime;
                return l > 0 ? -1 : 1;
            }
            return DEFAULT.compare(lhs, rhs);
        }
    };

    private final int titleResourceId;
    private final int summaryResourceId;

    /**
     * コンストラクタ
     * @param titleId 設定画面で表示するタイトルの文字列リソースID
     * @param summaryId 設定画面で表示する説明文の文字列リソースID
     */
    AppComparator(int titleId, int summaryId) {
        this.titleResourceId = titleId;
        this.summaryResourceId = summaryId;
    }

    @Override
    public int getTitleResourceId() {
        return titleResourceId;
    }

    @Override
    public int getSummaryResourceId() {
        return summaryResourceId;
    }

    @Override
    public String getName() {
        return name();
    }
}
