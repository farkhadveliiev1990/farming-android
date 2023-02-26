package org.bee.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.bee.Utils.Utils;
import com.dmy.farming.ErrorCodeConst;
import com.dmy.farming.R;
import com.dmy.farming.activity.A01_SigninActivity;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.view.ToastView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BaseModel implements BusinessResponse {

    protected BeeQuery aq ;
    protected ArrayList<BusinessResponse > businessResponseArrayList = new ArrayList<BusinessResponse>();
    protected Context mContext;
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;

    public BaseModel()
    {

    }

    public BaseModel(Context context)
    {
        aq = new BeeQuery(context);
        mContext = context;
    }
    protected void saveCache()
    {
        return ;
    }

    protected void cleanCache()
    {
        return ;
    }

    public void addResponseListener(BusinessResponse listener)
    {
        if (!businessResponseArrayList.contains(listener))
        {
            businessResponseArrayList.add(listener);
        }
    }

    public void removeResponseListener(BusinessResponse listener)
    {
        businessResponseArrayList.remove(listener);
    }

    void errorMsg(String msg)
    {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, (int) Utils.convertDpToPixel(mContext, 50));
        toast.show();
    }

    static long timeStamp = 0;

    //公共的错误处理
    public void callback(String url, JSONObject jo, AjaxStatus status)
    {
        if (null == jo)
        {
            Log.e("api error", url);
            if (timeStamp + 30 * 1000 < System.currentTimeMillis())
            {
                errorMsg("网络错误，请检查网络设置");
                timeStamp = System.currentTimeMillis();
            }
            return;
        }
//        try
//        {
            STATUS responseStatus = new STATUS();
//            responseStatus.fromJson(jo.optJSONObject("status"));
            responseStatus.succeed = jo.optInt("succeed");
            responseStatus.error_code = jo.optInt("error_code");
            responseStatus.error_desc = jo.optString("error_desc");
            if (responseStatus.succeed != ErrorCodeConst.ResponseSucceed)
            {
                if (responseStatus.error_code == ErrorCodeConst.InvalidSession)
                {
                    ToastView toast = new ToastView(mContext, mContext.getString(R.string.session_expires_tips));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //E0_ProfileFragment.isRefresh=true;
                    Intent intent = new Intent(mContext, A01_SigninActivity.class);
                    mContext.startActivity(intent);

                    SESSION.getInstance().updateValue(mContext, "", "", "", "",-1,"");
                }
            }
//        }
//        catch (JSONException e)
//        {
//
//        }

    }

    public  void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        for (BusinessResponse iterable_element : businessResponseArrayList)
        {
            iterable_element.OnMessageResponse(url,jo,status);
        }
    }
}
