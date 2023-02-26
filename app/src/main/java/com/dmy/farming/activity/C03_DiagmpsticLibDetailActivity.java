package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_AgrotechniqueVideoAdapter;
import com.dmy.farming.adapter.C02_ArticleCommentAdapter;
import com.dmy.farming.adapter.C03_DiagnosticCommentAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.data.DIAGNOSTICDETAIL;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.model.DiagnosticDetailModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.similarRequest;
import com.dmy.farming.view.comment.AgrotechniqueMoment;
import com.dmy.farming.view.comment.Comment;
import com.dmy.farming.view.comment.CommentFun;
import com.dmy.farming.view.comment.CustomTagHandler;
import com.dmy.farming.view.comment.User;
import com.dmy.farming.view.emojicon.EaseDefaultEmojiconDatas;
import com.dmy.farming.view.emojicon.EaseEmojicon;
import com.dmy.farming.view.emojicon.EaseSmileUtils;
import com.dmy.farming.view.emojicon.EmojiconGridAdapter;
import com.external.androidquery.callback.AjaxStatus;
import com.external.easing.Linear;
import com.external.maxwin.view.XListView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dmy.farming.R.id.size;

public class C03_DiagmpsticLibDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	static DiagnosticDetailModel diagnosticDetailModel;
	static String id,title,cycle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c03_activity_diagnosticlib_detail);

		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");
		cycle = getIntent().getStringExtra("cycle");

		diagnosticDetailModel = new DiagnosticDetailModel(this);
		diagnosticDetailModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		request = new similarRequest();

		artRequest =  new articleRequest();
		initView();
		initData();


	}

	GridView grid_video;
	C01_AgrotechniqueVideoAdapter videoAdapter;
//	C03_DiagnosticCommentAdapter commentAdapter;
	View expandView,comment,layout_input;
    LinearLayout layout;
	int maxDescripLine;
	static articleRequest artRequest;
	private static final int VIDEO_CONTENT_DESC_MAX_LINE = 3;// 默认展示最大行数3行
	private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充
	private static final int SHRINK_UP_STATE = 1;// 收起状态
	private static final int SPREAD_STATE = 2;// 展开状态
	private static int mState = SHRINK_UP_STATE;//默认收起状态

	private TextView mContentText,likenum,number;// 展示文本内容
	private LinearLayout mShowMore;// 展示更多
	private ImageView mImageSpread,like;// 展开
	private ImageView mImageShrinkUp;// 收起

	TextView text_title,keyword,vulgo,comment_num;

	// 语音合成对象
	private SpeechSynthesizer mTts;
	// 默认发音人
	private String voicer = "xiaoyan";
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;
	boolean isCompleted = true;
	boolean isPlaying = false;
	int code = -1;

	int count = 0;
	View left,right,layout_similar_video;
	String image[] ;
	String textvideo = "";
	XListView listView;
	static  String  publishuser ;
	ImageLoader imageLoader = ImageLoader.getInstance();
	ImageView imageView,more,img_emoji;
    LinearLayout similar_video,text_keyword;
	EditText edit_comment;
	DictionaryModel dictionaryModel;
	int iscollection;
	MomentAdapter momentAdapter;
	TextView textmore;
	similarRequest request;
	GridView gv_emoji;
	EmojiconGridAdapter smileAdapter;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		View headerView = LayoutInflater.from(this).inflate(R.layout.c03_activity_diagnosticlib_detail_header, null);

		comment =headerView.findViewById(R.id.comment);

		text_title = (TextView) findViewById(R.id.text_title);
		text_title.setText(title);

		//zhankai =(TextView) headerView.findViewById(R.id.zhankai);

		vulgo = (TextView) headerView.findViewById(R.id.vulgo);
		keyword = (TextView) headerView.findViewById(R.id.keyword);
		comment_num = (TextView) headerView.findViewById(R.id.comment_num);
		like = (ImageView)headerView.findViewById(R.id.like);
		headerView.findViewById(R.id.layout_like).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(textlike==0){
					artRequest.about_userid = "";
					artRequest.comment_id = "";
					artRequest.publish_userid = publishuser;
					artRequest.user_id = SESSION.getInstance().uid;
					artRequest.about_id = id;
					artRequest.like_type = 5;
					artRequest.from_openid = "";
					diagnosticDetailModel.like(artRequest);
				}else{
					artRequest.comment_id = "";
					//	quesRequest.publish_userid = publishuser;
					artRequest.user_id = SESSION.getInstance().uid;
					artRequest.about_id = id;
					artRequest.like_type = 5;
					artRequest.from_openid = "";
					diagnosticDetailModel.cancellike(artRequest);
				}
			}
		});
		likenum = (TextView) headerView.findViewById(R.id.likenum);

		//mContentText = (TextView) headerView.findViewById(R.id.text_content);
	//	mShowMore = (LinearLayout) headerView.findViewById(R.id.show_more);
	//	mImageSpread = (ImageView) headerView.findViewById(R.id.spread);
       layout =(LinearLayout) headerView.findViewById(R.id.layout);

		text_keyword= (LinearLayout)headerView.findViewById(R.id.text_keyword);
	/*	mImageShrinkUp = (ImageView) findViewById(R.id.shrink_up);*/
	//	mShowMore.setOnClickListener(this);

		//headerView.findViewById(R.id.img_play_voice).setOnClickListener(this);
		mTts = SpeechSynthesizer.createSynthesizer(C03_DiagmpsticLibDetailActivity.this, mTtsInitListener);
		number = (TextView)headerView.findViewById(R.id.number);
        similar_video = (LinearLayout) headerView.findViewById(R.id.similar_video);
        layout_similar_video = headerView.findViewById(R.id.layout_similar_video);

		imageView = (ImageView) headerView.findViewById(R.id.imageview);
//		image = new String[9];
		//imageLoader.displayImage(image[0], imageView, FarmingApp.options);
		left = headerView.findViewById(R.id.left);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				count--;
				if(count<0){
					count = image.length-1;
				}
				number.setText("第"+(count+1)+"张/共"+(image.length)+"张");
				imageLoader.displayImage(image[count], imageView, FarmingApp.options);
			}
		});
		right = headerView.findViewById(R.id.right);
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				count++;
				if(count == image.length){
					count = 0;
				}
				number.setText("第"+(count+1)+"张/共"+(image.length)+"张");
				imageLoader.displayImage(image[count], imageView, FarmingApp.options);
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
					edit_comment.append(EaseSmileUtils.getSmiledText(C03_DiagmpsticLibDetailActivity.this, emojicon.getEmojiText()));
				}
			}
		});

		textmore = (TextView)headerView.findViewById(R.id.textmore);
		textmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		listView =(XListView)findViewById(R.id.list_black);
		listView.addHeaderView(headerView);
		//	listView.setXListViewListener(this, 1);
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(false);
		listView.setAdapter(null);
	}

	private void initData() {
	//	mContentText.setText(R.string.txt_info);
		requestData();
	}

	void requestData(){
		diagnosticDetailModel.getDiagnosticDetail(id,cycle);
		requestCommentData(false);
	}

	void  requestCommentData(boolean isShow){
		diagnosticDetailModel.getComment(AppConst.info_from,id,isShow);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_send:
				String content = edit_comment.getText().toString();
				if ("".equals(content)){
					errorMsg("评论内容不能为空！");
				}else {
					dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"0","",content,"","2");
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
			/*case R.id.show_more:
				if (mState == SPREAD_STATE) {
					mContentText.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
					mContentText.requestLayout();
					//mImageShrinkUp.setVisibility(View.GONE);
					mImageSpread.setVisibility(View.VISIBLE);
					mState = SHRINK_UP_STATE;
					zhankai.setText("展开");

				} else if (mState == SHRINK_UP_STATE) {
					mContentText.setMaxLines(Integer.MAX_VALUE);
					mContentText.requestLayout();
					//mImageShrinkUp.setVisibility(View.VISIBLE);
					mImageSpread.setVisibility(View.GONE);
					mState = SPREAD_STATE;
					zhankai.setText("收起");
				}
				break;*/

		/*	case R.id.img_play_voice:
				// 移动数据分析，收集开始合成事件
				FlowerCollector.onEvent(C03_DiagmpsticLibDetailActivity.this, "tts_play");

				text = mContentText.getText().toString();
				// 设置参数
				setParam();

				if (isCompleted == true) {
					code = mTts.startSpeaking(text, mTtsListener);
				}

				if (isCompleted == false){
					if (isPlaying == true)
						mTts.pauseSpeaking();
					else
						mTts.resumeSpeaking();
				}


//			*//**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*//*
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

				if (code != ErrorCode.SUCCESS) {
					errorMsg("语音合成失败,错误码: " + code);
				}else {

				}

				break;*/


		}
	}

	public void closeKeyBoard(EditText edit_keyword) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
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
					diagnosticDetailModel.collection(id);
				} else {
					diagnosticDetailModel.cancelcollection(id);
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
				Intent intent = new Intent(C03_DiagmpsticLibDetailActivity.this,ReportActivity.class);
				intent.putExtra("id",id);
				intent.putExtra("from","0");
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

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				errorMsg("初始化失败,错误码："+code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {

		@Override
		public void onSpeakBegin() {
			errorMsg("开始播放");
			isPlaying = true;
			isCompleted = false;
		}

		@Override
		public void onSpeakPaused() {
			errorMsg("暂停播放");
			isPlaying = false;

		}

		@Override
		public void onSpeakResumed() {
			errorMsg("继续播放");
			isPlaying = true;

		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
									 String info) {
			// 合成进度
			mPercentForBuffering = percent;
			errorMsg(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
			errorMsg(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				errorMsg("播放完成");
			} else if (error != null) {
//				if (error.getErrorCode() == 20001){
//					// 没有检查到网络
//					mEngineType = SpeechConstant.TYPE_LOCAL;
//					setParam();
//					code = mTts.startSpeaking(text, mTtsListener);
//
//				}else {
//					errorMsg(error.getPlainDescription(true));
//				}
				errorMsg(error.getErrorDescription());
			}
			isCompleted = true;
			isPlaying = false;
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
	};

	/**
	 * 参数设置
	 * @return
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			//设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, "50");
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, "50");
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, "50");
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
			/**
			 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
			 * 开发者如需自定义参数，请参考在线合成参数设置
			 */
		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}

	@Override
	protected void onResume() {
		super.onResume();
//		updateData();
	}
	int textlike = 0;
	String  KeyWord[] = null;
	String  KeyWordcode[] = null;
	DIAGNOSTICDETAIL diagnosticdetail;
	void updateData()
	{
		if (diagnosticDetailModel.diagnosticdetail != null) {
			diagnosticdetail =diagnosticDetailModel.diagnosticdetail;
			vulgo.setText(diagnosticDetailModel.diagnosticdetail.vulgo);
			KeyWord = diagnosticDetailModel.diagnosticdetail.keyWord.split(",");
			KeyWordcode = diagnosticDetailModel.diagnosticdetail.keywordcode.split(",");
			if( KeyWord.length>0){
				for(int i = 0;i< KeyWord.length;i++){
					final TextView textView = new TextView(this);
					textView.setText("#"+KeyWord[i]+"# ");
					textView.setTag(KeyWord[i]);
					text_keyword.addView(textView);
					textView.setTextColor(getResources().getColor(R.color.green));
					textView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(C03_DiagmpsticLibDetailActivity.this,B01_SearchActivity.class);
							intent.putExtra("keyword",v.getTag().toString());
							startActivity(intent);
						}
					});
				}
			}
		//	keyword.setText(diagnosticDetailModel.diagnosticdetail.keyWord);

			comment_num.setText(diagnosticDetailModel.diagnosticdetail.commentNum +" 人评论");
			likenum.setText(diagnosticDetailModel.diagnosticdetail.likeNum+"人点赞");
			publishuser = diagnosticDetailModel.diagnosticdetail.createUser;
			textlike = diagnosticDetailModel.diagnosticdetail.islike;
			//mContentText.setText(diagnosticDetailModel.diagnosticdetail.item_list.get(0).content);
            for(int i = 0 ;i<diagnosticDetailModel.diagnosticdetail.item_list.size();i++){
                final  View item =  LayoutInflater.from(this).inflate(R.layout.c03_activity_diagnosticlib_detail_item,null);
				final   TextView text = (TextView)item.findViewById(R.id.text_content);
				String html =diagnosticDetailModel.diagnosticdetail.item_list.get(i).content  ;
				text.setText(Html.fromHtml(html));
			//	text.setText(diagnosticDetailModel.diagnosticdetail.item_list.get(i).content);
				final ImageView mImageSpread = (ImageView) item.findViewById(R.id.spread);
				final	TextView zhankai =(TextView) item.findViewById(R.id.zhankai);
				mShowMore = (LinearLayout)item.findViewById(R.id.show_more);
				mShowMore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mState == SPREAD_STATE) {
							text.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
							text.requestLayout();
							//mImageShrinkUp.setVisibility(View.GONE);
							mImageSpread.setVisibility(View.VISIBLE);
							mState = SHRINK_UP_STATE;
							zhankai.setText("展开");

						} else if (mState == SHRINK_UP_STATE) {
							text.setMaxLines(Integer.MAX_VALUE);
							text.requestLayout();
							//mImageShrinkUp.setVisibility(View.VISIBLE);
							mImageSpread.setVisibility(View.GONE);
							mState = SPREAD_STATE;
							zhankai.setText("收起");
						}
					}
				});
                TextView text1 = (TextView)item.findViewById(R.id.item_title);
                text1.setText(diagnosticDetailModel.diagnosticdetail.item_list.get(i).title);
				ImageView img_play_voice = (ImageView) item.findViewById(R.id.img_play_voice);
				img_play_voice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FlowerCollector.onEvent(C03_DiagmpsticLibDetailActivity.this, "tts_play");

						textvideo = text.getText().toString();
						// 设置参数
						setParam();

						if (isCompleted == true) {
							code = mTts.startSpeaking(textvideo, mTtsListener);
						}

						if (isCompleted == false){
							if (isPlaying == true) {
								mTts.pauseSpeaking();
								isCompleted=true;
						}
							else {
								mTts.resumeSpeaking();
								isCompleted = true;
							}
						}


//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

						if (code != ErrorCode.SUCCESS) {
							errorMsg("语音合成失败,错误码: " + code);
						}else {

						}

					}
				});
                layout.addView(item);
            }
			if(diagnosticDetailModel.diagnosticdetail.islike==1){
				like.setBackground(getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
			}else{
				like.setBackground(getResources().getDrawable(R.drawable.detail_icon_fabulous));
			}
			iscollection =diagnosticDetailModel.diagnosticdetail.iscollection;
			/*if (iscollection==1){
				collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
			}
			else{
				collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
			}*/
				image = diagnosticDetailModel.diagnosticdetail.img_url.split(",");
				imageLoader.displayImage(image[0], imageView, FarmingApp.options);
				number.setText("第1张/共"+(image.length)+"张");

			updatesimilararticle(true);
		}

	}

	int page = 1;
	int ismore = 0;
	void  updatesimilararticle(boolean isShow){

		request.info_from = AppConst.info_from;
		request.articletype = diagnosticdetail.typeCode;
		request.city = AppUtils.getCurrCity(this);
		String textword = "";
		if(KeyWordcode!=null){
			for(int i = 0;i<KeyWordcode.length;i++){
				textword += KeyWordcode[i]+",";
			}
			request.word =textword;
		}else{
			request.word ="";
		}
		request.cycle = cycle;
		request.id = id;
		request.page = page;
		diagnosticDetailModel.similarvideo(request,true);
	}
	String img[];
    private void updateSimilarVideo() {
        if (diagnosticDetailModel.similarvideo.size() > 0) {
            layout_similar_video.setVisibility(View.VISIBLE);
			similar_video.setVisibility(View.VISIBLE);
            similar_video.removeAllViews();
            for (int i=0;i<diagnosticDetailModel.similarvideo.size();i++){
                View view = LayoutInflater.from(this).inflate(R.layout.c02_agrotechnique_similar_video,null);
                ImageView textimage = (ImageView) view.findViewById(R.id.image);
				final TextView texttitle = (TextView) view.findViewById(R.id.title);
				final  TextView textid = (TextView) view.findViewById(R.id.id);
                img = diagnosticDetailModel.similarvideo.get(i).img_url.split(",");
                imageLoader.displayImage(img[0],textimage, FarmingApp.options);
				texttitle.setText(diagnosticDetailModel.similarvideo.get(i).name);
				textid.setText(diagnosticDetailModel.similarvideo.get(i).id);
                similar_video.addView(view);
				textimage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent  intent = new Intent(C03_DiagmpsticLibDetailActivity.this,C03_DiagmpsticLibDetailActivity.class);
						intent.putExtra("id",textid.getText());
						intent.putExtra("cycle",cycle);
						startActivity(intent);
						/*diagnosticDetailModel.getDiagnosticDetail(textid.getText().toString(),cycle);
						title = texttitle.getText().toString();
						image= img;
						layout.removeAllViews();*/
					}
				});
            }
			if (0 == diagnosticDetailModel.paginated.more) {
				ismore = 0;
			} else {
				ismore = 1;
			}
        }else{
			layout_similar_video.setVisibility(View.GONE);
			similar_video.setVisibility(View.GONE);
		}

    }

	void updateComment(){
		if (diagnosticDetailModel.comment_list.size() > 0) {
			comment.setVisibility(View.VISIBLE);

//			if(commentAdapter == null){
//				commentAdapter = new C03_DiagnosticCommentAdapter(this,diagnosticDetailModel.comment.comment);
//				listView.setAdapter(commentAdapter);
//			}else{
//				commentAdapter.notifyDataSetChanged();
//			}

			// 数据
			ArrayList<AgrotechniqueMoment> moments = new ArrayList<AgrotechniqueMoment>();
			ArrayList<a_COMMENT_LIST> comment_list = diagnosticDetailModel.comment_list;
			for (int i = 0; i < diagnosticDetailModel.comment_list.size(); i++) {
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
//						Toast.makeText(getApplicationContext(), commentator.mName, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onReceiverClick(View view, User receiver) {
//						Toast.makeText(getApplicationContext(), receiver.mName, Toast.LENGTH_SHORT).show();
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
			comment.setVisibility(View.GONE);
			momentAdapter = null;
			listView.setAdapter(null);
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
					intent.putExtra("from","2");
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
		CommentFun.inputAgComment(C03_DiagmpsticLibDetailActivity.this, listView, v, receiver, new CommentFun.InputAgCommentListener() {
			@Override
			public void onCommitAgComment(String content, AgrotechniqueMoment moment, Comment comment,String byreply_userid) {
				answerComment(moment.id,byreply_userid,content);
			}

		});
	}


	public void answerComment(String commentid,String byreply_userid,String content){
		dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"1",byreply_userid,content,commentid,"2");
	}


	public static void like(String uid,String byreplyId){
		artRequest.about_userid = byreplyId;
		artRequest.comment_id = uid;
		artRequest.publish_userid = publishuser;
		artRequest.user_id = SESSION.getInstance().uid;
		artRequest.about_id = id;
		artRequest.like_type = 5;
		artRequest.from_openid = "";
		diagnosticDetailModel.like(artRequest);
	}

	public static void cancellike(String uid){
		//	quesRequest.about_userid = byreplyId;
		artRequest.comment_id = uid;
		//	quesRequest.publish_userid = publishuser;
		artRequest.user_id = SESSION.getInstance().uid;
		artRequest.about_id = uid;
		artRequest.like_type = 5;
		artRequest.from_openid = "";
		diagnosticDetailModel.cancellike(artRequest);
	}


	public static void deleteComment(String commentid){
		diagnosticDetailModel.deleteComment(commentid,id);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.DIAGNOSTICDETAIL)) {
			if (diagnosticDetailModel.lastStatus.error_code == 200) {
				updateData();

			} else {
				errorMsg(diagnosticDetailModel.lastStatus.error_desc);
			}
		}else if (url.contains(ApiInterface.ARTICLECOMMENT)) {
			updateComment();
		}else if(url.contains(ApiInterface.DELETEDCOMMENT)){
			momentAdapter = null;
			requestData();
			requestCommentData(false);
		}
		if(url.contains(ApiInterface.USERLIKE)){
			errorMsg("点赞成功");
			momentAdapter = null;
			requestData();
			requestCommentData(false);
		}
		if(url.contains(ApiInterface.CANCELUSERLIKE)){
			errorMsg("取消点赞成功");
			momentAdapter = null;
			requestData();
			requestCommentData(false);
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
				requestData();
				momentAdapter = null;
				requestCommentData(false);
				edit_comment.setText("");
			} else
				errorMsg(dictionaryModel.lastStatus.error_desc);
		}
		if(url.contains(ApiInterface.SIMILARDIAG)){
			if (diagnosticDetailModel.lastStatus.error_code == 200) {
				updateSimilarVideo();
			} else
				errorMsg(diagnosticDetailModel.lastStatus.error_desc);
		}
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

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if( null != mTts ){
			mTts.stopSpeaking();
			// 退出时释放连接
			mTts.destroy();
		}
		diagnosticDetailModel.removeResponseListener(this);
		dictionaryModel.removeResponseListener(this);
	}
}