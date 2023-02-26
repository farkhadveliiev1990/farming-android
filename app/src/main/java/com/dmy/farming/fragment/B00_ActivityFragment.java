package com.dmy.farming.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.B01_SearchActivity;
import com.dmy.farming.activity.C00_CityActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.adapter.B00_ActivityAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.HomeModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONObject;


public class B00_ActivityFragment extends BaseFragment implements BusinessResponse, XListView.IXListViewListener, View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    HomeModel commonModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.b00_activity,null);
        initView(mainView);

        commonModel = HomeModel.getInstance(getContext());
        commonModel.addResponseListener(this);
        return mainView;
    }

    XListView list_home;

    View layout_recom,layout_activi;
    GridView grid_recom,grid_activity;

    B00_ActivityAdapter activityAdapter;
    View footerView,mFooter;

    void initView(View mainView) {


        list_home = (XListView) mainView.findViewById(R.id.list_home);

        list_home.setPullLoadEnable(false);
        list_home.setPullRefreshEnable(true);
        list_home.setXListViewListener(this, 0);
        list_home.setAdapter(null);
    }

    int BUFFER_TIME = 1 * 60 * 1000;

    @Override
    public void onResume() {
        super.onResume();

        updateData();
        if (commonModel.data.lastUpdateTime + BUFFER_TIME < System.currentTimeMillis() ) {
            requestData(true);
        } else {
            setChangeCity(false);
        }
    }

    void requestData(boolean bShow)
    {
        setChangeCity(true);
    }

    public void setChangeCity(boolean bMust) {
//        String newCity = AppUtils.getCurrCity(mActivity);
//        String prevCity = B00_HomeFragment.text_city.getText().toString();
////        String prevCity = "";
//
//        if (TextUtils.isEmpty(newCity) || newCity.equals(prevCity)) {
//            if (!bMust) return;
//        } else {
////            text_city.setText(newCity);
//            prevCity = newCity;
//        }

    }

    void updateData()
    {
        list_home.stopRefresh();

        updateRecom();
        updateActivity();
    }


    protected ImageLoader imageLoader = ImageLoader.getInstance();

    void updateRecom() {
//        int size = commonModel.data.recom_activities.size();
//        if (size > 0) {
//            layout_recom.setVisibility(View.VISIBLE);
//
//        } else {
//            layout_recom.setVisibility(View.GONE);
//        }
    }

    void updateActivity() {
//        int size = commonModel.data.home_activities.size();
//        if (size > 0) {
//            layout_activi.setVisibility(View.VISIBLE);
//            if (activityAdapter == null) {
//                activityAdapter = new B00_ActivityAdapter(mActivity, commonModel.data.home_activities);
//                list_home.setAdapter(activityAdapter);
//            } else {
//                activityAdapter.notifyDataSetChanged();
//            }
//
//        } else {
//            layout_activi.setVisibility(View.GONE);
//            activityAdapter = null;
//            list_home.setAdapter(null);
//            list_home.setPullLoadEnable(false);
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commonModel.removeResponseListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.text_city:
                intent = new Intent(getContext(), C00_CityActivity.class);
                startActivityForResult(intent, REQUEST_CITY);
                break;
            case R.id.view_search:
                intent = new Intent(getContext(), B01_SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.button_right:
                dispRightMenu();
                break;

        }
    }


    public void onRefresh(int id) {
        requestData(false);
    }

    @Override
    public void onLoadMore(int id) {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
        if (url.endsWith(ApiInterface.HOME_RECOMMENDS)) {
            updateData();
        } else if (url.endsWith(ApiInterface.HOME_GROUP_LIST)) {
            updateData();
        }
    }

    Dialog mDialog;
    void dispRightMenu() {
        if (mDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.b00_right_menu, null);

            mDialog = new Dialog(getContext(), R.style.transparentFrameWindowStyle);
            mDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Window window = mDialog.getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = 0;
            wl.width = AppUtils.getScWidth(getContext());
            wl.height = AppUtils.getScHeight(getContext());
            mDialog.onWindowAttributesChanged(wl);
            mDialog.setCanceledOnTouchOutside(true);

            View layout_frame = view.findViewById(R.id.layout_frame);
            layout_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeMenu();
                }
            });

            View button_menu_0 = view.findViewById(R.id.button_menu_0);
            button_menu_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeMenu();
//                    if (mActivity.checkLogined()) {
//                        Intent intent = new Intent(mActivity, B03_CreateGroupActivity.class);
//                        startActivity(intent);
//                    }
                }
            });

            View button_menu_1 = view.findViewById(R.id.button_menu_1);
            button_menu_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeMenu();
//                    if (mActivity.checkLogined()) {
//                        Intent intent = new Intent(mActivity, D02_AddActivity.class);
//                        startActivity(intent);
//                    }
                }
            });
        }
        mDialog.show();
    }

    void closeMenu() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    int REQUEST_CITY = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CITY) {
            if(data!=null) {
                setChangeCity(false);
            }
        }
    }
}
