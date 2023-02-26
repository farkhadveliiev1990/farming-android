package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleListResponse;
import com.dmy.farming.api.articleResponse;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_COLLECTION;
import com.dmy.farming.api.data.a_DICTIONARY;
import com.dmy.farming.api.diagnosticResponse;
import com.dmy.farming.api.dictionaryResponse;
import com.dmy.farming.api.farmarticleResponse;
import com.dmy.farming.api.farmvideoResponse;
import com.dmy.farming.protocol.PAGINATED;
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

public class CollectionModel extends BaseModel {
    public a_COLLECTION data = new a_COLLECTION();
    public PAGINATED paginated = new PAGINATED();

    final String fileName = "/collection.dat";

    private static CollectionModel instance;
    public static CollectionModel getInstance(Context context) {
        if (instance == null)
            instance = new CollectionModel(context);

        return instance;
    }

    public CollectionModel(Context context) {
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

    public void collectionVideolist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        farmvideoResponse response = new farmvideoResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.videolists.clear();
                            if (response.data.size() > 0) {
                                data.videolists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionArticlelist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        articleListResponse response = new articleListResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.articlelists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.articlelists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionQuestionlist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        QuestionResponse response = new QuestionResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.questionlists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.questionlists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionGongqiulist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        SaleResponse response = new SaleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.salelists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.salelists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }


    public void collectionBuylist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        BuyResponse response = new BuyResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.buylists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.buylists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionRentlist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        RentResponse response = new RentResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.rentlists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.rentlists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionFindHelplist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        FindHelpResponse response = new FindHelpResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.findhelplists.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.findhelplists.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    public void collectionfarmarticlelist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        farmarticleResponse response = new farmarticleResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.farmarticles.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.farmarticles.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    public void collectiondiagnosticlist(String info_from,String collection_type) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                CollectionModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        diagnosticResponse response = new diagnosticResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {
                            data.diagnostics.clear();
                            data.lastStatus = response.status;
                            if (response.data.size() > 0) {
                                data.diagnostics.addAll(response.data);
                            }
                            //fileSave();
                            CollectionModel.this.OnMessageResponse(url, jo, status);
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
            itemObject.put("collection_type", collection_type);
            itemObject.put("userid",SESSION.getInstance().uid);

            params.put("json", itemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.COLLECTIONLIST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
}
