package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.dictionaryResponse;
import com.dmy.farming.api.data.a_DICTIONARY;
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

public class DictionaryModel extends BaseModel {
    public a_DICTIONARY data = new a_DICTIONARY();
    public STATUS lastStatus;
    final String fileName = "/dictionary.dat";

    private static DictionaryModel instance;
    public static DictionaryModel getInstance(Context context) {
        if (instance == null)
            instance = new DictionaryModel(context);

        return instance;
    }

    public DictionaryModel(Context context) {
        super(context);
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

    public void cropType(boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {

                            if (response.data != null)
                            {
                                data.crop_type = response.data;
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/1";
        cb.url(ApiInterface.CROPTYPE + url).type(JSONObject.class);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void questionType() {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.question_type.clear();
                            if (response.data.size() > 0)
                            {
                                data.question_type.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/1";

        cb.url(ApiInterface.QUESTIONTYPE + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void followType(String info_from,boolean bshow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.follow_type.clear();
                            if (response.data.size() > 0)
                            {
                                data.follow_type.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+ SESSION.getInstance().uid;

        cb.url(ApiInterface.FOLLOWTYPE + url).type(JSONObject.class);
        aq.ajax(cb);
    }


    public void cropsubType(String info_from,String code_type,boolean bshow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.crop_sub_type.clear();
                            if (response.data.size() > 0)
                            {
                                data.crop_sub_type.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+code_type;

        cb.url(ApiInterface.CROPSUB + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void getArticleLabel(String info_from,String model_code) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.article_label.clear();
                            if (response.data.size() > 0)
                            {
                                data.article_label.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };



        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", info_from);
            itemObject.put("model_code", model_code);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.ARTICLELABEL).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void getPublishTypeList(String info_from,String model_code) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.gongqiu_label.clear();
                            if (response.data.size() > 0)
                            {
                                data.gongqiu_label.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from + "/"+ model_code;

        cb.url(ApiInterface.GONGQIULABEL + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void getsaleTypeList(String info_from,String model_code) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.sale_label.clear();
                            if (response.data.size() > 0)
                            {
                                data.sale_label.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            JSONObject itemObject = new JSONObject();
//            itemObject.put("info_from", info_from);
//            itemObject.put("model_code", model_code);
//
//            params.put("json", itemObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String url = "/"+ info_from + "/"+ model_code;

        cb.url(ApiInterface.SALELABEL + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void getUnit(String info_from,String model_code) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.unit.clear();
                            if (response.data.size() > 0)
                            {
                                data.unit.addAll(response.data);
                            }
                            //fileSave();
                            DictionaryModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+ info_from + "/"+ model_code;

        cb.url(ApiInterface.UNIT + url).type(JSONObject.class);
        aq.ajax(cb);
    }

    public void saveAttention(String info_from,String code,boolean bshow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        DictionaryModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("user_id", SESSION.getInstance().uid);
            localItemObject.put("about_code", code);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cb.url(ApiInterface.SAVEATTENTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void deleteAttention(String info_from,String code,boolean bshow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        DictionaryModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("user_id", SESSION.getInstance().uid);
            localItemObject.put("about_code", code);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.DELETEATTENTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    // 知识库添加作物
    public void addCrop(String info_from,String code,String sub_code,String area,String time,String position) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        DictionaryModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("user_id", SESSION.getInstance().uid);
            localItemObject.put("about_code", code);
            localItemObject.put("subabout_code", sub_code);
            localItemObject.put("plan_area", area);
            localItemObject.put("plan_time", time);
            localItemObject.put("position", position);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.ADDCROP).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void report(String info_from,String id,String type,String from,String report_content,String iscomment,
                       String content, String report_user,String about_user) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        DictionaryModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("id", id);
            localItemObject.put("type", type);
            localItemObject.put("from", from);
            localItemObject.put("report_content", report_content);
            localItemObject.put("is_comment", iscomment);
            localItemObject.put("content", content);
            localItemObject.put("report_user", report_user);
            localItemObject.put("about_user", about_user);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cb.url(ApiInterface.REPORT).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void comment(String info_from,String id,String userid,String type,String byreply_userid,String content,
                       String supcomid,String commenttype) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DictionaryModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        dictionaryResponse response = new dictionaryResponse();
                        response.fromJson(jo);

                        lastStatus = response.status;

                        DictionaryModel.this.OnMessageResponse(url, jo, status);

                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("id", id);
            localItemObject.put("user_id", userid);
            localItemObject.put("type", type);
            localItemObject.put("byreply_userid", byreply_userid);
            localItemObject.put("content", content);
            localItemObject.put("supcomid", supcomid);
            localItemObject.put("commenttype", commenttype);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        cb.url(ApiInterface.COMMENT).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

}
