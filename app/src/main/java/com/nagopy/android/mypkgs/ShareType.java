package com.nagopy.android.mypkgs;

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
            StringBuilder sb = new StringBuilder("\"pkg\",\"label\"");
            sb.append(Constants.LINE_SEPARATOR);
            for (AppData appData : appList) {
                sb.append(makeShareString(appData));
                sb.append(Constants.LINE_SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public String makeShareString(AppData appData) {
            StringBuilder sb = new StringBuilder();
            sb.append('"');
            sb.append(appData.packageName);
            sb.append("\",\"");
            sb.append(appData.label);
            sb.append('"');
            return sb.toString();
        }
    };

    public abstract String makeShareString(List<AppData> appList);

    public abstract String makeShareString(AppData appList);
}
