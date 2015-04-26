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
package com.nagopy.android.mypkgs.constants;

import android.app.ActivityManager;
import android.os.Build;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RunningStatus {

    private RunningStatus() {
    }

    public static final Map<Integer, String> MAP;

    static {
        Map<Integer, String> runningStatusMap = new HashMap<>();
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND, "Background");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND, "Foreground");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE, "Perceptible");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE, "Service");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE, "Visible");
        runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY, "Empty");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            runningStatusMap.put(ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE, "Gone");
        }
        MAP = Collections.unmodifiableMap(runningStatusMap);
    }
}
