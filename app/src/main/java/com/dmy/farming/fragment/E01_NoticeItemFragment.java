package com.dmy.farming.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.api.model.HomeModel;
import com.dmy.farming.api.model.UserInfoModel;
import com.dmy.farming.protocol.PHOTO;
import com.dmy.farming.view.B01_Home_Banner;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;

import java.util.ArrayList;


public class E01_NoticeItemFragment extends BaseFragment
        implements BusinessResponse, XListView.IXListViewListener, View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

        View mainView = inflater.inflate(R.layout.e01_notice_item,null);
       //initView(mainView);

        noticeItemFragment = new E01_NoticeItemFragment();
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_container, noticeItemFragment, "tab_one");
        localFragmentTransaction.commitAllowingStateLoss();
       /* commonModel = HomeModel.getInstance(mActivity);
        commonModel.addResponseListener(this);*/
        return mainView;
    }


    E01_NoticeItemFragment noticeItemFragment;
    int BUFFER_TIME = 60000;
    HomeModel commonModel;
    XListView list_home;
    BaseActivity mActivity;

    B01_Home_Banner mainBanner;
    UserInfoModel userInfoModel;

    ImageView knowledge;
    ImageView expert;
    ImageView quotation;
    ImageView demand;


    void initView(View mainView)
    {

        mainBanner = (B01_Home_Banner) mainView.findViewById(R.id.layout_banner);


        knowledge = ((ImageView)mainView.findViewById(R.id.knowledge));
        expert = ((ImageView)mainView.findViewById(R.id.expert));
        quotation = ((ImageView)mainView.findViewById(R.id.quotation));
        demand = ((ImageView)mainView.findViewById(R.id.demand));

       /* android.view.ViewGroup.LayoutParams params1 = mainBanner.getLayoutParams();
        params1.width = AppUtils.getScWidth(mActivity);
        params1.height = (int) (params1.width*1.0/720*330);*/
       // mainBanner.setLayoutParams(params1);

      // initBanner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh(int id) {
        requestData(false);
    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onResume() {
        super.onResume();
       // updateData();
      /*  if (commonModel.data.lastUpdateTime.longValue() + BUFFER_TIME < System.currentTimeMillis()) {
            requestData(true);
        }*/
    }


    void requestData(boolean isShow)
    {
        if (AppUtils.isLogin(mActivity)) {
            userInfoModel.getUserInfo(isShow);
        }
    }

    void updateData()
    {
       upadteBanner();
    }

    ArrayList<ADVER> adver_top = new ArrayList<>();

    String[] urls = new String[]{
            "https://cdn.pixabay.com/photo/2017/03/30/13/33/photography-2188440_960_720.jpg",
            "https://cdn.pixabay.com/photo/2014/09/22/00/56/photographer-455747_960_720.jpg",
            "https://thumb9.shutterstock.com/display_pic_with_logo/1619858/520127938/stock-photo-stylish-woman-photographer-with-retro-camera-on-the-yellow-wall-background-image-with-copy-space-520127938.jpg"};
    void initBanner()
    {
        for (int i = 0; i < urls.length; i++)
        {
            ADVER adver = new ADVER();
            PHOTO photo = new PHOTO();
            photo.url = urls[i];
            adver.adver_img = photo;
            adver.adver_id = (i + 5000) + "";
            adver.target = (i + 5000) + "";
            adver_top.add(adver);
        }
    }

    boolean upadteBanner()
    {
      /*  if (adver_top != null && adver_top.size() > 0)
        {
            mainBanner.bindData(adver_top);

            if (adver_top.size() == 1) {
                mainBanner.stopReply();
            }else {
                mainBanner.startReply();
            }
            return true;
        }*/ if (adver_top == null && adver_top.size() <= 0 ){
                return false;
            } else{
                mainBanner.bindData(adver_top);

              /*  if (adver_top.size() == 1) {
                    mainBanner.stopReply();
                }else {
                    mainBanner.startReply();
                }*/
                return true;
        }
    }

}