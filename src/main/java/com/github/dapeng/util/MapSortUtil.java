package com.github.dapeng.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author with struy.
 * Create by 2018/5/16 13:06
 * email :yq1724555319@gmail.com
 */

public class MapSortUtil {

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}
