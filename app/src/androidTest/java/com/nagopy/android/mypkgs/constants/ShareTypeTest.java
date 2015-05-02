package com.nagopy.android.mypkgs.constants;

import android.support.test.runner.AndroidJUnit4;

import com.nagopy.android.mypkgs.model.AppData;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class ShareTypeTest {

    @Test
    public void pkg() {
        AppData app0 = mock(AppData.class);
        app0.packageName = "test.aaa";
        assertThat("パッケージ名共有（1個）",
                ShareType.PACKAGE.makeShareString(app0),
                is("test.aaa"));

        List<AppData> apps = Collections.singletonList(app0);
        assertThat("パッケージ名共有（1個）",
                ShareType.PACKAGE.makeShareString(apps),
                is("test.aaa" + System.getProperty("line.separator")));

        AppData app1 = mock(AppData.class);
        app1.packageName = "test.xxx";
        List<AppData> apps2 = Arrays.asList(app0, app1);
        assertThat("パッケージ名共有（2個）",
                ShareType.PACKAGE.makeShareString(apps2),
                is("test.aaa" + System.getProperty("line.separator") + "test.xxx" + System.getProperty("line.separator")));
    }

    @Test
    public void アプリ名共有() {
        AppData app0 = mock(AppData.class);
        app0.label = "test.aaa";
        assertThat("アプリ名共有（1個）",
                ShareType.LABEL.makeShareString(app0),
                is("test.aaa"));

        List<AppData> apps = Collections.singletonList(app0);
        assertThat("アプリ名共有（1個）",
                ShareType.LABEL.makeShareString(apps),
                is("test.aaa" + System.getProperty("line.separator")));

        AppData app1 = mock(AppData.class);
        app1.label = "test.xxx";
        List<AppData> apps2 = Arrays.asList(app0, app1);
        assertThat("アプリ名共有（2個）",
                ShareType.LABEL.makeShareString(apps2),
                is("test.aaa" + System.getProperty("line.separator") + "test.xxx" + System.getProperty("line.separator")));
    }

    @Test
    public void アプリ名とパッケージ名共有() {
        String br = System.getProperty("line.separator");

        AppData app0 = mock(AppData.class);
        app0.label = "アプリ名１";
        app0.packageName = "com.nagopy.one";
        assertThat("アプリ名とパッケージ名共有（1個）",
                ShareType.LABEL_AND_PACKAGE.makeShareString(app0),
                is("アプリ名１" + br + "com.nagopy.one"));

        List<AppData> apps = Collections.singletonList(app0);
        assertThat("アプリ名とパッケージ名共有（1個）",
                ShareType.LABEL_AND_PACKAGE.makeShareString(apps),
                is("アプリ名１" + br + "com.nagopy.one" + br + br));

        AppData app1 = mock(AppData.class);
        app1.label = "アプリ名２";
        app1.packageName = "com.nagopy.two";
        List<AppData> apps2 = Arrays.asList(app0, app1);
        assertThat("アプリ名とパッケージ名共有（2個）",
                ShareType.LABEL_AND_PACKAGE.makeShareString(apps2),
                is("アプリ名１" + br + "com.nagopy.one" + br + br + "アプリ名２" + br + "com.nagopy.two" + br + br));
    }
}
