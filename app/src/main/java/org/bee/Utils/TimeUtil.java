package org.bee.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.lang.Math;
import java.util.GregorianCalendar;

/**
 * User: howie
 * Date: 13-5-11
 * Time: 下午4:09
 */
public class TimeUtil {

    public static String getMonthString(int diffMonth)
    {
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MONTH, diffMonth);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM");
        String dateStr = formatter.format(date);
        return dateStr;
    }

    public static String getMonthTimeString(int diffMonth)
    {
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MONTH, diffMonth);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        String dateStr = formatter.format(date);
        return dateStr;
    }

    public static String timeAgo(String timeStr)
    {
        Date date = null;
        try
        {
            SimpleDateFormat format =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss Z" );
            date = format.parse(timeStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();
        long seconds = (currentTimeStamp - timeStamp)/1000;

        long minutes = Math.abs(seconds/60);
        long hours = Math.abs(minutes/60);
        long days = Math.abs(hours/24);

        if ( seconds <= 15 )
        {
            return "刚刚";
        }
        else if ( seconds < 60 )
        {
            return seconds+"秒前";
        }
        else if ( seconds < 120 )
        {
            return"1分钟前";
        }
        else if ( minutes < 60 )
        {
            return minutes+"分钟前";
        }
        else if ( minutes < 120 )
        {
            return "1小时前";
        }
        else if ( hours < 24 )
        {
            return hours +"小时前";
        }
        else if ( hours < 24 * 2 )
        {
            return "1天前";
        }
        else if ( days < 30 )
        {
            return days+"天前" ;
        }
        else
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(date);
            return dateString;
        }
    }

    public static String timeLeft(String timeStr)
    {
        Date date = null;
        try
        {
            SimpleDateFormat format =   new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss Z" );
            date = format.parse(timeStr);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();

        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp)/1000;

        if (total_seconds <= 0)
        {
            return  "";
        }

        long days = Math.abs(total_seconds/(24*60*60));

        long hours = Math.abs((total_seconds - days*24*60*60)/(60*60));
        long minutes = Math.abs((total_seconds - days*24*60*60 - hours*60*60)/60);
        long seconds = Math.abs((total_seconds - days*24*60*60 - hours*60*60 -minutes*60));
        String leftTime;
        if (days > 0)
        {
            leftTime = days+"天" + hours + "小时" + minutes +"分" +seconds+"秒";
        }
        else if (hours > 0)
        {
            leftTime = hours + "小时" + minutes +"分" +seconds+"秒";
        }
        else if (minutes > 0)
        {
            leftTime = minutes +"分" +seconds+"秒";
        }
        else if (seconds > 0)
        {
            leftTime = seconds+"秒";
        }
        else
        {
            leftTime = "0秒";
        }

         return leftTime;
    }

    public static String timeDistance(String endTime, String startTime)
    {
        Date startDate = null, endDate = null;
        try
        {
            SimpleDateFormat format =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate = format.parse(startTime);
            endDate = format.parse(endTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return "";
        }

        long startStamp = startDate.getTime();
        long endStamp = endDate.getTime();

        long total_seconds = (endStamp - startStamp)/1000;
        if (total_seconds < 0) total_seconds = 1;
        if (total_seconds < 60) return  total_seconds + "秒";

        long days = Math.abs(total_seconds/(24*60*60));
        long hours = Math.abs((total_seconds - days*24*60*60)/(60*60));
        long minutes = Math.abs((total_seconds - days*24*60*60 - hours*60*60)/60);
        long seconds = Math.abs((total_seconds - days*24*60*60 - hours*60*60 -minutes*60));

        String leftTime = "";
        if (days > 0) leftTime += days+"天";
        if (hours > 0) leftTime += hours + "小时";
        if (minutes > 0) leftTime += minutes +"分";
        if (seconds > 0) leftTime += seconds+"秒";
        return leftTime;
    }

    /**
     * 毫秒转化
     * @param ms
     * @return
     */
    public static String formatVideoTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute + ":" + strSecond;
    }
}
