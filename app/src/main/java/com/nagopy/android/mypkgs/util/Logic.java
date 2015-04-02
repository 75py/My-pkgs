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
package com.nagopy.android.mypkgs.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.widget.ListView;
import android.widget.TextView;

import com.nagopy.android.mypkgs.AppData;
import com.nagopy.android.mypkgs.Constants;
import com.nagopy.android.mypkgs.FilterType;
import com.nagopy.android.mypkgs.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Logic {

    private static final Map<Integer, String> RUNNING_STATUS_MAP;

    static {
        Map<Integer, String> runningStatusMap = new HashMap<>();
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND, "Background");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND, "Foreground");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE, "Perceptible");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE, "Service");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE, "Visible");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY, "Empty");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE, "Gone");
        }
        RUNNING_STATUS_MAP = Collections.unmodifiableMap(runningStatusMap);
    }

    private Logic() {
    }

    public static boolean canLaunchImplicitIntent(@NonNull Context context, @NonNull String action) {
        Intent intent = new Intent(action);
        return canLaunchImplicitIntent(context, intent);
    }

    public static boolean canLaunchImplicitIntent(@NonNull Context context, @NonNull Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
    }

    public static void sendIntent(@NonNull Activity activity, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(Constants.MINE_TYPE_TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        activity.startActivity(intent);
    }

    public static void sendIntent(@NonNull Activity activity, String text) {
        sendIntent(activity, null, text);
    }


    public static String makeSearchQuery(@NonNull AppData appData, @NonNull Context context) {
        return appData.label + '+' + appData.packageName;
//        return context.getString(R.string.keyword_disable) + '+' + appData.label + '+' + appData.packageName;
    }

    public static String makeSearchUrl(AppData appData, Context context) {
        return "http://www.google.com/searchIntent?q=" + makeSearchQuery(appData, context);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> List<T> getCheckedItemList(@NonNull ListView listView) {
        List<T> checkedItemList = new ArrayList<>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                checkedItemList.add((T) listView.getItemAtPosition(checkedItemPositions.keyAt(i)));
            }
        }
        return checkedItemList;
    }

    /**
     * ステータスのintをもとに文字列に変換
     *
     * @param status RunningProcessImportance
     * @return 文字列（Foregroundとか）
     */
    public static String getStatusText(@Constants.RunningProcessImportance int status) {
        return RUNNING_STATUS_MAP.get(status);
    }

    /**
     * ランチャーで表示するアイコンのサイズを取得する.
     *
     * @param context Context
     * @return ランチャーのアイコンサイズ
     */
    public static int getIconSize(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getLauncherLargeIconSize();
        } else {
            return (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
        }
    }

    /**
     * ApplicationInfoを取得する.
     *
     * @param packageManager PackageManager
     * @param packageName    パッケージ名
     * @return ApplicationInfo.<br>見つからない場合はnull
     */
    public static ApplicationInfo getApplicationInfo(PackageManager packageManager, String packageName) {
        List<ApplicationInfo> installedAppList = packageManager.getInstalledApplications(getRetrieveFlags());
        for (ApplicationInfo info : installedAppList) {
            if (info.packageName.equals(packageName)) {
                return info;
            }
        }
        return null; // NOT_FOUND
    }

    /**
     * {@link android.content.pm.PackageManager#getInstalledApplications(int)}の引数に使う値を返す.<br>
     * 以下のクラスを参照。<br>
     * /packages/apps/Settings/src/com/android/settings/applications/ApplicationsState.java
     */
    public static int getRetrieveFlags() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 4.1以下
            return PackageManager.GET_UNINSTALLED_PACKAGES |
                    PackageManager.GET_DISABLED_COMPONENTS;
        }

        // 4.2以上
        // > Only the owner can see all apps.
        // とのことなので、IDが0（＝オーナー）は全部見られる、的なフラグ設定らしい
        MethodReflectWrapper myUserIdMethod = new MethodReflectWrapper(UserHandle.class, "myUserId");
        int myUserId = (int) myUserIdMethod.invoke(null);
        if (myUserId == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // 4.3以上
                return PackageManager.GET_UNINSTALLED_PACKAGES |
                        PackageManager.GET_DISABLED_COMPONENTS |
                        PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS;
            } else {
                // 4.2
                return PackageManager.GET_UNINSTALLED_PACKAGES |
                        PackageManager.GET_DISABLED_COMPONENTS;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // 4.3以上
                return PackageManager.GET_DISABLED_COMPONENTS |
                        PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS;
            } else {
                // 4.2
                return PackageManager.GET_DISABLED_COMPONENTS;
            }
        }
    }

    /**
     * TextViewの上下左右に画像を配置する.
     *
     * @param textView TextView
     * @param resId    リソースID
     */
    public static void setIcon(final TextView textView, final int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0);
        }
    }

    /**
     * TextViewの左側（START側）に画像を表示する。
     *
     * @param textView     対象View
     * @param drawable     画像
     * @param drawableSize 画像サイズ
     */
    public static void setIcon(TextView textView, Drawable drawable, int drawableSize) {
        drawable.setBounds(0, 0, drawableSize, drawableSize);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setCompoundDrawables(drawable, null, null, null);
        } else {
            textView.setCompoundDrawablesRelative(drawable, null, null, null);
        }
        drawable.setCallback(null);
    }

    public static List<FilterType> getFilters(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean advanced = sp.getBoolean(context.getString(R.string.pref_key_enabled_advanced), false);
        if (!advanced) {
            return FilterType.DEFAULT_FILTERS;
        }

        Set<String> values = sp.getStringSet(context.getString(R.string.pref_key_app_categories_advanced), Collections.<String>emptySet());
        if (values.isEmpty()) {
            return FilterType.DEFAULT_FILTERS;
        }
        List<FilterType> list = new ArrayList<>();
        for (String value : values) {
            list.add(FilterType.valueOf(value));
        }
        Collections.sort(list, FilterType.COMPARATOR);
        return list;
    }

    public static <T> Set<T> toSet(T[] array) {
        Set<T> set = new LinkedHashSet<>(array.length);
        Collections.addAll(set, array);
        return set;
    }
}
