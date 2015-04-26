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
public class FilterTypeTest {

    @Test
    public void all() {
        AppData appData = mock(AppData.class);
        assertThat("全て 常にtrue", FilterType.ALL.isTarget(appData), is(true));
    }

    @Test
    public void 実行中() {
        AppData appData = mock(AppData.class);
        assertThat("停止中", FilterType.RUNNING.isTarget(appData), is(false));

        appData.process = Arrays.asList("Background", "Service");
        assertThat("実行中", FilterType.RUNNING.isTarget(appData), is(true));
    }

    @Test
    public void システム() {
        AppData appData = mock(AppData.class);
        appData.isSystem = false;
        assertThat("非システム", FilterType.SYSTEM.isTarget(appData), is(false));

        appData.isSystem = true;
        assertThat("システム", FilterType.SYSTEM.isTarget(appData), is(true));
    }

    @Test
    public void システム実行中() {
        AppData appData = mock(AppData.class);
        appData.isSystem = false;
        {
            appData.process = Collections.emptyList();
            assertThat("非システム停止中", FilterType.SYSTEM_RUNNING.isTarget(appData), is(false));
            appData.process = Arrays.asList("Background", "Service");
            assertThat("非システム実行中", FilterType.SYSTEM_RUNNING.isTarget(appData), is(false));
        }

        appData.isSystem = true;
        {
            appData.process = Collections.emptyList();
            assertThat("システム停止中", FilterType.SYSTEM_RUNNING.isTarget(appData), is(false));
            appData.process = Arrays.asList("Background", "Service");
            assertThat("システム実行中", FilterType.SYSTEM_RUNNING.isTarget(appData), is(true));
        }
    }

    // TODO

}
