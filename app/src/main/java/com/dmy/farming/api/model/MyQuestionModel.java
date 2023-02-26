package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.data.a_QUESTION_LIST;
import com.dmy.farming.api.data.chat.a_WARN_LIST;
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


public class MyQuestionModel extends BaseModel {

    public a_QUESTION_LIST data = new a_QUESTION_LIST();
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();
    public USER user;
    String fileName;

    public MyQuestionModel(Context context) {
        super(context);
    }

    public void getBuyList(String info_from, String userphone, String page,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                MyQuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        QuestionResponse response = new QuestionResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.questions.clear();
                            //  data.paginated = response.paginated;
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.questions.addAll(response.data);
                            }
                            fileSave();
                            MyQuestionModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+userphone + "/"+ page;
        cb.url(ApiInterface.MYQUESTIONLIST + url).type(JSONObject.class);

        if (bShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    public void getBuyListMore(final String info_from, final String userphone, final String page,final boolean isCache) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                MyQuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        QuestionResponse response = new QuestionResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            paginated = response.paginated;
                            if (response.data.size() > 0) {
                                data.questions.addAll(response.data);
                            }
                            if (isCache)
                                fileSave();
                            MyQuestionModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+userphone;
        cb.url(ApiInterface.MYQUESTIONLIST + url).type(JSONObject.class);

        if (isCache) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    public void myQuestionDelete(String qid,boolean bShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                MyQuestionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        QuestionResponse response = new QuestionResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        MyQuestionModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject localItemObject = new JSONObject();

            localItemObject.put("info_from", AppConst.info_from);
            localItemObject.put("qid", qid);
            localItemObject.put("city", GLOBAL_DATA.getInstance(mContext).currCity);

            params.put("json",localItemObject .toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.DELETEQUESTION).type(JSONObject.class).params(params);

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
