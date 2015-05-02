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
public class AppComparatorTest {

    @Test
    public void パッケージ名() {
        AppData app0 = mock(AppData.class);
        AppData app1 = mock(AppData.class);
        AppData app2 = mock(AppData.class);
        AppData app3 = mock(AppData.class);
        AppData app4 = mock(AppData.class);

        // パッケージ名を設定。基本的にはこの順に並ぶはず
        app1.packageName = "com.nagopy.a";
        app0.packageName = "com.nagopy.b";
        app3.packageName = "com.nagopy.c";
        app4.packageName = "com.nagopy.d";
        app2.packageName = "com.nagopy.e";
        // ただし、app3は未インストール状態なので、一番下になる
        app0.isInstalled = true;
        app1.isInstalled = true;
        app2.isInstalled = true;
        app3.isInstalled = false;
        app4.isInstalled = true;

        // ソート前
        List<AppData> list = Arrays.asList(app0, app1, app2, app3, app4);
        assertThat(list.get(0), is(app0));
        assertThat(list.get(1), is(app1));
        assertThat(list.get(2), is(app2));
        assertThat(list.get(3), is(app3));
        assertThat(list.get(4), is(app4));

        // ソート後
        Collections.sort(list, AppComparator.PACKAGE_NAME);
        assertThat(list.get(0), is(app1));
        assertThat(list.get(1), is(app0));
        assertThat(list.get(2), is(app4));
        assertThat(list.get(3), is(app2));
        assertThat(list.get(4), is(app3));
    }

}
