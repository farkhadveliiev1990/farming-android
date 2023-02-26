package com.dmy.farming.api.model;

import android.content.Context;
import android.text.TextUtils;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.ShponResponse;
import com.dmy.farming.api.commonRequest;
import com.dmy.farming.api.commonResponse;
import com.dmy.farming.api.data.SHPON;
import com.dmy.farming.api.data.a_COMMON;
import com.dmy.farming.api.helpcenterResponse;
import com.dmy.farming.api.weatherResponse;
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

public class HomeModel extends BaseModel {
    public a_COMMON data = new a_COMMON();
    public SHPON shpon;
    int PAGE_COUNT = 15;
    final String fileName = "/home.dat";

    private static HomeModel instance;
    public static HomeModel getInstance(Context context) {
        if (instance == null)
            instance = new HomeModel(context);

        return instance;
    }

    public HomeModel(Context context) {
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

    public void reqWeather(String city) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HomeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        weatherResponse response = new weatherResponse();
                        response.fromJson(jo);
                        if (response.status.succeed == 1000) {
//                            data.weather.fromJson(response.data.toJson());
                            data.weather = response.data;
                            fileSave();
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }
                        return;
                    }
                } catch (JSONException e) {

                }
            }
        };

//        String url = "https://free-api.heweather.com/s6/weather/now?location="+ city +"&key=ee686c8bc5394534a4b3709dd609b356";
        String url = "http://wthrcdn.etouch.cn/weather_mini?city="+ city;

        cb.url(url).type(JSONObject.class);
//        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
//        aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    public void getSponsor() {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HomeModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        ShponResponse response = new ShponResponse();
                        response.fromJson(jo);
                        if (response.status.error_code == 200) {

                            if (response.data != null)
                            {
                                shpon = response.data;
                            }

                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }

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
            localItemObject.put("city", AppUtils.getCurrCity(mContext));

            params.put("json", localItemObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.HOME_SPONSOR).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

//    public void homeAdvers(boolean isShow) {
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        adverListResponse response = new adverListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == a1) {
//                            data.adver_top.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.adver_top.addAll(response.data);
//                            }
//
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
//        cb.setParams(ApiInterface.HOME_ADVERS, null).type(JSONObject.class);
//        if (isShow) {
//            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
//            aq.progress(pd.mDialog).ajax(cb);
//        } else {
//            aq.ajax(cb);
//        }
//    }

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
//                        if (response.status.succeed == a1) {
//                            data.tags_route.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.tags_route.addAll(response.data);
//                            }
//
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
//                        if (response.status.succeed == a1) {
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
//        pagination.page = a1;
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

//    public void homeRecom(boolean isShow) {
//        final commonRequest request = new commonRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        recomListResponse response = new recomListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == a1) {
//                            data.paginated_recom_activities.fromJson(response.paginated.toJson());
//                            data.recom_activities.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.recom_activities.addAll(response.data);
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
//        pagination.page = a1;
//        pagination.count = 4;
//        request.pagination = pagination;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.HOME_RECOMMENDS, params).type(JSONObject.class);
//        if (isShow) {
//            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
//            aq.progress(pd.mDialog).ajax(cb);
//        } else {
//            aq.ajax(cb);
//        }
//    }

//    public void homeActivity(String city) {
//        groupListRequest request = new groupListRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        homeActivityListResponse response = new homeActivityListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == a1) {
//                            data.paginated_activities.fromJson(response.paginated.toJson());
//                            data.home_activities.clear();
//                            if (response.data.size() > 0)
//                            {
//                                data.home_activities.addAll(response.data);
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
//        pagination.page = a1;
//        pagination.count = PAGE_COUNT;
//        request.pagination = pagination;
//        request.city = city;//GLOBAL_DATA.getInstance(mContext).currCity;
//
//        if (!TextUtils.isEmpty(city) && city.equals(GLOBAL_DATA.getInstance(mContext).currCity)) {
//            request.posX = GLOBAL_DATA.getInstance(mContext).currLon;
//            request.posY = GLOBAL_DATA.getInstance(mContext).currLat;
//        }
//        request.level = a1;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.HOME_GROUP_LIST, params).type(JSONObject.class);
//        aq.ajax(cb);
//    }

//    public void homeActivityMore(String city) {
//        groupListRequest request = new groupListRequest();
//        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
//
//            @Override
//            public void callback(String url, JSONObject jo, AjaxStatus status) {
//                HomeModel.this.callback(url, jo, status);
//                try {
//                    if (jo != null) {
//                        homeActivityListResponse response = new homeActivityListResponse();
//                        response.fromJson(jo);
//                        if (response.status.succeed == a1) {
//                            data.paginated_activities.fromJson(response.paginated.toJson());
//
//                            if (response.data.size() > 0)
//                            {
//                                data.home_activities.addAll(response.data);
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
//        pagination.page = (int) Math.ceil((double) data.home_activities.size() * a1.0 / PAGE_COUNT) + a1;
//        pagination.count = PAGE_COUNT;
//        request.pagination = pagination;
//        request.session = SESSION.getInstance();
//        request.city = city;//GLOBAL_DATA.getInstance(mContext).currCity;
//
//        if (!TextUtils.isEmpty(city) && city.equals(GLOBAL_DATA.getInstance(mContext).currCity)) {
//            request.posX = GLOBAL_DATA.getInstance(mContext).currLon;
//            request.posY = GLOBAL_DATA.getInstance(mContext).currLat;
//        }
//        request.level = a1;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.HOME_GROUP_LIST, params).type(JSONObject.class);
//        aq.ajax(cb);
//    }

//    public void homeGroups(String city, boolean isShow) {
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
//                        if (response.status.succeed == a1) {
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
//        pagination.page = a1;
//        pagination.count = PAGE_COUNT;
//        request.pagination = pagination;
//        request.session = SESSION.getInstance();
//        request.city = city;//GLOBAL_DATA.getInstance(mContext).currCity;
//
//        if (!TextUtils.isEmpty(city) && city.equals(GLOBAL_DATA.getInstance(mContext).currCity)) {
//            request.posX = GLOBAL_DATA.getInstance(mContext).currLon;
//            request.posY = GLOBAL_DATA.getInstance(mContext).currLat;
//        }
//        request.level = a1;
//
//        Map<String, String> params = new HashMap<String, String>();
//        try {
//            params.put("json", request.toJson().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        cb.setParams(ApiInterface.GROUP_LIST, params).type(JSONObject.class);
//
//        if (isShow) {
//            MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
//            aq.progress(pd.mDialog).ajax(cb);
//        } else {
//            aq.ajax(cb);
//        }
//    }

//    public void homeGroupsMore(String city) {
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
//                        if (response.status.succeed == a1) {
//                            data.paginated_groups.fromJson(response.paginated.toJson());
//
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
//        pagination.page = (int) Math.ceil((double) data.home_groups.size() * a1.0 / PAGE_COUNT) + a1;
//        pagination.count = PAGE_COUNT;
//        request.pagination = pagination;
//        request.session = SESSION.getInstance();
//        request.city = city;//GLOBAL_DATA.getInstance(mContext).currCity;
//
//        if (!TextUtils.isEmpty(city) && city.equals(GLOBAL_DATA.getInstance(mContext).currCity)) {
//            request.posX = GLOBAL_DATA.getInstance(mContext).currLon;
//            request.posY = GLOBAL_DATA.getInstance(mContext).currLat;
//        }
//        request.level = a1;
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
