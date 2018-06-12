package com.github.dapeng.util;

import java.sql.Timestamp;

/**
 * @author with struy.
 * Create by 2018/6/11 21:26
 * email :yq1724555319@gmail.com
 */

public class DateUtil {

    public static Timestamp now() {
        long nowMil = System.currentTimeMillis();
        return new Timestamp(nowMil);
    }
}
