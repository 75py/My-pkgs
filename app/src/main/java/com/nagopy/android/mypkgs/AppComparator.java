package com.nagopy.android.mypkgs;

import com.nagopy.android.mypkgs.preference.Selectable;

import java.util.Comparator;

public enum AppComparator implements Comparator<AppData>, Selectable {

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
    }, UPDATE_DATE_DESC(R.string.pref_title_sort_update_time_desc, R.string.pref_summary_sort_update_time_desc) {
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

    AppComparator(int titleResourceId, int summaryResourceId) {
        this.titleResourceId = titleResourceId;
        this.summaryResourceId = summaryResourceId;
    }

    public int getTitleResourceId() {
        return titleResourceId;
    }

    public int getSummaryResourceId() {
        return summaryResourceId;
    }

    public String getName() {
        return name();
    }
}
