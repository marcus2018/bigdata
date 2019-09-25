package com.example.storm.benchmark.util;

import com.example.storm.bean.ServiceException;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils4Vo {

    private static final HashMap<String, DateFormat> formats = new HashMap<String, DateFormat>();
    // 默认显示日期的格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIMEF_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String TIMEF_FORMAT_2 = "yyyy-MM-dd HH:mm";
    // 默认显示日期时间毫秒格式
    public static final String MSEL_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    // 默认显示简体中文日期的格式
    public static final String ZHCN_DATE_YEAR_MONTH = "yyyy-MM";
    // 默认显示简体中文日期的格式
    public static final String ZHCN_DATE_FORMAT = "yyyy年MM月dd日";
    // 默认显示简体中文日期的格式
    public static final String ZHCN_DATE_FORMAT2 = "MM月dd日";
    // 默认显示简体中文日期时间的格式
    public static final String ZHCN_TIME_FORMAT = "yyyy年MM月dd日HH时mm分ss秒";
    // 默认显示简体中文日期时间毫秒格式
    public static final String ZHCN_MSEL_FORMAT = "yyyy年MM月dd日HH时mm分ss秒SSS毫秒";
    // 获取日期串格式
    public static final String DATE_STR_FORMAT = "yyyyMMdd";
    // 获取日期时间串格式
    public static final String TIME_STR_FORMAT = "yyyyMMddHHmmss";
    // 获取日期时间毫秒串格式
    public static final String MSEL_STR_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String GMT_STR_FORMAT = "EEE d MMM yyyy HH:mm:ss";

    static {
        formats.put(DATE_FORMAT, new SimpleDateFormat(DATE_FORMAT));
        formats.put(TIMEF_FORMAT, new SimpleDateFormat(TIMEF_FORMAT));
        formats.put(TIME_FORMAT, new SimpleDateFormat(TIME_FORMAT));
        formats.put(TIMEF_FORMAT_2, new SimpleDateFormat(TIMEF_FORMAT_2));

        formats.put(MSEL_FORMAT, new SimpleDateFormat(MSEL_FORMAT));
        formats.put(ZHCN_DATE_FORMAT, new SimpleDateFormat(ZHCN_DATE_FORMAT));
        formats.put(ZHCN_DATE_YEAR_MONTH, new SimpleDateFormat(ZHCN_DATE_YEAR_MONTH));
        formats.put(ZHCN_DATE_FORMAT2, new SimpleDateFormat(ZHCN_DATE_FORMAT2));
        formats.put(ZHCN_TIME_FORMAT, new SimpleDateFormat(ZHCN_TIME_FORMAT));
        formats.put(ZHCN_MSEL_FORMAT, new SimpleDateFormat(ZHCN_MSEL_FORMAT));
        formats.put(DATE_STR_FORMAT, new SimpleDateFormat(DATE_STR_FORMAT));
        formats.put(TIME_STR_FORMAT, new SimpleDateFormat(TIME_STR_FORMAT));
        formats.put(MSEL_STR_FORMAT, new SimpleDateFormat(MSEL_STR_FORMAT));
        SimpleDateFormat sdf = new SimpleDateFormat(GMT_STR_FORMAT, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        formats.put(GMT_STR_FORMAT, sdf);
    }

    /**
     * 判断日期 是今天0 昨天-1 前天-2 一月内-3 更早1 Add comments here.
     *
     * @param date
     * @return
     */
    public static int judgmentDate(Date date) {
        String paramDate = DateUtils4Vo.dateToDateString(date, DATE_FORMAT);
        Date tempDate = getDate(paramDate, DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String d = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE);
        Date today = getDate(d, DATE_FORMAT);
        if (tempDate.compareTo(today) == 0) {
            return 0;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        d = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
        today = getDate(d, DATE_FORMAT);
        if (tempDate.compareTo(today) == 0) {
            return -1;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        d = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
        today = getDate(d, DATE_FORMAT);
        if (tempDate.compareTo(today) == 0) {
            return -2;
        }
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        calendar.add(Calendar.MONTH, -1);
        d = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
        today = getDate(d, DATE_FORMAT);
        if (tempDate.compareTo(today) > 0) {
            return -3;
        }
        return 1;
    }

    /**
     * 获取今天的日期，格式如：2006-11-09
     *
     * @return String - 返回今天的日期
     */
    public static String getToday() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 根据年份、周数获取当前的日期
     *
     * @param year
     * @param week
     * @return
     */
    public static String getDateByWeek(String year, String week) {
        if (StringVerifyUtils.isBlank(year) || StringVerifyUtils.isBlank(week)) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week));
        cal.set(Calendar.DAY_OF_WEEK, 2); // 1表示周日，2表示周一，7表示周六
        Date date = cal.getTime();
        return dateToDateString(date, DATE_FORMAT);
    }

    /**
     * 获取今天的日期，格式自定
     *
     * @param pattern - 设定显示格式
     * @return String - 返回今天的日期
     */
    public static String getToday(String pattern) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干天的时间 例如当前时间2006-05-16 间隔天数30天，则返回2006-04-16
     *
     * @param days - 间隔天数
     * @return String - 返回当时的时间
     */
    public static String getInternalTimeByDay(int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到指定时间的前/后若干天的时间 例如当前时间2006-05-16 间隔天数30天，则返回2006-04-16
     *
     * @param days - 间隔天数
     * @return String - 返回当时的时间
     */
    public static String getInternalTimeByDay(Date theDate, int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(theDate);
        SimpleDateFormat sdf = new SimpleDateFormat(TIMEF_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到指定时间的前/后若干天的时间 例如当前时间2006-05-16 间隔天数30天，则返回2006-04-16
     *
     * @param theDate - 指定日期
     * @param days    - 间隔天数
     * @param pattern - 日期格式
     * @return String - 返回当时的时间 add by lisonglin 2008-02-02
     */
    public static String getInternalTimeByDay(Date theDate, int days, String pattern) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(theDate);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干天的时间 例如当前时间2006-05-16 间隔天数30天，则返回2006-04-16
     *
     * @param days    - 间隔天数
     * @param pattern - 设定显示格式
     * @return String - 根据显示格式返回当时的时间
     */
    public static String getInternalTimeByDay(int days, String pattern) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干月的时间 例如当前时间2006-05-16 间隔月数3月，则返回
     *
     * @param months - 间隔月数
     * @return - 返回当时的时间
     */
    public static String getInternalTimeByMonth(int months) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.MONTH, months);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干月的时间 例如当前时间2006-05-16 间隔月数3月，则返回2006-02-16
     *
     * @param months  - 间隔月数
     * @param pattern - 设定显示格式
     * @return - 根据显示格式返回当时的时间
     */
    public static String getInternalTimeByMonth(int months, String pattern) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.MONTH, months);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干月的时间 例如当前时间2006-05-16 间隔月数3月，则返回2006-02-16
     *
     * @param months  - 间隔月数
     * @return - 根据显示格式返回当时的时间
     */
    public static long getMillisTimeByMonth(int months) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MONTH, months);
        return now.getTimeInMillis();
    }

    /**
     * 获取当前日期的years年后的一个time
     *
     * @param years
     * @return long
     */
    public static long getMillisTimeByYear(int years) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.YEAR, years);
        return now.getTimeInMillis();
    }

    /**
     * 得到中文日期
     *
     * @param dateStr - 日期串，格式为“yyyy-MM-dd”
     * @return String - 返回中文日期，格式为“yyyy年MM月dd日”
     */
    public static String chinaDate(String dateStr) {
        if (StringVerifyUtils.isBlank(dateStr)) {
            return "";
        }
        Date d = getDate(dateStr, DATE_FORMAT);
        SimpleDateFormat sdf = new SimpleDateFormat(ZHCN_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(d));
    }

    /**
     * 得到中文日期,自定设置格式
     *
     * @param dateStr    - 需要改变格式的时间串
     * @param inPattern  - 时间串的格式
     * @param outPattern - 改为时间串的格式
     * @return String - 根据outPattern格式返回时间
     */
    public static String alterDateByDynamic(String dateStr, String inPattern, String outPattern) {
        if (StringVerifyUtils.isBlank(dateStr)) {
            return "";
        }
        Date d = getDate(dateStr, inPattern);
        SimpleDateFormat sdf = new SimpleDateFormat(outPattern);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(d));
    }

    /**
     * 比较当前日期和指定日期 return boolean 如果当前日期在指定日期之后返回true否则返回flase
     *
     * @param dateStr 指定日期
     * @param pattern 指定日期的格式
     * @return boolean
     */
    public static boolean dateCompare(String dateStr, String pattern) {
        boolean bea = false;
        DateFormat sdf_d = getDateFormat(pattern);
        String isDate = sdf_d.format(new Date());
        Date date1;
        Date date0;
        try {
            date1 = sdf_d.parse(dateStr);
            date0 = sdf_d.parse(isDate);
            if (date0.after(date1)) {
                bea = true;
            }
            return bea;
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 比较指定两日期,如果dateStr1晚于dateStr2则return true;
     *
     * @param dateStr1 指定日期
     * @param dateStr2 指定日期
     * @param pattern  指定日期的格式
     * @return boolean
     */
    public static boolean dateCompare(String dateStr1, String dateStr2, String pattern) {
        boolean bea = false;
        Date date1;
        Date date0;
        DateFormat sdf_d = getDateFormat(pattern);
        try {
            date1 = sdf_d.parse(dateStr1);
            date0 = sdf_d.parse(dateStr2);
            if (date0.after(date1)) {
                bea = true;
            }
            return bea;
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 设置间隔数后返回时间
     *
     * @param type 间隔类型 秒或者天 秒的类型为s,天的类型为d
     * @param 间隔数字 比如1秒或者一天
     * @return String 返回时间格式为“yyyy-MM-dd HH:mm:ss”
     */
    public static String dateAdd(String type, int i) {
        DateFormat df = getDateFormat(TIMEF_FORMAT);
        String str = getToday(TIMEF_FORMAT);
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        if (type.equals("s")) {
            int s = c.get(Calendar.SECOND);
            s = s + i;
            c.set(Calendar.SECOND, s);
            str = df.format(c.getTime());
        } else if (type.equals("d")) {
            int d = c.get(Calendar.DAY_OF_MONTH); // 取出“日”数
            d = d + i;
            c.set(Calendar.DAY_OF_MONTH, d); // 将“日”数设置回去
            str = df.format(c.getTime());
        }
        return str;
    }

    /**
     * 设置间隔数后返回时间
     *
     * @param type 间隔类型 秒或者天 秒的类型为s,天的类型为d
     * @param 间隔数字 比如1秒或者一天
     * @return String 返回时间格式为“yyyy-MM-dd HH:mm:ss”
     */
    public static String dateAdd(Date date, String type, int i) {
        DateFormat df = getDateFormat(TIMEF_FORMAT);
        String str = getToday(TIMEF_FORMAT);
        Calendar c = Calendar.getInstance(); // 当时的日期和时间
        c.setTime(date);
        if (type.equals("s")) {
            int s = c.get(Calendar.SECOND);
            s = s + i;
            c.set(Calendar.SECOND, s);
            str = df.format(c.getTime());
        } else if (type.equals("d")) {
            int d = c.get(Calendar.DAY_OF_MONTH); // 取出“日”数
            d = d + i;
            c.set(Calendar.DAY_OF_MONTH, d); // 将“日”数设置回去
            str = df.format(c.getTime());
        }
        return str;
    }

    /**
     * 得到当前日期，如"2001-03-16".
     *
     * @version 1.0
     * @author wanghaibo.
     */
    public static String curDate() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-MM-dd";
        // String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        DateFormat sdf = getDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前详细日期、时间，如"2001-03-16 20:34:20".
     */
    public static String curTime() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-MM-dd";
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        DateFormat sdf = getDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前详细日期、时间，如"2001-03-16 20:34:20".
     *
     * @version 1.0
     */
    public static String getTimeAfter(int n) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MINUTE, n);
        // String DATE_FORMAT = "yyyy-MM-dd";
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        DateFormat sdf = getDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干天的时间
     *
     * @param day - 间隔时间
     * @return - 返回当时的时间 例如当前时间2003-05-16 间隔天数30天，则返回2003-04-16
     */
    public static String getInternalTime(int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-MM-dd";
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到当前时间的前/后若干天的时间
     *
     * @param day - 间隔时间
     * @return - 返回当时的时间 例如当前时间2003-05-16 间隔天数30天，则返回2003-04-16
     */
    public static long getInternalTimeMillis(int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-MM-dd";
        // String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        // java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
        // DATE_FORMAT);
        // sdf.setTimeZone(TimeZone.getDefault());
        now.add(Calendar.DATE, days);
        return now.getTimeInMillis();
    }

    /**
     * 得到当前时间的前/后若干天的时间
     *
     * @param currentTime - 当前时间
     * @param iHour       - 间隔时间
     * @return - 返回当时的时间 例如当前时间2003-05-16 08:10:10 间隔时间3小时，则返回2003-05-16
     * 05:10:10
     */
    public static String getTimeOut(String currentTime, int iHour) {
        try {
            DateFormat sdf = getDateFormat(TIMEF_FORMAT);
            Date result = sdf.parse(currentTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(result);
            cal.add(Calendar.HOUR, iHour);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 得到传入时间的前/后若干天的时间
     *
     * @param currentTime - 当前时间
     * @param iHour       - 入参时间
     * @return - 返回当时的时间 例如当前时间2003-05-16间隔时间3小时，则返回2003-05-16 03:00:00
     */
    public static String getTimeLastOut(String currentTime, int iHour) {
        try {
            DateFormat sdf = getDateFormat(DATE_FORMAT);
            Date result = sdf.parse(currentTime);
            DateFormat sdf2 = getDateFormat(TIMEF_FORMAT);
            Calendar cal = Calendar.getInstance();
            cal.setTime(result);
            cal.add(Calendar.HOUR, iHour);
            return sdf2.format(cal.getTime());
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 返回比当前日期时间晚N分钟的一个yyyy-MM-dd HH:mm:ss的日期串晚的分钟数可由输入参数currDate,minute控制
     *
     * @param currDate
     * @param minute
     * @return 返回延迟N分钟后的时间串
     */
    public static String getCurrDateNextMinute(String currDate, int minute) {
        try {
            DateFormat sdf = getDateFormat(TIMEF_FORMAT);
            Date result = sdf.parse(currDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(result);
            cal.add(Calendar.MINUTE, minute);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 得到当前月底的前/后若干天的时间
     *
     * @param days - 间隔时间
     * @return - 返回当时的时间
     */
    public static String getInternalTimeByLastDay(int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-MM-dd";
        // String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        DateFormat sdf = getDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        int maxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        now.set(Calendar.DATE, maxDay);
        now.add(Calendar.DATE, days);
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到前一天日期字符串
     *
     * @param dateStr String 时间字符串
     * @param fmt     String 时间格式
     * @return String 返回值
     */
    public static String getDateStr(String dateStr, String fmt) {
        try {
            if (StringVerifyUtils.isBlank(dateStr)) {
                return "";
            }
            DateFormat sdf = getDateFormat(fmt);
            Date d = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DATE, -1);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 得到时间串
     *
     * @param dateStr String 时间字符串
     * @return String 返回值
     */
    public static Date getDate(String dateStr) {
        try {
            if (StringVerifyUtils.isBlank(dateStr)) {
                return null;
            }
            DateFormat sdf = getDateFormat(TIMEF_FORMAT);
            Date d = sdf.parse(dateStr);
            return d;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 得到时分秒清零后的日期
     *
     * @param dateStr String 时间字符串
     * @return String 返回值
     */
    public static Date getDateYYMMDD(String dateStr) {
        return getDate(getDateStr(dateStr,DATE_FORMAT)+" 23:59:59");
    }

    /**
     * @return 得到当前时间目录例如 030524
     */
    public static String getCurrTimeDir() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        DateFormat sdf = getDateFormat("yyMMdd");
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * @return 得到上个月月份 如200505
     */
    public static String getYesterM() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MONTH, -2);
        DateFormat sdf = getDateFormat("yyyyMM");
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * @return 得到本年度年份 如2005
     */
    public static String getYear() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // now.add(Calendar.MONTH,-1);
        DateFormat sdf = getDateFormat("yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * @return 取得季度
     */
    public static String getQuarter() {
        String quarter = getYear();
        switch (Calendar.getInstance().get(Calendar.MONDAY)) {
            case 0:
            case 1:
            case 2:
                quarter += "1";
                break;
            case 3:
            case 4:
            case 5:
                quarter += "2";
                break;
            case 6:
            case 7:
            case 8:
                quarter += "3";
                break;
            case 9:
            case 10:
            case 11:
                quarter += "4";
                break;
        }
        return quarter;
    }

    /**
     * @return 得到本月月份 如09
     */
    public static String getMonth() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MONTH, 0);
        DateFormat sdf = getDateFormat("MM");
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * @return 得到本月月份 如09
     */
    public static int getMonth_2() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MONTH, 0);
        DateFormat sdf = getDateFormat("M");
        sdf.setTimeZone(TimeZone.getDefault());
        String month = (sdf.format(now.getTime()));
        return Integer.parseInt(month);
    }

    /**
     * 得到下一个月分，包括年，例如： 2003－1 月份的上一个月份是2002－12
     *
     * @param year
     * @param month
     * @return
     */
    public static String[] getBeforeMonth(String year, String month) {
        String[] time = new String[2];
        if (month.equals("12")) {
            time[1] = "01";
            time[0] = String.valueOf(Integer.parseInt(year) + 1);
        } else {
            int iMonth = Integer.parseInt(month) + 1;
            if (iMonth < 10) {
                time[1] = "0" + iMonth;
            } else {
                time[1] = String.valueOf(iMonth);
            }
            time[0] = year;
        }
        return time;
    }

    /**
     * 得到上一个月
     *
     * @param year  年
     * @param month 月
     * @return String[] 0为年,1为月
     */
    public static String[] beforeMonth(String year, String month) {
        String[] time = new String[2];
        if (month.equals("1")) {
            time[1] = "12";
            time[0] = String.valueOf(Integer.parseInt(year) - 1);
        } else {
            time[1] = String.valueOf(Integer.parseInt(month) - 1);
            time[0] = year;
        }
        return time;
    }

    /**
     * 得到当前日期，按照页面日期控件格式，如"2001-3-16".
     *
     * @return String
     */
    public static String curSingleNumDate() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        // String DATE_FORMAT = "yyyy-M-d";
        // String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        // String DATE_FORMAT = "yyyyMMdd";
        DateFormat sdf = getDateFormat("yyyy-M-d");
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 取自当前日期后的第n天的日期
     *
     * @param int 之后n天
     * @return String
     */
    public static String getDateAfter(int n) {
        try {
            DateFormat sdf = getDateFormat(DATE_FORMAT);
            ;
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.add(Calendar.DAY_OF_MONTH, n);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 得到半年前的日期
     *
     * @return String
     */
    public static String getHalfYearBeforeStr() {
        GregorianCalendar cal = new GregorianCalendar();
        /** @ 取当前日期 */
        String month = "";
        int tMonth = cal.get(GregorianCalendar.MONTH) + 1;
        if (tMonth < 10) {
            month = "0" + tMonth;
        } else {
            month = "" + tMonth;
        }
        int tDay = cal.get(GregorianCalendar.DATE);
        String day = "";
        if (tDay < 10) {
            day = "0" + tDay;
        } else {
            day = "" + tDay;
        }
        // String endDate = "" + cal.get(GregorianCalendar.YEAR) + month + day;
        /** @ 取半年前日期 */
        cal.add(GregorianCalendar.MONTH, -6);
        tMonth = cal.get(GregorianCalendar.MONTH) + 1;
        if (tMonth < 10) {
            month = "0" + tMonth;
        } else {
            month = "" + tMonth;
        }
        tDay = cal.get(GregorianCalendar.DATE);
        day = "";
        if (tDay < 10) {
            day = "0" + tDay;
        } else {
            day = "" + tDay;
        }
        String beginDate = "" + cal.get(GregorianCalendar.YEAR) + month + day;
        return beginDate;
    }

    /**
     * 返回比当前日期晚几分钟的一个yyyy-MM-dd HH:mm:ss的日期串晚的分钟数可由输入参数minute控制
     *
     * @param minute
     * @return 返回延迟N分钟后的时间串
     */
    public static String getCurrentNextMinute(int minute) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, minute);
            DateFormat sdf = getDateFormat(TIMEF_FORMAT);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 得到当前分钟
     *
     * @return int
     */
    public static int getCurMin() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        int currentTime = Integer.parseInt(simpleDateFormat.format(date));
        return currentTime;
    }

    /**
     * @param formatStr
     * @return
     */
    private static DateFormat getDateFormat(String formatStr) {
        DateFormat format = formats.get(formatStr);
        if (null == format) {
            format = new SimpleDateFormat(formatStr);
            formats.put(formatStr, format);
        }
        return format;
    }

    /**
     * 日期字符串转换成另外一种格式
     *
     * @return
     */
    public static String dateStrToFormat(String dateStr, String srcDateFormat, String targetFormat) {
        try {
            if (StringVerifyUtils.isBlank(dateStr) ||
                    StringVerifyUtils.isBlank(srcDateFormat) ||
                    StringVerifyUtils.isBlank(targetFormat)) {
                return null;
            }
            DateFormat sdf = getDateFormat(srcDateFormat);
            Date d = sdf.parse(dateStr);
            return dateToDateString(d,targetFormat);
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    // public static Date getDate(String dateTimeStr)
    // {
    // return getDate(dateTimeStr,DATATIMEF_STR);
    // }

    /**
     * 按照默认formatStr的格式，转化dateTimeStr为Date类型 dateTimeStr必须是formatStr的形式
     *
     * @param dateTimeStr
     * @param formatStr
     * @return
     */
    public static Date getDate(String dateTimeStr, String formatStr) {
        try {
            if (StringVerifyUtils.isBlank(dateTimeStr)) {
                return null;
            }
            DateFormat sdf = getDateFormat(formatStr);
            synchronized (sdf) {
                Date d = sdf.parse(dateTimeStr);
                return d;
            }
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 将Date转换成字符串“yyyy-mm-dd hh:mm:ss”的字符串
     *
     * @param date 日期
     * @return String 字符串
     */
    public static String dateToDateString(Date date) {
        return dateToDateString(date, TIMEF_FORMAT);
    }

    /**
     * 将Date转换成formatStr格式的字符串
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String dateToDateString(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        try {
            DateFormat sdf = getDateFormat(formatStr);
            synchronized (sdf) {
                return sdf.format(date);
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * 将long类型数据转日期字符串
     *
     * @param times
     * @return
     */
    public static String longToDateString(Long times) {
        if (times == null) {
            return "";
        }
        if (times.toString().length() < 13) {
            times = times * 1000;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(times);
        return dateToDateString(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 将long类型数据转日期字符串
     *
     * @param times
     * @return
     */
    public static String longToDateString(Long times,String pattern) {
        if (times == null) {
            return "";
        }
        if (times.toString().length() < 13) {
            times = times * 1000;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(times);
        return dateToDateString(cal.getTime(), pattern);
    }

    /**
     * 将long类型数据转datetime字符串
     *
     * @param times
     * @return
     */
    public static String longToDateTimeString(long times) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(times);
        return dateToDateString(cal.getTime(), TIMEF_FORMAT);
    }

    /**
     * 下一天的日期
     *
     * @param times
     * @return
     */
    public static String dateAfterOneDay(long times) {
        DateFormat sdf = getDateFormat(DATE_FORMAT);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(times);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(cal.getTime());
    }

    /**
     * 返回一个yyyy-MM-dd HH:mm:ss 形式的日期时间字符串中的HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static String getTimeString(String dateTime) {
        return getTimeString(dateTime, TIMEF_FORMAT);
    }

    /**
     * 返回一个formatStr格式的日期时间字符串中的HH:mm:ss
     *
     * @param dateTime
     * @param formatStr
     * @return
     */
    public static String getTimeString(String dateTime, String formatStr) {
        Date d = getDate(dateTime, formatStr);
        String s = dateToDateString(d);
        return s.substring(TIMEF_FORMAT.indexOf('H'));
    }

    /**
     * 获取当前日期yyyy-MM-dd的形式
     *
     * @return
     */
    public static String getCurDate() {
        return dateToDateString(new Date(), DATE_FORMAT);
    }

    /**
     * 获取当前日期yyyyMMdd的形式
     *
     * @return
     */
    public static String getCurDateNoSplit() {
        return dateToDateString(new Date(), DATE_STR_FORMAT);
    }

    /**
     * 获取当前时间HH:mm:ss的形式
     *
     * @return
     */
    public static String getCurTime() {
        return dateToDateString(new Date(), TIME_FORMAT);
    }

    /**
     * 获取当前日期yyyy年MM月dd日的形式
     *
     * @return
     */
    public static String getCurZhCNDate() {
        return dateToDateString(new Date(), ZHCN_DATE_FORMAT);
    }

    /**
     * 获取当前日期时间yyyy-MM-dd HH:mm:ss的形式
     *
     * @return
     */
    public static String getCurDateTime() {
        return dateToDateString(new Date(), TIMEF_FORMAT);
    }

    /**
     * 获取当前日期时间yyyy年MM月dd日HH时mm分ss秒的形式
     *
     * @return
     */
    public static String getCurZhCNDateTime() {
        return dateToDateString(new Date(), ZHCN_TIME_FORMAT);
    }

    /**
     * 获取日期d的days天后的一个Date
     *
     * @param d
     * @param days
     * @return Date
     */
    public static Date getInternalDateByDay(Date d, int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.DATE, days);
        return now.getTime();
    }

    /**
     * 获取日期d的months月后的一个Date
     *
     * @param d
     * @param months
     * @return Date
     */
    public static Date getInternalDateByMon(Date d, int months) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.MONTH, months);
        return now.getTime();
    }

    /**
     * 获取日期d的years年后的一个Date
     *
     * @param d
     * @param years
     * @return Date
     */
    public static Date getInternalDateByYear(Date d, int years) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.YEAR, years);
        return now.getTime();
    }

    /**
     * 获取日期d的sec秒后的一个Date
     *
     * @param d
     * @param sec
     * @return Date
     */
    public static Date getInternalDateBySec(Date d, int sec) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.SECOND, sec);
        return now.getTime();
    }

    /**
     * 获取日期d的min分前的一个Date
     *
     * @param d
     * @param min
     * @return Date
     */
    public static Date getOneMinAgoTime(Date d, int min) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - min);
        return now.getTime();
    }

    /**
     * 获取日期d的min分后的一个Date
     *
     * @param d
     * @param min
     * @return Date
     */
    public static Date getInternalDateByMin(Date d, int min) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.MINUTE, min);
        return now.getTime();
    }

    /**
     * 获取日期d的hours小时后的一个Date
     *
     * @param d
     * @param hours
     * @return Date
     */
    public static Date getInternalDateByHour(Date d, int hours) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.HOUR_OF_DAY, hours);
        return now.getTime();
    }

    /**
     * 根据一个日期字符串，返回日期格式，目前支持4种 如果都不是，则返回null
     *
     * @param DateString
     * @return 返回日期格式，目前支持4种
     */
    public static String getFormateStr(String DateString) {
        String patternStr1 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"; // "yyyy-MM-dd"
        String patternStr2 = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"; // "yyyy-MM-dd
        // HH:mm:ss";
        String patternStr3 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日"; // "yyyy年MM月dd日"
        String patternStr4 = "[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日[0-9]{1,2}时[0-9]{1,2}分[0-9]{1,2}秒"; // "yyyy年MM月dd日HH时mm分ss秒"
        Pattern p = Pattern.compile(patternStr1);
        Matcher m = p.matcher(DateString);
        boolean b = m.matches();
        if (b) {
            return DATE_FORMAT;
        }
        p = Pattern.compile(patternStr2);
        m = p.matcher(DateString);
        b = m.matches();
        if (b) {
            return TIMEF_FORMAT;
        }
        p = Pattern.compile(patternStr3);
        m = p.matcher(DateString);
        b = m.matches();
        if (b) {
            return ZHCN_DATE_FORMAT;
        }
        p = Pattern.compile(patternStr4);
        m = p.matcher(DateString);
        b = m.matches();
        if (b) {
            return ZHCN_TIME_FORMAT;
        }
        return null;
    }

    /**
     * 将一个"yyyy-MM-dd HH:mm:ss"字符串，转换成"yyyy年MM月dd日HH时mm分ss秒"的字符串
     *
     * @param dateStr
     * @return
     */
    public static String getZhCNDateTime(String dateStr) {
        Date d = getDate(dateStr);
        return dateToDateString(d, ZHCN_TIME_FORMAT);
    }

    /**
     * 将一个"yyyy-MM-dd"字符串，转换成"yyyy年MM月dd日"的字符串
     *
     * @param dateStr
     * @return
     */
    public static String getZhCNDate(String dateStr) {
        Date d = getDate(dateStr, DATE_FORMAT);
        return dateToDateString(d, ZHCN_DATE_FORMAT);
    }

    /**
     * 将dateStr从fmtFrom转换到fmtTo的格式
     *
     * @param dateStr
     * @param fmtFrom
     * @param fmtTo
     * @return
     */
    public static String getDateStr(String dateStr, String fmtFrom, String fmtTo) {
        Date d = getDate(dateStr, fmtFrom);
        return dateToDateString(d, fmtTo);
    }

    /**
     * 将小时数换算成返回以毫秒为单位的时间
     *
     * @param hours
     * @return
     */
    public static long getMicroSec(BigDecimal hours) {
        BigDecimal bd;
        bd = hours.multiply(new BigDecimal(3600 * 1000));
        return bd.longValue();
    }

    /**
     * 获取Date中的分钟
     *
     * @param d
     * @return
     */
    public static int getMin(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MINUTE);
    }

    /**
     * 获取xxxx-xx-xx的日
     *
     * @param d
     * @return
     */
    public static int getDay(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月份，1-12月
     *
     * @param d
     * @return
     */
    public static int getMonth(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取19xx,20xx形式的年
     *
     * @param d
     * @return
     */
    public static int getYear(Date d) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        return now.get(Calendar.YEAR);
    }

    /**
     * 得到d的上个月的年份+月份,如200505
     *
     * @return
     */
    public static String getYearMonthOfLastMon(Date d) {
        Date newdate = getInternalDateByMon(d, -1);
        String year = String.valueOf(getYear(newdate));
        String month = String.valueOf(getMonth(newdate));
        return year + month;
    }

    /**
     * 得到当前日期的年和月如2005-09
     *
     * @return String
     */
    public static String getCurYearMonth() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "yyyy-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 得到指定日期的年和月如2005-09
     *
     * @return String
     */
    public static String getYearMonth(Date date) {
        // Calendar now = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "yyyy-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(date));
    }

    /**
     * 得到当前日期的年和月如200509
     *
     * @return String
     */
    public static String getCurrentYearMonth() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "yyyyMM";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * @param year
     * @param month
     * @return
     */
    public static Date getNextMonth(String year, String month) {
        String datestr = year + "-" + month + "-01";
        Date date = getDate(datestr, DATE_FORMAT);
        return getInternalDateByMon(date, 1);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    public static Date getLastMonth(String year, String month) {
        String datestr = year + "-" + month + "-01";
        Date date = getDate(datestr, DATE_FORMAT);
        return getInternalDateByMon(date, -1);
    }

    /**
     * 得到日期d，按照页面日期控件格式，如"2001-3-16"
     *
     * @param d
     * @return
     */
    public static String getSingleNumDate(Date d) {
        return dateToDateString(d, DATE_FORMAT);
    }

    /**
     * 得到d半年前的日期,"yyyy-MM-dd"
     *
     * @param d
     * @return
     */
    public static String getHalfYearBeforeStr(Date d) {
        return dateToDateString(getInternalDateByMon(d, -6), DATE_FORMAT);
    }

    /**
     * 得到当前日期D的月底的前/后若干天的时间,<0表示之前，>0表示之后
     *
     * @param d
     * @param days
     * @return
     */
    public static String getInternalDateByLastDay(Date d, int days) {
        return dateToDateString(getInternalDateByDay(d, days), DATE_FORMAT);
    }

    public static java.sql.Date getSqlDate(String dateTimeStr) {
        // DateUtils4Vo.getTIME_STR_FORMAT
        java.sql.Date d = new java.sql.Date(DateUtils4Vo.getDate(dateTimeStr, DateUtils4Vo.TIME_STR_FORMAT).getTime());
        // d.setHours(Integer.parseInt(dateTimeStr.substring(8,10)));
        // d.setMinutes(Integer.parseInt(dateTimeStr.substring(10,12)));
        // d.setSeconds(Integer.parseInt(dateTimeStr.substring(12,14)));
        return d;
    }

    /**
     * 取得最近一年的下拉列表 如：200702-200802
     *
     * @return
     */
    public static String getSelectMonth() {
        String optionHtml = "";
        String nowdate = "";
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        DateFormat formater = getDateFormat("yyyyMM");
        for (int i = 0; i < 12; i++) {
            cale.add(Calendar.MONTH, -1);
            nowdate = formater.format(cale.getTime());
            optionHtml = optionHtml + "<option value=" + "" + nowdate + "" + ">" + nowdate + "</option>";
        }
        return optionHtml;
    }

    /**
     * 判断当前时间是否在某个时间段内（两个时间不能相同） 如果在时间段内，返回true
     *
     * @param dateStr1 指定日期
     * @param dateStr2 指定日期
     * @param pattern  指定日期的格式
     * @return add by lisonglin 2008-01-16
     */
    public static boolean dateBetween(String dateStr1, String dateStr2, String pattern) {
        boolean flag1 = dateCompare(dateStr1, pattern);
        boolean flag2 = dateCompare(dateStr2, pattern);
        return (flag1 & !flag2);
    }

    /**
     * 得到当前月份后（前）若干月的第一天
     *
     * @param months 相隔的月份
     * @return 格式："2008-01-01" add by lisonglin 2008-01-16
     */
    public static String getAfterMonthFirstDay(int months) {
        Date newdate = getInternalDateByMon(new Date(), months);
        return dateToDateString(newdate, "yyyy-MM") + "-" + "01";
    }

    /**
     * 得到当前年的后两位
     *
     * @return String 格式：08 如：2008 返回 08 add by lisonglin 2008-01-20
     */
    public static String getCurYearLastTwoNumb() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "yy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /* 得到当前月,格式为MM.add by wufan 20080116 */
    public static String getCurrMonth() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.MONTH, 0);
        String DATE_FORMAT = "MM";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(now.getTime());

    }

    /* 获得当前时间i个月的最后一天,格式YYYYMMDD23:59:59 add by wufan 20080116 */
    public static Date getBeforDate(int i) {
        String str = getInternalTimeByMonth(i, "yyyyMM") + "01235959";
        return getInternalDateByDay(getDate(str), -1);
    }

    /* add by wufan 20080116 */
    public static String getYears(int i) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.add(Calendar.YEAR, i);
        String DATE_FORMAT = "yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /* 得到月份的最后一天 add by wufan 20080116,传入200702得到200701的最后一天 */
    public static Date getAfterDate(String year, String month) {
        String time = "";
        String hours = "235959";
        if ("12".equals(month)) {
            time = String.valueOf((Integer.parseInt(year) + 1)) + "0101" + hours;
        } else {
            String str1 = "";
            int monthadd1 = (Integer.parseInt(month) + 1);
            if (monthadd1 < 10) {
                str1 = String.valueOf(monthadd1);
                time = year + "0" + str1 + "01" + hours;
            } else {
                str1 = String.valueOf(monthadd1);
                time = year + str1 + "01235959";
            }
        }
        return getInternalDateByDay(getDate(time), -1);
    }

    /**
     * 工作日天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getWorkingDay(Date startDate, Date endDate) {
        Calendar cal_start = Calendar.getInstance();
        Calendar cal_end = Calendar.getInstance();
        cal_start.setTime(startDate);
        cal_end.setTime(endDate);
        return getWorkingDay(cal_start, cal_end);
    }

    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 计算2个日期之间的相隔工作日天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getWorkingDay(Calendar d1, Calendar d2) {
        int result = -1;
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }

        // int betweendays = getDaysBetween(d1, d2);

        // int charge_date = 0;
        int charge_start_date = 0;// 开始日期的日期偏移量
        int charge_end_date = 0;// 结束日期的日期偏移量
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }
        // }
        result = (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7) * 5 + charge_start_date - charge_end_date;
        // System.out.println("charge_start_date>" + charge_start_date);
        // System.out.println("charge_end_date>" + charge_end_date);
        // System.out.println("between day is-->" + betweendays);
        return result;
    }

    public static String getChineseWeek(Calendar date) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

        // System.out.println(dayNames[dayOfWeek - 1]);
        return dayNames[dayOfWeek - 1];

    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date
     * @return
     */
    public static Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    /**
     * 休息日天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getHolidays(Calendar d1, Calendar d2) {
        return getDaysBetween(d1, d2) - getWorkingDay(d1, d2);

    }

    /**
     * 计算当前时间(year,month)的月份的天数
     *
     * @param d1
     * @param d2
     * @return tanghua add by 2008-1-29
     */
    public static int getMonthHasDays(int year, int month) {
        int monthDays = 0;
        int singleMonth[] = {1, 3, 5, 7, 8, 10, 12};
        int doubleMonth[] = {4, 6, 9, 11};
        // int response = Arrays.binarySearch(singleMonth, month);
        if (Arrays.binarySearch(singleMonth, month) >= 0) {
            monthDays = 31;
        } else if (Arrays.binarySearch(doubleMonth, month) >= 0) {
            monthDays = 30;
        } else {
            // 对于2月份的天数处理,判断是否为闰年
            if (DateUtils4Vo.YearIsLeapYear(year)) {
                monthDays = 29;
            } else {
                monthDays = 28;
            }
        }
        return monthDays;
    }

    /**
     * 判断是否为闰年
     *
     * @param d1
     * @param d2
     * @return tanghua add by 2008-1-29
     */
    public static boolean YearIsLeapYear(int year) {
        boolean flag = false;
        // 闰年判断的条件:1.能被4整除,但不能被100整除;2.能被400整除
        if ((year % 4 == 0) && (year % 100 != 0)) {
            flag = true;
        } else if (year % 400 == 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取经过转换后的科学计数值的数值字符串
     *
     * @param siceNum double
     * @return String
     */
    public static String getConverNumValue(double siceNum) {
        String siceNumStr = String.valueOf(siceNum); // 将double类型转换成string(可能格式如：2.0000000001E8)

        // 定义一个转换后的字符串，初始值为siceNumStr
        String resultStr = siceNumStr;

        String ePrefix;// 用于记录科学计数值的“E”前面的部分值

        String eSuffix;// 用于记录科学计数值的“E”后面的部分值

        int ePlace = siceNumStr.indexOf("E"); // 获得"E"所在字符串地址中的位置

        // 只有传入的double数值是采用科学计数的方式，才需要处理
        if (ePlace != -1) {
            ePrefix = siceNumStr.substring(0, ePlace);
            int dotPlace = ePrefix.indexOf("."); // 获得"."所在字符串地址
            int strLength = ePrefix.length(); // 获得字符长度
            int dotBKLength = strLength - dotPlace - 1; // 获得小数点后的位数

            ePrefix = ePrefix.substring(0, ePrefix.indexOf(".")) + ePrefix.substring(ePrefix.indexOf(".") + 1); // 过滤小数点

            eSuffix = siceNumStr.substring(ePlace + 1);// 获取科学计数值的“E”后面的部分

            int eSuffixInt = Integer.parseInt(eSuffix);

            int k = eSuffixInt - dotBKLength;// 计算E后的数值和小数点后的位数，用于定位数值类型的处理

            if (k > 0) {
                // 可以将小数点去掉，且需要补0
                String zeroStr = String.valueOf((long) Math.pow(10, k)).replaceAll("1", "");
                resultStr = ePrefix + zeroStr;
            } else if (k < 0) {
                // 小数点后位数太多，不能去掉小数位：采用 小数点前面部分值+“.”+后面部分值 来的其值
                resultStr = ePrefix.substring(0, dotPlace + eSuffixInt) + "."
                        + ePrefix.substring(dotPlace + eSuffixInt);
            } else if (k == 0) {
                // 恰好可以将小数点去掉，且不需要补0
                resultStr = ePrefix;
            }
        }

        // 把.0去掉
        if (resultStr.indexOf(".") != -1) {
            String str = resultStr.substring(resultStr.indexOf(".") + 1, resultStr.length());

            if (str.length() == 1) {
                if (str.equals("0")) {
                    resultStr = resultStr.substring(0, resultStr.indexOf("."));
                }
            }
        }
        return resultStr;
    }

    /**
     * trh 080308 根据制定的时间转换字符串得到当前指定的时间格式 如：yyyy-MM-dd 23:59:59 得到时间为当前日期的最后一刻
     * 返回一个指定的日期格式（yyyy-MM-dd HH:mm:ss）
     *
     * @param returnDateStyle
     * @param transFormatStyle
     * @return dateStr
     * @throws ParseException
     */
    public static Date getSpecialDate(Date date, String returnDateStyle, String transFormatStyle)
            throws ParseException {
        Date dateStr = null;
        DateFormat sdf = null;
        DateFormat returnSdf = null;
        if ((returnDateStyle != null) && (!returnDateStyle.equals("")) && (transFormatStyle != null)
                && (!transFormatStyle.equals(""))) {
            returnSdf = getDateFormat(returnDateStyle);
            sdf = getDateFormat(transFormatStyle);
            ;
            String dateString = sdf.format(date);
            Calendar myDate = Calendar.getInstance();
            myDate.setTime(returnSdf.parse(dateString));
            dateStr = myDate.getTime();
        }
        return dateStr;
    }

    /**
     * 取字符串开始到len的显示字符
     *
     * @param str 显示字符串
     * @param len 显示长度
     * @return 显示字符
     */
    public static String OverflowHidden(String str, int len) {
        if (StringVerifyUtils.isBlank(str) || str.getBytes().length <= len) {
            return str;
        }
        byte b[] = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = str.getBytes()[i];
        }
        int blen = new String(b).length();
        if (!new String(b).equals(str.substring(0, blen))) {
            return str.substring(0, blen - 1) + "...";
        }
        return new String(b) + "...";
    }

    /**
     * 比较一个年月份是否在当前年月的前或后i个之内
     * 如:200807是否在当前年月(200809)的前2个月之内,是(前2个个月是200807和200808):返回true
     * 200811是否在当前年月(200809)的后1个月之内,否(后一个月是200810):返回false
     *
     * @param i         ,正数:i=2表示当前月份的 后2个月 ; 负数:i=-1表示当前月份的 前1个月
     * @param yearMonth ,要比较的年月:如200809
     * @return tanghua by 2008-9-6
     */
    public static boolean getBeforeNowMonth(int i, String yearMonth) {
        List list = new ArrayList();
        if (i == 0) {
            list.add(getInternalTimeByMonth(0, "yyyyMM"));
        }
        if (i > 0) {
            for (int k = 1; k <= i; k++) {
                list.add(getInternalTimeByMonth(k, "yyyyMM"));
            }
        }
        if (i < 0) {
            for (int k = -1; k >= i; k--) {
                list.add(getInternalTimeByMonth(k, "yyyyMM"));
            }
        }
        return list.contains(yearMonth);
    }

    /** */
    /**
     * 取得日期所在月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstMonthDay(Date date) {
        initCalendar(date);
        int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
        gc.add(Calendar.DAY_OF_MONTH, (1 - dayOfMonth));
        return gc.getTime();
    }

    /** */
    /**
     * 取得日期所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastMonthDay(Date date) {
        initCalendar(date);
        int dayOfMonth = gc.get(Calendar.DAY_OF_MONTH);
        int maxDaysOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
        gc.add(Calendar.DAY_OF_MONTH, (maxDaysOfMonth - dayOfMonth));
        return gc.getTime();
    }

    private static void initCalendar(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("argument date must be not null");
        }

        gc.clear();
        gc.setTime(date);
    }

    private static GregorianCalendar gc = null;

    static {
        gc = new GregorianCalendar(Locale.CHINA);
        gc.setLenient(true);
        gc.setFirstDayOfWeek(Calendar.MONDAY);
    }

    /**
     * 取得当前日期前后month个月及前后days天的日期，与date进行比较，如果大于返回true，则否返回false
     *
     * @param date  需要比较的日期
     * @param month 前后个月数量
     * @param days  前后天数数量
     * @return true|false auth: add by zhaodb 20081107
     */
    public static boolean getAfterMonthAndDay(Date date, String month, String days) {
        int m = 0;
        int d = 0;
        if (null != month && !"".equals(month)) {
            m = Integer.parseInt(month);
        }
        if (null != days && !"".equals(days)) {
            d = Integer.parseInt(days);
        }
        String ymd = getInternalTimeByMonth(m, "yyyyMMdd");
        Date date1 = getDate(ymd, "yyyyMMdd");
        String ymdend = getInternalTimeByDay(date1, d, "yyyyMMdd");
        DateFormat sdf = getDateFormat("yyyyMMdd");
        ;
        String ymdbef = sdf.format(date);
        return dateCompare(ymdend, ymdbef, "yyyyMMdd");
    }

    /**
     * 得到date 日期的年和月如2005-09
     *
     * @return String
     */
    public static String getDateYearMonth(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        String DATE_FORMAT = "yyyy-MM";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return (sdf.format(now.getTime()));
    }

    /**
     * 根据年份和月份获取当月天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                    days = 29;
                } else {
                    days = 28;
                }
        }
        return days;
    }

    /**
     * 根据日期字符串和分隔字符获取其年、月、日
     *
     * @param dateStr
     * @param separate
     * @return
     */
    public static Map<String, Integer> getYYMMDDByDateStr(String dateStr, String separate) {
        int index = dateStr.indexOf(separate);
        int year = Integer.parseInt(dateStr.substring(0, index));
        dateStr = dateStr.substring(index + 1);
        index = dateStr.indexOf(separate);
        int month = Integer.parseInt(dateStr.substring(0, index));
        dateStr = dateStr.substring(index + 1);
        int date = Integer.parseInt(dateStr);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Y", year);
        map.put("M", month);
        map.put("D", date);
        return map;
    }

    /**
     * 通过日期得到年龄
     *
     * @return
     */
    public static int getCurAgeByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        Calendar calBirt = Calendar.getInstance();
        calBirt.setTime(date);

        // date 在当前日期之前则返回0
        if (cal.before(calBirt))
            return 0;
        // 获取当前的年月日
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH);
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);

        // 获取传入的年月日
        int birtYear = calBirt.get(Calendar.YEAR);
        int birtMonth = calBirt.get(Calendar.MONTH);
        int birtDay = calBirt.get(Calendar.DAY_OF_MONTH);

        int age = nowYear - birtYear;
        // 对月份进行判断
        if (0 != age) {
            if (nowMonth < birtMonth) {
                age--;
            } else if (nowMonth == birtMonth) {
                if (nowDay < birtDay) {
                    age--;
                }
            }
        }
        return age;
    }

    /**
     * 根据参数，判断当前日期，是否是时间段的最后一天
     *
     * @return boolean
     */
    public static boolean checkIsLastDate(String dateType) {
        Calendar cal = Calendar.getInstance();
        // 获取当前的年月日
        int nowMonth = cal.get(Calendar.MONTH);
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);

        if ("每年".equals(dateType)) {
            if (nowMonth == 12 && nowDay == 31) {
                return true;
            }
        } else if ("每月".equals(dateType)) {
            int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (maxDay == nowDay) {
                return true;
            }
        } else if ("每周".equals(dateType)) {
            if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
                return true;
            }
        } else if ("每日".equals(dateType)) {
            return true;
        }

        return false;
    }

    /**
     * 返回当天之间的时间，格式为eye-MM-dd，起止时间为分钟
     *
     * @param
     * @return Map<String,Object>
     * @author cc.l
     * @serialData 2011-08-03
     */
    public static Map<String, String> getDayDate() {
        String sdate; // 开始时间
        String edate; // 结束时间
        Calendar cal = Calendar.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        int month = cal.get(Calendar.MONTH) + 1;
        sdate = cal.get(Calendar.YEAR) + "-" + month + "-" + cal.get(Calendar.DAY_OF_MONTH);
        edate = cal.get(Calendar.YEAR) + "-" + month + "-" + cal.get(Calendar.DAY_OF_MONTH);
        map.put("sdate", sdate);
        map.put("edate", edate);
        return map;
    }

    /**
     * 返回本周之间的时间，格式为yyyy-MM-dd，起止时间为天()
     *
     * @param
     * @return Map<String,Object>
     * @author cc.l
     * @serialData 2011-08-03
     */
    public static Map<String, String> getWeekDate() {
        String sdate; // 开始时间
        String edate; // 结束时间
        Calendar cal = Calendar.getInstance(); // 当天时间
        Calendar startcal = Calendar.getInstance(); // 本周开始时间
        Calendar endcal = Calendar.getInstance(); //
        Map<String, String> map = new HashMap<String, String>();
        startcal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_WEEK) + 1); // 本周第一天时间
        endcal.set(Calendar.DAY_OF_MONTH, startcal.get(Calendar.DAY_OF_MONTH) + 6); // 本周最后一天日期
        int startyear = startcal.get(Calendar.YEAR);
        int endyear = endcal.get(Calendar.YEAR);
        int startmonth = startcal.get(Calendar.MONTH) + 1;
        int endmonth = endcal.get(Calendar.MONTH) + 1;
        int startday = startcal.get(Calendar.DAY_OF_MONTH);
        int endday = endcal.get(Calendar.DAY_OF_MONTH);
        sdate = startyear + "-" + startmonth + "-" + startday;
        edate = endyear + "-" + endmonth + "-" + endday;
        map.put("sdate", sdate);
        map.put("edate", edate);
        // System.out.println(sdate+" "+edate+" week");
        return map;
    }

    /**
     * 返回本月之间的时间，格式为yyyy-MM-dd，起止时间为天(当天非本月的第一天)
     *
     * @param
     * @return Map<String,Object>
     * @author cc.l
     * @serialData 2011-08-03
     */
    public static Map<String, String> getMonthDate() {
        String sdate; // 开始时间
        String edate; // 结束时间
        Calendar cal = Calendar.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day1 = 1; // 默认为本月第一天
        int day2 = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        sdate = year + "-" + month + "-" + day1;
        edate = year + "-" + month + "-" + day2;
        map.put("sdate", sdate);
        map.put("edate", edate);
        // System.out.println(sdate+" "+edate+" month");
        return map;
    }

    /**
     * 返回本年之间的时间，格式为yyyyMM，起止时间为月(当月非本年的第一个月)
     *
     * @param
     * @return Map<String,Object>
     * @author cc.l
     * @serialData 2011-08-03
     */
    public static Map<String, String> getYearDate() {
        String sdate; // 开始时间
        String edate; // 结束时间
        Calendar cal = Calendar.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        int year = cal.get(Calendar.YEAR);

        sdate = year + "-01-01";
        edate = year + "-12-31";
        map.put("sdate", sdate);
        map.put("edate", edate);
        return map;
    }

    /**
     * 计算两个日期的时间差
     *
     * @param startday 开始日期
     * @param endday   结束日期
     * @return
     */
    public static boolean dateBetweenOfMinute(Date startday, Date endday, int num) {
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        long sl = startday.getTime();
        long el = endday.getTime();
        long ei = el - sl;
        int number = (int) (ei / (1000 * 60));
        if (num >= number) {
            return true;
        }
        return false;
    }

    /**
     * 得到指定日期是星期几
     *
     * @param dateTime
     * @param dateFormat
     * @return
     */
    public static String getWeek(String dateTime, String dateFormat) {
        Date date = getDate(dateTime, dateFormat);
        if (date == null)
            return null;
        GregorianCalendar gcd = new GregorianCalendar(Locale.CHINA);
        gcd.setLenient(true);
        gcd.setFirstDayOfWeek(Calendar.MONDAY);
        gcd.clear();
        gcd.setTime(date);
        return getChineseWeek(gcd);
    }

    /**
     * 根据传进来的日期，查看当前该日期是该月的第几周
     *
     * @param d
     * @return
     */
    public static int getNowDateWeek(Date d) {
        GregorianCalendar gcd = new GregorianCalendar(Locale.CHINA);
        gcd.setLenient(true);
        gcd.setFirstDayOfWeek(Calendar.MONDAY);
        gcd.clear();
        gcd.setTime(d);
        return gcd.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 根据传进来的日期，返回前CONUT个月的日期或之后COUNT个月的日期
     *
     * @param dataTime
     * @param formatStr       传入日期格式
     * @param conut
     * @param returnFormatStr 返回格式 ，默认：yyyy-MM-dd
     * @return yyyy-MM-dd 类同数据库格式
     * @throws ParseException
     */
    public static String getLastOrNextMonth(String dataTime, String formatStr, int conut, String... returnFormatStr) {
        try {
            DateFormat format = getDateFormat(formatStr);
            Date d = format.parse(dataTime);
            GregorianCalendar gcd = new GregorianCalendar(Locale.CHINA);
            gcd.setLenient(true);
            gcd.clear();
            gcd.setTime(d);
            gcd.add(Calendar.MONTH, conut);
            format = getDateFormat(DATE_FORMAT);
            return format.format(gcd.getTime());

        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 获取今天之后的7天的日期(不包括今天)，返回格式为yyyy-MM-dd
     *
     * @return
     */
    public static List<String> getDateAfter_Seven() {
        List<String> dateList = new ArrayList<String>();
        for (int i = 1; i < 8; i++) {
            dateList.add(getDateAfter(i));
        }
        return dateList;
    }

    /**
     * 指定集合里面的每个日期是星期几
     *
     * @param dateList
     * @return
     */
    public static List<String> getWeek(List<String> dateList) {
        List<String> weekList = new ArrayList<String>();
        int size = dateList.size();
        for (int i = 0; i < size; i++) {
            weekList.add(getWeek(dateList.get(i), DATE_FORMAT));
        }
        return weekList;
    }

    /**
     * 得到时间串
     *
     * @param dateStr String 时间字符串
     * @param fmt     String 时间格式
     * @return String 返回值
     */
    public static String getDateStr_2(String dateStr, String fmt) {
        try {
            if (StringVerifyUtils.isBlank(dateStr)) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            Date d = df.parse(dateStr);
            String newDate = sdf.format(d);
            return newDate;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 验证字符串是否匹配默认时间格式(yyyy-MM-dd)
     *
     * @param strIn
     * @return
     */
    public static boolean isDate(String strIn) {
        return isDate(strIn, DATE_FORMAT);
    }

    /**
     * 验证字符串是否匹配时间格式
     *
     * @param strIn     String 需要验证的字符串
     * @param formatStr String 需要匹配的时间格式
     * @return
     */
    public static boolean isDate(String strIn, String formatStr) {
        if (StringVerifyUtils.isBlank(strIn) || StringVerifyUtils.isBlank(formatStr)
                || strIn.length() > formatStr.length()) {
            return false;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            format.setLenient(false);
            format.parse(strIn);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 判断一个时间跟另一个时间是否相差xx分钟
     *
     * @param lastDate上一个日期
     * @param nextDate下一个日期
     * @param minute分钟
     * @return
     */
    public static boolean isTimeInMillis(Date lastDate, Date nextDate, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(lastDate);
        long last = c.getTimeInMillis();
        c.setTime(nextDate);
        long next = c.getTimeInMillis();
        int timeInMillis = minute * 60000;
        if ((next - last) >= timeInMillis) {
            return true;
        }
        return false;
    }

    public static long parseTime(String time,String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }
    public static boolean isToday(long time,String pattern) {
        return isThisTime(time, pattern);
    }
    @Test
    public void testTime() {
        System.out.println( isToday(new Date().getTime(),DateUtils4Vo.DATE_FORMAT));

    }



}
