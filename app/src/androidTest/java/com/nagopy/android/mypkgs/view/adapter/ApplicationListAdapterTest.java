package com.nagopy.android.mypkgs.view.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.constants.Constants;
import com.nagopy.android.mypkgs.constants.FilterType;
import com.nagopy.android.mypkgs.model.AppData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RunWith(AndroidJUnit4.class)
public class ApplicationListAdapterTest extends AndroidTestCase {

    Context context;
    private Map<String, ?> all;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        all = PreferenceManager.getDefaultSharedPreferences(context).getAll();
    }

    @Override
    protected void tearDown() throws Exception {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (all != null) {
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                }
                if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                }
                if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                }
                if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                }
                if (value instanceof Set) {
                    editor.putStringSet(key, (Set) value);
                }
            }
        }
        editor.apply();
        super.tearDown();
    }

    @Test
    public void getCount() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ApplicationListAdapter adapter = new ApplicationListAdapter(context, FilterType.ALL);
                adapter.filteredData = Collections.singletonList(Mockito.mock(AppData.class));
                assertEquals(1, adapter.getCount());
            }
        });
    }

    @Test
    public void getItem() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ApplicationListAdapter adapter = new ApplicationListAdapter(context, FilterType.ALL);
                AppData app0 = Mockito.mock(AppData.class);
                AppData app1 = Mockito.mock(AppData.class);
                adapter.filteredData = Arrays.asList(app0, app1);

                assertEquals(app0, adapter.getItem(0));
                assertEquals(app1, adapter.getItem(1));
            }
        });
    }

    @Test
    public void getView() {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(context.getString(R.string.pref_key_enabled_advanced), false)
                .apply();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ApplicationListAdapter adapter = new ApplicationListAdapter(context, FilterType.ALL);

                // TODO アイコンのとことか
                AppData app0 = Mockito.mock(AppData.class);
                app0.label = "label 0";
                app0.packageName = "pkg 0";
                app0.process = null;
                app0.icon = new WeakReference<>(context.getResources().getDrawable(R.mipmap.ic_launcher));

                AppData app1 = Mockito.mock(AppData.class);
                app1.label = "label 1";
                app1.packageName = "pkg 1";
                app1.process = Arrays.asList("process1", "process2");
                app1.icon = new WeakReference<>(context.getResources().getDrawable(R.mipmap.ic_launcher));

                adapter.filteredData = Arrays.asList(app0, app1);

                View v0 = adapter.getView(0, null, null);
                assertEquals(app0.label, ((TextView) v0.findViewById(R.id.list_title)).getText());
                assertEquals(app0.packageName, ((TextView) v0.findViewById(R.id.list_package_name)).getText());
                assertEquals("", ((TextView) v0.findViewById(R.id.list_info)).getText());
                assertEquals(View.GONE, v0.findViewById(R.id.list_info).getVisibility());

                View v1 = adapter.getView(1, null, null);
                assertEquals(app1.label, ((TextView) v1.findViewById(R.id.list_title)).getText());
                assertEquals(app1.packageName, ((TextView) v1.findViewById(R.id.list_package_name)).getText());
                assertEquals("process1" + Constants.LINE_SEPARATOR + "process2", ((TextView) v1.findViewById(R.id.list_info)).getText());
                assertEquals(View.VISIBLE, v1.findViewById(R.id.list_info).getVisibility());
            }
        });
    }
}