package com.dmy.farming.utils;

import android.text.InputFilter;
import android.text.Spanned;

import org.bee.Utils.Utils;

public class ComponentDigitCtrlFilter implements InputFilter {

    private int maxFrontInt = 999999;
    private int maxPointCnt = 2;

    public ComponentDigitCtrlFilter() {

    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String oriValue = dest.toString();
        StringBuffer sb = new StringBuffer(oriValue);
        sb.append(source);
        String newValue = sb.toString();
        String[] newValueVec = newValue.split("\\.");

        if (newValueVec.length == 2) {

            boolean numberflag = true;
            numberflag = ((Double.parseDouble(newValueVec[0]) - maxFrontInt > 0.000001) ? false : true);

            boolean digitflag = true;
            try {
                String digitNumber = newValueVec[1];
                digitflag = digitNumber.toCharArray().length > maxPointCnt ? false : true;
            } catch (Exception ex) {
                digitflag = false;
            }

            if (numberflag && digitflag) {
                return source;
            } else {
                return "";
            }

        } else {
            double value = Utils.parseInt(newValue);
            return value > maxFrontInt ? "" : source;
        }
    }
}