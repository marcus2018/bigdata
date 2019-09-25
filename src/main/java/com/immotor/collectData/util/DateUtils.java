package com.immotor.collectData.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    public static final String DEFAULTIME = "02:00:00";

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat SDF1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static String longToDateTimeString(Long time) {
        return SDF.format(time);
    }

    public static Date stringToDate(String date) throws ParseException {
        return SDF.parse(date);
    }

    /**
     * 返回两个时间段所有日期
     */
    public static List<String> findDates(Date dBegin, Date dEnd) {
        List<String> lDate = new ArrayList<String>();
        lDate.add(SDF.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(SDF.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * @param date2  日期
     * @param amount 正数表示该日期后n天，负数表示该日期的前n天
     * @return
     * @throws ParseException
     */
    public static String findPreviousOrAfterDays(String date2, Integer amount) throws ParseException {

        Date date = SDF.parse(date2);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
        calendar.add(Calendar.DATE, amount);
        return SDF.format(calendar.getTime());

    }

    /**
     * @param date1
     * @param date2
     * @return 通过时间秒毫秒数判断两个时间的间隔
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    // 获取某年的第几周的开始日期
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
        return getFirstDayOfWeek(cal.getTime());
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取当前月份最后一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getMaxMonthDate(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());


    }
}