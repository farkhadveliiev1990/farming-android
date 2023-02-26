package com.dmy.farming.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_BuyActivity;
import com.dmy.farming.activity.C01_ExpertListActivity;
import com.dmy.farming.activity.C01_FindHelperActivity;
import com.dmy.farming.activity.C01_RentActivity;
import com.dmy.farming.activity.C01_SellActivity;
import com.dmy.farming.activity.C01_AgrotechniqueArticleActivity;
import com.dmy.farming.activity.C01_AgrotechniqueVideoActivity;
import com.dmy.farming.activity.C01_DiagnosticLibActivity;
import com.dmy.farming.activity.C01_ExpertActivity;
import com.dmy.farming.activity.C01_FarmNewsActivity;
import com.dmy.farming.activity.C01_MarketPriceActivity;
import com.dmy.farming.activity.E01_MyCollectionActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class C00_ActivityFragment extends BaseFragment implements BusinessResponse, XListView.IXListViewListener, View.OnClickListener {

    MainActivity mActivity;
    DictionaryModel dictionaryModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();
        View mainView = inflater.inflate(R.layout.c00_act, null);

        initView(mainView);
        dictionaryModel = new DictionaryModel(mActivity);
        dictionaryModel.addResponseListener(this);

        dictionaryModel.getArticleLabel(AppConst.info_from,"WENZHANGFENLEI");
//        dictionaryModel.getPublishTypeList("绿云","GONGQIU_PUBLISH");

        return mainView;
    }

    //
    View layout_nologin;
    //
    TextView text_sell,news,price,video,article,txt_diagnosis,expert,buy,rent,findhelper;
//    View img_line_0, img_line_1;
    GridView grid_article_label,grid_gongqiu_label;
    ArticleLabelAdapter articleLabelAdapter;
    GongqiuLabelAdapter gongqiuLabelAdapter;
    View null_pager,layout_article_label,layout_gongqiu_label;
    XListView list_activity;

    void initView(View mainView) {
//        layout_nologin = mainView.findViewById(R.id.layout_nologin);
//        View button_login = mainView.findViewById(R.id.button_login);
//        button_login.setOnClickListener(this);
//
//        text_tab_0 = (TextView) mainView.findViewById(R.id.text_tab_0);
//        text_tab_1 = (TextView) mainView.findViewById(R.id.text_tab_1);
//        text_tab_0.setOnClickListener(this);
//        text_tab_1.setOnClickListener(this);
//        img_line_0 = mainView.findViewById(R.id.img_line_0);
//        img_line_1 = mainView.findViewById(R.id.img_line_1);
//
//        null_pager = mainView.findViewById(R.id.null_pager);

        layout_article_label = mainView.findViewById(R.id.layout_article_label);
        grid_article_label = (GridView) mainView.findViewById(R.id.grid_article_label);

        layout_gongqiu_label = mainView.findViewById(R.id.layout_gongqiu_label);
        grid_gongqiu_label = (GridView) mainView.findViewById(R.id.grid_gongqiu_label);

        text_sell = (TextView)mainView.findViewById(R.id.sell) ;
        text_sell.setOnClickListener(this);
//        news = (TextView)mainView.findViewById(R.id.news) ;
//        news.setOnClickListener(this);
        price = (TextView)mainView.findViewById(R.id.price) ;
        price.setOnClickListener(this);

        video = (TextView)mainView.findViewById(R.id.video) ;
        video.setOnClickListener(this);
        expert = (TextView)mainView.findViewById(R.id.expert) ;
        expert.setOnClickListener(this);

        mainView.findViewById(R.id.expertlist).setOnClickListener(this);

        article = (TextView)mainView.findViewById(R.id.article) ;
        article.setOnClickListener(this);

        txt_diagnosis = (TextView)mainView.findViewById(R.id.txt_diagnosis) ;
        txt_diagnosis.setOnClickListener(this);

        buy =(TextView)mainView.findViewById(R.id.buy);
        buy.setOnClickListener(this);
        rent =(TextView)mainView.findViewById(R.id.rent);
        rent.setOnClickListener(this);
        findhelper =(TextView)mainView.findViewById(R.id.findhelper);
        findhelper.setOnClickListener(this);

       // clickTab(0);
    }

    int cur_tab = -1;
  /*  void clickTab(int tab_index) {
        if (tab_index != cur_tab)
        {
            cur_tab = tab_index;
            if (cur_tab == 0) {
                img_line_0.setVisibility(View.VISIBLE);
                img_line_1.setVisibility(View.INVISIBLE);
            } else {
                img_line_1.setVisibility(View.VISIBLE);
                img_line_0.setVisibility(View.INVISIBLE);
            }

            updateData();
            requestData();
        }
    }*/

    void updateArticleLabel(){
        if (dictionaryModel.data.article_label.size() > 0){
            layout_article_label.setVisibility(View.VISIBLE);
            if (articleLabelAdapter == null){
                articleLabelAdapter = new ArticleLabelAdapter(mActivity,dictionaryModel.data.article_label);
                grid_article_label.setAdapter(articleLabelAdapter);
            }else {
                articleLabelAdapter.notifyDataSetChanged();
            }

        }else {
            layout_article_label.setVisibility(View.GONE);
            articleLabelAdapter = null;
            grid_article_label.setAdapter(articleLabelAdapter);
        }
    }

    class ArticleLabelAdapter extends BaseAdapter {

        ArrayList<DICTIONARY> dataList;
        Context mContext;

        public ArticleLabelAdapter(Context context,ArrayList<DICTIONARY> datas) {
            mContext = context;
            dataList = datas;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public DICTIONARY getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.article_label_item, null);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text_label);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final DICTIONARY cat = getItem(position);
            if (cat != null)
            {
                viewHolder.text.setText(cat.name);
                viewHolder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, C01_FarmNewsActivity.class);
                        intent.putExtra("type_code",cat.code);
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }

    }

    class ViewHolder {
        TextView text;
    }

    void updateGongqiuLabel(){
        if (dictionaryModel.data.gongqiu_label.size() > 0){
            layout_gongqiu_label.setVisibility(View.VISIBLE);
            if (gongqiuLabelAdapter == null){
                gongqiuLabelAdapter = new GongqiuLabelAdapter(mActivity,dictionaryModel.data.gongqiu_label);
                grid_gongqiu_label.setAdapter(gongqiuLabelAdapter);
            }else {
                gongqiuLabelAdapter.notifyDataSetChanged();
            }

        }else {
            layout_gongqiu_label.setVisibility(View.GONE);
            gongqiuLabelAdapter = null;
            grid_gongqiu_label.setAdapter(gongqiuLabelAdapter);
        }
    }

    class GongqiuLabelAdapter extends BaseAdapter {

        ArrayList<DICTIONARY> dataList;
        Context mContext;

        public GongqiuLabelAdapter(Context context,ArrayList<DICTIONARY> datas) {
            mContext = context;
            dataList = datas;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        public DICTIONARY getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.article_label_item, null);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text_label);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            DICTIONARY cat = getItem(position);
            if (cat != null)
            {
                viewHolder.text.setText(cat.name);
                viewHolder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, C01_FarmNewsActivity.class);
//                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }

    }

    void requestData() {
        if (!AppUtils.isLogin(mActivity))
            return;

        if (cur_tab == 0) {

        } else {

        }
    }

    boolean refreshLoginState() {
        if (!AppUtils.isLogin(mActivity)) {
            layout_nologin.setVisibility(View.VISIBLE);
            return false;
        } else {
            layout_nologin.setVisibility(View.GONE);
            return true;
        }
    }

    void updateData() {
        if (!refreshLoginState()) {
            return;
        }

        list_activity.stopRefresh();
        list_activity.stopLoadMore();

        if (cur_tab == 0)
        {


        } else {



        }
    }

    @Override
    public void onResume() {
       // refreshLoginState();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dictionaryModel.removeResponseListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_login:
                mActivity.checkLogined();
                break;
           /* case R.id.text_tab_0:
                clickTab(0);
                break;
            case R.id.text_tab_1:
                clickTab(1);
                break;*/
            case R.id.sell:
                intent = new Intent(mActivity, C01_SellActivity.class);
                startActivity(intent);
                break;
            case R.id.video:
                if (mActivity.checkLogined()) {
                    intent = new Intent(mActivity, C01_AgrotechniqueVideoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.price:
                intent = new Intent(mActivity, C01_MarketPriceActivity.class);
                startActivity(intent);
                break;
            case R.id.article:
                if (mActivity.checkLogined()) {
                    intent = new Intent(mActivity, C01_AgrotechniqueArticleActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.txt_diagnosis:
                if (mActivity.checkLogined()) {
                    intent = new Intent(mActivity, C01_DiagnosticLibActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.expertlist:
                intent = new Intent(mActivity, C01_ExpertListActivity.class);
                startActivity(intent);
                break;
            case R.id.expert:
                intent = new Intent(mActivity, C01_ExpertActivity.class);
                startActivity(intent);
                break;
            case R.id.buy:
                intent = new Intent(mActivity, C01_BuyActivity.class);
                startActivity(intent);
                break;
            case R.id.rent:
                intent = new Intent(mActivity, C01_RentActivity.class);
                startActivity(intent);
                break;
            case R.id.findhelper:
                intent = new Intent(mActivity, C01_FindHelperActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == BaseActivity.REQUEST_GLOBAL_LOGIN) {
                cur_tab = -1;
//                clickTab(0);
            }
        }
    }

    public void onRefresh(int id) {
        requestData();
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
        if (url.contains(ApiInterface.ARTICLELABEL)){
            updateArticleLabel();
        }else if (url.contains(ApiInterface.GONGQIULABEL)){
            updateGongqiuLabel();
        }
    }
}
