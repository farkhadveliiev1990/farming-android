package com.dmy.farming.utils;


import android.content.Intent;
import android.util.Log;

import com.dmy.farming.AppUtils;

import org.bee.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PushMsgProcHandler {

    // chat
    public static final int TYPE_MSG_REQ_FRIEND = 100;
    public static final int TYPE_MSG_AGREE_FRIEND = 101;
    public static final int TYPE_MSG_CHAT_USER = 102;
    public static final int TYPE_MSG_CHAT_GROUP = 103;
    public static final int TYPE_MSG_GROUP_INVITE = 104;
    public static final int TYPE_MSG_GROUP_REQ_ENTER = 105;
    public static final int TYPE_MSG_GROUP_REFUSE_ENTER = 106;
    public static final int TYPE_MSG_GROUP_AGREE_ENTER = 107;
    public static final int TYPE_MSG_GROUP_EXIT = 108;


    // money
    public static final int TYPE_MSG_WITHDRAW_OK = 200;
    public static final int TYPE_MSG_WITHDRAW_FAIL = 201;

    // activity
    public static final int TYPE_MSG_ACT_SIGNIN = 300;

    public static void pushIntent(BaseActivity mActivity, String message) {
        Log.e("pushIntent", message);
        if(message != null) {
            try
            {
                JSONObject jsonObject = new JSONObject(message);
                int msgtype = jsonObject.optInt("msgtype");
                long lrev_0;
                if (AppUtils.isLogin(mActivity) && msgtype > 0)
                {
                    Intent intent;
                    switch (msgtype) {
                        case TYPE_MSG_REQ_FRIEND:
//                            intent = new Intent(mActivity, D01_NewFriendsActivity.class);
//                            mActivity.startActivity(intent);
                            break;
                        case TYPE_MSG_GROUP_REQ_ENTER: // 入群申请
//                            intent = new Intent(mActivity, D01_GroupEntersActivity.class);
//                            mActivity.startActivity(intent);
                            break;
                        case TYPE_MSG_GROUP_REFUSE_ENTER: // 拒绝入群申请
                            lrev_0 = Long.parseLong(jsonObject.optString("rev_0"));
//                            mActivity.clickGroup(lrev_0);
                            break;
                        case TYPE_MSG_GROUP_AGREE_ENTER: // 同意入群申请

                            break;
                        case TYPE_MSG_GROUP_EXIT: // 有人退出群
                            lrev_0 = Long.parseLong(jsonObject.optString("rev_0"));
//                            mActivity.clickGroup(lrev_0);
                            break;
                    }
                }
                else {

                }
            }
            catch (JSONException e) {
               e.printStackTrace();
            }
        }
    }

}
