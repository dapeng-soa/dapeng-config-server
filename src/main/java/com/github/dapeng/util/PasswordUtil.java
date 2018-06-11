package com.github.dapeng.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @author with struy.
 * Create by 2018/6/10 23:38
 * email :yq1724555319@gmail.com
 */

public class PasswordUtil {
    private static final Md5PasswordEncoder encoder = new Md5PasswordEncoder();

    /**
     *  加盐返回md5后的密码
     * @param rawPass
     * @param salt
     * @return
     */
    public static String createPassword(String rawPass, Object salt) {
        return encoder.encodePassword(rawPass, salt);
    }
}
