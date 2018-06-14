package com.github.dapeng.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author huyj
 * @Created 2018/6/13 15:33
 */
public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_SHORT = "yyyy-MM-dd HH:mm";
    public static final String TIMESTATMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    public static final String INFLUXDB_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

/*    public static String getTimeStamp(Date date) {
        return dateToStamp(getStringDate(date, DATE_FORMAT));
    }*/

    /**
     * 时间 格式化
     */
    public static String parseDateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 获得东巴区的 时间 （正常时间-8H）
     *
     * @author: huyj
     * @date: 2017年3月8日 上午9:26:23
     * @param: @param
     * date
     * @return: Date
     */
    public static Date getDateReduce8H(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, -8);//StringToDate(,format)
        return ca.getTime();
    }


    /**
     * 获得东巴区的 时间 （正常时间-8H）
     *
     * @author: huyj
     * @date: 2017年3月8日 上午9:26:23
     * @param: @param
     * date
     * @return: Date
     */
    public static Date getDateChangeHour(Date date, int num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, num);
        return ca.getTime();
    }

    /**
     * 获得东巴区的 时间 （正常时间-8H）
     *
     * @author: huyj
     * @date: 2017年3月8日 上午9:26:23
     * @param: @param
     * date
     * @return: Date
     */
    public static String getInfluxDbDate() {
        Date _date = getDateReduce8H(string2Date(parseDateToString(new Date(), DATE_FORMAT)));
        return parseDateToString(_date, INFLUXDB_FORMAT);
    }


    /**
     * 将日期字符串转换成日期型，日期格式为"yyyy-MM-dd"
     *
     * @param dateString 指定的日期字符串，格式为yyyyMMdd 或者yyyy-MM-dd
     * @return Date
     * @author lijunchen
     */
    public final static Date string2Date(String dateString) {
        if (dateString == null || dateString.trim().length() == 0) {
            return new Date(0);
        }

        // 处理不规范格式，例如：2013-1-2
        if (dateString.indexOf("-") > 0) {
            String month = dateString.substring(dateString.indexOf("-") + 1,
                    dateString.lastIndexOf("-"));
            if (month.length() == 1) {
                dateString = dateString.replace("-" + month + "-", "-0" + month
                        + "-");
            }
            String day = dateString.substring(dateString.lastIndexOf("-") + 1,
                    dateString.length());
            if (day.length() == 1) {
                dateString = dateString.substring(0,
                        dateString.lastIndexOf("-") + 1)
                        + "0" + day;
            }
            // System.out.println(dateString);
        }

        try {
            String strFormat = "";
            switch (dateString.length()) {
                case 4:
                    strFormat = "yyyy";
                    break;
                case 6: // yymmdd
                    strFormat = "yyMMdd";
                    break;
                case 8: // yyyymmdd
                    strFormat = "yyyyMMdd";
                    break;
                case 10: // yyyy-mm-dd
                    strFormat = "yyyy-MM-dd";
                    break;
                case 14:
                    strFormat = "yyyyMMddHHmmss";
                    break;
                default:
                    strFormat = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
            // 这里没有做非法日期的判断，比如：＂2007-05-33＂
            java.util.Date timeDate = simpleDateFormat.parse(dateString);
            return timeDate;
        } catch (Exception e) {
            return new Date(0);
        }
    }


    public static Date StringToDate(String dateStr, String formatStr) {
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当年12个月的信息
     *
     * @param String date
     * @param flag   是否截止到当月
     */
    public static List<String> getCurrYearMonths(String date, boolean flag) {
        List<String> monthList = new ArrayList<String>();
        Calendar ca = Calendar.getInstance();
        ca.setTime(StringToDate(date, "yyyy-MM-dd"));
        int month = flag ? ca.get(Calendar.MONTH) + 1 : 12;
        int year = ca.get(Calendar.YEAR);
        for (int i = 0; i < month; i++) {
            ca = Calendar.getInstance();
            ca.set(Calendar.YEAR, year);
            ca.set(Calendar.MONTH, i);
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            monthList.add(parseDateToString(ca.getTime(), "yyyy-MM-dd"));
        }
        return monthList;
    }

    /**
     * 获得当年12个月的信息
     *
     * @param String date
     * @param flag   是否截止到当月
     */
    public static List<String> get2YearMonths(String date, boolean flag) {
        List<String> monthList = new ArrayList<String>();
        Calendar ca = Calendar.getInstance();
        ca.setTime(StringToDate(date, "yyyy-MM-dd"));
        ca.add(Calendar.YEAR, -1);
        monthList.addAll(getCurrYearMonths(parseDateToString(ca.getTime(), "yyyy-MM-dd"), false));

        ca.add(Calendar.YEAR, 1);
        monthList.addAll(getCurrYearMonths(parseDateToString(ca.getTime(), "yyyy-MM-dd"), flag));
        return monthList;
    }

    public static void main(String[] arg0) {
        System.out.println(getInfluxDbDate());

    }

}
