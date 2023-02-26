package com.dmy.farming.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextUtils;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_MarketPriceListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.REPLYASK;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.REPLY;
import com.dmy.farming.api.model.QuestionDetailModel;
import com.dmy.farming.api.model.QuestionModel;
import com.dmy.farming.api.questiondatailRequest;
import com.dmy.farming.photopicker.PhotoPreviewActivity;
import com.dmy.farming.view.comment.Comment;
import com.dmy.farming.view.comment.CommentFun;
import com.dmy.farming.view.comment.CustomTagHandler;
import com.dmy.farming.view.comment.Moment;
import com.dmy.farming.view.comment.MomentAdapter;
import com.dmy.farming.view.comment.User;
import com.dmy.farming.view.emojicon.EaseDefaultEmojiconDatas;
import com.dmy.farming.view.emojicon.EaseEmojicon;
import com.dmy.farming.view.emojicon.EaseSmileUtils;
import com.dmy.farming.view.emojicon.EmojiconGridAdapter;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D01_QuestionDetailActivity extends BaseActivity implements OnClickListener, BusinessResponse {

    static QuestionDetailModel dataModel;
    static QuestionModel questionModel;
    static String questionId; //问题id
    ImageLoader imageLoader = ImageLoader.getInstance();
    boolean answer = false;
    QUESTION question;
    public static String type;
    static questiondatailRequest quesRequest;
    static String publishuser;
    static String commentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d00_chat_item_list_detail);

        questionId = getIntent().getStringExtra("id");
        answer = getIntent().getBooleanExtra("answer", false);
        type = getIntent().getStringExtra("type");

        initView();

        quesRequest = new questiondatailRequest();

        imageLoader = ImageLoader.getInstance();

        dataModel = new QuestionDetailModel(this);
        dataModel.addResponseListener(this);
        questionModel = new QuestionModel(this);
        questionModel.addResponseListener(this);

        requestData(true);
        updateData();


        if (answer == true) {
            //注意一定要提交数据，此步骤容易忘记
//			showInputComment(this, "回答", new CommentFun.CommentDialogListener() {
//				@Override
//				public void onClickPublish(Dialog dialog, EditText input, TextView btn) {
//					String content = input.getText().toString();
//					if (content.trim().equals("")) {
//						errorMsg("评论不能为空");
//						return;
//					}
//					answer(content);
//				//	dialog.dismiss();
//				}
//
//				@Override
//				public void onShow(int[] inputViewCoordinatesOnScreen) {
//
//				}
//
//				@Override
//				public void onDismiss() {
//
//				}
//			});

            edit_comment.setFocusable(true);
            edit_comment.setFocusableInTouchMode(true);
            edit_comment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edit_comment, 0);
        }

    }

    ListView list_address, mExpertListView;
    Moment mMoment;
    Comment mComment;
    MomentAdapter adoptAdapter, expertAdapter, netfriendAdapter;
    C01_MarketPriceListAdapter listAdapter;
    ImageView img_head, img_head1, img, img_emoji;
    TextView text_username, text_time, text_position, text_content, text_keyword, text_browse, text_comment, text_username_adopt,
            text_usertype, text_content_adopt, text_time_adopt, text_thumbup, time, position, content, keyword;
    XListView list_detail;
    GridView list_adopt, list_expert, grid_img, gv_emoji;
    LinearLayout layout_expert_answer, layout_netfriend, questiontypr, myquestion, layout_share, layout_collection, layout_report;
    View layout_adopt, layout_expert, expert_bottom, layout_input, null_pager, shield_null_pager;
    ImageView solve;
    String iscollection = "0";
    GridAdapter gridAdapter;
    ArrayList<String> imagePaths = new ArrayList<>();
    EditText edit_comment;
    private Dialog builder;
    FrameLayout more;
    EmojiconGridAdapter smileAdapter;

    void initView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.d00_chat_item_detail_header, null);

        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);
		shield_null_pager =  findViewById(R.id.shield_null_pager);

        // 评论输入框
        layout_input = findViewById(R.id.layout_input);
        edit_comment = (EditText) layout_input.findViewById(R.id.edit_comment);
        edit_comment.setOnClickListener(this);
        edit_comment.setHint("回答");
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
                    edit_comment.append(EaseSmileUtils.getSmiledText(D01_QuestionDetailActivity.this, emojicon.getEmojiText()));
                }
            }
        });

	/*	expression = (ImageView)findViewById(R.id.img);
		expression.setOnClickListener(this);*/

//		View more = headerView.findViewById(R.id.more);
//		more.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				switch (view.getId()){
//					case R.id.more:
//						showSelectDialog(view);
//						break;
//				}
//			}
//		});

        questiontypr = (LinearLayout) headerView.findViewById(R.id.questiontypr);
        myquestion = (LinearLayout) headerView.findViewById(R.id.myquestion);
        more = (FrameLayout) findViewById(R.id.more);
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    showSelectDialog(v);
                }
            }
        });
		/*collection = (ImageView)findViewById(R.id.collection);
		if (AppUtils.isLogin(this)) {
			collection.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (iscollection.equals("0")) {
						dataModel.collection(questionId);
					} else {
						dataModel.cancelcollection(questionId);
					}
				}
			});
		}
		share = (ImageView)findViewById(R.id.share);*/

        if (type.equals("myquestion")) {
            questiontypr.setVisibility(View.GONE);
            myquestion.setVisibility(View.VISIBLE);
			/*collection.setVisibility(View.INVISIBLE);
			share.setVisibility(View.INVISIBLE);*/
            more.setVisibility(View.GONE);
            layout_input.setVisibility(View.GONE);
        } else {
            questiontypr.setVisibility(View.VISIBLE);
            myquestion.setVisibility(View.GONE);
			/*collection.setVisibility(View.VISIBLE);
			share.setVisibility(View.VISIBLE);*/
            more.setVisibility(View.VISIBLE);
            layout_input.setVisibility(View.VISIBLE);
        }
        img_head = (ImageView) headerView.findViewById(R.id.img_head);
        grid_img = (GridView) headerView.findViewById(R.id.grid_img);
        solve = (ImageView) headerView.findViewById(R.id.solve);
        text_username = (TextView) headerView.findViewById(R.id.text_username);
        text_time = (TextView) headerView.findViewById(R.id.text_time);
        text_position = (TextView) headerView.findViewById(R.id.text_position);
        text_content = (TextView) headerView.findViewById(R.id.text_content);
        text_keyword = (TextView) headerView.findViewById(R.id.text_keyword);
        text_browse = (TextView) headerView.findViewById(R.id.text_browse);
        text_comment = (TextView) headerView.findViewById(R.id.text_comment);
        time = (TextView) headerView.findViewById(R.id.time);
        position = (TextView) headerView.findViewById(R.id.position);
        content = (TextView) headerView.findViewById(R.id.content);
        keyword = (TextView) headerView.findViewById(R.id.keyword);
        img = (ImageView) headerView.findViewById(R.id.img);

        // 采纳
        layout_adopt = headerView.findViewById(R.id.layout_adopt);
        list_adopt = (GridView) layout_adopt.findViewById(R.id.list_adopt);

//		img_head1 = (ImageView) layout_adopt.findViewById(R.id.img_head1);
//		text_username_adopt = (TextView) layout_adopt.findViewById(R.id.text_username_adopt);
//		text_usertype = (TextView) layout_adopt.findViewById(R.id.text_usertype);
//		text_content_adopt = (TextView) layout_adopt.findViewById(R.id.text_content_adopt);
//		text_time_adopt = (TextView) layout_adopt.findViewById(R.id.text_time_adopt);
//		text_thumbup = (TextView) layout_adopt.findViewById(R.id.text_thumbup);

        // 专家
        layout_expert = headerView.findViewById(R.id.layout_expert);
        list_expert = (GridView) layout_expert.findViewById(R.id.list_expert);
        expert_bottom = layout_expert.findViewById(R.id.expert_bottom);

        layout_netfriend = (LinearLayout) headerView.findViewById(R.id.layout_netfriend);

        gridAdapter = new GridAdapter(imagePaths);
        grid_img.setAdapter(gridAdapter);

        list_detail = (XListView) findViewById(R.id.list_detail);
        list_detail.addHeaderView(headerView);

        list_detail.setPullLoadEnable(false);
        list_detail.setPullRefreshEnable(false);
//		list_detail.setXListViewListener(this,0);
        list_detail.setAdapter(null);


    }

    String about_user = "";
    PopupWindow popupWindow;
    ImageView collection;

    private void showSelectDialog(View v) {
        //自定义布局，显示内容
        View view = LayoutInflater.from(this).inflate(R.layout.d01_question_item, null);
        final View layout_collection = view.findViewById(R.id.layout_collection);
        collection = (ImageView) view.findViewById(R.id.collection);
        if (iscollection.equals("0")) {
            collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
        } else {
            collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
        }
        layout_collection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iscollection.equals("0")) {
                    collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
                    dataModel.collection(questionId);
                } else {
                    dataModel.cancelcollection(questionId);
                    collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
                }
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
        if (publishuser.equals(SESSION.getInstance().sid)) {
            final View report = view.findViewById(R.id.layout_report);
            View line2 = view.findViewById(R.id.line2);
            report.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            report.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //	requestData(true);
                    Intent intent = new Intent(D01_QuestionDetailActivity.this, ReportActivity.class);
                    intent.putExtra("id", questionId);
                    intent.putExtra("from", "3");
                    intent.putExtra("iscomment", "0");
//                    if (SESSION.getInstance().usertype == 1) {
//                        about_user = SESSION.getInstance().uid;
//                    } else {
                        about_user = SESSION.getInstance().sid;
//                    }
                    intent.putExtra("about_user", about_user);
                    startActivity(intent);
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        } else {
            final View report = view.findViewById(R.id.layout_report);
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //	requestData(true);
                    Intent intent = new Intent(D01_QuestionDetailActivity.this, ReportActivity.class);
                    intent.putExtra("id", questionId);
                    intent.putExtra("from", "3");
                    intent.putExtra("iscomment", "0");
//                    if (SESSION.getInstance().usertype == 1) {
//                        about_user = SESSION.getInstance().uid;
//                    } else {
                        about_user = SESSION.getInstance().sid;
//                    }
                    intent.putExtra("about_user", about_user);
                    startActivity(intent);
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }

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

        popupWindow.showAsDropDown(v, 50, 10);

    }


    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            inflater = LayoutInflater.from(D01_QuestionDetailActivity.this);
        }

        public int getCount() {
            if (listUrls.size() > 3)
                return 3;
            else
                return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.question_item_image, parent, false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String path = listUrls.get(position);
            imageLoader.displayImage(path, holder.image, FarmingApp.options);

            holder.image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(D01_QuestionDetailActivity.this, ImagePreviewActivity.class);
                    intent.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, position);
                    intent.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, listUrls);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }

    public void inputComment(final View v) {
        inputComment(v, null);
    }

    public void inputComment(final View v, User receiver) {
        CommentFun.inputComment(D01_QuestionDetailActivity.this, list_detail, v, receiver, new CommentFun.InputCommentListener() {
            @Override
            public void onCommitComment(String content, Moment moment, Comment comment) {
//				netfriendAdapter.notifyDataSetChanged();

                mMoment = moment;
                mComment = comment;
                askAnswer(content, moment, comment);
            }
        });
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
	/*	sharedPreferences = getSharedPreferences("info2", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		//使用editor保存数据
		editor.putString("content", input.getText().toString());*/
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
            case R.id.text_send:
                if (question.userPhone.equals(SESSION.getInstance().sid)) {
                    errorMsg("不能回答自己的问题");
                } else {
                    String content = edit_comment.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        errorMsg("内容不能为空！");
                    } else {
                        answer(content);
                    }
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

    void requestData(boolean isShow) {
        dataModel.questionDetail(questionId, isShow);

    }

    public static void adoptComment(String id) {
        questionModel.adoptComment(AppConst.info_from, questionId, id);
    }

    public static void deleteComment(String id, String questionId) {
        questionModel.deleteComment(id, questionId);
    }

    public void answer(String content) {
//        if (SESSION.getInstance().usertype == 1) {
            questionModel.answer(AppConst.info_from, questionId, content, SESSION.getInstance().uid, "0", dataModel.data.question.userPhone, SESSION.getInstance().usertype);
//        } else {
//            questionModel.answer(AppConst.info_from, questionId, content, SESSION.getInstance().sid, "0", dataModel.data.question.userPhone, SESSION.getInstance().usertype);
//        }
    }

    public void askAnswer(String content, Moment moment, Comment comment) {
        User receiver = comment.mReceiver;
        if (receiver.mId.equals(SESSION.getInstance().uid))
            questionModel.askAnswer(AppConst.info_from, moment.id, content, receiver.mId, "2", SESSION.getInstance().uid, questionId);
        else
            questionModel.askAnswer(AppConst.info_from, moment.id, content, SESSION.getInstance().uid, "1", receiver.mId, questionId);
    }

    private void updateData() {
        updateQuestionDetail();
        updateAdoptAnswer();
        updateExpert();
        updateNetfriend();
    }

    private void updateQuestionDetail() {
        if (dataModel.data.question != null) {
			null_pager.setVisibility(View.GONE);
            //	QUESTION question = dataModel.question;
            if (!TextUtils.isEmpty(question.img_url)) {
                final List<String> imgurls = Arrays.asList(question.img_url.split(","));

                imagePaths.clear();
                imagePaths.addAll(imgurls);
                gridAdapter.notifyDataSetChanged();

//				img1.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(D01_QuestionDetailActivity.this,ImagePreviewActivity.class);
//						intent.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, 0);
//						intent.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, imgs);
//						startActivity(intent);
//					}
//				});

            }
            imageLoader.displayImage(question.head_url, img_head, FarmingApp.options_head);
            text_username.setText(question.user_name);
            text_time.setText(AppUtils.time(question.create_time));
            text_position.setText(question.city + question.district);
            if (question.isShield == 1) {
                content.setText("此问题已被管理员屏蔽");
            } else {
                content.setText(question.content);
            }


            text_content.setText("\u3000\u3000" + question.content);

            String a[] = question.problemType.split(",");
            String tkeyword = "";
            for (int i = 0; i < a.length; i++) {
                tkeyword += "#" + a[i] + "#  ";
            }
            text_keyword.setText(tkeyword);
            text_comment.setText(question.comment_num);
            text_browse.setText(question.page_view + "");
            time.setText(AppUtils.time(question.create_time));
            position.setText(question.provience + question.city + question.district);

            keyword.setText(tkeyword);
            publishuser = question.userPhone;
            List<String> imgurls = Arrays.asList(question.img_url.split(","));
            imageLoader.displayImage(imgurls.get(0), img, FarmingApp.options);
            if (question.accept == 0) {
                solve.setVisibility(View.GONE);
            } else {
                solve.setVisibility(View.VISIBLE);
            }
            iscollection = question.iscollection;
			/*if (iscollection.equals("1")){
				collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
			}
			else{
				collection.setBackground(getResources().getDrawable(R.drawable.more_icon_collection));
			}*/
        } else {
			null_pager.setVisibility(View.VISIBLE);
        }
    }

    private void updateAdoptAnswer() {
        if (dataModel.data.reply_list.size() > 0) {
            int size = dataModel.data.reply_list.get(0).isadopt;
            if (size > 0) {
                layout_adopt.setVisibility(View.VISIBLE);

                REPLY reply = dataModel.data.reply_list.get(0);
			/*	imageLoader.displayImage(reply.headurl, img_head1, FarmingApp.options_head);
				text_username_adopt.setText(reply.username);
				if (reply.reply_usertype == 1)
					text_usertype.setText(reply.expert_type);
				else
					text_usertype.setText("");
				text_content_adopt.setText("\u3000\u3000" + reply.content);
				text_time_adopt.setText(reply.comment_time);
				text_thumbup.setText(reply.like_num);*/
                commentid = dataModel.data.reply_list.get(0).id;

                ArrayList<Moment> moment = new ArrayList<Moment>();
                ArrayList<Comment> comments = new ArrayList<Comment>();
                for (REPLYASK replyask : reply.subreply_list) {
                    if (replyask.userPhone.equals(SESSION.getInstance().sid))
                        comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, null));
                    else
                        comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, new User(replyask.byreply_userPhone, "")));

                }
                moment.add(new Moment(reply.id, reply, comments));

                if (adoptAdapter == null) {
                    adoptAdapter = new MomentAdapter(this, moment, question, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
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

                    list_adopt.setAdapter(adoptAdapter);
                } else {
                    adoptAdapter.notifyDataSetChanged();
                }

            } else {
                adoptAdapter = null;
                layout_adopt.setVisibility(View.GONE);
            }
        } else {
            layout_adopt.setVisibility(View.GONE);
        }
    }

    public ArrayList<REPLY> expert_reply_list = new ArrayList();
    ArrayList<Moment> expertMoments;
    public ArrayList<REPLY> netfriend_reply_list = new ArrayList();

    private void updateExpert() {
        int size = expert_reply_list.size();
        if (size > 0) {
            layout_expert.setVisibility(View.VISIBLE);

            if (size == 1)
                expert_bottom.setVisibility(View.GONE);
            else {
                expert_bottom.setVisibility(View.VISIBLE);
                expert_bottom.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expertMoments = new ArrayList<Moment>();
                        for (int i = 0; i < expert_reply_list.size(); i++) {
                            ArrayList<Comment> comments = new ArrayList<Comment>();
                            for (REPLYASK replyask : expert_reply_list.get(i).subreply_list) {
                                if (replyask.userPhone.equals(SESSION.getInstance().sid))
                                    comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, null));
                                else
                                    comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, new User(replyask.byreply_userPhone, "")));

                            }
                            expertMoments.add(new Moment(expert_reply_list.get(i).id, expert_reply_list.get(i), comments));
                        }
                        expertAdapter = null;
                        expert_bottom.setVisibility(View.GONE);
                        updateExpertData();
                    }
                });
            }

            expertMoments = new ArrayList<Moment>();
            ArrayList<Comment> comments = new ArrayList<Comment>();
            for (REPLYASK replyask : expert_reply_list.get(0).subreply_list) {
                if (replyask.userPhone.equals(SESSION.getInstance().sid))
                    comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, null));
                else
                    comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, new User(replyask.byreply_userPhone, "")));

            }
            expertMoments.add(new Moment(expert_reply_list.get(0).id, expert_reply_list.get(0), comments));

            updateExpertData();

        } else {
            layout_expert.setVisibility(View.GONE);
        }
    }

    private void updateExpertData() {
        if (expertAdapter == null) {
            expertAdapter = new MomentAdapter(this, expertMoments, question, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
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
                    if (commentator != null && (commentator.mId.equals(SESSION.getInstance().sid) || commentator.mId.equals(SESSION.getInstance().uid))) { // 不能回复自己的评论
                        return;
                    }
                    inputComment(view, commentator);
                }
            }));

            list_expert.setAdapter(expertAdapter);
        } else {
            expertAdapter.notifyDataSetChanged();
        }
    }

    void updateNetfriend() {
        int size = netfriend_reply_list.size();

        if (size > 0) {
            layout_netfriend.setVisibility(View.VISIBLE);

            // 数据
            ArrayList<Moment> moments = new ArrayList<Moment>();
            for (int i = 0; i < size; i++) {
                ArrayList<Comment> comments = new ArrayList<Comment>();
                for (REPLYASK replyask : netfriend_reply_list.get(i).subreply_list) {
//				comments.add(new Comment(new User(i + 2, "用户" + i), "评论" + i, null));
//				comments.add(new Comment(new User(i + 100, "用户" + (i + 100)), "评论" + i, new User(i + 200, "用户" + (i + 200))));
                    if (replyask.userPhone.equals(SESSION.getInstance().sid))
                        comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, null));
                    else
                        comments.add(new Comment(new User(replyask.userPhone, ""), replyask.content, new User(replyask.byreply_userPhone, "")));

                }
                moments.add(new Moment(netfriend_reply_list.get(i).id, netfriend_reply_list.get(i), comments));
            }

            if (netfriendAdapter == null) {
                netfriendAdapter = new MomentAdapter(this, moments, question, new CustomTagHandler(this, new CustomTagHandler.OnCommentClickListener() {
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

                list_detail.setAdapter(netfriendAdapter);
            } else {
                netfriendAdapter.notifyDataSetChanged();
            }
        } else {
            layout_netfriend.setVisibility(View.GONE);
            netfriendAdapter = null;
            list_detail.setAdapter(null);
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

    public static void like(String uid, String byreplyId) {
        quesRequest.about_userid = byreplyId;
        quesRequest.comment_id = uid;
        quesRequest.publish_userid = publishuser;
        quesRequest.user_id = SESSION.getInstance().uid;
        quesRequest.about_id = questionId;
        quesRequest.like_type = 0;
        quesRequest.from_openid = "";
        dataModel.like(quesRequest);
    }

    public static void cancellike(String uid) {
        //	quesRequest.about_userid = byreplyId;
        quesRequest.comment_id = uid;
        //	quesRequest.publish_userid = publishuser;
        quesRequest.user_id = SESSION.getInstance().uid;
        quesRequest.about_id = questionId;
        quesRequest.like_type = 0;
        quesRequest.from_openid = "";

        dataModel.cancellike(quesRequest);
    }

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        if (url.contains(ApiInterface.QUESTIONDETAIL)) {
//			list_news.setRefreshTime();
            question = dataModel.data.question;
            if (question.isShield == 1) {
				shield_null_pager.setVisibility(View.VISIBLE);
                return;
            } else {
				shield_null_pager.setVisibility(View.GONE);
            }
            expert_reply_list.clear();
            netfriend_reply_list.clear();
            if (dataModel.data.reply_list.size() > 0) {
                for (REPLY reply : dataModel.data.reply_list) {
                    if (reply.isadopt == 0) {
                        if (reply.reply_usertype == 1)
                            expert_reply_list.add(reply);
                        else
                            netfriend_reply_list.add(reply);
                    }
                }
            } else {

            }
            expertAdapter = null;
            netfriendAdapter = null;
            updateData();
        } else if (url.contains(ApiInterface.ADOPTCOMMENT)) {
            errorMsg("采纳成功");
            requestData(false);
        } else if (url.contains(ApiInterface.DELETECOMMENT)) {
            if (questionModel.lastStatus.error_code == 200) {
                errorMsg("删除成功");
                requestData(false);
            } else
                errorMsg(questionModel.lastStatus.error_desc);
        } else if (url.contains(ApiInterface.ASKANSWER)) {
            if (questionModel.lastStatus.error_code == 200) {
                mMoment.mComment.add(mComment);
                if (adoptAdapter != null)
                    adoptAdapter.notifyDataSetChanged();
                else if (expertAdapter != null)
                    expertAdapter.notifyDataSetChanged();
                else if (netfriendAdapter != null)
                    netfriendAdapter.notifyDataSetChanged();
            } else
                errorMsg(questionModel.lastStatus.error_desc);

        } else if (url.contains(ApiInterface.ANSWER)) {
            if (questionModel.lastStatus.error_code == 200) {
                requestData(false);
            } else
                errorMsg(questionModel.lastStatus.error_desc);
        } else if (url.contains(ApiInterface.COLLECTION)) {
            errorMsg("收藏成功");
            iscollection = "1";
        }
        if (url.contains(ApiInterface.CANCELCOLLECTION)) {
            errorMsg("取消收藏成功");
            iscollection = "0";
        }
        if (url.contains(ApiInterface.USERLIKE)) {
            errorMsg("点赞成功");
            adoptAdapter = null;
            expertAdapter = null;
            netfriendAdapter = null;
            expert_reply_list.clear();
            netfriend_reply_list.clear();
            requestData(false);
            //updatecommentData(false);
        }
        if (url.contains(ApiInterface.CANCELUSERLIKE)) {
            errorMsg("取消点赞成功");
            adoptAdapter = null;
            expertAdapter = null;
            netfriendAdapter = null;
            expert_reply_list.clear();
            netfriend_reply_list.clear();
            requestData(false);
            //updatecommentData(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataModel.removeResponseListener(this);
        questionModel.removeResponseListener(this);
    }

}