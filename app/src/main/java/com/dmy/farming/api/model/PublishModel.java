package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.MarkerPriceRequest;
import com.dmy.farming.api.publishBuyRequest;
import com.dmy.farming.api.publishQuestionRequest;
import com.dmy.farming.api.publishRentRequest;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class PublishModel extends BaseModel {

    public STATUS lastStatus;

    public PublishModel(Context context) {
        super(context);
    }

    // 发布问题
    public void publishQuestion(publishQuestionRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHQUESTION).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 发布买
    public void publishBuy(publishBuyRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHBUY).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 发布卖
    public void publishSale(publishBuyRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHSALE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 发布租
    public void publishRent(publishRentRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHRENT).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 发布招帮手
    public void publishFindHelper(publishRentRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHFINDHELPER).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 发布行情价格
    public void publishPrice( MarkerPriceRequest markerPriceRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                PublishModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        STATUS  s = new STATUS();
                        s.succeed = jo.optInt("state");
                        s.error_code = jo.optInt("errCode");
                        s.error_desc = jo.optString("errMsg");
                        lastStatus = s;

                        PublishModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", markerPriceRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PUBLISHPRICE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

      /*  String url = "/" + infrom + "/"+ crop_type + "/"+ provience + "/" + city + "/" + district + "/" + pricelow + "/" + phone + "/" + pricehigh;

        cb.url(ApiInterface.PUBLISHPRICE + url).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);*/
    }

}