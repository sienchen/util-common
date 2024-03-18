package com.tongtu.cyber.util.chart;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 图表日期补齐工具类
 * @author : 陈世恩
 * @date : 2023/6/6 17:46
 */
public class ChartDateUtil {
    /**
     * 构建柱状图/折线图等图表数据模型
     *
     * @param datas
     * @param name
     * @param date
     * @return
     */
    public static ChartModel buildChart(List<Map<String, Object>> datas, String name, String date) {
        ChartModel charRetrun = new ChartModel();
        charRetrun.setValues(datas);
        charRetrun.setName(name);
        charRetrun.setDate(date);
        return charRetrun;
    }


    /**
     * 获取一天的(小时集合)
     *
     * @param day       默认当天
     * @param outFormat 返回小时格式 默认 yyyy-MM-dd HH
     * @param now       false 24小时 true 截止到当前小时
     * @return
     */
    public static List<String> getHoursOfDay(String day, String outFormat, boolean now) {
        DateTime nowTime = DateTime.now();
        String today = DateUtil.format(nowTime, "yyyyMMdd");
        if (StrUtil.isBlank(day)) {
            day = today;
        } else {
            day = day.trim().replace("-", "");
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM-dd HH";
        }
        DateTime dayTime = DateUtil.parse(day);
        DateTime start;
        DateTime end;
        List<String> hours = new ArrayList<>();
        start = DateUtil.beginOfDay(dayTime);
        if (now && today.equals(day)) {
            end = DateTime.now();
        } else {
            end = DateUtil.endOfDay(dayTime);
        }
        do {
            hours.add(DateUtil.format(start, outFormat));
            start = DateUtil.offsetHour(start, 1);
        } while (start.isBeforeOrEquals(end));
        return hours;
    }

    /**
     * 获取一个月的（天数集合）
     *
     * @param month     默认当月
     * @param outFormat 返回天格式 默认：yyyy-MM-dd
     * @param now       false 30/31天 true 截止到当前日
     * @return
     */
    public static List<String> getDayOfMonth(String month, String outFormat, boolean now) {
        String currentMonth = DateUtil.format(new Date(), "yyyyMM");
        DateTime monthDate;
        if (StrUtil.isBlank(month)) {
            monthDate = DateUtil.parse(currentMonth);
        } else {
            month = month.trim().replace("-", "");
            monthDate = DateUtil.parse(month, "yyyyMM");
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM-dd";
        }
        DateTime start = DateUtil.beginOfMonth(monthDate);
        DateTime end;
        List<String> days = new ArrayList<>();
        if (now && currentMonth.equals(month)) {
            end = DateUtil.parseDate(DateUtil.today());
        } else {
            end = DateUtil.endOfMonth(monthDate);
        }
        do {
            days.add(DateUtil.format(start, outFormat));
            start = DateUtil.offsetDay(start, 1);
        } while (start.isBeforeOrEquals(end));
        return days;
    }


    /**
     * 两个日期之间的 间隔所有日期数组
     *
     * @param startDay  开始日期 默认当日
     * @param endDay    结束日期 默认当日
     * @param outFormat 默认 yyyy-MM-dd
     * @param now       false 全部 true 当前日期截止
     * @return
     */
    public static List<String> getDayOfRangeDate(String startDay, String endDay, String outFormat, boolean now) {
        String format = "yyyyMMdd";
        DateTime date = DateUtil.date();
        String today = DateUtil.format(date, format);
        if (StrUtil.isNotBlank(startDay)) {
            startDay = startDay.trim().replace("-", "");
        } else {
            startDay = today;
        }
        if (StrUtil.isNotBlank(endDay)) {
            endDay = endDay.trim().replace("-", "");
        } else {
            endDay = today;
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM-dd";
        }
        DateTime startDate = DateUtil.parse(startDay, format);
        DateTime endDate = DateUtil.parse(endDay, format);
        if (now && DateUtil.isIn(date, startDate, endDate)) {
            endDate = date;
        }
        List<DateTime> ranges = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
        List<String> days = new ArrayList<>(ranges.size());
        for (DateTime range : ranges) {
            days.add(DateUtil.format(range, outFormat));
        }
        return days;
    }


    /**
     * 获取一年的(月份集合)
     *
     * @param year      默认当年
     * @param outFormat 返回月份格式 默认 yyyy-MM
     * @param now       false 12月份 true 截止到当前月份
     * @return
     */
    public static List<String> getMonthOfYear(String year, String outFormat, boolean now) {
        String currentYear = DateUtil.thisYear() + "";
        if (StrUtil.isBlank(year)) {
            year = currentYear;
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM";
        }
        DateTime startDate = DateUtil.parse(year + "0101", "yyyyMMdd");
        DateTime endDate = DateUtil.endOfYear(startDate);
        if (now && currentYear.equals(year)) {
            endDate = new DateTime();
        }
        List<DateTime> monthList = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
        List<String> months = new ArrayList<>();
        for (DateTime range : monthList) {
            months.add(DateUtil.format(range, outFormat));
        }
        return months;
    }

    /**
     * 获取指定月份前step的(月份集合)
     *
     * @param month     默认当前月份
     * @param outFormat 返回月份格式 默认 yyyy-MM
     * @param step      默认往前12月份
     * @return
     */
    public static List<String> getRecentTwelveMonths(String month, String outFormat, Integer step) {
        DateTime endMonth;
        if (StrUtil.isBlank(month)) {
            endMonth = DateUtil.parse(DateUtil.format(new Date(), "yyyyMM"));
        } else {
            month = month.trim().replace("-", "");
            endMonth = DateUtil.parse(month, "yyyyMM");
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM";
        }
        DateTime beginMonth = DateUtil.offsetMonth(endMonth, step);
        List<DateTime> monthList = DateUtil.rangeToList(beginMonth, endMonth, DateField.MONTH);
        List<String> months = new ArrayList<>();
        for (DateTime range : monthList) {
            months.add(DateUtil.format(range, outFormat));
        }
        return months;
    }

    /**
     * 两个月份之间的 间隔所有月份数组
     *
     * @param startMonth 开始日期 默认当日
     * @param endMonth   结束日期 默认当日
     * @param outFormat  默认 yyyy-MM
     * @return
     */
    public static List<String> getMonthOfRangeDate(String startMonth, String endMonth, String outFormat) {
        String format = "yyyyMM";
        Integer year = DateUtil.thisYear();
        if (StrUtil.isNotBlank(startMonth)) {
            startMonth = startMonth.trim().replace("-", "");
        } else {
            startMonth = year + "01";
        }
        if (StrUtil.isNotBlank(endMonth)) {
            endMonth = endMonth.trim().replace("-", "");
        } else {
            endMonth = DateUtil.format(DateUtil.offsetMonth(new Date(), -1), "yyyyMM");
        }
        if (StrUtil.isBlank(outFormat)) {
            outFormat = "yyyy-MM";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        SimpleDateFormat out = new SimpleDateFormat(outFormat);
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startMonth);
            Date endDate = sdf.parse(endMonth);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {

                // 把日期添加到集合
                list.add(out.format(startDate));

                // 设置日期
                calendar.setTime(startDate);

                //把月数增加 1
                calendar.add(Calendar.MONTH, 1);

                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 小时数据补齐
     *
     * @param values    数据集合
     * @param key       属性(和value里面保持一致)默认 key
     * @param value     值(和value里面保持一致) 默认 value
     * @param day       日期 默认当日
     * @param outFormat 返回小时格式 默认 yyyy-MM-dd HH
     * @param now       是否返回截止到目前为止的小时
     * @return List
     */
    public static List<Map<String, Object>> makeUpHourValues(List<Map<String, Object>> values, String key, String value, String day, String outFormat, boolean now) {
        return makeUpDateValues(getHoursOfDay(day, outFormat, now), values, key, value);
    }

    /**
     * 天数数据补齐
     *
     * @param values    数据集合
     * @param key       属性(和value里面保持一致)默认 key
     * @param value     值(和value里面保持一致) 默认 value
     * @param month     日期 默认当日
     * @param outFormat 返回小时格式 默认 yyyy-MM-dd HH
     * @param now       是否返回截止到目前为止的小时
     * @return List
     */
    public static List<Map<String, Object>> makeUpDayValues(List<Map<String, Object>> values, String key, String value, String month, String outFormat, boolean now) {
        return makeUpDateValues(getDayOfMonth(month, outFormat, now), values, key, value);
    }

    /**
     * 月份数据补齐
     *
     * @param values    数据集合
     * @param key       属性(和value里面保持一致)默认 key
     * @param value     值(和value里面保持一致) 默认 value
     * @param year      日期 默认当日
     * @param outFormat 返回小时格式 默认 yyyy-MM-dd HH
     * @param now       是否返回截止到目前为止的小时
     * @return List
     */
    public static List<Map<String, Object>> makeUpMonthValues(List<Map<String, Object>> values, String key, String value, String year, String outFormat, boolean now) {
        return makeUpDateValues(getMonthOfYear(year, outFormat, now), values, key, value);
    }

    /**
     * 年份数据补齐
     *
     * @param values      年数据
     * @param dateKey     年属性
     * @param valueKey    年属性
     * @param betweenYear 年份，为空默认查询当年
     * @return List
     */
    public static List<Map<String, Object>> makeUpYearValues(List<Map<String, Object>> values, String dateKey, String valueKey, List<String> betweenYear) {
        return makeUpDateValues(betweenYear, values, dateKey, valueKey);
    }

    /**
     * 数据日期自动补齐
     *
     * @param dates    日期集合(格式和values保持一致)
     * @param values   数据集合
     * @param dateKey  key(格式和values保持一致)
     * @param valueKey value (格式和values保持一致)
     * @return
     */
    public static List<Map<String, Object>> makeUpDateValues(List<String> dates, List<Map<String, Object>> values, String dateKey, String valueKey) {
        if (StrUtil.isBlank(dateKey)) {
            dateKey = "key";
        }
        if (StrUtil.isBlank(valueKey)) {
            valueKey = "value";
        }
        List<Map<String, Object>> results = new ArrayList<>(dates.size());
        if (CollectionUtil.isEmpty(values)) {
            for (String date : dates) {
                HashMap<String, Object> map = new HashMap<>(2);
                map.put(dateKey, date);
                map.put(valueKey, null);
                results.add(map);
            }
            return results;
        }
        for (String date : dates) {
            Map<String, Object> result = MapUtil.newHashMap();
            for (Map<String, Object> value : values) {
                if (value.get(dateKey) != null && value.get(dateKey).toString().equalsIgnoreCase(date)) {
                    if (!value.containsKey(valueKey)) {
                        value.put(valueKey, null);
                    }
                    value.put(dateKey, value.get(dateKey));
                    result.putAll(value);
                    break;
                }
            }
            if (result.isEmpty()) {
                result.put(dateKey, date);
                result.put(valueKey, null);
            }
            results.add(result);
        }
        return results;
    }


    public static void main(String[] args) {
        System.out.println(getHoursOfDay("2023-06-29", "yyyy-MM-dd HH", true));
        System.out.println(getDayOfMonth("2023-06", "yyyy-MM-dd", true));
        System.out.println(getMonthOfYear("2023", "yyyy-MM", true));
    }
}
