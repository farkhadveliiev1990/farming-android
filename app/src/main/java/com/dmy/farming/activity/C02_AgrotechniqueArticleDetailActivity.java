package com.dmy.farming.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_AgrotechniqueVideoAdapter;
import com.dmy.farming.adapter.C01_ArticleAdapter;
import com.dmy.farming.adapter.C02_ArticleCommentAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.api.model.AgrotechniqueArticleDetailModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.similarRequest;
import com.dmy.farming.view.comment.AgrotechniqueMoment;
import com.dmy.farming.view.comment.Comment;
import com.dmy.farming.view.comment.CommentFun;
import com.dmy.farming.view.comment.CustomTagHandler;
import com.dmy.farming.view.comment.Moment;
import com.dmy.farming.view.comment.User;
import com.dmy.farming.view.emojicon.EaseDefaultEmojiconDatas;
import com.dmy.farming.view.emojicon.EaseEmojicon;
import com.dmy.farming.view.emojicon.EaseSmileUtils;
import com.dmy.farming.view.emojicon.EmojiconGridAdapter;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class C02_AgrotechniqueArticleDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c02_activity_agrotechnique_article_detail);

		id = getIntent().getStringExtra("id");
        cycle = getIntent().getStringExtra("cycle");
		agrotechniqueArticleDetailModel = new AgrotechniqueArticleDetailModel(this,id);
		agrotechniqueArticleDetailModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		artRequest =  new articleRequest();
		request = new similarRequest();
		initView();

		updateData(true);
		updatecommentData(true);

	}

    static String id,cycle;
	static	AgrotechniqueArticleDetailModel agrotechniqueArticleDetailModel;
//	C02_ArticleCommentAdapter articleAdapter;
	MomentAdapter momentAdapter;
	View layout_comment,layout_input,layout_similar_video;
	XListView listView;
	TextView title, keyword, time, publish_user, content,likenum,text_comment_num,textmore;
	View like,null_pager;
	ImageView likeimg,more,img_emoji;
	View layout;
	Moment mMoment;
	Comment mComment;
    static  String  publishuser ;
    static articleRequest artRequest;
	EditText edit_comment;
	DictionaryModel dictionaryModel;
	int textlike = 0;
	int iscollection;
	LinearLayout text_keyword,similar_article,layout_similar_article;
	similarRequest request;
    GridView gv_emoji;
    EmojiconGridAdapter smileAdapter;

	void initView()
	{
		View headerView = LayoutInflater.from(this).inflate(R.layout.c02_activity_agrotechnique_article_detail_header, null);
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

	//	keyword = (TextView)  headerView.findViewById(R.id.keyword);


		null_pager =  findViewById(R.id.null_pager);

		layout_similar_article =(LinearLayout) headerView.findViewById(R.id.layout_similar_article);
		similar_article = (LinearLayout) headerView.findViewById(R.id.similar_article);
		

		title = (TextView) headerView.findViewById(R.id.title);
		publish_user = (TextView) headerView.findViewById(R.id.publish_user);
		time = (TextView)  headerView.findViewById(R.id.time);
		content = (TextView)  headerView.findViewById(R.id.content);
		likeimg = (ImageView)headerView.findViewById(R.id.likeimg);
		like = headerView.findViewById(R.id.like);
		like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(textlike == 0)
				{
					artRequest.about_userid = "";
					artRequest.comment_id = "";
					artRequest.publish_userid = publishuser;
					artRequest.user_id = SESSION.getInstance().uid;
					artRequest.about_id = id;
					artRequest.like_type = 1;
					artRequest.from_openid = "";
					agrotechniqueArticleDetailModel.like(artRequest);
				}else{
					artRequest.comment_id = "";
					//	quesRequest.publish_userid = publishuser;
					artRequest.user_id = SESSION.getInstance().uid;
					artRequest.about_id = id;
					artRequest.like_type = 1;
					artRequest.from_openid = "";
					agrotechniqueArticleDetailModel.cancellike(artRequest);
				}
			}
		});
		likenum = (TextView)headerView.findViewById(R.id.likenum);
		text_comment_num = (TextView)headerView.findViewById(R.id.text_comment_num);
		headerView.findViewById(R.id.expert).setOnClickListener(this);
		layout_comment = headerView.findViewById(R.id.layout_comment);

		// 评论输入框
		layout_input = findViewById(R.id.layout_input);
		edit_comment = (EditText) layout_input.findViewById(R.id.edit_comment);
        edit_comment.setOnClickListener(this);
		layout_input.findViewById(R.id.text_send).setOnClickListener(this);
        img_emoji = (ImageView) layout_input.findViewById(R.id.img_emoji);
        img_emoji.setOnClickListener(this);
        gv_emoji = (GridView) layout_input.findViewById(R.id.gv_emoji);


        final List<EaseEmojicon> list = new ArrayList<EaseEmojicon>();
        list.addAll(Arrays.asList(EaseDefaultEmojiconDatas.getData()));
        smileAdapter = new EmojiconGridAdapter(this, 1, list);
        gv_emoji.setAdapter(smileAdapter);
        gv_emoji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EaseEmojicon emojicon = list.get(i);
                String emojiText = emojicon.getEmojiText();
                if (emojiText != null && emojiText.equals(EaseSmileUtils.DELETE_KEY)) {
                    if (!TextUtils.isEmpty(edit_comment.getText())) {
                        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                        edit_comment.dispatchKeyEvent(event);
                    }
                } else {
                    edit_comment.append(EaseSmileUtils.getSmiledText(C02_AgrotechniqueArticleDetailActivity.this, emojicon.getEmojiText()));
                }
            }
        });

		more =(ImageView) findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupWindow == null) {
					showSelectDialog(v);
				}
			}
		});
		text_keyword= (LinearLayout)headerView.findViewById(R.id.text_keyword);
		textmore = (TextView)headerView.findViewById(R.id.textmore);
		textmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(ismore==1){
					page = page + 1;
					updatesimilararticle(false);
				}else{
					page = 1;
					updatesimilararticle(false);
				}

			}
		});

		listView =(XListView)findViewById(R.id.list_black);
		listView.addHeaderView(headerView);
	//	listView.setXListViewListener(this, 1);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setAdapter(null);



	}


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.expert:
				Intent intent = new Intent(this,C01_ExpertListActivity.class);
				startActivity(intent);
				break;
			case R.id.text_send:
				String content = edit_comment.getText().toString();
				if ("".equals(content)){
					errorMsg("评论内容不能为空！");
				}else {
					dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"0","",content,"","0");
                    edit_comment.setText("");
				}
				break;
            case R.id.img_emoji:
                closeKeyBoard(edit_comment);
                gv_emoji.setVisibility(View.VISIBLE);
                break;
            case R.id.edit_comment:
                gv_emoji.setVisibility(View.GONE);
                break;
		}
	}

    public void closeKeyBoard(EditText edit_keyword) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
    }

	@Override
	protected void onResume() {
		super.onResume();

	}

	String about_user="";
	PopupWindow popupWindow;
	ImageView collection;
	private void showSelectDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.d01_question_item, null);
		final View layout_collection = view.findViewById(R.id.layout_collection);
		collection = (ImageView) view.findViewById(R.id.collection);
		if(iscollection==0){
			collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
		}else{
			collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
		}
		layout_collection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (iscollection==0) {
					collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
					agrotechniqueArticleDetailModel.collection(id);
				} else {
					agrotechniqueArticleDetailModel.cancelcollection(id);
					collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
				}
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
		final View report = view.findViewById(R.id.layout_report);
		report.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//	requestData(true);
				Intent intent = new Intent(C02_AgrotechniqueArticleDetailActivity.this,ReportActivity.class);
				intent.putExtra("id",id);
				intent.putExtra("from","1");
				intent.putExtra("iscomment","0");
//				if(SESSION.getInstance().usertype ==1){
//					about_user =SESSION.getInstance().uid;
//				}else{
					about_user =SESSION.getInstance().sid;
//				}
				intent.putExtra("about_user",about_user);
				startActivity(intent);
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
		final View share = view.findViewById(R.id.layout_share);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//	requestData(true);
				if (popupWindow != null)
					popupWindow.dismiss();


			}
		});

		popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				//这里如果返回true的话，touch事件将被拦截
				//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow = null;
			}
		});

		popupWindow.showAsDropDown(v,50,10);

	}


	Dialog mMenuDialog;
	private void showSelectDialog() {
		// TODO Auto-generated method stub

		if (mMenuDialog == null)
		{
			LayoutInflater inflater = LayoutInflater.from(this);

			View view = inflater.inflate(R.layout.a07_select_menus, null);
			mMenuDialog = new Dialog(this, R.style.transparentFrameWindowStyle);
			mMenuDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			Window window = mMenuDialog.getWindow();
			window.setWindowAnimations(R.style.main_menu_animstyle);
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = getWindowManager().getDefaultDisplay().getHeight();
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
		web.setThumb(new UMImage(this, R.drawable.app_logo));
		ShareAction action = new ShareAction(this).withMedia(web)
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

	private UMShareListener umShareListener = new UMShareListener() {

		@Override
		public void onStart(SHARE_MEDIA platform) {

		}

		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.e("bird","platform"+ platform);
			if(platform.name().equals("WEIXIN_FAVORITE")){
				errorMsg("收藏成功啦");
			}else{
				errorMsg("分享成功");
			}
		}

		@Override
		public void onError(SHARE_MEDIA share_media, Throwable throwable) {
			Log.e("bird","onError"+share_media);
			errorMsg("分享失败了");
		}

		@Override
		public void onCancel(SHARE_MEDIA share_media) {
			Log.e("bird","onCancel"+share_media);
			errorMsg("分享取消了");
		}
	};

	void updateData(boolean isShow)
	{
	//	updateVideo();
		agrotechniqueArticleDetailModel.getArticleDetail();

	}

	void  updatecommentData(boolean isShow){
		agrotechniqueArticleDetailModel.articlecomment(AppConst.info_from,id,isShow);
	}


	ImageLoader imageLoader = ImageLoader.getInstance();
	int page = 1;
	int ismore = 0;
	void  updatesimilararticle(boolean isShow){

		request.info_from = AppConst.info_from;
		request.articletype = route.articleType;
		request.city = AppUtils.getCurrCity(this);
		String textword = "";
		if(keyWord!=null){
			for(int i = 0;i<keyWord.size();i++){
				textword += keyWord.get(i).wordCode+",";
			}
			request.word =textword;
		}else{
			request.word ="";
		}
		request.cycle = cycle;
		request.id = id;
		request.page = page;
		agrotechniqueArticleDetailModel.API_getFarmSimilararticle(request,isShow);
	}

	private void updateSimilarVideo() {
		if (agrotechniqueArticleDetailModel.similararticle.size() > 0) {
			layout_similar_article.setVisibility(View.VISIBLE);
			similar_article.removeAllViews();
			for (int i=0;i<agrotechniqueArticleDetailModel.similararticle.size();i++){
				 View view = LayoutInflater.from(this).inflate(R.layout.c02_agrotechnique_similar_video,null);
				ImageView image = (ImageView) view.findViewById(R.id.image);
				TextView title = (TextView) view.findViewById(R.id.title);
				final TextView textid = (TextView) view.findViewById(R.id.id);
				imageLoader.displayImage(agrotechniqueArticleDetailModel.similararticle.get(i).titlePicurl,image, FarmingApp.options);
				title.setText(agrotechniqueArticleDetailModel.similararticle.get(i).title);
				textid.setText(agrotechniqueArticleDetailModel.similararticle.get(i).id);
				similar_article.addView(view);
				//view.setTag(agrotechniqueArticleDetailModel.similararticle.get(i).id);
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent  intent = new Intent(C02_AgrotechniqueArticleDetailActivity.this,C02_AgrotechniqueArticleDetailActivity.class);
						intent.putExtra("id",textid.getText());
						intent.putExtra("cycle",cycle);
						startActivity(intent);
					}
				});
			}
			if (0 == agrotechniqueArticleDetailModel.paginated.more) {
				ismore = 0;
			} else {
				ismore = 1;
			}
		}else
			layout_similar_article.setVisibility(View.GONE);
	}

	public static void deleteComment(String commentid){
		agrotechniqueArticleDetailModel.deletedcomment(commentid,id);
	}

	public void answerComment(String commentid,String byreply_userid,String content){
		dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"1",byreply_userid,content,commentid,"0");
	}

    public static void like(String uid,String byreplyId){
        artRequest.about_userid = byreplyId;
        artRequest.comment_id = uid;
        artRequest.publish_userid = publishuser;
        artRequest.user_id = SESSION.getInstance().uid;
        artRequest.about_id = id;
        artRequest.like_type = 1;
        artRequest.from_openid = "";

        agrotechniqueArticleDetailModel.like(artRequest);
    }
	public static void cancellike(String uid){
		//	quesRequest.about_userid = byreplyId;
		artRequest.comment_id = uid;
		//	quesRequest.publish_userid = publishuser;
		artRequest.user_id = SESSION.getInstance().uid;
		artRequest.about_id = uid;
		artRequest.like_type = 1;
		artRequest.from_openid = "";
		agrotechniqueArticleDetailModel.cancellike(artRequest);
	}



	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult( requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.ARTICLEDETAIL)) {
			if (agrotechniqueArticleDetailModel.lastStatus.error_code == 200) {
				updatedatile();

			} else {
				errorMsg(agrotechniqueArticleDetailModel.lastStatus.error_desc);
			}
		}
		if (url.contains(ApiInterface.ARTICLECOMMENT)) {
			//commentData();
			/*if(articleAdapter!=null){
				articleAdapter.notifyDataSetChanged();
			}*/
			momentAdapter = null;
			comment();

		}if(url.contains(ApiInterface.DELETEDCOMMENT)){
			momentAdapter=null;
			updatecommentData(false);
		}
		if(url.contains(ApiInterface.USERLIKE)){
			errorMsg("点赞成功");
			updateData(false);
			updatecommentData(false);
			momentAdapter=null;
			likeimg.setBackground(getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
			textlike = 1;
		}
		if(url.contains(ApiInterface.CANCELUSERLIKE)){
			errorMsg("取消点赞成功");
			updateData(false);
			updatecommentData(false);
			momentAdapter=null;

			likeimg.setBackground(getResources().getDrawable(R.drawable.adrtail_icon_default_fabulous));
			textlike =0;
		}
		if(url.contains(ApiInterface.COLLECTION)){
			errorMsg("收藏成功");
			iscollection = 1;
		}
		if(url.contains(ApiInterface.CANCELCOLLECTION)){
			errorMsg("取消收藏成功");
			iscollection = 0;
		}
		if(url.contains(ApiInterface.COMMENT)){
			if (dictionaryModel.lastStatus.error_code == 200) {
				momentAdapter=null;
				updatecommentData(false);
				edit_comment.setText("");
			} else
				errorMsg(dictionaryModel.lastStatus.error_desc);
		}
		if(url.contains(ApiInterface.SIMILARARTICLE)){
			if (agrotechniqueArticleDetailModel.lastStatus.error_code == 200) {
				updateSimilarVideo();
			} else
				errorMsg(agrotechniqueArticleDetailModel.lastStatus.error_desc);
		}

	}

	FARMARTICLE route;
	ArrayList<KEYWORD> keyWord;
	void updatedatile() {

		if (id.equals(agrotechniqueArticleDetailModel.id)) {
			 route = agrotechniqueArticleDetailModel.data;
			if (route.id != null) {
				null_pager.setVisibility(View.GONE);
				title.setText(route.title);
				 keyWord = agrotechniqueArticleDetailModel.keyWord;
				if(  agrotechniqueArticleDetailModel.keyWord.size()>0){
					for(int i = 0;i<keyWord.size();i++){
						final TextView textView = new TextView(this);
						textView.setText("#"+keyWord.get(i).wordName+"#   ");
						textView.setTag(keyWord.get(i).wordName);
						text_keyword.addView(textView);
						textView.setTextColor(getResources().getColor(R.color.green));
						textView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(C02_AgrotechniqueArticleDetailActivity.this,B01_SearchActivity.class);
								intent.putExtra("keyword",v.getTag().toString());
								startActivity(intent);
							}
						});
					}
				}
				time.setText(route.publishTime);
				publish_user.setText(route.author);
				content.setText(Html.fromHtml(route.content));
				publishuser = route.author;
				textlike = route.islike;
				iscollection = route.iscollection;
				if (route.islike == 1) {
					likeimg.setBackground(getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
				} else {
					likeimg.setBackground(getResources().getDrawable(R.drawable.adrtail_icon_default_fabulous));
				}
			/*	if (iscollection == 1) {
					collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
				} else {
					collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
				}*/
				likenum.setText(route.likeNum + "人点赞");
				updatesimilararticle(true);


			}else {
				null_pager.setVisibility(View.VISIBLE);

			}

		}

	}

	 void comment(){
		 int size = agrotechniqueArticleDetailModel.comment_list.size();
		 if (size > 0) {
			 ArrayList<a_COMMENT_LIST> comment_list = agrotechniqueArticleDetailModel.comment_list;
			 layout_comment.setVisibility(View.VISIBLE);
			 text_comment_num.setText("（"+ agrotechniqueArticleDetailModel.comment_list.size() +"条）");

//			 if(articleAdapter == null){
//				 articleAdapter = new C02_ArticleCommentAdapter(this,agrotechniqueArticleDetailModel.comment.comment,1);
//				 listView.setAdapter(articleAdapter);
//			 }else{
//				 articleAdapter.notifyDataSetChanged();
//			 }

			 // 数据
			 ArrayList<AgrotechniqueMoment> moments = new ArrayList<AgrotechniqueMoment>();
			 for (int i = 0; i < size; i++) {
				 ArrayList<Comment> comments = new ArrayList<Comment>();
				 for (COMMENT reply : comment_list.get(i).reply_list) {

//                    if (reply.commentId.equals(SESSION.getInstance().uid))
//                        comments.add(new Comment(new User(reply.commentId, reply.commentName), reply.content, null));
//                    else
					 comments.add(new Comment(new User(reply.commentId, reply.commentName), reply.content, new User(reply.byreplyId, reply.byreplyName)));

				 }
				 moments.add(new AgrotechniqueMoment(comment_list.get(i).comment.id,comment_list.get(i), comments));
			 }

			 if (momentAdapter == null) {
				 momentAdapter = new MomentAdapter(this, moments, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
					 @Override
					 public void onCommentatorClick(View view, User commentator) {
						 Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
					 }

					 @Override
					 public void onReceiverClick(View view, User receiver) {
						 Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
					 }

					 @Override
					 public void onContentClick(View view, User commentator, User receiver) {
						 if (commentator != null && commentator.mId.equals(SESSION.getInstance().uid)) { // 不能回复自己的评论
							 return;
						 }
						 inputComment(view, commentator);
					 }
				 }));

				 listView.setAdapter(momentAdapter);
			 } else {
				 momentAdapter.notifyDataSetChanged();
			 }

		 }else{
			 momentAdapter = null;
			 listView.setAdapter(null);
			 layout_comment.setVisibility(View.GONE);
		 }
	 }


	class MomentAdapter extends BaseAdapter {

		public static final int VIEW_HEADER = 0;
		public static final int VIEW_MOMENT = 1;

		private ArrayList<AgrotechniqueMoment> mList;
		private Context mContext;
		private CustomTagHandler mTagHandler;

		ImageLoader imageLoader = ImageLoader.getInstance();

		public MomentAdapter(Context context, ArrayList<AgrotechniqueMoment> list,CustomTagHandler tagHandler) {
			mList = list;
			mContext = context;
			mTagHandler = tagHandler;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
//            if (position == 0) {
//                convertView = View.inflate(mContext, R.layout.item_header, null);
//            } else {
				convertView = View.inflate(mContext, R.layout.c02_comment_item, null);
				ViewHolder holder = new ViewHolder();

				holder.mCommentList = (LinearLayout) convertView.findViewById(R.id.comment_list);
				holder.mBtnInput = (TextView) convertView.findViewById(R.id.btn_input_comment);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.mContent = (TextView) convertView.findViewById(R.id.content);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.num = (TextView) convertView.findViewById(R.id.num);
				holder.like_num = (TextView) convertView.findViewById(R.id.like_num);
				holder.point = (ImageView) convertView.findViewById(R.id.point);
				holder.id = (TextView) convertView.findViewById(R.id.id);
				holder.like =(ImageView) convertView.findViewById(R.id.like);
				holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);

				convertView.setTag(holder);
//            }
			}
			//防止ListView的OnItemClick与item里面子view的点击发生冲突
			((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

			final int index = position;
			final ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.name.setText(mList.get(index).a_comment_list.comment.commentName);
			imageLoader.displayImage(mList.get(index).a_comment_list.comment.commentHeader,holder.img_head, FarmingApp.options_head);

            Spannable span = EaseSmileUtils.getSmiledText(mContext, mList.get(index).a_comment_list.comment.content);
            holder.mContent.setText(span, TextView.BufferType.SPANNABLE);

			holder.time.setText(mList.get(index).a_comment_list.comment.time);
			if(mList.get(index).a_comment_list.comment.islike==1){
				holder.like.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
			}else{
				holder.like.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icon_default_fabulous));
			}
			holder.like_num.setText(mList.get(index).a_comment_list.comment.likeNum);
			holder.num.setText(mList.get(index).a_comment_list.comment.reply_num);



			holder.like.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 点赞
					if (mList.get(index).a_comment_list.comment.islike == 1) {
						cancellike(mList.get(index).a_comment_list.comment.id);
					} else {
						like(mList.get(index).a_comment_list.comment.id, mList.get(index).a_comment_list.comment.byreplyId);
					}

				}
			});


			holder.point.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showSelectDialog(v, mList.get(index).id, mList.get(index).a_comment_list.comment, holder.mBtnInput);
				}
			});


			CommentFun.parseAgCommentList(mContext, mList.get(index), holder.mCommentList, holder.mBtnInput, mTagHandler);

			return convertView;
		}

		class ViewHolder {
			LinearLayout mCommentList;
			TextView mContent,name,time,mBtnInput,like_num,num,id;
			ImageView point,like,img_head;
		}

		PopupWindow popupWindow;
		private void showSelectDialog(View v, final String commentid, final COMMENT comment, final View input) {
			//自定义布局，显示内容
			View view = LayoutInflater.from(mContext).inflate(R.layout.c02_agrotechnique_comment_more, null);
			View layout_dialog = view.findViewById(R.id.layout_dialog);
			View zhuiwen = view.findViewById(R.id.layout_zhuiwen);
			View line = view.findViewById(R.id.line);
			View jubao = view.findViewById(R.id.layout_jubao);
			View line2 = view.findViewById(R.id.line2);
			View delete = view.findViewById(R.id.layout_delete);

			if (!comment.commentId.equals(SESSION.getInstance().uid)){
				zhuiwen.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
				zhuiwen.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						showInputView(input);
						if (popupWindow != null)
							popupWindow.dismiss();
					}
				});
			}else {
				zhuiwen.setVisibility(View.GONE);
				line.setVisibility(View.GONE);
			}

			if (comment.commentId.equals(SESSION.getInstance().uid)){
				jubao.setVisibility(View.GONE);
				zhuiwen.setVisibility(View.GONE);
				line.setVisibility(View.GONE);
				line2.setVisibility(View.GONE);
				delete.setVisibility(View.VISIBLE);
			}else {
				jubao.setVisibility(View.VISIBLE);
				line2.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
			}

			jubao.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,ReportActivity.class);
					intent.putExtra("id",commentid);
					intent.putExtra("from","1");
					intent.putExtra("iscomment","1");
					intent.putExtra("about_user",comment.commentId);
					mContext.startActivity(intent);
					if (popupWindow != null)
						popupWindow.dismiss();
				}
			});

			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteComment(commentid);
					if (popupWindow != null)
						popupWindow.dismiss();
				}
			});

			popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
			//测量view 注意这里，如果没有测量  ，下面的popupHeight高度为-2  ,因为LinearLayout.LayoutParams.WRAP_CONTENT这句自适应造成的
			view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
			int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
			int[] location = new int[2];

			popupWindow.setTouchable(true);
			popupWindow.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					//这里如果返回true的话，touch事件将被拦截
					//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});
			//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
			popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
			// 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
			v.getLocationOnScreen(location);

			//这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以

			if (AppUtils.getScHeight(mContext) - location[1] > popupHeight) {
				layout_dialog.setBackgroundResource(R.drawable.down_bg);
				popupWindow.showAsDropDown(v);
			} else {
				layout_dialog.setBackgroundResource(R.drawable.up_bg);
				popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
			}
		}

		private void showInputView(View view) {
			inputComment(view);
		}


	}


	public void inputComment(final View v) {
		inputComment(v, null);
	}

	public void inputComment(final View v, User receiver) {
		CommentFun.inputAgComment(C02_AgrotechniqueArticleDetailActivity.this, listView, v, receiver, new CommentFun.InputAgCommentListener() {
			@Override
			public void onCommitAgComment(String content, AgrotechniqueMoment moment, Comment comment, String byreply_userid) {
				answerComment(moment.id,byreply_userid,content);
			}

		});
	}


/*

	 void commentData() {
		if (agrotechniqueArticleDetailModel.comment.comment.size() > 0) {
			if (articleAdapter == null) {
				articleAdapter = new C02_ArticleCommentAdapter(this, agrotechniqueArticleDetailModel.comment.comment, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener(){
					@Override
					public void onCommentatorClick(View view, User commentator) {
						Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onReceiverClick(View view, User receiver) {
						Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onContentClick(View view, User commentator, User receiver) {
						if (commentator != null && commentator.mId.equals(SESSION.getInstance().sid)) { // 不能回复自己的评论
							return;
						}
						inputComment(view, commentator);
					}
				}));
				listView.setAdapter(articleAdapter);
			} else {
				// farmArticleModel.getFarmArticle(info_from,crop_type,code_type,true);
				articleAdapter.notifyDataSetChanged();
			}
		} else {
			articleAdapter = null;
			//gridView.setAdapter(null);
		}
	}
*/

/*
	public void inputComment(final View v, User receiver) {
		CommentFun.inputComment(C02_AgrotechniqueArticleDetailActivity.this, listView, v, receiver, new CommentFun.InputCommentListener() {
			@Override
			public void onCommitComment(String content, Moment moment, Comment comment) {
//				netfriendAdapter.notifyDataSetChanged();

				mMoment = moment;
				mComment = comment;
				askAnswer(content,moment,comment);
			}
		});
	}*/


/*	public void askAnswer(String content,Moment moment,Comment comment) {
		User receiver = comment.mReceiver;
		if (receiver.mId.equals(SESSION.getInstance().sid))
			agrotechniqueArticleDetailModel.articlecomment("绿云", id, true);
		else
			agrotechniqueArticleDetailModel.articlecomment("绿云", id, true);

	}*/

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UMShareAPI.get(this).release();
		agrotechniqueArticleDetailModel.removeResponseListener(this);
		dictionaryModel.removeResponseListener(this);
	}
}