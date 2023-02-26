package org.bee.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.json.JSONException;
import org.json.JSONTokener;

import java.text.DecimalFormat;

public class Utils
{
    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (metrics == null)
            return dp;
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static String strMoney(String money)
    {
        double fMoney = parseMoney(money);
        DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
        return myFormatter.format(fMoney);
    }

    public static double parseMoney(String strMoney)
    {
        if (TextUtils.isEmpty(strMoney))
            return 0;

        String str = strMoney.replace('￥', ' ').replace('¥', ' ').trim();

        double money;
        try {
            money = Double.parseDouble(str);
        } catch (Exception e){
            money = 0;
        }
        return money;
    }

    public static double parseDouble(String strDouble) {
        if (TextUtils.isEmpty(strDouble))
            return 0;

        double value;
        try {
            value = Double.parseDouble(strDouble);
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    public static int parseInt(String strInt) {
        if (TextUtils.isEmpty(strInt))
            return 0;

        int value = 0;
        try {
            value = Integer.parseInt(strInt);
        } catch (Exception e) {
            value = 0;
        }
        return value;
    }

    public static void freeBitmap(Bitmap bitmap)
    {
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
            System.gc();
            //bitmap = null;
        }
    }

    public static Object parseResponse(String responseBody) throws JSONException {
        Object result = null;
        //trim the string to prevent start with blank, and test if the string is valid JSON, because the parser don't do this :(. If Json is not valid this will return null
        responseBody = responseBody.trim();
        if(responseBody.startsWith("{") || responseBody.startsWith("[")) {
            result = new JSONTokener(responseBody).nextValue();
        }
        if (result == null) {
            result = responseBody;
        }
        return result;
    }

    public static Bitmap scaleBitmap(Bitmap bm,int pixel){
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();


        if(srcHeight>pixel || srcWidth>pixel)
        {
            float scale_y = 0;
            float scale_x = 0;
            if (srcHeight > srcWidth)
            {
                scale_y = ((float)pixel)/srcHeight;
                scale_x = scale_y;
            }
            else
            {
                scale_x = ((float)pixel)/srcWidth;
                scale_y = scale_x;
            }

            Matrix  matrix = new Matrix();
            matrix.postScale(scale_x, scale_y);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm,int dstHeight,int dstWidth){
        if(bm == null) return null;//java.lang.NullPointerException
        int srcHeight = bm.getHeight();
        int srcWidth = bm.getWidth();
        if(srcHeight>dstHeight || srcWidth>dstWidth){
            float scale_y = ((float)dstHeight)/srcHeight;
            float scale_x = ((float)dstWidth)/srcWidth;
            float scale = scale_y<scale_x?scale_y:scale_x;
            Matrix  matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap dstbmp = Bitmap.createBitmap(bm, 0, 0, srcWidth, srcHeight, matrix, true);
            return dstbmp;
        }
        else{
            return Bitmap.createBitmap(bm);
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try
        {
            // ---get the package info---
            PackageManager pm  = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0)
            {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }


}
