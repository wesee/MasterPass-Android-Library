package com.anypresence.masterpass_android_library.util;

import java.util.Map;

/**
 * Created by diego.rotondale on 1/17/2015.
 * Copyright (c) 2015 AnyPresence, Inc. All rights reserved.
 */
public class ConvertUtil {
    public static Integer getInteger(Map<String, Object> dictionary, String key) {
        if (dictionary.containsKey(key)) {
            return (Integer) dictionary.get(key);
        }
        return null;
    }

    public static String getString(Map<String, Object> dictionary, String key) {
        if (dictionary.containsKey(key)) {
            String value = (String) dictionary.get(key);
            if (!value.isEmpty())
                return value;
        }
        return null;
    }
}
