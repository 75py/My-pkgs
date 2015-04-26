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
package com.nagopy.android.mypkgs.util;

import java.lang.reflect.Field;

/**
 * リフレクションでフィールドを取得する際、try-catchを書かずに済ませるためのラッパークラス.
 */
public class FieldReflectWrapper {

    private final Field field;

    public FieldReflectWrapper(Class<?> cls, String fieldName) {
        Field field;
        try {
            field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        this.field = field;
    }

    public Object get(Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(Object object) {
        try {
            return field.getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
