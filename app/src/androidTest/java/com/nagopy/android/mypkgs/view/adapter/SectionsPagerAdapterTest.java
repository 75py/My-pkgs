package com.nagopy.android.mypkgs.view.adapter;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.AndroidTestCase;

import com.nagopy.android.mypkgs.ApplicationListFragment;
import com.nagopy.android.mypkgs.constants.FilterType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class SectionsPagerAdapterTest extends AndroidTestCase {

    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void getItem() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
                SectionsPagerAdapter adapter = new SectionsPagerAdapter(fragmentManager, context, Arrays.asList(FilterType.ALL, FilterType.RUNNING));

                Fragment item0 = adapter.getItem(0);
                assertEquals(ApplicationListFragment.class, item0.getClass());
                ApplicationListFragment fragment0 = (ApplicationListFragment) item0;
                assertEquals(FilterType.ALL.name(), fragment0.getArguments().getString("filter_type"));

                Fragment item1 = adapter.getItem(1);
                assertEquals(ApplicationListFragment.class, item1.getClass());
                ApplicationListFragment fragment1 = (ApplicationListFragment) item1;
                assertEquals(FilterType.RUNNING.name(), fragment1.getArguments().getString("filter_type"));

                assertEquals(item0, adapter.getItem(0));
                assertEquals(item1, adapter.getItem(1));
            }
        });
    }

    @Test
    public void getCount() {
        FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(fragmentManager, context, Arrays.asList(FilterType.ALL, FilterType.RUNNING));
        assertEquals(2, adapter.getCount());
    }

    @Test
    public void getPageTitle() {
        FragmentManager fragmentManager = Mockito.mock(FragmentManager.class);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(fragmentManager, context, Arrays.asList(FilterType.ALL, FilterType.RUNNING));
        assertEquals(context.getString(FilterType.ALL.titleResourceId), adapter.getPageTitle(0));
        assertEquals(context.getString(FilterType.RUNNING.titleResourceId), adapter.getPageTitle(1));
    }


}
