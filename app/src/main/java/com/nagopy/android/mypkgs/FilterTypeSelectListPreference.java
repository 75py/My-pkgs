/*
 * Copyright (C) 2015 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nagopy.android.mypkgs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FilterTypeSelectListPreference extends MultiSelectListPreference implements Preference.OnPreferenceChangeListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FilterTypeSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FilterTypeSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FilterTypeSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterTypeSelectListPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String key = getKey();
        Set<String> value = sp.getStringSet(key, Collections.<String>emptySet());
        if (value == null || value.isEmpty()) {
            sp.edit().putStringSet(key, FilterType.DEFAULT_FILTERS_NAME_SET).apply();
        }
        updateSummary();
        setOnPreferenceChangeListener(this);
    }

    private void updateSummary() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String key = getKey();
        Set<String> value = sp.getStringSet(key, Collections.<String>emptySet());
        updateSummary(value);
    }

    private void updateSummary(Set<String> value) {
        if (!isEnabled()) {
            setSummary("");
            return;
        }

        if (value.isEmpty()) {
            setSummary("");
        } else {

            Map<Integer, CharSequence> map = new TreeMap<>();
            int i;
            for (String wk : value) {
                i = 0;
                for (CharSequence title : getEntryValues()) {
                    if (wk.equals(title)) {
                        map.put(i, getEntries()[i]);
                        break;
                    }
                    i++;
                }
            }
            StringBuilder sb = new StringBuilder();
            for (CharSequence val : map.values()) {
                sb.append(val);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            setSummary(sb.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Set<String> newValues = (Set<String>) o;
        if (newValues == null || newValues.isEmpty()) {
            return false;
        }
        updateSummary(newValues);
        return true;
    }
}
