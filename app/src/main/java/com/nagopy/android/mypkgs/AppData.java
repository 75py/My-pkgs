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

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.nagopy.android.mypkgs.util.DebugUtil;
import com.nagopy.android.mypkgs.util.DevicePolicyUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AppData {
    /**
     * ラベル名
     */
    public String label;

    /**
     * パッケージ名
     */
    public String packageName;

    /**
     * 有効かどうか
     */
    public boolean isEnabled;

    /**
     * システムアプリかどうか
     */
    public boolean isSystem;

    /**
     * アプリアイコン
     */
    public WeakReference<Drawable> icon;

    /**
     * 実行中のプロセスの情報
     */
    public List<String> process;

    /**
     * 実行ユーザーでインストールされているか.
     * API17以上で使用するフラグ。
     */
    public boolean isInstalled = true;

    public boolean isThisASystemPackage;
    public boolean hasActiveAdmins;

    public boolean isDefaultApp;

    /**
     * 初回インストール日時（ミリ秒）
     */
    public long firstInstallTime;
    /**
     * 最終更新日時（ミリ秒）
     */
    public long lastUpdateTime;

    public AppData(Context context, ApplicationInfo applicationInfo) {
        load(context, applicationInfo);
    }

    public void load(Context context, ApplicationInfo applicationInfo) {
        PackageManager packageManager = context.getPackageManager();
        this.label = applicationInfo.loadLabel(packageManager).toString();
        this.packageName = applicationInfo.packageName;
        this.isEnabled = applicationInfo.enabled;
        this.isSystem = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
                || (applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
        try {
            // ここはしょうがないので警告を抑制
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_DISABLED_COMPONENTS | PackageManager.GET_UNINSTALLED_PACKAGES
                            | PackageManager.GET_SIGNATURES);
            this.isThisASystemPackage = DevicePolicyUtil.isThisASystemPackage(context, packageInfo);

            firstInstallTime = packageInfo.firstInstallTime;
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            DebugUtil.errorLog("Package not found:" + packageName);
        }
        this.hasActiveAdmins = DevicePolicyUtil.packageHasActiveAdmins(context, applicationInfo.packageName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 4.2以上。4.1以下は無条件でtrue
            this.isInstalled = (applicationInfo.flags & ApplicationInfo.FLAG_INSTALLED) != 0;
        }

        List<IntentFilter> outFilters = new ArrayList<>();
        List<ComponentName> outActivities = new ArrayList<>();
        packageManager.getPreferredActivities(outFilters, outActivities, applicationInfo.packageName);
        isDefaultApp = !outActivities.isEmpty();

        DebugUtil.verboseLog(toString());
    }

    @Override
    public String toString() {
        return packageName
                + ", label=" + label
                + ", enabled=" + isEnabled
                + ", system=" + isSystem
                + ", installed=" + isInstalled
                + ", isThisASystemPackage=" + isThisASystemPackage
                + ", hasActiveAdmins=" + hasActiveAdmins;
    }
}
