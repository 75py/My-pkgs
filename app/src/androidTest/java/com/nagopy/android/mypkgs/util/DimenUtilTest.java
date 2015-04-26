package com.nagopy.android.mypkgs.util;

import android.app.ActivityManager;
import android.content.Context;
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
public class DimenUtilTest {

    @Test
    public void getIconSize() {
        Context context = mock(Context.class);
        ActivityManager activityManager = mock(ActivityManager.class);
        when(context.getSystemService(Context.ACTIVITY_SERVICE)).thenReturn(activityManager);
        when(activityManager.getLauncherLargeIconSize()).thenReturn(75);

        assertThat("モックで指定した値が返る", DimenUtil.getIconSize(context), is(75));
        verify(activityManager, times(1)).getLauncherLargeIconSize();
    }

}
