package com.tongtu.cyber.util.characters;

import cn.hutool.core.util.StrUtil;
import com.tongtu.cyber.common.util.StringUtil;

import java.util.List;

/**
 * 桩号格式化工具类
 */
public class StakeUtils {

    /**
     * 1.123 -> K1+123
     *
     * @param str
     * @return
     */
    public static String addKByStake(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        if (StrUtil.contains(str, '.')) {
            String right = "";
            List<String> strs = StrUtil.splitTrim(str, ".");
            if (strs.get(1).length() < 3) {
                if (strs.get(1).length() == 1) {
                    right = strs.get(1) + "00";
                }
                if (strs.get(1).length() == 2) {
                    right = strs.get(1) + "0";
                }
            } else {
                right = strs.get(1);
            }
            str = "K" + strs.get(0) + "+" + right;
        } else {
            str = "K" + str + "+" + "000";
        }
        return str;
    }

    /**
     * K1+123 -> 1.123
     *
     * @param str
     * @return
     */
    public static String subKByStake(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        if (StrUtil.contains(str, '+')) {
            List<String> strs = StrUtil.splitTrim(str, "+");
            if (Double.parseDouble(strs.get(1)) > 0) {
                str = StrUtil.splitTrim(strs.get(0), "K").get(0) + "." + strs.get(1);
                str = StringUtil.trimZero(str);
            } else {
                str = StrUtil.splitTrim(strs.get(0), "K").get(0);
            }
        }
        return str;
    }
}
