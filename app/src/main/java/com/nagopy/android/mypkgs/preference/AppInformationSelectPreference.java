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
package com.nagopy.android.mypkgs.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.AppInformation;

public class AppInformationSelectPreference extends AbstractMultiSelectPreference<AppInformation> {

    @SuppressWarnings("unused")
    public AppInformationSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("unused")
    public AppInformationSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public AppInformationSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
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
