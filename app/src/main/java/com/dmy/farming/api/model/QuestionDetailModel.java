package com.dmy.farming.api.model;

import android.content.Context;
import android.text.TextUtils;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.CollectResponse;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_QUESTION_DETAIL;
import com.dmy.farming.api.farmarticleDetailResponse;
import com.dmy.farming.api.queationDetailResponse;
import com.dmy.farming.api.questiondatailRequest;
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

public class QuestionDetailModel extends BaseModel {
    public a_QUESTION_DETAIL data = new a_QUESTION_DETAIL();
 //   public QUESTION question = new QUESTION();
    public STATUS lastStatus;

    int PAGE_COUNT = 15;
    final String fileName = "/questiondetail.dat";

    private static QuestionDetailModel instance;
    public static QuestionDetailModel getInstance(Context context) {
        if (instance == null)
            instance = new QuestionDetailModel(context);

        return instance;
    }

    public QuestionDetailModel(Context context) {
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

    public void questionDetail(String id, boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        queationDetailResponse response = new queationDetailResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {

                            if (response.data != null)
                            {
                                data.question = response.data;
                                data.reply_list.clear();
                                data.reply_list.addAll(response.reply_list);
                            }
                            //fileSave();
                            QuestionDetailModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String userid = "";
        if (SESSION.getInstance().uid == null || TextUtils.isEmpty(SESSION.getInstance().uid))
            userid = "1";
        else
            userid = SESSION.getInstance().uid;
        String url = "/" + id+"/" + userid ;
        cb.url(ApiInterface.QUESTIONDETAIL + url).type(JSONObject.class);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void like(questiondatailRequest artRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        queationDetailResponse response = new queationDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.data.toJson());
                               fileSave();
                        }
                        QuestionDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", artRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String url = "/"+info_from+"/"+ type_code+"/"+ provience+"/"+city+"/"+district+"/"+page+"/"+this_app + "/" + type;
        cb.url(ApiInterface.USERLIKE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }
    public void cancellike(questiondatailRequest artRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        queationDetailResponse response = new queationDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.data.toJson());
                            fileSave();
                        }
                        QuestionDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", artRequest.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String url = "/"+info_from+"/"+ type_code+"/"+ provience+"/"+city+"/"+district+"/"+page+"/"+this_app + "/" + type;
        cb.url(ApiInterface.CANCELUSERLIKE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    public void collection(String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        QuestionDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "3");
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("collection_id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    public void cancelcollection(String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                QuestionDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        QuestionDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "3");
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("collection_id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.CANCELCOLLECTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

//    public void routeTags() {
//        tagListRequest request = new tagListRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        tagListResponse response = new tagListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == 1) {
//                            data.tags_route.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.tags_route.addAll(response.data);
//                            }
//                            //fileSave();
//                            HomeModel.this.OnMessageResponse(url, jo, status);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    // TODO: handle exception
//                }
//            }
//        };
//
//        request.type_id = 0; //路线
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.ROUTE_TAGS, params).type(JSONObject.class);
//        aq.ajax(cb);
//    }

//    public void homeRoutes() {
//        final commonRequest request = new commonRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        routeListResponse response = new routeListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == 1) {
//                            data.paginated_recom_routes.fromJson(response.paginated.toJson());
//                            data.recom_routes.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.recom_routes.addAll(response.data);
//                            }
//                            fileSave();
//                            HomeModel.this.OnMessageResponse(url, jo, status);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    // TODO: handle exception
//                }
//            }
//        };
//
//        PAGINATION pagination = new PAGINATION();
//        pagination.page = 1;
//        pagination.count = 3;
//        request.pagination = pagination;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.HOME_RECOMS, params).type(JSONObject.class);
//        aq.ajax(cb);
//    }

//    public void homeGroups(String city) {
//        groupListRequest request = new groupListRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        groupListResponse response = new groupListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == 1) {
//                            data.paginated_groups.fromJson(response.paginated.toJson());
//                            data.home_groups.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.home_groups.addAll(response.data);
//                            }
//                            fileSave();
//                            HomeModel.this.OnMessageResponse(url, jo, status);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    // TODO: handle exception
//                }
//            }
//        };
//
//        PAGINATION pagination = new PAGINATION();
//        pagination.page = 1;
//        pagination.count = 10;
//        request.pagination = pagination;
//        request.session = SESSION.getInstance();
//        request.city = city;//GLOBAL_DATA.getInstance(mContext).currCity;
//
//        if (!TextUtils.isEmpty(city) && city.equals(GLOBAL_DATA.getInstance(mContext).currCity)) {
//            request.posX = GLOBAL_DATA.getInstance(mContext).currLon;
//            request.posY = GLOBAL_DATA.getInstance(mContext).currLat;
//        }
//        request.level = 1;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.GROUP_LIST, params).type(JSONObject.class);
//        aq.ajax(cb);
//    }
}
