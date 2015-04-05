package com.nagopy.android.mypkgs.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.FilterType;

public class AppFilterSelectPreference extends AbstractMultiSelectPreference<FilterType> {
    public AppFilterSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AppFilterSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppFilterSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppFilterSelectPreference(Context context) {
        super(context);
    }

    @Override
    protected FilterType[] getValues() {
        return FilterType.values();
    }

    @Override
    protected String getDefaultName() {
        return FilterType.DEFAULT_VALUE;
    }
}
