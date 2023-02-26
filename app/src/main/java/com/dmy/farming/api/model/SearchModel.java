package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SEARCHHISTORY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_SEARCH_LIST;
import com.dmy.farming.api.searchHistoryRequest;
import com.dmy.farming.api.searchHistoryResponse;
import com.dmy.farming.api.searchResponse;
import com.dmy.farming.api.searchUserRequest;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.PAGINATION;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchModel extends BaseModel {
    final String TAG = "search model";
    //
    int PAGE_COUNT = 3;
    int PAGE_COUNTS = 10;
    //
    public a_SEARCH_LIST data = new a_SEARCH_LIST();
    //
    public PAGINATED historyPaginated = new PAGINATED();
    public PAGINATED groupPagePaginated = new PAGINATED();
    public PAGINATED activityPagePaginated = new PAGINATED();
    public PAGINATED routePagePaginated = new PAGINATED();

    public ArrayList<SEARCHHISTORY> hotSearchList = new ArrayList<>();
    public ArrayList<SEARCHHISTORY> historyList = new ArrayList<>();
//    public ArrayList<SEARCHGROUP> groupList = new ArrayList<>();
//    public ArrayList<ACTIVITYLIST> activityList = new ArrayList<>();
//    public ArrayList<ROUTELIST> routeList = new ArrayList<ROUTELIST>();

    public STATUS lastStatus;

    public SearchModel(Context context) {
        super(context);
    }

    // 热搜
    public void hotSearch(String info_from,String userid,int page) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        searchHistoryResponse response = new searchHistoryResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
//                            historyPaginated.fromJson(response.paginated.toJson());
                            hotSearchList.clear();
                            if (response.historyList.size() > 0)
                                hotSearchList.addAll(response.historyList);

                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        searchHistoryRequest request = new searchHistoryRequest();
        request.info_from = info_from;
        request.userid = userid;
        request.page = page;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.HOME_HOTSEARCH).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    // 搜索历史
    public void searchHistory(String info_from,String userid) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        searchHistoryResponse response = new searchHistoryResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            historyPaginated.fromJson(response.paginated.toJson());

                            historyList.clear();
                            if (response.historyList.size() > 0)
                                historyList.addAll(response.historyList);

                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        searchHistoryRequest request = new searchHistoryRequest();
        request.info_from = info_from;
        request.userid = userid;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.HOME_SEARCHHISTORY).type(JSONObject.class).params(params);
//        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
//        aq.progress(pd.mDialog).ajax(cb);
        aq.ajax(cb);
    }

    // 删除历史记录
    public void deleteSearchHistory(String info_from,String id) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        searchHistoryResponse response = new searchHistoryResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;

                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<>();
        try {
            JSONObject localItemObject = new JSONObject();
            localItemObject.put("info_from", info_from);
            localItemObject.put("id", id);
            localItemObject.put("user", SESSION.getInstance().uid);

            params.put("json", localItemObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.HOME_DELETESEARCHHISTORY).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

    // 搜索
    public void search(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        searchResponse response = new searchResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
//                            data.paginated.fromJson(response.paginated.toJson());
                            data.paginated = response.paginated;

                            data.questionList.clear();
                            if (response.questionList.size() > 0)
                                data.questionList.addAll(response.questionList);

                            data.diagnosticList.clear();
                            if (response.diagnosticList.size() > 0)
                                data.diagnosticList.addAll(response.diagnosticList);

                            data.articleList.clear();
                            if (response.articleList.size() > 0)
                                data.articleList.addAll(response.articleList);

                            data.videoList.clear();
                            if (response.videoList.size() > 0)
                                data.videoList.addAll(response.videoList);

                            data.expertList.clear();
                            if (response.expertList.size() > 0)
                                data.expertList.addAll(response.expertList);

                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.HOME_SEARCH).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 搜索用户
    public void searchUserMore(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
                        searchResponse response = new searchResponse();
                        response.fromJson(jo);
                        lastStatus = response.status;
                        if (response.status.error_code == 200) {
                            data.paginated.fromJson(response.paginated.toJson());

//                            data.groupList.clear();
//                            if (response.groupList.size() > 0)
//                                data.groupList.addAll(response.groupList);
//
//                            data.activityList.clear();
//                            if (response.activityList.size() > 0)
//                                data.activityList.addAll(response.activityList);
//
//                            data.routeList.clear();
//                            if (response.routeList.size() > 0)
//                                data.routeList.addAll(response.routeList);
                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
//        pagination.page = (int) Math.ceil((double) data.activityList.size() * a1.0 / PAGE_COUNT) + a1;
        pagination.count = PAGE_COUNT;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCH, params).type(JSONObject.class);
        aq.ajax(cb);
    }

    // 搜索群
    public void searchGroup(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchGroupResponse response = new searchGroupResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            groupPagePaginated.fromJson(response.paginated.toJson());
//
//                           groupList.clear();
//                            if (response.groupList.size() > 0)
//                               groupList.addAll(response.groupList);

//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
        pagination.page = 1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHGROUP, params).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 搜索群
    public void searchGroupMore(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchGroupResponse response = new searchGroupResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            groupPagePaginated.fromJson(response.paginated.toJson());
//
//                            if (response.groupList.size() > 0)
//                                groupList.addAll(response.groupList);
//
//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
//        pagination.page = (int) Math.ceil((double) groupList.size() * a1.0 / PAGE_COUNTS) + a1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHGROUP, params).type(JSONObject.class);
        aq.ajax(cb);
    }

    // 搜索活动
    public void searchActivity(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchActivityResponse response = new searchActivityResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            activityPagePaginated.fromJson(response.paginated.toJson());
//
//                            activityList.clear();
//                            if (response.activityList.size() > 0)
//                                activityList.addAll(response.activityList);
//
//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
        pagination.page = 1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHACTIVITY, params).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 搜索活动
    public void searchActivityMore(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchActivityResponse response = new searchActivityResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            activityPagePaginated.fromJson(response.paginated.toJson());
//
//                            if (response.activityList.size() > 0)
//                                activityList.addAll(response.activityList);
//
//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
//        pagination.page = (int) Math.ceil((double) activityList.size() * a1.0 / PAGE_COUNTS) + a1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHACTIVITY, params).type(JSONObject.class);
        aq.ajax(cb);
    }

    // 搜索路线
    public void searchRoute(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchRouteResponse response = new searchRouteResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            routePagePaginated.fromJson(response.paginated.toJson());
//
//                            routeList.clear();
//                            if (response.routeList.size() > 0)
//                                routeList.addAll(response.routeList);
//
//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
        pagination.page = 1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHROUTE, params).type(JSONObject.class);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }

    // 搜索路线
    public void searchRouteMore(searchUserRequest request) {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                SearchModel.this.callback(url, jo, status);
                try {
                    if (jo != null) {
//                        searchRouteResponse response = new searchRouteResponse();
//                        response.fromJson(jo);
//                        lastStatus = response.status;
//                        if (response.status.succeed == 1) {
//                            routePagePaginated.fromJson(response.paginated.toJson());
//
//                            if (response.routeList.size() > 0)
//                                routeList.addAll(response.routeList);
//
//                        }
                        SearchModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

        };

        PAGINATION pagination = new PAGINATION();
//        pagination.page = (int) Math.ceil((double) routeList.size() * a1.0 / PAGE_COUNTS) + a1;
        pagination.count = PAGE_COUNTS;

        Map<String, String> params = new HashMap<>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.setParams(ApiInterface.HOME_SEARCHROUTE, params).type(JSONObject.class);
        aq.ajax(cb);
    }

}