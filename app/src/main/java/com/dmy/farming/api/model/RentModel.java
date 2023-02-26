package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_BUY_LIST;
import com.dmy.farming.api.data.a_RENT_LIST;
import com.dmy.farming.api.myRentResponse;
import com.dmy.farming.api.publishRentRequest;
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


public class RentModel extends BaseModel {

    public a_RENT_LIST data = new a_RENT_LIST();
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();
    public USER user;
    String fileName;

    public RentModel(Context context) {
        super(context);
    }

    public void getrentList(publishRentRequest request, boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        RentResponse response = new RentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.data.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }

                            fileSave();
                            RentModel.this.OnMessageResponse(url, jo, status);
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

        cb.url(ApiInterface.RENT).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }


    public void getrentListMore(publishRentRequest request, final boolean  bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        RentResponse response = new RentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }
                            if (bShow)
                                fileSave();
                            RentModel.this.OnMessageResponse(url, jo, status);
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

        cb.url(ApiInterface.RENT).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void getMyRentList(String info_from,String crop_code,int page,int this_app,String publish_user,boolean bShow) {

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        myRentResponse response = new myRentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.data.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }

                            fileSave();
                            RentModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/" + info_from+"/"+ crop_code+ "/" + page + "/" + this_app + "/" + publish_user;

        cb.url(ApiInterface.MYRENT + url).type(JSONObject.class);
        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void getMyRentListmore(String info_from,String crop_code,int page,int this_app,String publish_user,boolean bShow) {

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        myRentResponse response = new myRentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.data.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.data.addAll(response.data);
                            }

                            fileSave();
                            RentModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/" + info_from+"/"+ crop_code+ "/" + page + "/" + this_app + "/" + publish_user;

        cb.url(ApiInterface.MYRENT + url).type(JSONObject.class);
        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void myRentDelete(String info_from, String id,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                RentModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        RentModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+ id+"/2";
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
