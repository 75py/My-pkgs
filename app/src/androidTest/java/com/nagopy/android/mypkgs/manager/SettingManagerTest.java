package com.nagopy.android.mypkgs.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.nagopy.android.mypkgs.constants.AppComparator;
import com.nagopy.android.mypkgs.constants.AppInformation;
import com.nagopy.android.mypkgs.constants.FilterType;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SettingManagerTest {

    @Test
    public void getAppComparator() {
        Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        SettingManager settingManager = new SettingManager(context);
        SharedPreferences mockSp = mock(SharedPreferences.class);
        settingManager.sp = mockSp;

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(false);
        assertEquals("Advancedが無効ならデフォルトが返る", AppComparator.DEFAULT, settingManager.getAppComparator());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_SORT), anyString())).thenReturn(AppComparator.DEFAULT.name());
        assertEquals("Advancedが有効・デフォルト設定", AppComparator.DEFAULT, settingManager.getAppComparator());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_SORT), anyString())).thenReturn(AppComparator.UPDATE_DATE_DESC.name());
        assertEquals("Advancedが有効・更新日時降順", AppComparator.UPDATE_DATE_DESC, settingManager.getAppComparator());
    }

    @Test
    public void getAppInformation() {
        Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        SettingManager settingManager = new SettingManager(context);
        SharedPreferences mockSp = mock(SharedPreferences.class);
        settingManager.sp = mockSp;

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(false);
        assertEquals("Advancedが無効ならデフォルトが返る", AppInformation.DEFAULT_LIST, settingManager.getAppInformation());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.contains(settingManager.KEY_INFORMATION)).thenReturn(false);
        assertEquals("保存されてない場合はデフォルトが返る", AppInformation.DEFAULT_LIST, settingManager.getAppInformation());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.contains(settingManager.KEY_INFORMATION)).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_INFORMATION), anyString()))
                .thenReturn("");
        assertTrue("空なら空のリストが返る", settingManager.getAppInformation().isEmpty());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.contains(settingManager.KEY_INFORMATION)).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_INFORMATION), anyString()))
                .thenReturn(AppInformation.PROCESS.name() + "," + AppInformation.NOT_INSTALLED.name());
        assertEquals("カンマ区切りで保存されたEnumが返る",
                Arrays.asList(AppInformation.PROCESS, AppInformation.NOT_INSTALLED),
                settingManager.getAppInformation());
    }

    @Test
    public void getFilters() {
        Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();
        SettingManager settingManager = new SettingManager(context);
        SharedPreferences mockSp = mock(SharedPreferences.class);
        settingManager.sp = mockSp;

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(false);
        assertEquals("Advancedが無効ならデフォルトが返る", FilterType.DEFAULT_FILTERS, settingManager.getFilters());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_FILTERS), anyString()))
                .thenReturn("");
        assertEquals("空ならデフォルトが返る", FilterType.DEFAULT_FILTERS, settingManager.getFilters());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_FILTERS), anyString()))
                .thenReturn(FilterType.ALL.name());
        assertEquals("保存されたEnumのリストが返る", Collections.singletonList(FilterType.ALL), settingManager.getFilters());

        when(mockSp.getBoolean(eq(settingManager.KEY_ADVANCED), anyBoolean())).thenReturn(true);
        when(mockSp.getString(eq(settingManager.KEY_FILTERS), anyString()))
                .thenReturn(FilterType.ALL.name() + "," + FilterType.SYSTEM.getName());
        assertEquals("保存されたEnumのリストが返る", Arrays.asList(FilterType.ALL, FilterType.SYSTEM), settingManager.getFilters());
    }
}
