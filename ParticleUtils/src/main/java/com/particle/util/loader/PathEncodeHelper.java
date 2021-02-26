package com.particle.util.loader;

import org.apache.commons.lang3.StringUtils;

public class PathEncodeHelper {
    public PathEncodeHelper() {
    }

    public static String buildKey(String space, String key, String split) {
        if (StringUtils.isBlank(key)) {
            return space;
        } else {
            return StringUtils.isBlank(space) ? key : space + split + key;
        }
    }

    public static String decodeKey(String key) {
        return key.contains(".") ? key.substring(key.lastIndexOf(".") + 1) : key;
    }

    public static String decodeSpace(String key) {
        return key.contains(".") ? key.substring(0, key.lastIndexOf(".")) : "";
    }
}
