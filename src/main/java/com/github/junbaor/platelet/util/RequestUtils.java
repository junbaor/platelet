package com.github.junbaor.platelet.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @packagename: com.github.junbaor.platelet.util
 * @author: zhumingji@jiumaojiu.com
 * @date: 2021-08-30 10:57
 **/
public class RequestUtils {

    public static Map<String, String> getQueryMap(String query) {
        Map<String, String> map = new HashMap<>(8);

        if (!StringUtils.isEmpty(query)) {

            String[] params = query.split("&");
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                map.put(name, value);
            }
        }

        return map;
    }
}
