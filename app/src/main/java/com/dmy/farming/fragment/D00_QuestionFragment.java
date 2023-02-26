package com.dmy.farming.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.ChooseCropActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.adapter.D00_QuestionListAdapter;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.QuestionListModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class D00_QuestionFragment extends BaseFragment implements BusinessResponse, XListView.IXListViewListener, View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    MainActivity mActivity;

    QuestionListModel dataModel;
    DictionaryModel followModel;
    View mainView;
    int type_id = 1; // 分类

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

        mainView = inflater.inflate(R.layout.d00_chat, null);
        initView(mainView);
        dataModel = new QuestionListModel(getContext(),type_id);
        dataModel.addResponseListener(this);
        followModel = new DictionaryModel(getContext());
        followModel.addResponseListener(this);

        if (AppUtils.isLogin(mActivity)) {
            layout_position.setVisibility(View.VISIBLE);
            followModel.followType(AppConst.info_from, true);
        }else {
            layout_position.setVisibility(View.GONE);
        }

        requestData(true);

        return mainView;
    }


    View layout_nologin,layout_position;

    View add;
    String  attention = "";
    int p  = 1 ;
    XListView list_news;
    View null_pager;
    TextView text_tab_0,text_tab_1,text_tab_2,text_tab_3,text;
    ImageView img_line_0,img_line_1,img_line_2,img_line_3,imageView;
    D00_QuestionListAdapter questionListAdapter;
    View item;
    private LinearLayout mGallery,mGallery1;
    private String[] mImgIds;
    private String[] codesubname;
    private String[] diccode;

    void initView(View mainView) {

        text_tab_0 = (TextView) mainView.findViewById(R.id.text_tab_0);
        text_tab_1 = (TextView) mainView.findViewById(R.id.text_tab_1);
        text_tab_2 = (TextView) mainView.findViewById(R.id.text_tab_2);
        text_tab_3 = (TextView) mainView.findViewById(R.id.text_tab_3);
        text_tab_3.setText(AppUtils.getCurrCity(mActivity));
        img_line_0 = (ImageView) mainView.findViewById(R.id.img_line_0);
        img_line_1 = (ImageView) mainView.findViewById(R.id.img_line_1);
        img_line_2 = (ImageView) mainView.findViewById(R.id.img_line_2);
        img_line_3 = (ImageView) mainView.findViewById(R.id.img_line_3);
        text_tab_0.setOnClickListener(this);
        text_tab_1.setOnClickListener(this);
        text_tab_2.setOnClickListener(this);
        text_tab_3.setOnClickListener(this);

        add = mainView.findViewById(R.id.img_add);
        add.setOnClickListener(this);
        null_pager = mainView.findViewById(R.id.null_pager);
        mGallery = (LinearLayout) mainView.findViewById(R.id.id_gallery);
        layout_position = mainView.findViewById(R.id.layout_position);

        list_news = (XListView) mainView.findViewById(R.id.list_black);

        list_news.setPullLoadEnable(false);
        list_news.setPullRefreshEnable(true);
        list_news.setXListViewListener(this, 1);
        list_news.setAdapter(null);
        attention = "NEW";
       // InitViewPager(mainView);
        clickTab(0);
    }

    int m = 0;
    private void initView1()// 填充数据
    {
        int left, top, right, bottom;
        left = top = right = bottom = 20;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(left, top, right, bottom);
        int childsize = mGallery.getChildCount();
        if (childsize > 4){
            for(int j = childsize;j<=childsize&&j>=0;j--){
                if(j>4){
                    mGallery.removeViewAt(j-1);
                }
            }
        }
        if(mImgIds != null) {
            for ( int i = 0; i < mImgIds.length; i++) {
                 item =  LayoutInflater.from(mActivity).inflate(R.layout.question_tab_item,null);
                TextView text = (TextView)item.findViewById(R.id.text_tab);
              final   ImageView  imageView = (ImageView) item.findViewById(R.id.img_line);
                imageView.setVisibility(View.GONE);
                text.setText(mImgIds[i]);
                text.setTag(i);
                item.setId(i);
                mGallery.addView(item);
                item.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int id =v.getId();
                        for(int j = 0 ; j <  mImgIds.length;j++){
                            if(j == id){
                                ((TextView)v.findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.green));
                                v.findViewById(R.id.img_line).setVisibility(View.VISIBLE);
                                clickTab(4);
                            }else{
                               ((TextView)mainView.findViewById(j).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.text_grey));
                                mainView.findViewById(j).findViewById(R.id.img_line).setVisibility(View.INVISIBLE);
                                clickTab(4);
                            }
                        }
                        attention = diccode[id];
                        p = 1;
                        requestData(true);

                     /*   text.setTextColor(getResources().getColor(R.color.green));
                        m=(Integer) text.getTag();
                        selectTab(m);*/
                      //  imageView.setVisibility(View.VISIBLE);
                    }
                });

            }


        }
    }
    private void selectTab(int position) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mGallery.getChildCount(); i++) {
            //TextView childAt = (TextView) mGallery.getChildAt(position);
            TextView child = (TextView) mGallery.getChildAt(i);
            if (position == i) {
                child.setTextColor(getResources().getColor(R.color.green));
                clickTab(4);
            } else {
                child.setTextColor(getResources().getColor(R.color.text_grey));
                clickTab(4);
            }

        }

    }

    int cur_tab = -1;
    void clickTab(int tab_index) {
        if (tab_index != cur_tab)
        {
            cur_tab = tab_index;
            updateBottomLine();
          //  tabSelected(cur_tab);
        }
    }

    void updateBottomLine() {
        img_line_0.setVisibility(View.INVISIBLE);
        img_line_1.setVisibility(View.INVISIBLE);
        img_line_2.setVisibility(View.INVISIBLE);
        img_line_3.setVisibility(View.INVISIBLE);

        if (cur_tab == 0) {
            img_line_0.setVisibility(View.VISIBLE);
            text_tab_0.setTextColor(getResources().getColor(R.color.green));
            text_tab_1.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_2.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_3.setTextColor(getResources().getColor(R.color.text_grey));
            img_line_0.setBackgroundColor(getResources().getColor(R.color.green));

        }
        else if (cur_tab == 1) {
            img_line_1.setVisibility(View.VISIBLE);
            text_tab_1.setTextColor(getResources().getColor(R.color.green));
            text_tab_0.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_2.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_3.setTextColor(getResources().getColor(R.color.text_grey));
            img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if (cur_tab == 2) {
            img_line_2.setVisibility(View.VISIBLE);
            text_tab_2.setTextColor(getResources().getColor(R.color.green));
            text_tab_1.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_0.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_3.setTextColor(getResources().getColor(R.color.text_grey));
            img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if(cur_tab == 3){
            img_line_3.setVisibility(View.VISIBLE);
            text_tab_3.setTextColor(getResources().getColor(R.color.green));
            text_tab_1.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_2.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_0.setTextColor(getResources().getColor(R.color.text_grey));
            img_line_3.setBackgroundColor(getResources().getColor(R.color.green));
        }else{
            text_tab_3.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_1.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_2.setTextColor(getResources().getColor(R.color.text_grey));
            text_tab_0.setTextColor(getResources().getColor(R.color.text_grey));
        }
    }
    int page = 1;

    public void requestData(boolean bShow)
    {
        //user_attention = "XIAOMAI_CODE";
        page = 1;
        dataModel.getQuestionList(attention,page,p ,bShow);
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

    /**
     * 刷新页面
     */
    public void refresh() {
        if(!handler.hasMessages(MSG_REFRESH)){
            Log.e("chat conv", "refresh possible");
            handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
        } else {
            Log.e("chat conv", "refresh dispossible");
        }
        refreshMetaInfo();
    }

    void refreshMetaInfo() {
    }

    private final static int MSG_REFRESH = 2;
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //onConnectionDisconnected();
                    break;
                case 1:
                    //onConnectionConnected();
                    break;
                case MSG_REFRESH:
                {

                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        if(m==1){
            if (AppUtils.isLogin(mActivity)) {
                followModel.followType(AppConst.info_from, true);
                m=0;
            }
        }
        requestData(true);
        super.onResume();
    }


    private void updateData() {
        list_news.stopRefresh();
        list_news.stopLoadMore();

        if (dataModel.data.questions.size() > 0)
        {
            null_pager.setVisibility(View.GONE);
            if (questionListAdapter == null) {
                questionListAdapter = new D00_QuestionListAdapter(getContext(), dataModel.data.questions);
                list_news.setAdapter(questionListAdapter);
            } else {
                questionListAdapter.notifyDataSetChanged();
            }
            if (0 == dataModel.data.paginated.more) {
//				mFooter.setVisibility(View.VISIBLE);
                list_news.setPullLoadEnable(false);

            } else {
//				mFooter.setVisibility(View.GONE);
                list_news.setPullLoadEnable(true);
            }
        } else {
            questionListAdapter = null;
            list_news.setAdapter(null);
            null_pager.setVisibility(View.VISIBLE);
        }

    }

    private ArrayList<DICTIONARY> ad;
    private void updatecroyData() {
        if (followModel.data.follow_type.size() > 0) {
            ad =  followModel.data.follow_type;
            diccode = new String[followModel.data.follow_type.size()];
            if(followModel.data.follow_type.size()>6){
                mImgIds = new String[6];
                for(int i = 0 ;i<6;i++){
                    mImgIds[i] = ad.get(i).name;
                    diccode[i] = ad.get(i).aboutCode;
                }
            }else{
                mImgIds = new String[followModel.data.follow_type.size()];
                for(int i = 0 ;i<followModel.data.follow_type.size();i++){
                    mImgIds[i] = ad.get(i).name;
                    diccode[i] = ad.get(i).aboutCode;
                }
            }
            initView1();
        } else {

        }
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        if(url.contains(ApiInterface.QUESTIONLIST)) {
            list_news.setRefreshTime();
            updateData();
        }
        if (url.contains(ApiInterface.FOLLOWTYPE)) {
            updatecroyData();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_login:
                mActivity.checkLogined();
                break;
           case R.id.text_tab_0:
                clickTab(0);
               if(mImgIds!=null) {
                   for (int i = 0; i < mImgIds.length; i++) {
                       ((TextView)mainView.findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.text_grey));
                       mainView.findViewById(i).findViewById(R.id.img_line).setVisibility(View.GONE);
                   }
               }
                attention ="NEW" ;
                p = 1;
                requestData(true);
                break;
            case R.id.text_tab_1:
                clickTab(1);
                if(mImgIds!=null){
                    for(int i = 0;i<mImgIds.length;i++){
                        ((TextView)mainView.findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.text_grey));
                        mainView.findViewById(i).findViewById(R.id.img_line).setVisibility(View.GONE);
                    }
                }
                attention ="HOT" ;
                p = 1;
                requestData(true);
                break;
            case R.id.text_tab_2:
                clickTab(2);
                if(mImgIds!=null) {
                    for (int i = 0; i < mImgIds.length; i++) {
                        ((TextView)mainView.findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.text_grey));
                        mainView.findViewById(i).findViewById(R.id.img_line).setVisibility(View.GONE);
                    }
                }
                attention ="JIAJING" ;
                p = 1;
                requestData(true);
                break;
            case R.id.text_tab_3:
                clickTab(3);
                if(mImgIds!=null) {
                    for (int i = 0; i < mImgIds.length; i++) {
                        ((TextView)mainView.findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.text_grey));
                        mainView.findViewById(i).findViewById(R.id.img_line).setVisibility(View.GONE);
                    }
                }
                attention = text_tab_3.getText().toString();
                p = 0;
                requestData(true);
                break;
            case R.id.img_add:
               if(mActivity.checkLogined()){
                   intent = new Intent(mActivity, ChooseCropActivity.class);
                   ArrayList<String> attentname = new ArrayList<>();
                   ArrayList<String> attentcode = new ArrayList<>();
                   if(ad !=null){
                       for (DICTIONARY dictionary : ad){
                           attentname.add(dictionary.name);
                           attentcode.add(dictionary.aboutCode);
                       }
                   }
                   intent.putStringArrayListExtra("attentname",attentname);
                   intent.putStringArrayListExtra("attentcode",attentcode);
                   startActivity(intent);
                   m=1;
               }
                break;

        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == BaseActivity.REQUEST_GLOBAL_LOGIN) {
               /* cur_tab = -1;
                clickTab(1);
                clickTab(0);*/
            }
        }
    }


    Dialog mDialog;
    void dispRightMenu() {
        if (mDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View view = inflater.inflate(R.layout.b00_right_menu, null);

            mDialog = new Dialog(mActivity, R.style.transparentFrameWindowStyle);
            mDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Window window = mDialog.getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = 0;
            wl.width = AppUtils.getScWidth(mActivity);
            wl.height = AppUtils.getScHeight(mActivity);
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
                    if (mActivity.checkLogined()) {
//                        Intent intent = new Intent(mActivity, B03_CreateGroupActivity.class);
//                        startActivity(intent);
                    }
                }
            });

            View button_menu_1 = view.findViewById(R.id.button_menu_1);
            button_menu_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeMenu();
                    if (mActivity.checkLogined()) {
//                        Intent intent = new Intent(mActivity, D02_AddActivity.class);
//                        startActivity(intent);
                    }
                }
            });
        }
        mDialog.show();
    }


    D00_QuestionItemFragment chatItemFragment;

    void closeMenu() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void onRefresh(int id) {
        requestData(false);
    }

    @Override
    public void onLoadMore(int id) {
        page = page+1;
        dataModel.getQuestionListmore(attention,page,p ,true);
    }

    public void ScrollToTop() {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            list_news.smoothScrollToPosition(0);
        } else {
            list_news.setSelection(0);
        }
    }
}
