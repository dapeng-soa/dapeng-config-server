package com.github.dapeng.util;

import java.util.Collection;

/**
 * @author with struy.
 * Create by 2018/6/12 01:30
 * email :yq1724555319@gmail.com
 */

public class NullUtil {

    public static boolean isEmpty(Object objs) {
        return (objs == null);
    }


    public static boolean isEmpty(Object[] objs) {
        return ((objs == null) || (objs.length == 0));
    }


    public static boolean isEmpty(Collection<?> objs) {
        return ((objs == null) || (objs.size() <= 0));
    }


    public static boolean isEmpty(byte[] objs) {
        return ((objs == null) || (objs.length == 0));
    }


    public static boolean isEmpty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }


    public static boolean isEmpty(Long l) {
        return ((l == null) || (l == 0L));
    }
}
