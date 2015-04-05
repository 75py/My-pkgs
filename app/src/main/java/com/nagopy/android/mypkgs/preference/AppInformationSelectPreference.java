package com.nagopy.android.mypkgs.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.AppInformation;
import com.nagopy.android.mypkgs.FilterType;

public class AppInformationSelectPreference extends AbstractMultiSelectPreference<AppInformation> {

    public AppInformationSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AppInformationSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppInformationSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppInformationSelectPreference(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        allowEmpty = true;
    }

    @Override
    protected AppInformation[] getValues() {
        return AppInformation.values();
    }

    @Override
    protected String getDefaultName() {
        return AppInformation.DEFAULT_VALUE;
    }
}
