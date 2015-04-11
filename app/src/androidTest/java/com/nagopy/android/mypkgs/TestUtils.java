package com.nagopy.android.mypkgs;

import android.util.Log;

/**
 * テスト用ユーティリティクラス.
 */
public class TestUtils {


    public static final String PACKAGE_NAME = "com.nagopy.android.mypkgs";
    public static final String TAG = "nagopy_uiautomator";

    /**
     * コンストラクタ.
     */
    private TestUtils() {
    }

    public static void debugLog(Object obj) {
        Log.d(TAG, obj == null ? "null" : obj.toString());
    }

    public static void infoLog(Object obj) {
        Log.i(TAG, obj == null ? "null" : obj.toString());
    }

    public static void warningLog(Object obj) {
        Log.w(TAG, obj == null ? "null" : obj.toString());
    }

    public static void errorLog(Throwable t) {
        Log.e(TAG, t.getMessage());
    }

}
