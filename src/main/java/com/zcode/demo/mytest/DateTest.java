package com.zcode.demo.mytest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhouwb
 * @since 2018/12/21
 */
public class DateTest {

    public static void main(String[] args) {

//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        try {
//            date = format.parse("2018-12-23");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//        System.out.println(dayOfWeek);

        DateTest dateTest = new DateTest();
//        dateTest.printRange();
        dateTest.print2();
    }

    private void printRange() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /*
            上个月最后一天
         */

        Calendar calendar1 = Calendar.getInstance();
        //本月第一天
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        //减掉一天，得上月最后一天
        calendar1.add(Calendar.DAY_OF_MONTH, -1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);

        //上月最后一天时间戳
        Long lStartTime = calendar1.getTimeInMillis();

        System.out.println(lStartTime);
        System.out.println(dateFormat.format(calendar1.getTime()));


        /*
            下个月第一天
         */
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 1);
        calendar2.set(Calendar.DAY_OF_MONTH, 1);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        //下月第一天时间戳
        Long lEndTime = calendar1.getTimeInMillis();

        System.out.println(lEndTime);
        System.out.println(dateFormat.format(calendar2.getTime()));

        /*
            下周一
         */
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.WEEK_OF_YEAR, 1);
        calendar3.set(Calendar.DAY_OF_WEEK, 2);
        calendar3.set(Calendar.HOUR_OF_DAY, 0);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);
        //下周一时间戳
        Long nextMonday = calendar3.getTimeInMillis();

        System.out.println(nextMonday);
        System.out.println(dateFormat.format(calendar3.getTime()));

    }

    private void print2() {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, +1);  //明天
            String senderDate = sf.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, +7);  //7天后
            String nextWeekDate = sf.format(cal.getTime());

            String strStartTime = senderDate + " 13:00:00";
            String strEndTime = nextWeekDate + " 13:00:00";

            SimpleDateFormat stampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dStartTime = stampFormat.parse(strStartTime);
            Date dEndTime = stampFormat.parse(strEndTime);

            String startDatestamp = String.valueOf(dStartTime.getTime());
            String endDatestamp = String.valueOf(dEndTime.getTime());

            System.out.println(startDatestamp);
            System.out.println(endDatestamp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
