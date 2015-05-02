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
package com.nagopy.android.mypkgs.model.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.model.AppData;
import com.nagopy.android.mypkgs.util.DebugUtil;
import com.nagopy.android.mypkgs.util.Logic;
import com.nagopy.android.mypkgs.view.AppLabelView;

import java.lang.ref.WeakReference;

/**
 * アイコン読み込みを非同期で行うためのクラス.
 */
public class ApplicationIconLoader extends AsyncTask<AppData, Void, AppData> {
    private final Context context;
    private final WeakReference<AppLabelView> textViewWeakReference;
    private static final int RETRIEVE_FLAGS = Logic.getRetrieveFlags();
    private final String packageName;

    /**
     * コンストラクタ.
     *
     * @param context     コンテキスト
     * @param packageName 対象パッケージ名
     * @param textView    表示対象のView
     */
    public ApplicationIconLoader(Context context, String packageName, AppLabelView textView) {
        this.context = context.getApplicationContext();
        this.packageName = packageName;
        this.textViewWeakReference = new WeakReference<>(textView);
    }

    /**
     * 読み込み処理.<br>
     * キャッシュが存在しない場合は読み込みを行う。
     *
     * @param params 対象アプリ情報（可変長だが一つ目のみ使用する）
     * @return パラメータで渡されたものと同じインスタンス
     */
    @Override
    protected AppData doInBackground(AppData... params) {
        TextView textView = textViewWeakReference.get();
        if (textView == null) {
            return null;
        }
        AppData appData = params[0];
        PackageManager packageManager = context.getPackageManager();
        DebugUtil.verboseLog("doInBackground :" + appData.packageName);

        if (appData.icon != null && appData.icon.get() != null) {
            DebugUtil.verboseLog("use cache icon :" + appData.packageName);
            return appData;
        }

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(appData.packageName, RETRIEVE_FLAGS);
            Drawable icon;
            if (applicationInfo.icon == 0x0) {
                DebugUtil.verboseLog(appData.packageName + ", icon=0x0");
                icon = getDefaultIcon(context);
            } else {
                icon = applicationInfo.loadIcon(packageManager);
            }
            appData.icon = new WeakReference<>(icon);
            DebugUtil.verboseLog("load icon complete :" + appData.packageName);
            return appData;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("ApplicationInfoの取得に失敗 packageName=" + appData.packageName, e);
        }
    }

    /**
     * 後処理.<br>
     * TextViewがこのクラスが変更するパッケージのものの場合、アイコンを反映する
     * （ListViewではViewが使いまわされるため、アイコン読み込み後には別アプリの表示用Viewに変わっている場合がある）。<br>
     * TextViewの変更時は、対象TextViewの操作をロックする。
     *
     * @param appData アプリ情報
     */
    @Override
    protected void onPostExecute(AppData appData) {
        AppLabelView labelView = textViewWeakReference.get();
        if (labelView == null) {
            return;
        }
        Drawable icon = appData.icon.get();
        if (icon == null) {
            return;
        }
        synchronized (labelView.getTag()) {
            if (packageName.equals(labelView.getTag(R.id.tag_package_name))) {
                DebugUtil.verboseLog("onPostExecute updateIcon :" + packageName);
                labelView.setIcon(icon);
            }
        }
    }

    private static Drawable defaultIcon = null;

    private synchronized static Drawable getDefaultIcon(Context context) {
        if (defaultIcon == null) {
            defaultIcon = ResourcesCompat.getDrawable(context.getResources(), android.R.drawable.sym_def_app_icon, null);
        }
        return defaultIcon;
    }
}