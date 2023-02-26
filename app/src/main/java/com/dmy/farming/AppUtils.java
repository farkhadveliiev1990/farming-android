package com.dmy.farming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.dmy.farming.api.data.GPS;

import org.bee.model.ActivityManagerModel;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    //
    public final static String PRE_ID = "wk2312";

    private final static String MAIN_UID = "uid";
    private final static String MAIN_LOGO = "logo";

    private final static String CHANNEL_ID = "CHANNEL_ID";
    private final static String UPLOAD_DEV = "UPLOAD_DEV";

    private final static String SC_WIDTH = "SC_WIDTH";
    private final static String SC_HEIGHT = "SC_HEIGHT";

    private final static String CURR_PROVINCE = "currProvince";
    private final static String CURR_CITY = "CURR_CITY";
    private final static String CURR_DISTRICT = "currDistrict";

    private final static String CNT_NEW_FRIEND = "CNT_NEW_FRIEND";
    private final static String CNT_REQ_GROUP = "CNT_REQ_GROUP";

    private final static String CURRLON = "currLon";
    private final static String CURRLAT = "currLat";

    private final static String FULLADDR = "currFullAddr";

    public static String getChannelId(Context context) {
        return getStringValue(context, CHANNEL_ID);
    }

    public static void setChannelId(Context context, String channelID) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putString(CHANNEL_ID, channelID).commit();
    }

    public static String getCurrCity(Context context) {
        return getStringValue(context, CURR_CITY);
    }

    public static void setCurrCity(Context context, String channelID) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putString(CURR_CITY, channelID).commit();
    }

    public static String getCurrProvince(Context context) {
        return getStringValue(context, CURR_PROVINCE);
    }

    public static String getCurrDistrict(Context context) {
        return getStringValue(context, CURR_DISTRICT);
    }

    public static float getCurrLon(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getFloat(CURRLON,0);
    }

    public static float getCurrLat(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getFloat(CURRLAT,0);
    }

    public static String getFullAddr(Context context) {
        return getStringValue(context, FULLADDR);
    }

    public static void setUploadDev(Context context, boolean bUploaded) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putBoolean(UPLOAD_DEV, bUploaded).commit();
    }

    public static boolean getUploadDev(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getBoolean(UPLOAD_DEV, false);
    }

    public static String getMyId(Context context) {
        return getStringValue(context, MAIN_UID);
    }
    public static String getMyLogo(Context context) {
        return getStringValue(context, MAIN_LOGO);
    }
    public static void saveMyLogo(Context context, String logo) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putString(MAIN_LOGO, logo).commit();
    }

    public static boolean isLogin(Context context)
    {
        return !(TextUtils.isEmpty(getStringValue(context, MAIN_UID)));
    }

    public static int getScWidth(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getInt(SC_WIDTH, 500);
    }

    public static void setScWidth(Context context, int width) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putInt(SC_WIDTH, width).commit();
    }

    public static int getScHeight(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getInt(SC_HEIGHT, 500);
    }

    public static void setScHeight(Context context, int height) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putInt(SC_HEIGHT, height).commit();
    }

    public static int getCntNewFriend(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getInt(CNT_NEW_FRIEND, 0);
    }

    public static void setCntNewFriend(Context context, int count) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putInt(CNT_NEW_FRIEND, count).commit();
    }

    public static int getCntReqGroup(Context context) {
        shared = context.getSharedPreferences("userInfo", 0);
        return shared.getInt(CNT_REQ_GROUP, 0);
    }

    public static void setCntReqGroup(Context context, int count) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putInt(CNT_REQ_GROUP, count).commit();
    }

    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;

    public static String getStringValue(Context context, String key) {
        String val = "";
        try {
            shared = context.getSharedPreferences("userInfo", 0);
            val = shared.getString(key, "");
            if (val == null) val = "";
        }
        catch (NullPointerException ex)
        {

        }
        return val;
    }

    static void setStringValue(Context context, String key, String value) {
        shared = context.getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        editor.putString(key, value).commit();
    }

    public static String getPhoneWithStar(String phonenum) {
        if (TextUtils.isEmpty(phonenum))
            return "";

        int len = phonenum.length();

        if (len < 9)
            return phonenum;
        else {
            return phonenum.substring(0, len-8) + "****" + phonenum.substring(len-4);
        }
    }

    public static String getIdcardWithStar(String idCard) {
        if (TextUtils.isEmpty(idCard))
            return "";

        int len = idCard.length();

        if (len < 8)
            return idCard;
        else
            return idCard.substring(0, len-8) + "****" + idCard.substring(len-4);
    }

    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0,1,2-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isChineseName(String name) {
        if (!name.matches("[\u4E00-\u9FA5]{2,4}")) {
            return false; // 只能输入2到4个汉字
        }else return true;
    }

    public static boolean isAppRunningForeground(Context context) {
        return ActivityManagerModel.foregroundActivityList.size() > 0;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static void openBrowser(Context context, String url)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static GPS bd09_To_Gcj02(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new GPS(gg_lat, gg_lon);
    }

    public static int dp2px(Context context, float dp) {
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * dp);
    }

    public static String time(String time) {
        String pattern ="yyyy-MM-dd HH:mm:ss";
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (!TextUtils.isEmpty(time)) {
            try {
                Date tDate = new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
                    } else {
                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) Math.ceil(dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) Math.ceil(dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                            display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
                        } else {
                            long days = dTime / (1000 * 60 * 60 * 24);
                            //display = halfDf.format(tDate);
                            if(days<7){
                                display = (int) Math.ceil(days) +"天前";
                            } else if(days>=7 && days <28){
                                display = (int) Math.ceil(days/7)+"周前";
                            }else if(days>=28){
                                display = (int) Math.ceil(days/30)+"个月前";
                            }else{
                                display = halfDf.format(tDate);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return display;
    }

    /**
     * 加密算法
     */
    public static String encode(String openid){

        String x = "15648523215";
        Format format = new SimpleDateFormat("yyyyMMddHHmmss");
        String y =  format.format(new Date());
        StringBuffer sb = new StringBuffer();
        StringBuffer str = sb.append(openid).insert(2,x).insert(17, y);
        String inStr = str.toString();

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        s = s.replaceAll("\"", "vip");
        s = s.replaceAll("\'", "mvp");

        return s;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 the first date
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(sdf.format( new Date()));
            dt2 = sdf.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() <= dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    //得到"*"的数量，然后进行替换相应的字符串
    public static String getXing(String f){
        String a = "";
        for (int i = 0; i < f.length(); i++) {
            a = a + "*";
        }
        return a;
    }




}
