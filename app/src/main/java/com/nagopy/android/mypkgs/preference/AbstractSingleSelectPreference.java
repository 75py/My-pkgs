package com.nagopy.android.mypkgs.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.R;
import com.nagopy.android.mypkgs.util.DebugUtil;

public abstract class AbstractSingleSelectPreference<T extends Selectable> extends PreferenceCategory implements Preference.OnPreferenceChangeListener {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbstractSingleSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public AbstractSingleSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AbstractSingleSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractSingleSelectPreference(Context context) {
        super(context);
        init(context);
    }

    protected SharedPreferences sp;

    protected void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        super.onAttachedToHierarchy(preferenceManager);

        for (T t : getValues()) {
            Preference preference = buildPreference(t);
            addPreference(preference);
        }
    }

    protected Preference buildPreference(T t) {
        CheckBoxPreference preference = new CheckBoxPreference(getContext());
        preference.setWidgetLayoutResource(R.layout.preference_widget_checkbox_single);
        preference.setTitle(t.getTitleResourceId());
        preference.setSummary(t.getSummaryResourceId());
        preference.setKey(t.getName());
        preference.setChecked(t.getName().equals(getValue()));
        preference.setOnPreferenceChangeListener(this);
        return preference;
    }

    protected String getValue() {
        return sp.getString(getKey(), getDefaultName());
    }

    protected abstract T[] getValues();

    protected abstract String getDefaultName();

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (Boolean.FALSE.equals(newValue)) {
            return false;
        }

        boolean checked = (Boolean) newValue;
        for (T t : getValues()) {
            CheckBoxPreference p = (CheckBoxPreference) findPreference(t.getName());
            boolean isChecked = p == preference;
            p.setChecked(isChecked);
            if (isChecked) {
                DebugUtil.debugLog("save " + getKey());
                sp.edit().putString(getKey(), t.getName()).apply();
            }
        }
        return false;
    }
}
