package com.nagopy.android.mypkgs;

import android.support.test.uiautomator.Configurator;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.nagopy.android.mypkgs.uiautomator.UiAutomatorUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.fail;

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
