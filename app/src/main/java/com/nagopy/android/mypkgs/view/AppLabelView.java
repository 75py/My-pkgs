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
package com.nagopy.android.mypkgs.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import com.nagopy.android.mypkgs.util.DimenUtil;

public class AppLabelView extends TextView {

    final int iconSize;

    public AppLabelView(Context context) {
        super(context);
        iconSize = DimenUtil.getIconSize(context);
    }

    public AppLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iconSize = DimenUtil.getIconSize(context);
    }

    public AppLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iconSize = DimenUtil.getIconSize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        iconSize = DimenUtil.getIconSize(context);
    }

    /**
     * TextViewの左側（START側）にアイコンを表示する。
     *
     * @param icon アイコン
     */
    public void setIcon(Drawable icon) {
        icon.setBounds(0, 0, iconSize, iconSize);
        setCompoundDrawablesRelative(icon, null, null, null);
        /*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawables(icon, null, null, null);
        } else {
            setCompoundDrawablesRelative(icon, null, null, null);
        }
        */
        icon.setCallback(null);
    }


    @Override
    public void onInitializeAccessibilityNodeInfo(@NonNull AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setPackageName(getContext().getPackageName());
        info.setClassName(getClass().getName());
        info.setText(getText());
    }
}
