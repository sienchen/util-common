package com.tongtu.cyber.util.characters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串处理工具类
 * @author : 陈世恩
 * @date : 2024/3/20 9:09
 */
public class StrRegFilterUtil {
    /**
     * 正则匹配过滤掉特殊字符
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String filter(String str) throws PatternSyntaxException {
        String regEx = "[`_《》~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
