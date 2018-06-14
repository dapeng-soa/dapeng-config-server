package com.github.dapeng.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author with struy.
 * Create by 2018/6/11 23:50
 * email :yq1724555319@gmail.com
 */

public class VersionUtil {

    private static final String TIMEFORMAT = "yyyyMMddHHmmss";

    public static String version() {

        return DateTimeFormatter
                .ofPattern(TIMEFORMAT)
                .format(LocalDateTime.now());
    }

    public static void main(String[] args) {
        System.out.println(version());
    }
}
