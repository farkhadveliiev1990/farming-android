package org.bee.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import org.bee.model.ActivityManagerModel;
import org.bee.model.BusinessResponse;
import org.bee.view.ToastView;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.A01_SigninActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.api.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity implements Handler.Callback, BusinessResponse {
    public Handler mHandler;
    public static int mStatusBarHight = 0;

    public final static int REQUEST_GLOBAL_LOGIN = 1000;

    public BaseActivity()
    {

    }

    public void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void clickActivity(long activity_id) {
//        Intent intent = new Intent(this, B04_ActivityDetail.class);
//        intent.putExtra("activity_id", activity_id);
//        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        mHandler = new Handler(this);
        ActivityManagerModel.addLiveActivity(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ActivityManagerModel.addVisibleActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ActivityManagerModel.removeVisibleActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManagerModel.addForegroundActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManagerModel.removeForegroundActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerModel.removeLiveActivity(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
//        if (url.endsWith(ApiInterface.GROUP_LIST)) {
//            if (chatModel.lastStatus.succeed == 1) {
//                clickChat(chat_id, chat_type);
//            } else {
//                errorMsg(chatModel.lastStatus.error_desc);
//            }
//        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode)
    {
        super.startActivityForResult(intent,requestCode);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void errorMsg(String strMsg)
    {
        ToastView toast = new ToastView(this, strMsg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public boolean checkLogined()
    {
        if (!AppUtils.isLogin(this))
        {
            Intent intent = new Intent(this, A01_SigninActivity.class);
            startActivityForResult(intent, REQUEST_GLOBAL_LOGIN);
            return false;
        }
        else
            return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_GLOBAL_LOGIN) {
                // activity fragment
                android.support.v4.app.Fragment f = getSupportFragmentManager().findFragmentByTag("tab_two");
                if (f != null)
                    f.onActivityResult(requestCode, resultCode, data);

                // chat fragment
                f = getSupportFragmentManager().findFragmentByTag("tab_three");
                if (f != null)
                    f.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    ////////////////////////////////////////////////
    //	状态栏处理
    ////////////////////////////////////////////////
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        refreshStatusBar(true);
    }

    public void refreshStatusBar(boolean isShow)
    {
        // 状态栏处理
        View barTopStatus = findViewById(R.id.bar_top_status);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mStatusBarHight == 0)
                setStatusBarHeight(this);

            if (mStatusBarHight > 0)
            {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (barTopStatus != null) {
                    if (isShow)
                        barTopStatus.setPadding(0, mStatusBarHight, 0, 0);
                    else
                        barTopStatus.setPadding(0, 0, 0, 0);
                }
            }
            else
            {
                if (barTopStatus != null)
                    barTopStatus.setVisibility(View.GONE);
            }
        } else {
            if (barTopStatus != null)
                barTopStatus.setVisibility(View.GONE);
        }
    }

    static void setStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHight = context.getResources().getDimensionPixelSize(resourceId);
        }
    }

}
