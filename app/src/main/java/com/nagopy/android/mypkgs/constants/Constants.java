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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 定数クラス
 */
public class Constants {

    /**
     * 改行
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * text/plain
     */
    public static final String MINE_TYPE_TEXT_PLAIN = "text/plain";

    /**
     * 更新有無フラグを保存するためのキー
     */
    public static final String KEY_UPDATE_FLG = "UPDATE_FLG";

    /**
     * 2000/01/01（ミリ秒）
     */
    public static final long Y2K;

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Y2K = simpleDateFormat.parse("20000101").getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Constants() {
    }

}
