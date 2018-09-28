package com.github.dapeng.util;

import java.security.MessageDigest;

/**
 * @author with struy.
 * Create by 2018/9/27 14:56
 * email :yq1724555319@gmail.com
 */

public class Md5 {
    /**
     * encrypt32
     *
     * @param encryptStr
     * @return String
     */
    public static String encrypt32(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * encrypt16
     *
     * @param encryptStr
     * @return String
     */
    public static String encrypt16(String encryptStr) {
        return encrypt32(encryptStr).substring(8, 24);
    }
}
