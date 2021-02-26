package com.particle.util;

import java.util.Collection;

public class ListUtils {


    /**
     * 字符串拼接
     *
     * @param lists
     * @param separator
     * @return
     */
    public static String join(Collection<String> lists, String separator) {
        if (lists == null || lists.isEmpty() || separator == null) {
            return null;
        }
        return String.join(separator, lists);
    }
}
