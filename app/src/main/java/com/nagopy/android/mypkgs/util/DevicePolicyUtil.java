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

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class DevicePolicyUtil {

    private DevicePolicyUtil() {
    }

    private static volatile WeakReference<DevicePolicyManagerWrapper> devicePolicyManagerWrapper;

    public static boolean isThisASystemPackage(Context context, PackageInfo packageInfo) {
        if (devicePolicyManagerWrapper == null) {
            devicePolicyManagerWrapper = new WeakReference<>(new DevicePolicyManagerWrapper(context));
        }
        DevicePolicyManagerWrapper wrapper = devicePolicyManagerWrapper.get();
        if (wrapper == null) {
            wrapper = new DevicePolicyManagerWrapper(context);
            devicePolicyManagerWrapper = new WeakReference<>(wrapper);
        }
        return wrapper.isThisASystemPackage(packageInfo);
    }

    public static boolean packageHasActiveAdmins(Context context, String packageName) {
        if (devicePolicyManagerWrapper == null) {
            devicePolicyManagerWrapper = new WeakReference<>(new DevicePolicyManagerWrapper(context));
        }
        DevicePolicyManagerWrapper wrapper = devicePolicyManagerWrapper.get();
        if (wrapper == null) {
            wrapper = new DevicePolicyManagerWrapper(context);
            devicePolicyManagerWrapper = new WeakReference<>(wrapper);
        }
        return wrapper.packageHasActiveAdmins(packageName);
    }

    public static class DevicePolicyManagerWrapper {

        private DevicePolicyManager devicePolicyManager;
        /**
         * システムのPackageInfo
         */
        private PackageInfo mSystemPackageInfo;
        private boolean enableSystemPackageInfo;

        private Method packageHasActiveAdmins;
        private boolean enablePackageHasActiveAdminsMethod;

        @SuppressLint("PackageManagerGetSignatures")
        public DevicePolicyManagerWrapper(Context context) {
            devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            try {
                packageHasActiveAdmins = DevicePolicyManager.class.getDeclaredMethod("packageHasActiveAdmins", String.class);
                enablePackageHasActiveAdminsMethod = true;
            } catch (NoSuchMethodException e) {
                DebugUtil.errorLog("リフレクション失敗:" + e.getMessage());
                enablePackageHasActiveAdminsMethod = false;
            }

            try {
                mSystemPackageInfo = context.getPackageManager().getPackageInfo("android", PackageManager.GET_SIGNATURES);
                enableSystemPackageInfo = true;
            } catch (PackageManager.NameNotFoundException e) {
                DebugUtil.errorLog("システムのシグネチャ取得に失敗:" + e.getMessage());
                enableSystemPackageInfo = false;
            }
        }

        /**
         * {@link android.app.admin.DevicePolicyManager}のpackageHasActiveAdminsメソッドを実行する
         *
         * @param packageName パッケージ名
         * @return packageHasActiveAdminsの結果を返す。<br>
         * エラーがあった場合はfalseを返す。
         */
        public boolean packageHasActiveAdmins(String packageName) {
            try {
                return enablePackageHasActiveAdminsMethod &&
                        (boolean) packageHasActiveAdmins.invoke(devicePolicyManager, packageName);
            } catch (IllegalAccessException e) {
                DebugUtil.errorLog("実行失敗:" + e.getMessage());
            } catch (InvocationTargetException e) {
                DebugUtil.errorLog("実行失敗:" + e.getMessage());
            }
            return false;
        }

        /**
         * {@link android.app.admin.DevicePolicyManager}のisThisASystemPackageメソッドと同じ内容.<br>
         * 4.4以下で使用。
         *
         * @param packageInfo 判定したいpackageinfo
         * @return isThisASystemPackageの結果をそのまま返す。<br>
         * エラーがあった場合はfalseを返す。
         */
        protected boolean isThisASystemPackage(PackageInfo packageInfo) {
            return enableSystemPackageInfo &&
                    (packageInfo != null && packageInfo.signatures != null && mSystemPackageInfo != null && mSystemPackageInfo.signatures[0]
                            .equals(packageInfo.signatures[0]));
        }
    }
}
