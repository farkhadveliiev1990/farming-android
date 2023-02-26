package com.dmy.farming.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextUtils;
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

import com.bumptech.glide.Glide;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.ShareUtils;
import com.dmy.farming.adapter.C01_AgrotechniqueVideoAdapter;
import com.dmy.farming.adapter.C02_ArticleCommentAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.model.AgrotechniqueVideoDetailModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.similarRequest;
import com.dmy.farming.view.bdvideoplayer.BDVideoView;
import com.dmy.farming.view.bdvideoplayer.VideoDetailInfo;
import com.dmy.farming.view.bdvideoplayer.listener.SimpleOnVideoControlListener;
import com.dmy.farming.view.bdvideoplayer.utils.DisplayUtils;
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
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


public class C02_AgrotechniqueVideoDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c02_activity_agrotechnique_video_detail);

        id = getIntent().getStringExtra("id");
        initView();
        agrotechniqueVideoDetailModel = new AgrotechniqueVideoDetailModel(this,id);
        agrotechniqueVideoDetailModel.addResponseListener(this);
        dictionaryModel = new DictionaryModel(this);
        dictionaryModel.addResponseListener(this);
        artRequest =  new articleRequest();
        request = new similarRequest();
        requestData();
    }

    C01_AgrotechniqueVideoAdapter videoAdapter;
    View more, layout_0,comment,layout_input,layout_similar_video;
    int flag = 0;
    int iscollection  = 0;
    LinearLayout text_keyword;
    private Runnable mRunnable;
    private Handler uiHandler;
    Thread thread1;
    static String url,id;
    TextView title,time,keyword,content,collection,num,like_num,likenum,text_comment_num;
    ImageView collectionimg;
    static AgrotechniqueVideoDetailModel agrotechniqueVideoDetailModel;
    XListView listView;
    private BDVideoView videoView;
    static articleRequest artRequest;
    similarRequest request;

    //将长度转换为时间
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    C02_ArticleCommentAdapter commentAdapter;
    MomentAdapter momentAdapter;
    VIDEOLIST route;
    String cycle = "",cyclename="";
    ImageView likeimg,img_emoji;
    static  String  publishuser ;
    LinearLayout similar_video,layoutcollection;
    EditText edit_comment;
    DictionaryModel dictionaryModel;
    ImageLoader imageLoader = ImageLoader.getInstance();
    int textlike = 0;
    TextView textmore,textcycle,collectionnum;
    GridView gv_emoji;
    EmojiconGridAdapter smileAdapter;


    void initView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.c02_activity_agrotechnique_video_detail_header, null);

        more = findViewById(R.id.more);
        more.setOnClickListener(this);
        layout_0 =findViewById(R.id.layout_0);

        findViewById(R.id.layout_share).setOnClickListener(this);
        findViewById(R.id.layout_report).setOnClickListener(this);

        title = (TextView) headerView.findViewById(R.id.title);
        time = (TextView) headerView.findViewById(R.id.time);
   //     keyword = (TextView) headerView.findViewById(R.id.keyword);
        content = (TextView) headerView.findViewById(R.id.content);
        likenum = (TextView) headerView.findViewById(R.id.likenum);
        likeimg = (ImageView)headerView.findViewById(R.id.likeimg);
        likeimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textlike == 0)
                {
                    artRequest.about_userid = "";
                    artRequest.comment_id = "";
                    artRequest.publish_userid = publishuser;
                    artRequest.user_id = SESSION.getInstance().uid;
                    artRequest.about_id = id;
                    artRequest.like_type = 2;
                    artRequest.from_openid = "";
                    agrotechniqueVideoDetailModel.like(artRequest);
                }else{
                        artRequest.comment_id = "";
                        //	quesRequest.publish_userid = publishuser;
                        artRequest.user_id = SESSION.getInstance().uid;
                        artRequest.about_id = id;
                        artRequest.like_type = 2;
                        artRequest.from_openid = "";
                    agrotechniqueVideoDetailModel.cancellike(artRequest);
                    }
            }
        });
        layoutcollection = (LinearLayout)findViewById(R.id.layoutcollection);
        collection = (TextView)findViewById(R.id.collection);
        collectionimg = (ImageView)findViewById(R.id.collectionimg);

        layoutcollection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogined() && id.equals(agrotechniqueVideoDetailModel.videolist.video_id)){
                    if(iscollection==1){
                        collectionimg.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
                        agrotechniqueVideoDetailModel.cancelcollection(id);
                    }else{
                        collectionimg.setBackground(getResources().getDrawable(R.drawable.ccc));
                        agrotechniqueVideoDetailModel.collection(id);
                    }
                }
            }
        });
        num = (TextView) headerView.findViewById(R.id.num);
        collectionnum = (TextView)headerView.findViewById(R.id.collectionnum);
        comment = headerView.findViewById(R.id.comment);
        layout_similar_video = headerView.findViewById(R.id.layout_similar_video);
        similar_video = (LinearLayout) headerView.findViewById(R.id.similar_video);
        text_comment_num = (TextView)comment.findViewById(R.id.text_comment_num);
        text_keyword= (LinearLayout)headerView.findViewById(R.id.text_keyword);

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
                    edit_comment.append(EaseSmileUtils.getSmiledText(C02_AgrotechniqueVideoDetailActivity.this, emojicon.getEmojiText()));
                }
            }
        });

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
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setAdapter(null);

        //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
        //String videoUrl1 = Environment.getExternalStorageDirectory().getPath()+"/fl1234.mp4" ;

        //网络视频
        //	String videoUrl2 = Utils.videoUrl ;

        cycle = getIntent().getStringExtra("cycle");
        url = getIntent().getStringExtra("url");
        cyclename = getIntent().getStringExtra("cyclename");
		Uri uri = Uri.parse(url);
        textcycle = (TextView)headerView.findViewById(R.id.textcycle) ;
        textcycle.setText(cyclename);

	/*	videoView = (VideoView) headerView.findViewById(R.id.videoView );
        videoView1 = (VideoView) headerView.findViewById(R.id.videoView1);

		//设置视频控制器
		videoView.setMediaController(new MediaController(this));

		//播放完成回调
		videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
		//设置视频路径
		videoView.setVideoURI(uri);
		videoView.start();

//        timer.schedule(task, 10000);

        Uri uri1 = Uri.parse( "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" );
        //设置视频控制器
        videoView1.setMediaController(new MediaController(C02_AgrotechniqueVideoDetailActivity.this));
        //播放完成回调
        videoView1.setOnCompletionListener( new MyPlayerOnCompletionListener1());
        //设置视频路径
        videoView1.setVideoURI(uri1);*/

        VideoDetailInfo info = new VideoDetailInfo();
        info.title = "";
        info.videoPath = url;
        videoView = (BDVideoView) findViewById(R.id.vv);
        videoView.setOnVideoControlListener(new SimpleOnVideoControlListener() {

            @Override
            public void onRetry(int errorStatus) {
                // TODO: 2017/6/20 调用业务接口重新获取数据
                // get info and call method "videoView.startPlayVideo(info);"
            }

            @Override
            public void onBack() {
                onBackPressed();
            }

            @Override
            public void onFullScreen() {
                DisplayUtils.toggleScreenOrientation(C02_AgrotechniqueVideoDetailActivity.this);
            }
        });
        videoView.startPlayVideo(info);

}

    int per;

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (msg.what == 1) {
//               // tvShow.setText(Integer.toString(i++));
//
//                videoView.pause();
//                per = videoView.getCurrentPosition();
//                videoView.setVisibility(View.INVISIBLE);
//                videoView1.setVisibility(View.VISIBLE);
//                videoView1.start();
//            }
//            super.handleMessage(msg);
//        };
//    };
//    Timer timer = new Timer();
//    TimerTask task = new TimerTask() {
//        @Override
//        public void run() {
//            // 需要做的事:发送消息
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessage(message);
//        }
//    };


class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {

		}
	}

    class MyPlayerOnCompletionListener1 implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
//            videoView.setVisibility(View.VISIBLE);
//            videoView1.setVisibility(View.INVISIBLE);
//            videoView.seekTo(per);
//            videoView.start();
            Toast.makeText( C02_AgrotechniqueVideoDetailActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
	public void onClick(View v) {
        Intent intent;
		switch(v.getId()) {
//			case R.id.img_back:
//				finish();
//				break;
			case R.id.more:
				if(flag==0){
					layout_0.setVisibility(View.VISIBLE);
					flag = 1;
				}else{
					layout_0.setVisibility(View.GONE);
					flag = 0;
				}
				break;
            case R.id.layout_share:
                showSelectDialog();
                break;
            case R.id.layout_report:
                intent = new Intent(this,ReportActivity.class);
                intent.putExtra("id",route.video_id);
                intent.putExtra("from","2");
                intent.putExtra("iscomment","0");
                intent.putExtra("about_user","");
                startActivity(intent);
                break;
            case R.id.text_send:
                String content = edit_comment.getText().toString();
                if ("".equals(content)){
                    errorMsg("评论内容不能为空！");
                }else {
                    dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"0","",content,"","1");
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

            View layout_menu_0, layout_menu_1, layout_menu_2, layout_menu_3, layout_menu_4;
            layout_menu_0 = view.findViewById(R.id.layout_menu_0);
            layout_menu_0.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    ShareUtils.shareWeb(C02_AgrotechniqueVideoDetailActivity.this, url, agrotechniqueVideoDetailModel.videolist.title
                            , agrotechniqueVideoDetailModel.videolist.content, "", SHARE_MEDIA.WEIXIN);
                }
            });

            layout_menu_1 = view.findViewById(R.id.layout_menu_1);
            layout_menu_1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    ShareUtils.shareWeb(C02_AgrotechniqueVideoDetailActivity.this, url, agrotechniqueVideoDetailModel.videolist.title
                            , agrotechniqueVideoDetailModel.videolist.content, "", SHARE_MEDIA.WEIXIN_CIRCLE);
                }
            });

            layout_menu_2 = view.findViewById(R.id.layout_menu_2);
            layout_menu_2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    ShareUtils.shareWeb(C02_AgrotechniqueVideoDetailActivity.this, url, agrotechniqueVideoDetailModel.videolist.title
                            , agrotechniqueVideoDetailModel.videolist.content, "", SHARE_MEDIA.QQ);
                }
            });

            layout_menu_3 = view.findViewById(R.id.layout_menu_3);
            layout_menu_3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    ShareUtils.shareWeb(C02_AgrotechniqueVideoDetailActivity.this, url, agrotechniqueVideoDetailModel.videolist.title
                            , agrotechniqueVideoDetailModel.videolist.content, "", SHARE_MEDIA.QZONE);
                }
            });

            layout_menu_4 = view.findViewById(R.id.layout_menu_4);
            layout_menu_4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    ShareUtils.shareWeb(C02_AgrotechniqueVideoDetailActivity.this, url, agrotechniqueVideoDetailModel.videolist.title
                            , agrotechniqueVideoDetailModel.videolist.content, "", SHARE_MEDIA.SINA);
                }
            });

        }

        mMenuDialog.show();
    }

	@Override
	protected void onResume() {
		super.onResume();
//		updateData();

	}

    void requestData(){
        agrotechniqueVideoDetailModel.getVideoDetail(AppConst.info_from,cycle, AppUtils.getCurrCity(this));
        requestCommentData(false);
    }
	void updateData()
	{
        updateDatile();
        updateComment();
	}

    void  requestCommentData(boolean isShow){
        agrotechniqueVideoDetailModel.getComment(AppConst.info_from,id,isShow);
    }

    public static void deleteComment(String commentid){
        agrotechniqueVideoDetailModel.deleteComment(commentid,id);
    }

    public void answerComment(String commentid,String byreply_userid,String content){
        dictionaryModel.comment(AppConst.info_from,id,SESSION.getInstance().uid,"1",byreply_userid,content,commentid,"1");
    }

    ArrayList<KEYWORD> kWord;
    void updateDatile() {
        if (agrotechniqueVideoDetailModel.videolist != null) {
            route = agrotechniqueVideoDetailModel.videolist;
            title.setText(route.title);
            ArrayList<KEYWORD> keyWord = route.keyWord;
            kWord = route.keyWord;
            if( route.keyWord.size()>0){
                text_keyword.removeAllViews();
                for(int i = 0;i< keyWord.size();i++){
                    final TextView textView = new TextView(this);
                    textView.setText("#"+keyWord.get(i).wordName+"# ");
                    textView.setTag(keyWord.get(i).wordName);
                    text_keyword.addView(textView);
                    textView.setTextColor(getResources().getColor(R.color.green));
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(C02_AgrotechniqueVideoDetailActivity.this,B01_SearchActivity.class);
                            intent.putExtra("keyword",v.getTag().toString());
                            startActivity(intent);
                        }
                    });
                }
            }
            time.setText(route.create_time);
            collectionnum.setText(route.collect_num);
            num.setText(route.page_view);
            likenum.setText(route.like_num);
            content.setText(route.content);
            publishuser = route.author;
            textlike= route.islike;
            cycle = cycle;
            if(route.islike==1){
                likeimg.setBackground(getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
            }else{
                likeimg.setBackground(getResources().getDrawable(R.drawable.detail_icon_fabulous));
            }
            iscollection =route.iscollection;
            if (iscollection==1){
                collectionimg.setBackground(getResources().getDrawable(R.drawable.ccc));
            }
            else{
                collectionimg.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
            }
            updatesimilararticle(true);
        }

    }

    int page = 1;
    int ismore = 0;
    void  updatesimilararticle(boolean isShow){

        request.info_from = AppConst.info_from;
        request.articletype = route.articleType;
        request.city = AppUtils.getCurrCity(this);
        String textword = "";
        if(kWord!=null){
            for(int i = 0;i<kWord.size();i++){
                textword += kWord.get(i).wordCode+",";
            }
            request.word =textword;
        }else{
            request.word ="";
        }
        request.cycle = cycle;
        request.id = id;
        request.page = page;
        agrotechniqueVideoDetailModel.similarvideo(request,true);
    }
    private void updateSimilarVideo() {
        if (agrotechniqueVideoDetailModel.similarvideo.size() > 0) {
            layout_similar_video.setVisibility(View.VISIBLE);
            similar_video.removeAllViews();
            for (int i=0;i<agrotechniqueVideoDetailModel.similarvideo.size();i++){
                final  View view = LayoutInflater.from(this).inflate(R.layout.c02_agrotechnique_similar_video,null);
                ImageView image = (ImageView) view.findViewById(R.id.image);
                TextView title = (TextView) view.findViewById(R.id.title);
                final TextView textid = (TextView) view.findViewById(R.id.id);
                imageLoader.displayImage(agrotechniqueVideoDetailModel.similarvideo.get(i).titlePicurl,image, FarmingApp.options);
                title.setText(agrotechniqueVideoDetailModel.similarvideo.get(i).title);
                textid.setText(agrotechniqueVideoDetailModel.similarvideo.get(i).video_id);
                similar_video.addView(view);
                view.setTag(agrotechniqueVideoDetailModel.similarvideo.get(i).video_url);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent  intent = new Intent(C02_AgrotechniqueVideoDetailActivity.this,C02_AgrotechniqueVideoDetailActivity.class);
                        intent.putExtra("id",textid.getText());
                        intent.putExtra("cycle",cycle);
                        intent.putExtra("url",view.getTag().toString());
                        intent.putExtra("cyclename",cyclename);
                        startActivity(intent);
                    }
                });
            }
            if (0 == agrotechniqueVideoDetailModel.paginated.more) {
                ismore = 0;
            } else {
                ismore = 1;
            }
        }else
            layout_similar_video.setVisibility(View.GONE);
    }

    void updateComment(){
        int size = agrotechniqueVideoDetailModel.comment_list.size();
        if (size > 0) {
            ArrayList<a_COMMENT_LIST> comment_list = agrotechniqueVideoDetailModel.comment_list;
            comment.setVisibility(View.VISIBLE);
            text_comment_num.setText("（"+ agrotechniqueVideoDetailModel.comment_list.size() +"条）");

//            if(commentAdapter == null){
//                commentAdapter = new C02_ArticleCommentAdapter(this,agrotechniqueVideoDetailModel.comment_list.comment,2);
//                listView.setAdapter(commentAdapter);
//            }else{
//                commentAdapter.notifyDataSetChanged();
//            }

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
        CommentFun.inputAgComment(C02_AgrotechniqueVideoDetailActivity.this, listView, v, receiver, new CommentFun.InputAgCommentListener() {
            @Override
            public void onCommitAgComment(String content, AgrotechniqueMoment moment, Comment comment,String byreply_userid) {
                answerComment(moment.id,byreply_userid,content);
            }

        });
    }

    public static void like(String uid,String byreplyId){
        artRequest.about_userid = byreplyId;
        artRequest.comment_id = uid;
        artRequest.publish_userid = publishuser;
        artRequest.user_id = SESSION.getInstance().uid;
        artRequest.about_id = "";
        artRequest.like_type = 2;
        artRequest.from_openid = "";
        agrotechniqueVideoDetailModel.like(artRequest);
    }

    public static void cancellike(String uid){
        //	quesRequest.about_userid = byreplyId;
        artRequest.comment_id = uid;
        //	quesRequest.publish_userid = publishuser;
        artRequest.user_id = SESSION.getInstance().uid;
        artRequest.about_id = "";
        artRequest.like_type = 2;
        artRequest.from_openid = "";
        agrotechniqueVideoDetailModel.cancellike(artRequest);
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
        if (url.contains(ApiInterface.VIDEODETAIL)) {
            if (agrotechniqueVideoDetailModel.lastStatus.error_code == 200) {
                updateDatile();
              //  updateSimilarVideo();
                iscollection = agrotechniqueVideoDetailModel.videolist.iscollection;
            } else {
                errorMsg(agrotechniqueVideoDetailModel.lastStatus.error_desc);
            }
        }else if (url.contains(ApiInterface.ARTICLECOMMENT)) {
            momentAdapter = null;
            updateComment();
        }else if(url.contains(ApiInterface.DELETEDCOMMENT)){
            requestCommentData(false);
        }else if(url.contains(ApiInterface.COLLECTION)){
            errorMsg("收藏成功");
            collectionimg.setBackground(getResources().getDrawable(R.drawable.ccc));
            requestData();
            iscollection = 1;
            commentAdapter=null;
        }
        if(url.contains(ApiInterface.CANCELCOLLECTION)){
            errorMsg("取消收藏成功");
            collectionimg.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
            requestData();
            iscollection = 0;
            commentAdapter=null;
        }
        if(url.contains(ApiInterface.USERLIKE)){
            errorMsg("点赞成功");
            requestData();
            commentAdapter=null;
        }
        if(url.contains(ApiInterface.CANCELUSERLIKE)){
            errorMsg("取消点赞成功");
            requestData();
            commentAdapter=null;
        }
        if(url.contains(ApiInterface.COMMENT)){
            if (dictionaryModel.lastStatus.error_code == 200) {
                requestCommentData(false);
                edit_comment.setText("");
            } else
                errorMsg(dictionaryModel.lastStatus.error_desc);
        }
        if(url.contains(ApiInterface.SIMILARVIDEO)){
            if (agrotechniqueVideoDetailModel.lastStatus.error_code == 200) {
                updateSimilarVideo();
            } else
                errorMsg(agrotechniqueVideoDetailModel.lastStatus.error_desc);
        }
    }

    @Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        videoView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        videoView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        agrotechniqueVideoDetailModel.removeResponseListener(this);
        dictionaryModel.removeResponseListener(this);
        videoView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!DisplayUtils.isPortrait(this)) {
            if(!videoView.isLock()) {
                DisplayUtils.toggleScreenOrientation(this);
            }
        } else {
            super.onBackPressed();
        }
    }

}