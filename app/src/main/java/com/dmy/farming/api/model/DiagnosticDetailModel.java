package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.CollectResponse;
import com.dmy.farming.api.agrotechniqueCommentResponse;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.data.DIAGNOSTICDETAIL;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.api.data.chat.a_COMMENT;
import com.dmy.farming.api.data.chat.a_FARMVIDEO;
import com.dmy.farming.api.diagnosticDetailResponse;
import com.dmy.farming.api.farmarticleDetailResponse;
import com.dmy.farming.api.farmarticleResponse;
import com.dmy.farming.api.farmvideoDetailResponse;
import com.dmy.farming.api.saleRequest;
import com.dmy.farming.api.similarRequest;
import com.dmy.farming.api.similardiagResponse;
import com.dmy.farming.api.similarvideoResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DiagnosticDetailModel extends BaseModel {

    public DIAGNOSTICDETAIL diagnosticdetail = new DIAGNOSTICDETAIL();
    public ArrayList<DIAGNOSTICDETAIL> similarvideo = new ArrayList<>();
    public a_FARMVIDEO data = new a_FARMVIDEO();
    public a_COMMENT  comment = new a_COMMENT();
    public ArrayList<a_COMMENT_LIST> comment_list = new ArrayList<>();
    String fileName;
    public String id;
    public STATUS lastStatus;
    public PAGINATED paginated = new PAGINATED();

    public DiagnosticDetailModel(Context context) {
        super(context);
    }

    public void getDiagnosticDetail(String id,String cycle) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        diagnosticDetailResponse response = new diagnosticDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            //	comment.comment .clear();
                            diagnosticdetail = response.diagnosticdetail;
                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        // request.session = SESSION.getInstance();
//        request.id = id;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String url = "/绿云"+"/"+id+"/"+SESSION.getInstance().uid+"/"+cycle;
        cb.url(ApiInterface.DIAGNOSTICDETAIL + url).type(JSONObject.class);
        //  cb.setParams(ApiInterface.SALEDETAIL, params).type(JSONObject.class);

        //MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        //aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    public void getComment(String info_from,String id,boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        agrotechniqueCommentResponse response = new agrotechniqueCommentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {

                            if (response.comment_list.size() > 0)
                            {
                                comment_list = response.comment_list;
                                fileSave();
                            }

                            DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                }
            }
        };

        String url = "/"+info_from+"/"+id+"/"+ SESSION.getInstance().uid;
        cb.url(ApiInterface.ARTICLECOMMENT + url).type(JSONObject.class);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }

    public void deleteComment(String commentid,String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        farmarticleDetailResponse response = new farmarticleDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.farmarticle.toJson());
//                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        String url = "/绿云"+"/"+commentid+"/2"+"/"+id;
        cb.url(ApiInterface.DELETEDCOMMENT + url).type(JSONObject.class);
        //  cb.setParams(ApiInterface.SALEDETAIL, params).type(JSONObject.class);

        //MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        //aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    public void like(articleRequest artRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        diagnosticDetailResponse response = new diagnosticDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.diagnosticdetail.toJson());
                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
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

    public void cancellike(articleRequest artRequest) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        farmvideoDetailResponse response = new farmvideoDetailResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.fromJson(response.video.toJson());
                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
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
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "5");
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
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null)
                    {
                        CollectResponse response = new CollectResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            fileSave();
                        }
                        DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                }
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONObject itemObject = new JSONObject();
            itemObject.put("info_from", AppConst.info_from);
            itemObject.put("collection_type", "5");
            itemObject.put("userid", SESSION.getInstance().uid);
            itemObject.put("collection_id", id);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.CANCELCOLLECTION).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void similarvideo(similarRequest request, boolean isShow) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                DiagnosticDetailModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        similardiagResponse response = new similardiagResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            similarvideo.clear();
                            if (response.similardiag.size() > 0)
                            {
                                similarvideo = response.similardiag;
                                paginated = response.paginated;
                                fileSave();
                            }
                            DiagnosticDetailModel.this.OnMessageResponse(url, jo, status);
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
        cb.url(ApiInterface.SIMILARDIAG).type(JSONObject.class).params(params);

        if (isShow) {
            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
            aq.progress(pd.mDialog).ajax(cb);
        } else {
            aq.ajax(cb);
        }
    }


    // 缓存数据
    private PrintStream ps = null;

    public void fileSave() {
      //  data.lastUpdateTime = System.currentTimeMillis();

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
