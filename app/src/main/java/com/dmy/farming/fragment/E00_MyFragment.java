package com.dmy.farming.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.bee.view.MyDialog;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.A01_SigninActivity;
import com.dmy.farming.activity.A02_ThirdSignupActivity;
import com.dmy.farming.activity.E01_ActCenterActivity;
import com.dmy.farming.activity.E01_FindHelpActivity;
import com.dmy.farming.activity.E01_HelpCenterActivity;
import com.dmy.farming.activity.E01_InfoActivity;
import com.dmy.farming.activity.E01_InsuranceActivity;
import com.dmy.farming.activity.E01_InvitationActivity;
import com.dmy.farming.activity.E01_MyBuyActivity;
import com.dmy.farming.activity.E01_MyCollectionActivity;
import com.dmy.farming.activity.E01_MyFollowActivity;
import com.dmy.farming.activity.E01_MyQuestionActivity;
import com.dmy.farming.activity.E01_MyQuestionAnswerActivity;
import com.dmy.farming.activity.E01_MyRentActivity;
import com.dmy.farming.activity.E01_MySellActivity;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.activity.E01_SettingActivity;
import com.dmy.farming.activity.E01_StoreActivity;
import com.dmy.farming.activity.E01_TracingActivity;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.USER;
import com.dmy.farming.api.model.NoticeModel;
import com.dmy.farming.api.model.UserInfoModel;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.warnRequest;
import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.UserInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class E00_MyFragment extends BaseFragment implements IXListViewListener, OnClickListener, BusinessResponse
{
	MainActivity mActivity;

	UserInfoModel userInfoModel;
    NoticeModel noticeModel;
    warnRequest warnRequest;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		mActivity = (MainActivity) getActivity();
		View mainView = inflater.inflate(R.layout.e00_my_head, null);
		initView(mainView);

		userInfoModel = new UserInfoModel(mActivity);
		userInfoModel.addResponseListener(this);
        noticeModel = new NoticeModel(mActivity);
        warnRequest = new warnRequest();
        noticeModel.addResponseListener(this);
		return mainView;
	}

		//XListView list_my;
	ImageView img_head;
	TextView text_name,text_location,text_signin_num,text_signin,text_coupon,text_unread_friend;
	SharedPreferences.Editor editor;
	SharedPreferences shared;
	View layout_coupon;

	void initView(View mainView)
	{
		View notice = mainView.findViewById(R.id.notice);
		notice.setOnClickListener(this);

		View setting = mainView.findViewById(R.id.setting);
		setting.setOnClickListener(this);

		View question = mainView.findViewById(R.id.question);
		question.setOnClickListener(this);
		View collection = mainView.findViewById(R.id.collection);
		collection.setOnClickListener(this);
		View follow = mainView.findViewById(R.id.follow);
		follow.setOnClickListener(this);
		View tracing = mainView.findViewById(R.id.tracing);
		tracing.setOnClickListener(this);
		View insurance = mainView.findViewById(R.id.insurance);
		insurance.setOnClickListener(this);
		View layout_menu_6 = mainView.findViewById(R.id.buy);
		layout_menu_6.setOnClickListener(this);
		View layout_menu_7 = mainView.findViewById(R.id.sell);
		layout_menu_7.setOnClickListener(this);
		View layout_menu_8 = mainView.findViewById(R.id.rent);
		layout_menu_8.setOnClickListener(this);
		View layout_menu_9 = mainView.findViewById(R.id.findhelp);
		layout_menu_9.setOnClickListener(this);
		View store = mainView.findViewById(R.id.store);
		store.setOnClickListener(this);
		View actcenter = mainView.findViewById(R.id.actcenter);
		actcenter.setOnClickListener(this);
		View helpcenter = mainView.findViewById(R.id.helpcenter);
		helpcenter.setOnClickListener(this);
		View invitation = mainView.findViewById(R.id.invitation);
		invitation.setOnClickListener(this);
		View share = mainView.findViewById(R.id.share);
		share.setOnClickListener(this);
		mainView.findViewById(R.id.layout_head).setOnClickListener(this);

		img_head =(ImageView)mainView.findViewById(R.id.img_head);
		img_head.setOnClickListener(this);
		text_name =(TextView)mainView.findViewById(R.id.text_name);
		text_location =(TextView)mainView.findViewById(R.id.text_location);
		text_coupon =(TextView)mainView.findViewById(R.id.text_coupon);
		text_signin_num =(TextView)mainView.findViewById(R.id.text_signin_num);
		text_signin =(TextView)mainView.findViewById(R.id.text_signin);
		text_signin.setOnClickListener(this);
        text_unread_friend = (TextView)mainView.findViewById(R.id.text_unread_friend);

		layout_coupon = mainView.findViewById(R.id.layout_coupon);

		shared = mActivity.getSharedPreferences("userInfo", 0);
		editor = shared.edit();
		String signintime = shared.getString("signintime","");
		if(signintime.equals("")){
			text_signin.setEnabled(true);
		}else{
			if(AppUtils.isDateOneBigger(signintime)){
				text_signin.setEnabled(true);
			}
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		noticeModel.getNoticnum();
		updateData();
		//requestData();
	}

	void requestData()
	{
		if (AppUtils.isLogin(mActivity)) {
			userInfoModel.getUserInfo();
		} else {

		}
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	USER userInfo;

	void updateData() {
//		list_my.stopRefresh();

		if (AppUtils.isLogin(mActivity))
			userInfo = userInfo(SESSION.getInstance().uid);
		else
			userInfo = null;

		if (userInfo != null) {
			imageLoader.displayImage(userInfo.avatar, img_head, FarmingApp.options_head);
			text_name.setText(userInfo.nickname);
			text_location.setText(userInfo.provience + userInfo.city + userInfo.district);
			layout_coupon.setVisibility(View.VISIBLE);
			text_coupon.setText(userInfo.coupon + "");
			text_signin_num.setText(userInfo.signnum);
		} else {
			layout_coupon.setVisibility(View.GONE);
			imageLoader.displayImage("", img_head, FarmingApp.options_head);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_head:
				clickInfo();
				break;
			case R.id.layout_head:
				clickInfo();
				break;
			case R.id.layout_menu_0:
				if (mActivity.checkLogined()) {

				}
				break;
			case R.id.layout_menu_1:
				if (mActivity.checkLogined()) {
//					intent = new Intent(mActivity, E01_CollectActivity.class);
//					startActivity(intent);
				}
				break;
			case R.id.layout_menu_2:
				if (mActivity.checkLogined()) {
//					intent = new Intent(mActivity, E02_HistoryActivity.class);
//					startActivity(intent);
				}
				break;
			case R.id.setting:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_SettingActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.notice:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_NoticeActivity.class);
					startActivityForResult(intent, NOTICE_CODE);
				//	startActivity(intent);
				}
				break;
			case R.id.collection:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_MyCollectionActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.follow:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_MyFollowActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.question:
				if (mActivity.checkLogined()) {
					if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null){
						startActivity(new Intent(getContext(),A02_ThirdSignupActivity.class));
						return;
					}
//					intent = new Intent(mActivity, E01_MyQuestionActivity.class);
					intent = new Intent(mActivity, E01_MyQuestionAnswerActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.buy:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_MyBuyActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.sell:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_MySellActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.tracing:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_TracingActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.findhelp:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_FindHelpActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.insurance:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_InsuranceActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.rent:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_MyRentActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.store:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_StoreActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.actcenter:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_ActCenterActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.helpcenter:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_HelpCenterActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.invitation:
				if (mActivity.checkLogined()) {
					intent = new Intent(mActivity, E01_InvitationActivity.class);
					startActivity(intent);
				}
				break;
			case R.id.share:
				if (mActivity.checkLogined()) {
					showSelectDialog();
				}
				break;
			case R.id.text_signin:
				if (mActivity.checkLogined()) {
					userInfoModel.signin(false);
				}
				break;

		}
	}


	void clickInfo()
	{
		if (mActivity.checkLogined()) {
			Intent intent = new Intent(mActivity, E01_InfoActivity.class);
			startActivity(intent);
		}
	}

	void clickQianBao()
	{
//		if (userInfo.paypwd == 0)
//		{
//			Intent intent = new Intent(mActivity, E03_ChangePayPassActivity.class);
//			startActivityForResult(intent, WAIT_QIANBAO_PASS);
//		}
//		else
//		{
//			gotoQianBao();
//		}
	}

	void gotoQianBao()
	{
//		Intent intent = new Intent(mActivity, E02_PurseActivity.class);
//		startActivity(intent);
	}

	//
	Dialog mMenuDialog;
	private void showSelectDialog() {
		// TODO Auto-generated method stub

		if (mMenuDialog == null)
		{
			LayoutInflater inflater = LayoutInflater.from(mActivity);

			View view = inflater.inflate(R.layout.a07_select_menus, null);
			mMenuDialog = new Dialog(mActivity, R.style.transparentFrameWindowStyle);
			mMenuDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			Window window = mMenuDialog.getWindow();
			window.setWindowAnimations(R.style.main_menu_animstyle);
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
			wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
			wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			mMenuDialog.onWindowAttributesChanged(wl);
			mMenuDialog.setCanceledOnTouchOutside(true);

			View layout_cancel = view.findViewById(R.id.layout_cancel);
			layout_cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
				}
			});

			view.findViewById(R.id.button_login).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mMenuDialog.dismiss();
					mMenuDialog = null;
				}
			});

			View layout_menu_0, layout_menu_1, layout_menu_2, layout_menu_3, layout_menu_4;
			layout_menu_0 = view.findViewById(R.id.layout_menu_0);
			layout_menu_0.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					clickShare(0);
				}
			});

			layout_menu_1 = view.findViewById(R.id.layout_menu_1);
			layout_menu_1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					clickShare(1);
				}
			});

			layout_menu_2 = view.findViewById(R.id.layout_menu_2);
			layout_menu_2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mMenuDialog.dismiss();
					mMenuDialog = null;
					clickShare(2);
				}
			});

			layout_menu_3 = view.findViewById(R.id.layout_menu_3);
			layout_menu_3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mMenuDialog.dismiss();
					mMenuDialog = null;
					clickShare(3);
				}
			});

			layout_menu_4 = view.findViewById(R.id.layout_menu_4);
			layout_menu_4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mMenuDialog.dismiss();
					mMenuDialog = null;
					clickShare(4);
				}
			});

		}

		mMenuDialog.show();
	}

	private void clickShare(int platform) {

		String shareTitle = (!TextUtils.isEmpty(SESSION.getInstance().nick) ? SESSION.getInstance().nick : "我") + "在@农事在线 发现了好多农业方面的资讯，快来看看吧！";
		String shareContent = "农事在线咨询";
		String shareLink = "http://a.app.qq.com/o/simple.jsp?pkgname=com.beidou.wukong";

		UMWeb web = new UMWeb(shareLink);
		web.setTitle(shareTitle);
		web.setDescription(shareContent);
		web.setThumb(new UMImage(mActivity, R.drawable.app_logo));
		ShareAction action = new ShareAction(mActivity).withMedia(web)
				.setCallback(umShareListener);

		switch(platform)
		{
			case 0:
				action.setPlatform(SHARE_MEDIA.WEIXIN)
						.share();
				break;
			case 1:
				action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
						.share();
				break;
			case 2:
				action.setPlatform(SHARE_MEDIA.QQ)
						.share();
				break;
			case 3:
				action.setPlatform(SHARE_MEDIA.QZONE)
						.share();
				break;
			case 4:
				action.setPlatform(SHARE_MEDIA.SINA)
						.share();
				break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		UMShareAPI.get(mActivity).release();
	}

	private UMShareListener umShareListener = new UMShareListener() {

		@Override
		public void onStart(SHARE_MEDIA platform) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.e("bird","platform"+ platform);
			if(platform.name().equals("WEIXIN_FAVORITE")){
				mActivity.errorMsg("收藏成功啦");
			}else{
				onShareSucc();
			}
		}

		@Override
		public void onError(SHARE_MEDIA share_media, Throwable throwable) {
			Log.e("bird","onError"+share_media);
			mActivity.errorMsg("分享失败了");
		}

		@Override
		public void onCancel(SHARE_MEDIA share_media) {
			Log.e("bird","onCancel"+share_media);
			mActivity.errorMsg("分享取消了");
		}
	};

	void onShareSucc() {
		mActivity.errorMsg("分享成功");
	}

	@Override
	public void onRefresh(int id) {
		if (AppUtils.isLogin(mActivity)) {
			requestData();
		}
		else {
//			list_my.stopRefresh();
		}
	}

	@Override
	public void onLoadMore(int id) {
		//
	}

	public final static int WAIT_QIANBAO_PASS = 2;

	public final static int NOTICE_CODE = 3;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mActivity).onActivityResult( requestCode, resultCode, data);

		if (data != null)
		{
			if (requestCode == WAIT_QIANBAO_PASS)
				gotoQianBao();
		}
		if(resultCode == RESULT_OK) {
			if (requestCode == NOTICE_CODE)
				//gotoQianBao();
				noticeModel.getNoticnum();
		}
	}

	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		if (url.contains(ApiInterface.USER_INFO)) {
//			list_my.setRefreshTime();
			userInfo = userInfoModel.user;
			updateData();
		}else if (url.contains(ApiInterface.SIGNIN)){
			text_signin.setEnabled(false);
			userInfo = userInfoModel.user;
			//text_signin_num.setText((Integer.parseInt(text_signin_num.getText().toString())+1) +"");
			updateData();

			SimpleDateFormat tDate = new SimpleDateFormat("yyyyMMdd");
			editor.putString("signintime",tDate.format( new Date()));
			editor.commit();
		}else if(url.contains(ApiInterface.getNoticnum)){

           int a = noticeModel.num;
			if(a==0){
				text_unread_friend.setVisibility(View.GONE);
			}else{
				text_unread_friend.setVisibility(View.VISIBLE);
				text_unread_friend.setText(String.valueOf(a));
			}


        }
	}

	public static USER userInfo(String uid) {
		return new Select().from(USER.class).where("USER_id = ?", uid).executeSingle();
	}

	@Override
	public void onDestroy() {
		userInfoModel.removeResponseListener(this);
		super.onDestroy();
	}
	@Override
	public void onPause() {
		super.onPause();
	}

}
