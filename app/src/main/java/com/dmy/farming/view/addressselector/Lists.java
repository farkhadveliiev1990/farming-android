package com.dmy.farming.view.addressselector;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 */

public class Lists {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
