package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_BUY_LIST;
import com.dmy.farming.api.data.chat.a_Sale_LIST;
import com.dmy.farming.api.saleRequest;
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


public class SaleModel extends BaseModel {

    public a_Sale_LIST data = new a_Sale_LIST();
    public STATUS lastStatus;
    public USER user;
    public PAGINATED paginated = new PAGINATED();
    String fileName;

    public SaleModel(Context context) {
        super(context);
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

    public void getSaleList( saleRequest request,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                SaleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.routes.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);
                            }

                            fileSave();
                            SaleModel.this.OnMessageResponse(url, jo, status);
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

        cb.url(ApiInterface.SALE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }


    public void getBuyListMore(saleRequest request, final boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                SaleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.routes.addAll(response.data);

                            }
                            if (isCache)
                                fileSave();
                            SaleModel.this.OnMessageResponse(url, jo, status);
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

        cb.url(ApiInterface.SALE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void getMySaleList(String info_from, int page, String this_app,String phone,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                SaleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.mySale.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.mySale.addAll(response.data);
                            }

                            fileSave();
                            SaleModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from +"/"+ page +"/"+this_app + "/"+ phone;
        cb.url(ApiInterface.MYSALE + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void getMySaleListmore(String info_from, int page, String this_app,String phone,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                SaleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.mySale.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.mySale.addAll(response.data);
                            }

                            fileSave();
                            SaleModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from +"/"+ page +"/"+this_app + "/"+ phone;
        cb.url(ApiInterface.MYSALE + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void mySaleDelete(String info_from, String id, String type,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                SaleModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        SaleModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+ id+"/"+ type;
        cb.url(ApiInterface.MYDELETE + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

}
