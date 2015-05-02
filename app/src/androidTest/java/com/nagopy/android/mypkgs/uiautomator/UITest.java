package com.nagopy.android.mypkgs.uiautomator;

import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.nagopy.android.mypkgs.BuildConfig;
import com.nagopy.android.mypkgs.MainActivity;
import com.nagopy.android.mypkgs.R;
import com.viewpagerindicator.TabPageIndicator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class UITest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private UiDevice uiDevice;

    public UITest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        mActivity = getActivity();

        UiAutomatorUtil.initConfigure();
        uiDevice = UiDevice.getInstance(getInstrumentation());
    }

    @After
    public void tearDown() throws Exception {
        uiDevice.pressBack();
        UiAutomatorUtil.finishApp(uiDevice, BuildConfig.APPLICATION_ID);
        super.tearDown();
    }

    @Test
    public void allApps() throws Exception {
        validateCurrentPageTitle(mActivity.getString(R.string.title_all));

        UiScrollable listView = new UiScrollable(new UiSelector().className(ListView.class).scrollable(true));
        listView.scrollToBeginning(10);

        // リストの一つ一つをチェックする前に、現状のスクリーンショットを撮っておく
        UiAutomatorUtil.takeScreenshot(uiDevice, mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "LIST.jpg");

        List<String> errorAppList = validateAllItems(listView, new InstalledAppDetailValidator() {
                    @Override
                    public boolean validate(String packageName) throws UiObjectNotFoundException {
                        // とりあえず落ちなければいいので何でもOK
                        return true;
                    }
                }
        );
    }

    /**
     * ViewPagerを左スワイプ（＝右から左へスワイプ＝右の画面へ移動）する.
     *
     * @return {@link android.support.test.uiautomator.UiScrollable#swipeLeft(int)}の戻り値
     * @throws UiObjectNotFoundException
     */
    private boolean swipeLeft() throws UiObjectNotFoundException {
        return new UiScrollable((new UiSelector().className(ViewPager.class))).swipeLeft(10);
    }

    /**
     * ViewPagerを右スワイプ（＝左から右へスワイプ＝右の画面へ移動）する.
     *
     * @return {@link android.support.test.uiautomator.UiScrollable#swipeLeft(int)}の戻り値
     * @throws UiObjectNotFoundException
     */
    private boolean swipeRight() throws UiObjectNotFoundException {
        return new UiScrollable((new UiSelector().className(ViewPager.class))).swipeRight(10);
    }

    private interface InstalledAppDetailValidator {
        /**
         * 検証を行う.
         *
         * @param packageName 対象パッケージ名（警告がある場合のメッセージ出力に使用）
         * @return 検証エラーがなければtrue、エラーがあればfalse.<br>
         * 警告がある場合はtrueを返し、標準出力にメッセージを出力する。
         * @throws UiObjectNotFoundException
         */
        boolean validate(String packageName) throws UiObjectNotFoundException;
    }

    /**
     * ListViewの各要素を検証する.
     *
     * @param listView  ListViewを取得した UiScrollable
     * @param validator InstalledAppDetailValidatorを実装したクラス
     * @return エラーがあったアプリのパッケージ名のリスト
     * @throws UiObjectNotFoundException
     */
    private List<String> validateAllItems(UiScrollable listView, InstalledAppDetailValidator validator) throws UiObjectNotFoundException {
        try {
            listView.setSwipeDeadZonePercentage(0.2); // デッドゾーンを若干大きめにして、スクロール幅を縮小する
        } catch (NoSuchMethodError e) {
            // API16以上のはずだが、SH-02Eでエラーになる。テストに大きく支障があるわけではないため無視する。
            UiAutomatorUtil.errorLog(e);
        }

        List<String> errorAppList = new ArrayList<>();
        Set<String> testedPackages = new HashSet<>();
        boolean hasNext;
        do { // ListViewのスクロールをループするdo-whileループ
            hasNext = false;
            for (int i = 0; ; i++) { // ListViewに今表示されている要素を一つ一つ見ていくforループ
                try {
                    UiObject clickable = uiDevice.findObject(
                            new UiSelector()
                                    .resourceId(mActivity.getResources().getResourceName(R.id.list_parent))
                                    .description("listItem")
                                    .index(i)
                    );
                    if (!clickable.exists()) {
                        // i番目の要素が見つからない場合
                        break;
                    }

                    UiObject titleTextView;
                    String label;
                    try {
                        titleTextView =
                                clickable.getChild(
                                        new UiSelector()
                                                .resourceId(mActivity.getResources().getResourceName(R.id.list_title))
                                                .className(TextView.class)
                                                .description("application name")
                                );
                        label = titleTextView.getText();
                    } catch (UiObjectNotFoundException e) {
                        // タイトルが取得できない場合
                        UiAutomatorUtil.debugLog("continue (title not found)");
                        continue;
                    }
                    UiObject packageNameTextView =
                            clickable.getChild(
                                    new UiSelector()
                                            .resourceId(mActivity.getResources().getResourceName(R.id.list_package_name))
                                            .className(TextView.class)
                                            .description("package name")
                            );
                    String packageName = packageNameTextView.getText();
                    if (TextUtils.isEmpty(packageName) || testedPackages.contains(packageName)) {
                        UiAutomatorUtil.debugLog("continue " + packageName);
                        continue;
                    }
                    UiAutomatorUtil.infoLog(label + " [" + packageName + "]");
                    hasNext = true;
                    testedPackages.add(packageName);
                    titleTextView.clickAndWaitForNewWindow();

                    UiAutomatorUtil.takeScreenshot(uiDevice, mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), packageName + ".png");

                    if (!validator.validate(packageName)) {
                        // バリデータでエラーになった場合
                        if (UiAutomatorUtil.isIgnorePackage(packageName)) {
                            // 無視リストに入っている場合はエラーにせず、ログ出力のみで次へ進む
                            UiAutomatorUtil.infoLog("[Info] Ignore application:" + packageName);
                        } else {
                            // バリデータでエラーになり、かつ無視リストに入っていない場合はエラーとする
                            errorAppList.add(packageName);
                        }
                    }

                    uiDevice.pressBack();
                } catch (UiObjectNotFoundException e) {
                    // 画面に表示されているListViewの要素のi番目が見つからない OR パッケージ名が見つからない
                    // ＝ 今表示されている分は全部見た
                    // ＝ forループは抜け、次へ進む
                    UiAutomatorUtil.debugLog(i + ", continue(e) " + e.getMessage());
                    break;
                }
            }
            listView.scrollForward();
        } while (hasNext);
        return errorAppList;
    }

    private void validateCurrentPageTitle(String title) throws UiObjectNotFoundException {
        UiAutomatorUtil.debugLog("validateCurrentPageTitle " + title);
        UiObject titlePageIndicator = uiDevice.findObject(
                new UiSelector()
                        .className(TabPageIndicator.class)
        );
        assertEquals("TabPageIndicatorが【" + title + "】であることを確認", title, titlePageIndicator.getText());
    }


}