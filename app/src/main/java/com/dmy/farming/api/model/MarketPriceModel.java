package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.MarkerPriceRequest;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_PRICE_LIST;
import com.dmy.farming.api.data.a_RENT_LIST;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MarketPriceModel extends BaseModel {

    public a_PRICE_LIST data = new a_PRICE_LIST();
    public int type;
    String fileName;
    public PAGINATED paginated = new PAGINATED();

    public MarketPriceModel(Context context, int type) {
        super(context);
        this.type = type;
        fileName = "/priceL_" + type +".dat";
        readCache();
    }

    public void readCache() {
        String path = DataCleanManager.getCacheDir(mContext) + fileName;
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                parseCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void parseCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                data.fromJson(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void getpriceList(MarkerPriceRequest markerPriceRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                MarketPriceModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        MarketPriceResponse response = new MarketPriceResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.prices.clear();
                            data.paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.prices.addAll(response.data);
                            }

                            fileSave();
                            MarketPriceModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", markerPriceRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String url = "/"+info_from+"/"+ type_code+"/"+ provience+"/"+city+"/"+district+"/"+page+"/"+this_app + "/" + type;
        cb.url(ApiInterface.PRICE).type(JSONObject.class).params(params);
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
          aq.progress(pd.mDialog).ajax(cb);
    }


    public void getpriceListMore(MarkerPriceRequest markerPriceRequest, boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                MarketPriceModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        MarketPriceResponse response = new MarketPriceResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.prices.addAll(response.data);
                            }
                            fileSave();
                            MarketPriceModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", markerPriceRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.PRICE).type(JSONObject.class).params(params);

        if (isCache) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

}
