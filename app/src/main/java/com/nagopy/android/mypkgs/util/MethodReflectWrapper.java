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

import java.lang.reflect.Method;

/**
 * リフレクションでメソッドを取得する際、try-catchを書かずに済ませるためのラッパークラス.
 */
public class MethodReflectWrapper {

    private final Method method;

    public MethodReflectWrapper(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Method method;
        try {
            method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.method = method;
    }

    public Object invoke(Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (Exception e) {
//        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
