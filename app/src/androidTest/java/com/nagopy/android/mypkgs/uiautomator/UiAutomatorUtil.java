package com.nagopy.android.mypkgs.uiautomator;

import android.support.test.uiautomator.Configurator;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.fail;

/**
 * uiautomatorのテスト用ユーティリティクラス.
 */
public class UiAutomatorUtil {

    public static final boolean TAKE_SCREENSHOT = false;

    /**
     * {@link #sleep()}のデフォルト停止時間.
     */
    private static final long WAIT_MS = 251;

    /**
     * コンストラクタ.
     */
    private UiAutomatorUtil() {
    }


    /**
     * スレッドを一時停止する.
     */
    public static void sleep() {
        sleep(WAIT_MS);
    }

    /**
     * スレッドを一時停止する.
     *
     * @param time 停止時間（ミリ秒）
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * 対象アプリが表示されているかを確認する.
     *
     * @param uiDevice    UiDevice
     * @param packageName パッケージ名
     * @return 指定されたパッケージのなんらかの要素が存在すればtrue
     */
    public static boolean isTargetApp(UiDevice uiDevice, String packageName) {
        return uiDevice.findObject(new UiSelector().packageName(packageName)).exists();
    }

    /**
     * スクリーンショットを撮る.
     *
     * @param uiDevice  UiDevice
     * @param outputDir 出力ディレクトリ
     * @param fileName  出力ファイル名
     */
    public static void takeScreenshot(UiDevice uiDevice, File outputDir, String fileName) {
        if (!TAKE_SCREENSHOT) {
            return;
        }

        UiAutomatorUtil.sleep(); // ちょっとWaitしてちゃんとSSに映るようにする

        File ss = new File(outputDir, fileName);
        if (!uiDevice.takeScreenshot(ss)) {
            fail("スクリーンショットの保存に失敗 path=" + ss.getAbsolutePath());
        }
    }

    /**
     * タイムアウト時間の設定等を行う.
     */
    public static void initConfigure() {
        Configurator configurator = Configurator.getInstance();
        configurator.setWaitForSelectorTimeout(WAIT_MS);
    }


    /**
     * 対象アプリが表示されなくなるまでバックキーを押し続ける.
     *
     * @param uiDevice    UiDevice
     * @param packageName 対象アプリのパッケージ名
     */
    public static void finishApp(UiDevice uiDevice, String packageName) {
        while (UiAutomatorUtil.isTargetApp(uiDevice, packageName)) {
            uiDevice.pressBack();
        }
    }

    private static final List<String> ignoreAppPrefixList;

    static {
        List<String> tmpIgnoreAppList = new ArrayList<>();
        tmpIgnoreAppList.add("com.google.android.gms"); // 開発者サービス
        tmpIgnoreAppList.add("jp.co.nttdocomo");
        tmpIgnoreAppList.add("com.nttdocomo");
        tmpIgnoreAppList.add("com.sonyericsson");
        tmpIgnoreAppList.add("com.sonymobile");
        ignoreAppPrefixList = Collections.unmodifiableList(tmpIgnoreAppList);
    }

    public static boolean isIgnorePackage(String packageName) {
        for (String ignorePrefix : ignoreAppPrefixList) {
            if (packageName.startsWith(ignorePrefix)) {
                return true;
            }
        }
        return false;
    }
}
