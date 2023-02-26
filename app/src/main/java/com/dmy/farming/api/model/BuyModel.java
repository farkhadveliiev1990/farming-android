package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.commonRequest;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_BUY_LIST;
import com.dmy.farming.api.publishBuyRequest;
import com.dmy.farming.api.userInfoResponse;
import com.dmy.farming.api.userUpdateRequest;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;



public class BuyModel extends BaseModel {

    public a_BUY_LIST data = new a_BUY_LIST();
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();
    public USER user;
    String fileName;

    public BuyModel(Context context) {
        super(context);
    }

    public void getBuyList(publishBuyRequest request , boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                BuyModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        BuyResponse response = new BuyResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.routes.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }

                            fileSave();
                            BuyModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.BUY).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }


    public void getBuyListMore( publishBuyRequest request ,final boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                BuyModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        BuyResponse response = new BuyResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }
                            if (isCache)
                                fileSave();
                            BuyModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.BUY).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void getMyBuyList(String info_from, int page, String this_app,String phone,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                BuyModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        BuyResponse response = new BuyResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.myBuy.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.myBuy.addAll(response.data);
                            }

                            fileSave();
                            BuyModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from +"/"+ page +"/"+this_app + "/"+ phone;
        cb.url(ApiInterface.MYBUY + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void getMyBuyListmore(String info_from, int page, String this_app,String phone,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                BuyModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        BuyResponse response = new BuyResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.myBuy.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.myBuy.addAll(response.data);
                            }

                            fileSave();
                            BuyModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from +"/"+ page +"/"+this_app + "/"+ phone;
        cb.url(ApiInterface.MYBUY + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    public void myBuyDelete(String info_from, String id,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                BuyModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        BuyModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+ id+"/0";
        cb.url(ApiInterface.MYDELETE + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


        // 缓存数据
    private PrintStream ps = null;

    public void fileSave() {
        data.lastUpdateTime = System.currentTimeMillis();

        String path = DataCleanManager.getCacheDir(mContext);
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(filePath + fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(data.toJson().toString());
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
