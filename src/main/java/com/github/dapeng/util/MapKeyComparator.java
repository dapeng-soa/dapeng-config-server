package com.github.dapeng.util;

import java.util.Comparator;

/**
 * @author with struy.
 * Create by 2018/5/16 13:07
 * email :yq1724555319@gmail.com
 */

class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.toUpperCase().compareTo(str2.toUpperCase());
    }
}
