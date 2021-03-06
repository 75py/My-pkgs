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
package com.nagopy.android.mypkgs.view.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.util.DebugUtil;

public abstract class AbstractMultiSelectPreference<T extends Selectable> extends PreferenceCategory implements Preference.OnPreferenceChangeListener {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractMultiSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public AbstractMultiSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AbstractMultiSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractMultiSelectPreference(Context context) {
        super(context);
        init(context);
    }

    protected SharedPreferences sp;

    protected void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onAttachedToHierarchy(@NonNull PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);

        for (T t : getValues()) {
            Preference preference = buildPreference(t);
            addPreference(preference);
        }
    }

    protected Preference buildPreference(T t) {
        CheckBoxPreference preference = new CheckBoxPreference(getContext());
        preference.setTitle(t.getTitleResourceId());
        preference.setSummary(t.getSummaryResourceId());
        preference.setKey(t.getName());
        preference.setChecked(false);
        for (String v : getValue().split(",")) {
            if (t.getName().equals(v)) {
                preference.setChecked(true);
                break;
            }
        }
        preference.setOnPreferenceChangeListener(this);
        return preference;
    }

    protected String getValue() {
        return sp.getString(getKey(), getDefaultName());
    }

    protected abstract T[] getValues();

    protected abstract String getDefaultName();

    protected boolean allowEmpty;

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((CheckBoxPreference) preference).setChecked((Boolean) newValue);

        StringBuilder sb = new StringBuilder();
        for (T t : getValues()) {
            CheckBoxPreference p = (CheckBoxPreference) findPreference(t.getName());
            if (p.isChecked()) {
                sb.append(t.getName());
                sb.append(',');
            }
        }

        DebugUtil.verboseLog(getKey() + " " + sb.toString());
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
            sp.edit().putString(getKey(), sb.toString()).apply();
        } else {
            if (allowEmpty) {
                sp.edit().putString(getKey(), sb.toString()).apply();
            } else {
                ((CheckBoxPreference) preference).setChecked(true);
            }
        }
        return false;
    }
}
