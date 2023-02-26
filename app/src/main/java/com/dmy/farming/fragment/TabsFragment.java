package com.dmy.farming.fragment;

import android.annotation.TargetApi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.ShareConst;
import com.dmy.farming.activity.A02_ThirdSignupActivity;
import com.dmy.farming.activity.E01PubilcSellActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.activity.PublishBuyActivity;
import com.dmy.farming.activity.PublishFindFriendActivity;
import com.dmy.farming.activity.PublishQuestionActivity;
import com.dmy.farming.activity.PublishRentActivity;
import com.dmy.farming.api.data.SESSION;
import com.external.eventbus.EventBus;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class TabsFragment extends Fragment implements View.OnClickListener {

    public TabsFragment() {

    }

    static MainActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.toolbar, container, false);
        mContext = (MainActivity) getActivity();
        init(mainView);

        EventBus.getDefault().register(this);
        return mainView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        setRetainInstance(true);
    }

    View layout_menu,layout_menu_0, layout_menu_1, layout_menu_2, layout_menu_3;
    ImageView img_menu_0, img_menu_1, img_menu_2, img_menu_3;
//    TextView text_unread_cnt;

    int cur_index = -1;

    void init(View mainView) {

        layout_menu = mainView.findViewById(R.id.layout_menu);
        layout_menu.setOnClickListener(this);
        layout_menu_0 = mainView.findViewById(R.id.layout_menu_0);
        layout_menu_0.setOnClickListener(this);
        layout_menu_1 = mainView.findViewById(R.id.layout_menu_1);
        layout_menu_1.setOnClickListener(this);
        layout_menu_2 = mainView.findViewById(R.id.layout_menu_2);
        layout_menu_2.setOnClickListener(this);
        layout_menu_3 = mainView.findViewById(R.id.layout_menu_3);
        layout_menu_3.setOnClickListener(this);

        img_menu_0 = (ImageView) mainView.findViewById(R.id.img_menu_0);
        img_menu_1 = (ImageView) mainView.findViewById(R.id.img_menu_1);
        img_menu_2 = (ImageView) mainView.findViewById(R.id.img_menu_2);
        img_menu_3 = (ImageView) mainView.findViewById(R.id.img_menu_3);


        tabSelected(0);
    }

    B00_HomeFragment homeFragment;
    C00_ActivityFragment activityFragment;
    D00_QuestionFragment chatFragment;
    E00_MyFragment myFragment;

	public void tabSelected(int index)
    {
        if (cur_index == index) return;
        cur_index = index;

        this.img_menu_0.setImageResource(R.drawable.tab_home);
        this.img_menu_1.setImageResource(R.drawable.tab_service);
        this.img_menu_2.setImageResource(R.drawable.tab_doubt);
        this.img_menu_3.setImageResource(R.drawable.tab_personal);

        if (index == 0)
        {

           this.img_menu_0.setImageResource(R.drawable.tab_home_h);
            homeFragment = new B00_HomeFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, homeFragment, "tab_one");
            localFragmentTransaction.commitAllowingStateLoss();
        }
        else if (index == 1)
        {
            this.img_menu_1.setImageResource(R.drawable.tab_service_h);
            activityFragment = new C00_ActivityFragment();
           // activityFragment = （C00_ActivityFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, activityFragment, "tab_two");
            localFragmentTransaction.commitAllowingStateLoss();
        }
        else if (index == 2)
        {
            this.img_menu_2.setImageResource(R.drawable.tab_doubt_h);
            chatFragment = new D00_QuestionFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, chatFragment, "tab_three");
            localFragmentTransaction.commitAllowingStateLoss();
        }
        else
        {
            this.img_menu_3.setImageResource(R.drawable.tab_personal_h);
            myFragment = new E00_MyFragment();
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, myFragment, "tab_four");
            localFragmentTransaction.commitAllowingStateLoss();
        }
    }

    long firstClickTime = 0;
    long secondClickTime = 0;

    public void doubleClick(View view) {

        if (firstClickTime > 0) {
            secondClickTime = SystemClock.uptimeMillis();
            if (secondClickTime - firstClickTime < 500) {
//                LogUtils.d("***************   double click  ******************");
                chatFragment.ScrollToTop();
            }
            firstClickTime = 0;
            return;
        }

        firstClickTime = SystemClock.uptimeMillis();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    firstClickTime = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    final static int publishquestion = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == publishquestion) {
                tabSelected(2);
            }
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layout_menu_0:
                tabSelected(0);
                break;
            case R.id.layout_menu_1:
                tabSelected(1);
                break;
            case R.id.layout_menu_2:
                tabSelected(2);
                doubleClick(view);
                break;
            case R.id.layout_menu_3:
                tabSelected(3);
                break;
            case R.id.layout_menu:
                homeFragment.removeSuspend();
                showSelectDialog();
                break;
        }
    }

    Dialog mMenuDialog;
    Intent intent;
    private void showSelectDialog() {
        // TODO Auto-generated method stub

        if (mMenuDialog == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            View view = inflater.inflate(R.layout.issue_menus, null);
            mMenuDialog = new Dialog(getContext(), R.style.transparentFrameWindowStyle);
            mMenuDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Window window = mMenuDialog.getWindow();
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mMenuDialog.onWindowAttributesChanged(wl);
            mMenuDialog.setCanceledOnTouchOutside(false);

            Calendar c = Calendar.getInstance();//
            String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
            String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当日期
            int mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期

            if (mDay.length() == 1)
                ((TextView)view.findViewById(R.id.text_day)).setText("0" + mDay);
            else
                ((TextView)view.findViewById(R.id.text_day)).setText(mDay);

            switch (mWay){
                case 1:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期日");
                    break;
                case 2:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期一");
                    break;
                case 3:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期二");
                    break;
                case 4:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期三");
                    break;
                case 5:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期四");
                    break;
                case 6:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期五");
                    break;
                case 7:
                    ((TextView)view.findViewById(R.id.text_week)).setText("星期六");
                    break;
            }

            if (mMonth.length() == 1)
                ((TextView)view.findViewById(R.id.text_date)).setText("0"+ mMonth + "/" + mYear);
            else
                ((TextView)view.findViewById(R.id.text_date)).setText(mMonth + "/" + mYear);

            ((TextView)view.findViewById(R.id.text_city)).setText(AppUtils.getCurrCity(getContext()));
            ((TextView)view.findViewById(R.id.text_weather)).setText(mContext.localWEATHER == null ? "":mContext.localWEATHER.dayCond);
            ((TextView)view.findViewById(R.id.text_temp)).setText(mContext.localWEATHER == null ? "":mContext.localWEATHER.currentTemp);

            View layout_cancel = view.findViewById(R.id.layout_cancel);
            layout_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    if (homeFragment.isShow)
                        homeFragment.showSuspend();
                }
            });

            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    if (homeFragment.isShow)
                        homeFragment.showSuspend();
                }
            });

            View layout_menu_0, layout_menu_1,layout_menu_4;
            layout_menu_0 = view.findViewById(R.id.layout_menu_0);
            layout_menu_0.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
//                    mMenuDialog.dismiss();
//                    mMenuDialog = null;
                    if (mContext.checkLogined()) {
                        if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null){
                            startActivity(new Intent(getContext(),A02_ThirdSignupActivity.class));
                            return;
                        }
                        intent = new Intent(mContext,PublishBuyActivity.class);
                        startActivity(intent);
                    }


                }
            });
            layout_menu_1 = view.findViewById(R.id.layout_menu_1);
            layout_menu_1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (mContext.checkLogined()) {
                        if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null) {
                            startActivity(new Intent(getContext(), A02_ThirdSignupActivity.class));
                            return;
                        }
                        intent = new Intent(mContext, E01PubilcSellActivity.class);
                        startActivity(intent);
                    }
                }
            });

            layout_menu_2 = view.findViewById(R.id.layout_menu_2);
            layout_menu_2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (mContext.checkLogined()) {
                        if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null) {
                            startActivity(new Intent(getContext(), A02_ThirdSignupActivity.class));
                            return;
                        }
                        intent = new Intent(mContext, PublishRentActivity.class);
                        startActivity(intent);
                    }
                }
            });

            layout_menu_3 = view.findViewById(R.id.layout_menu_3);
            layout_menu_3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (mContext.checkLogined()) {
                        if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null) {
                            startActivity(new Intent(getContext(), A02_ThirdSignupActivity.class));
                            return;
                        }
                        intent = new Intent(mContext, PublishFindFriendActivity.class);
                        startActivity(intent);
                    }
                }
            });

            layout_menu_4 = view.findViewById(R.id.layout_menu_4);
            layout_menu_4.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (mContext.checkLogined()) {
                        if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null) {
                            startActivity(new Intent(getContext(), A02_ThirdSignupActivity.class));
                            return;
                        }
                        intent = new Intent(getContext(), PublishQuestionActivity.class);
                        startActivityForResult(intent,publishquestion);
                        mMenuDialog.dismiss();
                        mMenuDialog = null;
                    }
                }
            });
        }

        mMenuDialog.show();
    }

    public void onEvent(Object event){
        Message message = (Message) event;
        if(message.what == ShareConst.WEIXIN_PAY){

        }
    }

    @Override
    public void onDestroy() {
        chatFragment = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void refreshUnreadCnt() {
        int count = 0;

//        if (AppUtils.isLogin(mContext))
//            count = ChatMng.getInstance().getUnreadMsgCount();

//        if (count > 0) {
//            text_unread_cnt.setVisibility(View.VISIBLE);
//            text_unread_cnt.setText(String.valueOf(count));
//        } else {
//            text_unread_cnt.setVisibility(View.GONE);
//        }
    }

}