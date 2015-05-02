package com.nagopy.android.mypkgs.constants;

import android.app.ActivityManager;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RunningStatusTest {

    @Test
    public void test() {
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND), is("Background"));
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND), is("Foreground"));
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE), is("Perceptible"));
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE), is("Service"));
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE), is("Visible"));
        assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY), is("Empty"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assertThat(RunningStatus.MAP.get(ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE), is("Gone"));
        }
    }
}
