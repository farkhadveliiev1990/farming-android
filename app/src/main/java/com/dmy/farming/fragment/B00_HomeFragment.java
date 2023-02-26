package com.dmy.farming.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.AddCropActivity;
import com.dmy.farming.activity.B00_WarningActivity;
import com.dmy.farming.activity.B01_AgriculturalTechnologyActivity;
import com.dmy.farming.activity.B01_KnowledgeActivity;
import com.dmy.farming.activity.B01_SearchActivity;
import com.dmy.farming.activity.B01_WarnDetailActivity;
import com.dmy.farming.activity.C00_CityActivity;
import com.dmy.farming.activity.C01_ExpertListActivity;
import com.dmy.farming.activity.C01_FarmNewsActivity;
import com.dmy.farming.activity.C01_FarmNewsItemDetailActivity;
import com.dmy.farming.activity.C01_MarketPriceActivity;
import com.dmy.farming.activity.C01_SellDetailActivity;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleListRequest;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.api.data.SHPON;
import com.dmy.farming.api.data.WEATHER;
import com.dmy.farming.api.model.ArticleListModel;
import com.dmy.farming.api.model.HomeModel;
import com.dmy.farming.api.model.UserInfoModel;
import com.dmy.farming.api.model.WarnModel;
import com.dmy.farming.api.warnRequest;
import com.dmy.farming.protocol.PHOTO;
import com.dmy.farming.utils.DropDownAnim;
import com.dmy.farming.view.B01_Home_Banner;
import com.dmy.farming.view.CustomBanner;
import com.dmy.farming.view.MyObserScrollview;
import com.dmy.farming.view.MyScrollView;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.viewpagerindicator.PageIndicator;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.Utils.Utils;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.view.WindowManager.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class B00_HomeFragment extends BaseFragment implements BusinessResponse, XListView.IXListViewListener, View.OnClickListener,MyScrollView.OnScrollListener, ObservableScrollViewCallbacks, View.OnTouchListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    View mainView;
    private CustomBanner<String> mBanner;
    Boolean flag = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

       mainView = inflater.inflate(R.layout.b00_home,null);
        initView(mainView);

        commonModel = HomeModel.getInstance(mActivity);
        commonModel.addResponseListener(this);
        warnModel = new WarnModel(mActivity);
        wRequest = new warnRequest();
        wRequest.city = AppUtils.getCurrCity(mActivity);
        wRequest.district = AppUtils.getCurrDistrict(mActivity);
        wRequest.province = AppUtils.getCurrProvince(mActivity);
        wRequest.info_from = "德铭源";
        wRequest.page = 1;
        warnModel.addResponseListener(this);
        warnModel.getWarnList(wRequest,true);
        request = new articleListRequest();
        articleListModel = new ArticleListModel(mActivity,"");
        articleListModel.addResponseListener(this);
        //初始化布局


        return mainView;
    }

    int BUFFER_TIME = 60000;
    HomeModel commonModel;
    XListView list_home;
    MainActivity mActivity;
    WarnModel warnModel;
    ArticleListModel articleListModel;
    articleListRequest request;
    warnRequest wRequest;

    UserInfoModel userInfoModel;

    ImageView knowledge;
    ImageView expert;
    ImageView quotation;
    ImageView demand;

    // myScrollView 滑动改变搜索框
    MyObserScrollview myScrollView;
    LinearLayout searchLayout;
    WindowManager mWindowManager;
    View mainBanner;
    int screenWidth;
    int searchLayoutHeight;
    int searchLayoutTop;
    int myScrollViewTop;
    View suspendView,layout_0,layout_1,layout_2,line_view;
    WindowManager.LayoutParams suspendLayoutParams;
    LinearLayout qixiang,layout_sponsor,warn,zhibao,tufei,layout_view;
    ImageView img_weather,img_news1,img_news2,img_news3;
    TextView more,text_city,/*zhineng,jiagou,*/text_cond,text_windDir,text_windSc,text_temp,text_tab_1,text_tab_2,text_tab_3,text_tab_4,text_tab_5,text_tab_6,headline;
    TextView content1,content2,content3,type1,type2,type3,collection1,collection2,collection3,num1,num2,num3,time1,time2,time3,qixiangid,zhibaoid,tufeiid;
    boolean isShow = false;
    SHPON sponsor;

    View header;

    private Animation animationDown;

    private Animation animationUp;

    void initView(View mainView)
    {
     //   mainBanner = mainView.findViewById(R.id.layout_banner);

        View mainSearch =  mainView.findViewById(R.id.layout_search);
        mainSearch.findViewById(R.id.edit_keyword).setOnClickListener(this);


        text_city = (TextView) mainSearch.findViewById(R.id.text_city);
        text_city.setOnClickListener(this);
        warn = (LinearLayout) mainView.findViewById(R.id.warn);
        warn.setOnClickListener(this);
        // 天气
        img_weather = (ImageView) mainSearch.findViewById(R.id.img_weather);
        text_cond = (TextView) mainSearch.findViewById(R.id.text_cond);
        text_windDir = (TextView) mainSearch.findViewById(R.id.text_windDir);
        text_windSc = (TextView) mainSearch.findViewById(R.id.text_windSc);
        text_temp = (TextView) mainSearch.findViewById(R.id.text_temp);

        text_tab_1 = (TextView) mainView.findViewById(R.id.text_tab_1);
        text_tab_2 = (TextView) mainView.findViewById(R.id.text_tab_2);
        text_tab_3 = (TextView) mainView.findViewById(R.id.text_tab_3);
        text_tab_4 = (TextView) mainView.findViewById(R.id.text_tab_4);
        text_tab_5 = (TextView) mainView.findViewById(R.id.text_tab_5);
        text_tab_6 = (TextView) mainView.findViewById(R.id.text_tab_6);

        headline = (TextView)mainView.findViewById(R.id.headline);
        headline.setOnClickListener(this);

        searchLayout = (LinearLayout) mainSearch.findViewById(R.id.view_search);
        myScrollView = (MyObserScrollview) mainView.findViewById(R.id.scrollView);
        myScrollView.setScrollViewCallbacks(this);
        myScrollView.setOnTouchListener(this);
//        myScrollView.setOnScrollListener(this);
//        myScrollView.setScrollViewListener(new MyScrollView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(int x, int y, int oldxX, int oldY) {
//                if(oldY >= searchLayoutTop){
//                    if(suspendView == null){
//                        showSuspend();
//                        isShow = true;
//                    }
//                }else if(oldY <= searchLayoutTop + searchLayoutHeight){
//                    if(suspendView != null){
//                        removeSuspend();
//                        isShow = false;
//                    }
//                }
//            }
//        });
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();

        ViewTreeObserver vto = searchLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
            // TODO 自动生成的方法存根
                searchLayoutHeight = searchLayout.getHeight();
                searchLayoutTop = searchLayout.getTop();
            }
        });

        ViewTreeObserver vto1 = myScrollView.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO 自动生成的方法存根
                myScrollViewTop = myScrollView.getTop();
            }
        });

        knowledge = ((ImageView)mainView.findViewById(R.id.knowledge));
        knowledge.setOnClickListener(this);
        expert = ((ImageView)mainView.findViewById(R.id.expert));
        expert.setOnClickListener(this);
        quotation = ((ImageView)mainView.findViewById(R.id.quotation));
        quotation.setOnClickListener(this);
        demand = ((ImageView)mainView.findViewById(R.id.demand));
        demand.setOnClickListener(this);
        line_view = mainView.findViewById(R.id.line_view);
        layout_view =(LinearLayout) mainView.findViewById(R.id.layout_view);

        more = (TextView)mainView.findViewById(R.id.more);
        more.setOnClickListener(this);
        layout_0 = mainView.findViewById(R.id.layout_0);
        layout_0.setOnClickListener(this);
        layout_1 = mainView.findViewById(R.id.layout_1);
        layout_1.setOnClickListener(this);
        layout_2 = mainView.findViewById(R.id.layout_2);
        layout_2.setOnClickListener(this);
        qixiangid = (TextView) mainView.findViewById(R.id.qixiangid);
        zhibaoid = (TextView)mainView.findViewById(R.id.zhibaoid);
        tufeiid = (TextView)mainView.findViewById(R.id.tufeiid) ;
        header = mainView.findViewById(R.id.layout_header);
//        zhineng = (TextView)mainView.findViewById(R.id.zhineng);
//        zhineng.setOnClickListener(this);
//        jiagou = (TextView)mainView.findViewById(R.id.jiagou);
//        jiagou.setOnClickListener(this);

       /* android.view.ViewGroup.LayoutParams params1 = mainBanner.getLayoutParams();
        params1.width = AppUtils.getScWidth(mActivity);
        params1.height = (int) (params1.width*a1.0/720*330);*/
       // mainBanner.setLayoutParams(params1);

        qixiang = (LinearLayout)mainView.findViewById(R.id.qixiang);
        qixiang.setOnClickListener(this);
        zhibao = (LinearLayout)mainView.findViewById(R.id.zhibao);
        zhibao.setOnClickListener(this);
        tufei = (LinearLayout)mainView.findViewById(R.id.tufei);
       tufei.setOnClickListener(this);

        layout_sponsor = (LinearLayout) mainView.findViewById(R.id.layout_sponsor);
        layout_sponsor.setOnClickListener(this);
        WindowManager manager = mActivity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
      //  int height = outMetrics.heightPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout_sponsor.getLayoutParams();
        lp.weight = width-40;
        lp.height = (662*148)/(width-40);
        layout_sponsor.setLayoutParams(lp);
        mBanner = (CustomBanner)mainView.findViewById(R.id.banner);

        if (animationDown == null) {
            animationDown = new DropDownAnim(header, dip2px(getActivity(), 80), true);
            animationDown.setDuration(100); // SUPPRESS CHECKSTYLE
        }
        if (animationUp == null) {
            animationUp = new DropDownAnim(header, dip2px(getActivity(), 80), false);
            animationUp.setDuration(100); // SUPPRESS CHECKSTYLE
        }

        ArrayList<String> images = new ArrayList<>();
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3778456200,3076998411&fm=23&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3535338527,4000198595&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1017904219,2460650030&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2863927798,667335035&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3885596348,1190704919&fm=23&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1597254274,1405139366&fm=23&gp=0.jpg");
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3513269361,2662598514&fm=23&gp=0.jpg");

        setBean(images);

    }

    //设置普通指示器
    private void setBean(final ArrayList beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.text_city:
                intent = new Intent(mActivity, C00_CityActivity.class);
                startActivityForResult(intent, REQUEST_CITY);
                break;
            case R.id.knowledge:
                if (mActivity.checkLogined()) {
                    intent = new Intent(mActivity, B01_KnowledgeActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.expert:
                intent = new Intent(mActivity, C01_ExpertListActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_keyword:
                intent = new Intent(mActivity, B01_SearchActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.quotation:
                intent = new Intent (mActivity, C01_MarketPriceActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.demand:
                mActivity.tabs_fragment.tabSelected(1);
                break;
            case R.id.more:
                intent = new Intent(mActivity, C01_FarmNewsActivity.class);
                intent.putExtra("type_code","JIAJING");
                mActivity.startActivity(intent);
                break;
            case R.id.layout_0:
                if(articleListModel.data.articleList.size()>=1) {
                    intent = new Intent(mActivity, C01_FarmNewsItemDetailActivity.class);
                    intent.putExtra("id", articleListModel.data.articleList.get(0).id);
                    intent.putExtra("type", "sale");
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.layout_1:
                if(articleListModel.data.articleList.size()>=2) {
                    intent = new Intent(mActivity, C01_FarmNewsItemDetailActivity.class);
                    intent.putExtra("id", articleListModel.data.articleList.get(1).id);
                    intent.putExtra("type", "sale");
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.layout_2:
                if(articleListModel.data.articleList.size()>=3){
                    intent = new Intent(mActivity,C01_FarmNewsItemDetailActivity.class);
                    intent.putExtra("id", articleListModel.data.articleList.get(2).id);
                    intent.putExtra("type", "sale");
                    mActivity.startActivity(intent);
                }
                break;
//            case R.id.zhineng:
//                intent = new Intent(mActivity, B01_AgriculturalTechnologyActivity.class);
//                intent.putExtra("flag","1");
//                mActivity.startActivity(intent);
//                break;
//            case R.id.jiagou:
//                intent = new Intent(mActivity, B01_AgriculturalTechnologyActivity.class);
//                intent.putExtra("flag","2");
//                mActivity.startActivity(intent);
//                break;
            case R.id.qixiang:
                intent = new Intent(mActivity, B01_WarnDetailActivity.class);
                intent.putExtra("id",qixiangid.getText());
                mActivity.startActivity(intent);
                break;
            case R.id.zhibao:
                intent = new Intent(mActivity, B01_WarnDetailActivity.class);
                intent.putExtra("id",zhibaoid.getText());
                mActivity.startActivity(intent);
                break;
            case R.id.tufei:
                intent = new Intent(mActivity, B01_WarnDetailActivity.class);
                intent.putExtra("id",tufeiid.getText());
                mActivity.startActivity(intent);
                break;
            case R.id.layout_sponsor:
                if (sponsor != null) {
                    intent = new Intent(mActivity, B01_AgriculturalTechnologyActivity.class);
                    intent.putExtra("title",sponsor.title);
                    intent.putExtra("content",sponsor.description);
                    mActivity.startActivity(intent);
                }
                break;
            case R.id.warn:
                intent = new Intent(mActivity, E01_NoticeActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.headline:
                intent = new Intent(mActivity, E01_NoticeActivity.class);
                mActivity.startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        commonModel.removeResponseListener(this);
        flag=false;
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
        requestnewData(true);
        updateData();
        warnModel.getWarn(wRequest,true);
//        if (commonModel.data.lastUpdateTime + BUFFER_TIME < System.currentTimeMillis() ) {
            requestData(true);
//        } else {
            setChangeCity(false);
//        }

        if (isShow)
            showSuspend();
    }

    String type_code = "0";
    String provience = "辽宁";
    String city = "沈阳";
    String district = "浑南";
    int page = 1;
    String this_app = "0";
    public void requestnewData(boolean bShow)
    {
        provience = AppUtils.getCurrProvince(mActivity);
        city = AppUtils.getCurrCity(mActivity);
        district = AppUtils.getCurrDistrict(mActivity);
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        request.info_from = AppConst.info_from;
        request.type_code = "";
        request.provience = provience;
        request.city = city;
        request.district = district;
        request.lon = AppUtils.getCurrLon(mActivity);
        request.lat = AppUtils.getCurrLat(mActivity);
        request.page = page;
        request.this_app = this_app;
        articleListModel.getArticleList(request,bShow);
    }


    void requestData(boolean isShow)
    {
        if (AppUtils.isLogin(mActivity)) {
//            userInfoModel.getUserInfo(isShow);
        }
        commonModel.reqWeather(GLOBAL_DATA.getInstance(mActivity).currCity);
        commonModel.getSponsor();
    }

    public void setChangeCity(boolean bMust) {
        String newCity = AppUtils.getCurrCity(mActivity);   //GLOBAL_DATA.getInstance(mActivity).currCity
        String prevCity = text_city.getText().toString();

        if (TextUtils.isEmpty(newCity) || newCity.equals(prevCity)) {
            if (!bMust) return;
        } else {
            text_city.setText(newCity);
            prevCity = newCity;
            wRequest.city = prevCity;
            wRequest.district = AppUtils.getCurrDistrict(mActivity);
            wRequest.province = AppUtils.getCurrProvince(mActivity);
            wRequest.info_from = "德铭源";
            wRequest.page = 1;
            warnModel.addResponseListener(this);
            warnModel.getWarnList(wRequest,true);
        }

    }

    void updateData()
    {

    }

    void updateWeather()
    {
        WEATHER localWEATHER = commonModel.data.weather;
        mActivity.localWEATHER = localWEATHER;
        if (!TextUtils.isEmpty(localWEATHER.city)) {
            switch (localWEATHER.dayCond){
                case "晴":
                    img_weather.setBackgroundResource(R.drawable.sunny_day);
                    break;
                case "多云":
                    img_weather.setBackgroundResource(R.drawable.cloud);
                    break;
                case "阴":
                    img_weather.setBackgroundResource(R.drawable.yin);
                    break;
                case "雾":
                    img_weather.setBackgroundResource(R.drawable.wu);
                    break;
                case "雷阵雨":
                    img_weather.setBackgroundResource(R.drawable.leizhenyu);
                    break;
                case "雷阵雨冰雹":
                    img_weather.setBackgroundResource(R.drawable.leizhenyubingbao);
                    break;
                case "阵雨":
                    img_weather.setBackgroundResource(R.drawable.zhenyu);
                    break;
                case "小雨":
                    img_weather.setBackgroundResource(R.drawable.xiaoyu);
                    break;
                case "中雨":
                    img_weather.setBackgroundResource(R.drawable.zhongyu);
                    break;
                case "大雨":
                    img_weather.setBackgroundResource(R.drawable.dayu);
                    break;
                case "暴雨":
                    img_weather.setBackgroundResource(R.drawable.baoyu);
                    break;
                case "大暴雨":
                    img_weather.setBackgroundResource(R.drawable.dabaoyu);
                    break;
                case "特大暴雨":
                    img_weather.setBackgroundResource(R.drawable.tedabaoyu);
                    break;
                case "冻雨":
                    img_weather.setBackgroundResource(R.drawable.dongyu);
                    break;
                case "雨夹雪":
                    img_weather.setBackgroundResource(R.drawable.yujiaxue);
                    break;
                case "阵雪":
                    img_weather.setBackgroundResource(R.drawable.zhenxue);
                    break;
                case "小雪":
                    img_weather.setBackgroundResource(R.drawable.xiaoxue);
                    break;
                case "中雪":
                    img_weather.setBackgroundResource(R.drawable.zhongxue);
                    break;
                case "大雪":
                    img_weather.setBackgroundResource(R.drawable.daxue);
                    break;
                case "暴雪":
                    img_weather.setBackgroundResource(R.drawable.baoxue);
                    break;
                case "浮尘":
                    img_weather.setBackgroundResource(R.drawable.fuchen);
                    break;
                case "霾":
                    img_weather.setBackgroundResource(R.drawable.mai);
                    break;
                case "扬沙":
                    img_weather.setBackgroundResource(R.drawable.yangsha);
                    break;
                case "沙尘暴":
                    img_weather.setBackgroundResource(R.drawable.shachenbao);
                    break;
                case "强沙尘暴":
                    img_weather.setBackgroundResource(R.drawable.qiangshachenbao);
                    break;
            }
            text_cond.setText(localWEATHER.dayCond);
            text_temp.setText(localWEATHER.currentTemp);
            text_windDir.setText(localWEATHER.windDir);
            text_windSc.setText(localWEATHER.windSc);

        } else {
            text_cond.setText("");
            text_temp.setText("");
            text_windDir.setText("");
            text_windSc.setText("");

        }
    }




    @Override
    public void onScroll(int scrollY) {
        if(scrollY >= searchLayoutTop){
            if(suspendView == null){
                showSuspend();
                isShow = true;
            }
        }else if(scrollY <= searchLayoutTop + searchLayoutHeight){
            if(suspendView != null){
                removeSuspend();
                isShow = false;
            }
        }
    }

    /**
     * 显示搜索的悬浮框
     */
    public void showSuspend(){
        if(suspendView == null){
            suspendView = LayoutInflater.from(mActivity).inflate(R.layout.b00_home_search2, null);
            if(suspendLayoutParams == null){
                suspendLayoutParams = new LayoutParams();
                suspendLayoutParams.type = LayoutParams.TYPE_PHONE; //悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下
                suspendLayoutParams.format = PixelFormat.RGBA_8888;
                suspendLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等
                suspendLayoutParams.gravity = Gravity.TOP;  //悬浮窗的对齐方式
                suspendLayoutParams.width = screenWidth;
                suspendLayoutParams.height = searchLayoutHeight;
                suspendLayoutParams.x = 0;  //悬浮窗X的位置
                suspendLayoutParams.y = myScrollViewTop;  ////悬浮窗Y的位置
            }
            TextView city = (TextView) suspendView.findViewById(R.id.text_city);
            city.setText(text_city.getText().toString());
            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, C00_CityActivity.class);
                    startActivityForResult(intent, REQUEST_CITY);
                }
            });

            suspendView.findViewById(R.id.edit_keyword).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, B01_SearchActivity.class);
                    mActivity.startActivity(intent);
                }
            });
        }

        mWindowManager.addView(suspendView, suspendLayoutParams);
    }


    /**
     * 移除购买的悬浮框
     */
    public void removeSuspend(){
        if(suspendView != null){
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeSuspend();
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
    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
        if (url.contains("http://wthrcdn.etouch.cn/weather_mini")) {
            updateWeather();
        }else if (url.contains(ApiInterface.HOME_SPONSOR)){
                sponsor = commonModel.shpon;
        }else if(url.contains(ApiInterface.WARN)){
            if(warnModel.data.routes.size()>=3){
                text_tab_1.setText(warnModel.data.routes.get(0).warning_type);
                text_tab_3.setText(warnModel.data.routes.get(1).warning_type);
                text_tab_5.setText(warnModel.data.routes.get(2).warning_type);
                text_tab_2.setText(warnModel.data.routes.get(0).title);
                text_tab_4.setText(warnModel.data.routes.get(1).title);
                text_tab_6.setText(warnModel.data.routes.get(2).title);
                qixiangid.setText(warnModel.data.routes.get(0).id);
                zhibaoid.setText(warnModel.data.routes.get(1).id);
                tufeiid.setText(warnModel.data.routes.get(2).id);
            }else if(warnModel.data.routes.size()>=2){
                text_tab_1.setText(warnModel.data.routes.get(0).warning_type);
                text_tab_3.setText(warnModel.data.routes.get(1).warning_type);
                text_tab_5.setVisibility(View.GONE);
                text_tab_2.setText(warnModel.data.routes.get(0).title);
                text_tab_4.setText(warnModel.data.routes.get(1).title);
                text_tab_6.setVisibility(View.GONE);
                qixiangid.setText(warnModel.data.routes.get(0).id);
                zhibaoid.setText(warnModel.data.routes.get(1).id);
                tufei.setVisibility(View.GONE);
            }else if(warnModel.data.routes.size()>=1){
                text_tab_1.setText(warnModel.data.routes.get(0).warning_type);
                text_tab_3.setVisibility(View.GONE);
                text_tab_5.setVisibility(View.GONE);
                text_tab_2.setText(warnModel.data.routes.get(0).title);
                text_tab_4.setVisibility(View.GONE);
                text_tab_6.setVisibility(View.GONE);
                qixiangid.setText(warnModel.data.routes.get(0).id);
                zhibao.setVisibility(View.GONE);
                tufei.setVisibility(View.GONE);
            }else if(warnModel.data.routes.size()==0){
                text_tab_1.setVisibility(View.GONE);
                text_tab_3.setVisibility(View.GONE);
                text_tab_5.setVisibility(View.GONE);
                text_tab_2.setVisibility(View.GONE);
                text_tab_4.setVisibility(View.GONE);
                text_tab_6.setVisibility(View.GONE);
                qixiang.setVisibility(View.GONE);
                zhibao.setVisibility(View.GONE);
                tufei.setVisibility(View.GONE);
                warn.setVisibility(View.GONE);
                layout_view.setVisibility(View.GONE);
                line_view.setVisibility(View.GONE);
            }
        }else if(url.contains(ApiInterface.ARTICLELIST)){
                content1 =(TextView)mainView.findViewById(R.id.content1);
                type1 =(TextView)mainView.findViewById(R.id.type1);
                collection1 =(TextView)mainView.findViewById(R.id.collection1);
                num1 =(TextView)mainView.findViewById(R.id.num1);
                time1 =(TextView)mainView.findViewById(R.id.time1);
                img_news1 =(ImageView) mainView.findViewById(R.id.img_news1);
                content1.setText(articleListModel.data.articleList.get(0).title);
                type1.setText(articleListModel.data.articleList.get(0).columen_ves);
                collection1.setText(articleListModel.data.articleList.get(0).collection_num);
                num1.setText(articleListModel.data.articleList.get(0).page_view);
                time1.setText(AppUtils.time(articleListModel.data.articleList.get(0).publish_time));
                imageLoader.displayImage(articleListModel.data.articleList.get(0).img_url, img_news1, FarmingApp.options_small_with_text);
                content2 =(TextView)mainView.findViewById(R.id.content2);
                type2 =(TextView)mainView.findViewById(R.id.type2);
                collection2 =(TextView)mainView.findViewById(R.id.collection2);
                num2 =(TextView)mainView.findViewById(R.id.num2);
                time2 =(TextView)mainView.findViewById(R.id.time2);
                img_news2 =(ImageView) mainView.findViewById(R.id.img_news2);
                time2.setText(AppUtils.time(articleListModel.data.articleList.get(1).publish_time));
                content2.setText(articleListModel.data.articleList.get(1).title);
                type2.setText(articleListModel.data.articleList.get(1).columen_ves);
                collection2.setText(articleListModel.data.articleList.get(1).collection_num);
                num2.setText(articleListModel.data.articleList.get(1).page_view);
                imageLoader.displayImage(articleListModel.data.articleList.get(1).img_url, img_news2, FarmingApp.options_small_with_text);
                //time2.setText(articleListModel.data.articleList.get(1).publish_time);
                content3 =(TextView)mainView.findViewById(R.id.content3);
                type3 =(TextView)mainView.findViewById(R.id.type3);
                collection3 =(TextView)mainView.findViewById(R.id.collection3);
                num3 =(TextView)mainView.findViewById(R.id.num3);
                time3 =(TextView)mainView.findViewById(R.id.time3);
                img_news3 =(ImageView) mainView.findViewById(R.id.img_news3);
                content3.setText(articleListModel.data.articleList.get(2).title);
                type3.setText(articleListModel.data.articleList.get(2).columen_ves);
                collection3.setText(articleListModel.data.articleList.get(2).collection_num);
                num3.setText(articleListModel.data.articleList.get(2).page_view);
                time3.setText(AppUtils.time(articleListModel.data.articleList.get(2).publish_time));
                imageLoader.displayImage(articleListModel.data.articleList.get(2).img_url, img_news3, FarmingApp.options_small_with_text);

        }
        if(url.contains(ApiInterface.WARNDTEAD)){
                headline.setText(warnModel.data.routes.get(0).title);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    boolean istop = true;

    int getScrollY;

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        getScrollY = scrollY;
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (istop) {
            header.startAnimation(animationDown);
        } else {
            if (header.getHeight() > 0)
                header.startAnimation(animationUp);

            if (myScrollView.getScrollerY()<dip2px(getContext(), 80)){
                myScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                },200);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY == 0) {
                    istop = true;
                } else {
                    istop = false;
                }
                break;
        }
        return false;
    }
}