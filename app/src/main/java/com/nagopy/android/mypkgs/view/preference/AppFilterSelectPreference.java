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

import android.content.Context;
import android.util.AttributeSet;

import com.nagopy.android.mypkgs.constants.FilterType;

public class AppFilterSelectPreference extends AbstractMultiSelectPreference<FilterType> {

    @SuppressWarnings("unused")
    public AppFilterSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("unused")
    public AppFilterSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    public AppFilterSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
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
