package com.nagopy.android.mypkgs.util;

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

    private static WeakReference<DevicePolicyManagerWrapper> devicePolicyManagerWrapper;

    public static boolean isThisASystemPackage(Context context, PackageInfo packageInfo) {
        if (devicePolicyManagerWrapper == null) {
            devicePolicyManagerWrapper = new WeakReference<DevicePolicyManagerWrapper>(new DevicePolicyManagerWrapper(context));
        }
        DevicePolicyManagerWrapper wrapper = devicePolicyManagerWrapper.get();
        if (wrapper == null) {
            wrapper = new DevicePolicyManagerWrapper(context);
            devicePolicyManagerWrapper = new WeakReference<DevicePolicyManagerWrapper>(wrapper);
        }
        return wrapper.isThisASystemPackage(packageInfo);
    }

    public static boolean packageHasActiveAdmins(Context context, String packageName) {
        if (devicePolicyManagerWrapper == null) {
            devicePolicyManagerWrapper = new WeakReference<DevicePolicyManagerWrapper>(new DevicePolicyManagerWrapper(context));
        }
        DevicePolicyManagerWrapper wrapper = devicePolicyManagerWrapper.get();
        if (wrapper == null) {
            wrapper = new DevicePolicyManagerWrapper(context);
            devicePolicyManagerWrapper = new WeakReference<DevicePolicyManagerWrapper>(wrapper);
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
            } catch (IllegalAccessException | InvocationTargetException e) {
                DebugUtil.errorLog("実行失敗:" + e.getMessage());
                return false;
            }
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
