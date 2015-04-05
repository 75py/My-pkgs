package com.nagopy.android.mypkgs.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.AppComparator;

public class AppComparatorSelectPreference extends AbstractSingleSelectPreference<AppComparator> {
    public AppComparatorSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AppComparatorSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppComparatorSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppComparatorSelectPreference(Context context) {
        super(context);
    }

    @Override
    protected AppComparator[] getValues() {
        return AppComparator.values();
    }

    @Override
    protected String getDefaultName() {
        return AppComparator.DEFAULT.name();
    }
}
