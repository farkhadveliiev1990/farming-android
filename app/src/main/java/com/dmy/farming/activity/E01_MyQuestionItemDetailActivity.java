package com.dmy.farming.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_MarketPriceListAdapter;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.QuestionDetailModel;
import com.dmy.farming.api.model.QuestionModel;
import com.dmy.farming.view.comment.Comment;
import com.dmy.farming.view.comment.CommentFun;
import com.dmy.farming.view.comment.Moment;
import com.dmy.farming.view.comment.MomentAdapter;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

public class E01_MyQuestionItemDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	QuestionDetailModel dataModel;
	static QuestionModel questionModel;
	static String questionId; //问题id
	ImageLoader imageLoader;
	boolean answer = false;
	QUESTION question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d00_chat_item_list_detail);
		questionId = getIntent().getStringExtra("id");
		answer = getIntent().getBooleanExtra("answer",false);
		imageLoader = ImageLoader.getInstance();

		initView();

		dataModel = new QuestionDetailModel(this);
		dataModel.addResponseListener(this);
		questionModel = new QuestionModel(this);
		questionModel.addResponseListener(this);

		if (answer == true){
			showInputComment(this, "回答", new CommentFun.CommentDialogListener() {
				@Override
				public void onClickPublish(Dialog dialog, EditText input, TextView btn) {
					String content = input.getText().toString();
					if (content.trim().equals("")) {
						errorMsg("评论不能为空");
						return;
					}
					answer(content);
					dialog.dismiss();
				}

				@Override
				public void onShow(int[] inputViewCoordinatesOnScreen) {

				}

				@Override
				public void onDismiss() {

				}
			});
		}
	}

	public void answer(String content){
		questionModel.answer(AppConst.info_from,questionId,content, SESSION.getInstance().sid,"0",dataModel.data.question.userPhone,SESSION.getInstance().usertype);
	}


	/*XListView zuanjia,wangyou;
	View null_pager;
	E10_MyQuestionDetailListAdapter listAdapter;
	E10_MyQuestionDetailList1Adapter listAdapter1;*/

	ListView list_address,mExpertListView;
	Moment mMoment;
	Comment mComment;
	MomentAdapter adoptAdapter,expertAdapter,netfriendAdapter;
	C01_MarketPriceListAdapter listAdapter;
	ImageView img_head,img1,img2,img3,img_head1;
	TextView text_username,text_time,text_position,text_content,text_keyword,text_browse,text_comment,text_username_adopt,
			text_usertype,text_content_adopt,text_time_adopt,text_thumbup;
	XListView list_detail;
	GridView list_adopt,list_expert;
	LinearLayout layout_expert_answer,layout_netfriend;
	View layout_adopt,layout_expert,expert_bottom;
	ImageView solve;

	void initView() {
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

	}


	private void showInputComment(Activity activity, CharSequence hint, final CommentFun.CommentDialogListener listener) {
		final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.view_input_comment);
		dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (listener != null) {
					listener.onDismiss();
				}
			}
		});
		final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
		input.setHint(hint);
		final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClickPublish(dialog, input, btn);
				}
			}
		});
		dialog.setCancelable(true);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (listener != null) {
					int[] coord = new int[2];
					dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(coord);
					// 传入 输入框距离屏幕顶部（不包括状态栏）的长度
					listener.onShow(coord);
				}
			}
		}, 300);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			/*case R.id.more:
				showSelectDialog(v);
				break;*/
		}
	}

	int cur_tab = -1;

	void clickTab(int tab_index) {
		if (tab_index != cur_tab) {
			cur_tab = tab_index;
			//tabSelected(cur_tab);
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
	}




	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}





	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int pageIndex) {
			cur_tab = pageIndex;
			if (pageIndex == 0) {

			} else {

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}


}