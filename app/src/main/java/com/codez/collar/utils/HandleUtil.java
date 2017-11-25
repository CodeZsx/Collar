package com.codez.collar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by codez on 2017/11/25.
 * Description:
 */

public class HandleUtil {
    public static String handleSource(String source) {
        Pattern p = Pattern.compile("<a[^>]*>([^<]*)</a>");
        Matcher m = p.matcher(source);
        while(m.find()) {
            return m.group(1);
        }
        return null;
    }
}
