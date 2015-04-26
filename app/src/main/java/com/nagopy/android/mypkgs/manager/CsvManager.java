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
package com.nagopy.android.mypkgs.manager;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvManager<T> {

    private List<Field> fields;
    private String header;

    public CsvManager(Class<T> cls) {
        Field[] f = cls.getFields();

        if (f.length == 0) {
            fields = Collections.emptyList();
            header = "";
        } else {
            fields = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (Field field : f) {
                if (field.isAnnotationPresent(Exclude.class)) {
                    continue;
                }
                fields.add(field);
                sb.append('"');
                String str = field.getName().replace("\"", "\"\"");
                sb.append(str);
                sb.append('"');
                sb.append(',');
            }
            header = sb.toString();
        }
    }

    public String getHeader() {
        return header;
    }

    public String toCSV(T obj) {
        if (fields.isEmpty()) {
            return "";
        }
        if (obj == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            sb.append('"');
            try {
                Object value = field.get(obj);
                if (value != null) {
                    String str = value.toString().replace("\"", "\"\"");
                    sb.append(str);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            sb.append('"');
            sb.append(',');
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}