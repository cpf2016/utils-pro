package com.alibaba.intl.affsettle.biz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 时间工具类
 * 
 * @author luqiang.lq 2015年6月17日 上午9:35:40
 */
public class AffsettleDateUtil {

    private static Log    logger            = LogFactory.getLog(AffsettleDateUtil.class);
    private static String YEAR_MONTH_FORMAT = "yyyyMM";

    /**
     * 获取日期的年月
     * 
     * @param date
     * @return
     */
    public static String getYearMontString(Date date) {
        return formatDateString(date, YEAR_MONTH_FORMAT);
    }

    /**
     * 获取下一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date nextDate(String date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date dateTime = null;
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            logger.error("parse date exception, date=" + date + ",pattern=" + pattern);
            return null;
        }

        return nextDate(dateTime);
    }

    /**
     * 获取下一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String nextDateString(String date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date dateTime = null;
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            logger.error("parse date exception, date=" + date + ",pattern=" + pattern);
            return null;
        }

        return nextDateString(dateTime, pattern);
    }

    /**
     * 获取下一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String nextDateString(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);

        return sdf.format(c.getTime());
    }

    /**
     * 获取下一天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date nextDate(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);

        return c.getTime();
    }

    /**
     * 获取下一个月
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date nextMonth(Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1);

        return c.getTime();
    }

    public static Integer getDaysFromFirstDayOfMonth(String date, String pattern) {
        Date cur = parseDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(cur);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将字符转换成日期
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date parseDate(String date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date dateTime = null;
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            logger.error("parse date exception, date=" + date + ",pattern=" + pattern);
            return null;
        }

        return dateTime;
    }

    /**
     * 获取月初的那天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date firstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取月初的那天
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String firstDateOfMonth(String date, String pattern) {
        Date curDate = parseDate(date, pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return formatDateString(c.getTime(), pattern);
    }

    /**
     * 将日期转换成字符
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDateString(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    /**
     * 日期格式化
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static Date formatDate(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Date dateTime = null;
        try {
            dateTime = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            logger.error("parse date exception, date=" + date + ",pattern=" + pattern);
            return null;
        }

        return dateTime;
    }

    /**
     * 检查时间是否在两个时间的指定的时间内
     * 
     * @param startDate
     * @param endDate
     * @param checkDate
     * @return
     */
    public static boolean betweenTime(Date startDate, Date endDate, String checkDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Long start = startDate.getTime();
            Long end = endDate.getTime();
            Long check = sdf.parse(checkDate).getTime();
            return start <= check && check <= end;
        } catch (ParseException e) {
            logger.error("parse date exception, startDate=" + startDate + ",endDate=" + endDate + ",checkDate="
                         + checkDate);
        }

        return false;
    }

    /**
     * 检查时间是否在两个时间的指定的时间内
     * 
     * @param startDate
     * @param endDate
     * @param checkDate
     * @return
     */
    public static boolean betweenTime(Date startDate, Date endDate, Date checkDate) {

        Long start = startDate.getTime();
        Long end = endDate.getTime();
        Long check = checkDate.getTime();

        return start <= check && check <= end;
    }

    public static void main(String[] args) {
        System.out.println("2016-03-09 17:07:14".substring(0, 10));
    }
}
