package com.dmy.farming.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.DIAGNOSTIC;
import com.dmy.farming.api.data.EXPERTINFO;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.VIDEOLIST;
import com.dmy.farming.api.model.SearchModel;
import com.dmy.farming.api.searchUserRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class B00_SearchActivity extends BaseActivity implements OnClickListener,BusinessResponse, XListView.IXListViewListener{

	SearchModel searchModel;
	searchUserRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b00_search);

		request = new searchUserRequest();
		request.search_word = getIntent().getStringExtra("keyword");

		initView();

		searchModel = new SearchModel(this);
		searchModel.addResponseListener(this);

		if (!TextUtils.isEmpty(request.search_word)) {
			edit_keyword.setText(request.search_word);
			requestData();
		} else {
			request.search_word = "";
			requestData();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	EditText edit_keyword;
	GridView grid_question, grid_diagnostic, grid_article, grid_video, grid_expert;
	XListView list_search;
	View null_pager,layout_search,layout_question,layout_diagnostic,layout_article,layout_video,layout_expert;
    B00_QuestionAdapter questionAdapter;
    B00_DiagnosticAdapter diagnosticAdapter;
    B00_ArticleAdapter articleAdapter;
    B00_VideoAdapter videoAdapter;
    B00_ExpertAdapter expertAdapter;
	TextView text_type;
	//ImageView img_head;
//	TextView text_name,text_tag;

	void initView()
	{
		View button_right =  findViewById(R.id.button_right);
		button_right.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);
		layout_search = findViewById(R.id.layout_search);
		list_search = (XListView) findViewById(R.id.list_search);
		text_type = (TextView) findViewById(R.id.text_type);
		text_type.setOnClickListener(this);

		View headerView = LayoutInflater.from(this).inflate(R.layout.b00_search_header, null);

		layout_question = headerView.findViewById(R.id.layout_question);
		layout_diagnostic = headerView.findViewById(R.id.layout_diagnostic);
		layout_article = headerView.findViewById(R.id.layout_article);
		layout_video = headerView.findViewById(R.id.layout_video);
		layout_expert = headerView.findViewById(R.id.layout_expert);


		//img_head = (ImageView) layout_friend.findViewById(R.id.img_head);
		//text_name = (TextView) layout_friend.findViewById(R.id.text_name);
		//text_tag = (TextView) layout_friend.findViewById(R.id.text_tag);

		grid_question = (GridView) layout_question.findViewById(R.id.grid_question);
		grid_diagnostic = (GridView) layout_diagnostic.findViewById(R.id.grid_diagnostic);
		grid_article = (GridView) layout_article.findViewById(R.id.grid_article);
		grid_video = (GridView) layout_video.findViewById(R.id.grid_video);
		grid_expert = (GridView) layout_expert.findViewById(R.id.grid_expert);

		list_search.addHeaderView(headerView);
		list_search.setPullLoadEnable(false);
		list_search.setAdapter(null);
		list_search.setXListViewListener(this, 1);

		edit_keyword = (EditText) findViewById(R.id.edit_keyword);
		edit_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					requestData();
					closeKeyBoard();
				}
				return false;
			}
		});
        edit_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text_type.setText("全部");
                search_type = "";
                request.search_word = s.toString();
            }
        });
	}

	String keyword = "";
	int page = 1;
	String search_type = "";
	void requestData() {
//		request.city = AppUtils.getCurrCity(this);
		request.info_from = AppConst.info_from;
		request.page = page + "";
		request.search_type = search_type;
		request.userid = SESSION.getInstance().uid;
		searchModel.search(request);
	}



	ImageLoader imageLoader = ImageLoader.getInstance();

	void updateData(){
		int questionsize = searchModel.data.questionList.size();
		int diagnosticsize = searchModel.data.diagnosticList.size();
		int articlesize = searchModel.data.articleList.size();
		int videosize = searchModel.data.videoList.size();
		int expertsize = searchModel.data.expertList.size();

		if (questionsize > 0) {
			null_pager.setVisibility(View.GONE);
			layout_question.setVisibility(View.VISIBLE);

			if (questionAdapter == null) {
				questionAdapter = new B00_QuestionAdapter(this, searchModel.data.questionList);
				grid_question.setAdapter(questionAdapter);
			} else {
				questionAdapter.notifyDataSetChanged();
			}
		} else {
			layout_question.setVisibility(View.GONE);

		}

		if (diagnosticsize > 0) {
			null_pager.setVisibility(View.GONE);
			layout_diagnostic.setVisibility(View.VISIBLE);

			if (diagnosticAdapter == null) {
				diagnosticAdapter = new B00_DiagnosticAdapter(this, searchModel.data.diagnosticList);
				grid_diagnostic.setAdapter(diagnosticAdapter);
			} else {
				diagnosticAdapter.notifyDataSetChanged();
			}
		} else {
			layout_diagnostic.setVisibility(View.GONE);
		}

		if (articlesize > 0) {
			null_pager.setVisibility(View.GONE);
			layout_article.setVisibility(View.VISIBLE);

			if (articleAdapter == null) {
				articleAdapter = new B00_ArticleAdapter(this, searchModel.data.articleList);
				grid_article.setAdapter(articleAdapter);
			} else {
				articleAdapter.notifyDataSetChanged();
			}
		} else {
			layout_article.setVisibility(View.GONE);
		}

		if (videosize > 0) {
			null_pager.setVisibility(View.GONE);
			layout_video.setVisibility(View.VISIBLE);
			if (videoAdapter == null) {
				videoAdapter = new B00_VideoAdapter(this, searchModel.data.videoList);
				grid_video.setAdapter(videoAdapter);
			} else {
				videoAdapter.notifyDataSetChanged();
			}
		} else {
			layout_video.setVisibility(View.GONE);
		}

		if (expertsize > 0) {
			null_pager.setVisibility(View.GONE);
			layout_expert.setVisibility(View.VISIBLE);
			if (expertAdapter == null) {
				expertAdapter = new B00_ExpertAdapter(this, searchModel.data.expertList);
				grid_expert.setAdapter(expertAdapter);
			} else {
				expertAdapter.notifyDataSetChanged();
			}
		} else {
			layout_expert.setVisibility(View.GONE);
		}

		if (questionsize == 0 && diagnosticsize == 0 && articlesize == 0 && videosize == 0 && expertsize == 0)
			null_pager.setVisibility(View.VISIBLE);

	}

	class B00_QuestionAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<QUESTIONLIST> dataList;

		public B00_QuestionAdapter(Context c, ArrayList<QUESTIONLIST> dataList) {
			mContext = c;
			this.dataList = dataList;
		}

		public int getCount() {
			return dataList.size();
		}

		public QUESTIONLIST getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}

		public long getItemId(int position) {
			return position;
		}

		private class ViewHolder {
			public View layout_frame;
			public TextView title;
			public ImageView img;
		}

		protected ImageLoader imageLoader = ImageLoader.getInstance();

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_search_item, null, false);
				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final QUESTIONLIST cat = getItem(position);
			if (cat != null)
			{
				viewHolder.title.setText(cat.content);
				if (cat.imgUrl.contains(",")){
					String[] img = cat.imgUrl.split(",");
					imageLoader.displayImage(img[0], viewHolder.img, FarmingApp.options);
				}else
					imageLoader.displayImage(cat.imgUrl, viewHolder.img, FarmingApp.options);

				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
                        Intent intent = new Intent(mContext, D01_QuestionDetailActivity.class);
                        intent.putExtra("id", cat.id);
                        intent.putExtra("answer", true);
                        intent.putExtra("type","questiontypr");
                        mContext.startActivity(intent);
					}

				});
			}
			return convertView;
		}
	}

    class B00_DiagnosticAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<DIAGNOSTIC> dataList;

        public B00_DiagnosticAdapter(Context c, ArrayList<DIAGNOSTIC> dataList) {
            mContext = c;
            this.dataList = dataList;
        }

        public int getCount() {
            return dataList.size();
        }

        public DIAGNOSTIC getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public View layout_frame;
            public TextView title;
            public ImageView img;
        }

        protected ImageLoader imageLoader = ImageLoader.getInstance();

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_search_item, null, false);
                viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final DIAGNOSTIC cat = getItem(position);
            if (cat != null)
            {
                viewHolder.title.setText(cat.name);
                if (cat.img_url.contains(",")){
                    String[] img = cat.img_url.split(",");
                    imageLoader.displayImage(img[0], viewHolder.img, FarmingApp.options);
                }else
                    imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options);

                viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, C03_DiagmpsticLibDetailActivity.class);
                        intent.putExtra("id",cat.id);
                        intent.putExtra("title",cat.name);
                        intent.putExtra("cycle",cat.cycle);
                        mContext.startActivity(intent);
                    }

                });
            }
            return convertView;
        }
    }

    class B00_ArticleAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<ARTICLELIST> dataList;

        public B00_ArticleAdapter(Context c, ArrayList<ARTICLELIST> dataList) {
            mContext = c;
            this.dataList = dataList;
        }

        public int getCount() {
            return dataList.size();
        }

        public ARTICLELIST getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public View layout_frame;
            public TextView title;
            public ImageView img;
        }

        protected ImageLoader imageLoader = ImageLoader.getInstance();

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_search_item, null, false);
                viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final ARTICLELIST cat = getItem(position);
            if (cat != null)
            {
                viewHolder.title.setText(cat.title);
                if (cat.img_url.contains(",")){
                    String[] img = cat.img_url.split(",");
                    imageLoader.displayImage(img[0], viewHolder.img, FarmingApp.options);
                }else
                    imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options);

                viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, C01_FarmNewsItemDetailActivity.class);
                        intent.putExtra("id", cat.id);
                        mContext.startActivity(intent);
                    }

                });
            }
            return convertView;
        }
    }

    class B00_VideoAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<VIDEOLIST> dataList;

        public B00_VideoAdapter(Context c, ArrayList<VIDEOLIST> dataList) {
            mContext = c;
            this.dataList = dataList;
        }

        public int getCount() {
            return dataList.size();
        }

        public VIDEOLIST getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public View layout_frame;
            public TextView title;
            public ImageView img;
        }

        protected ImageLoader imageLoader = ImageLoader.getInstance();

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_search_item, null, false);
                viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final VIDEOLIST cat = getItem(position);
            if (cat != null)
            {
                viewHolder.title.setText(cat.title);
                imageLoader.displayImage(cat.titlePicurl, viewHolder.img, FarmingApp.options);

                viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, C02_AgrotechniqueVideoDetailActivity.class);
                        intent.putExtra("url",cat.video_url);
                        intent.putExtra("id",cat.video_id);
                        intent.putExtra("cycle",cat.cycleName);
                        mContext.startActivity(intent);
                    }

                });
            }
            return convertView;
        }
    }

    class B00_ExpertAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<EXPERTINFO> dataList;

        public B00_ExpertAdapter(Context c, ArrayList<EXPERTINFO> dataList) {
            mContext = c;
            this.dataList = dataList;
        }

        public int getCount() {
            return dataList.size();
        }

        public EXPERTINFO getItem(int position) {
            if (position >= 0 && position < dataList.size())
                return dataList.get(position);
            else
                return null;
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            public View layout_frame;
            public TextView title;
            public ImageView img;
        }

        protected ImageLoader imageLoader = ImageLoader.getInstance();

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_search_item, null, false);
                viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final EXPERTINFO cat = getItem(position);
            if (cat != null)
            {
                viewHolder.title.setText(cat.nick_name);
                imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options_head);

                viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, C01_ExpertDatailActivity.class);
                        intent.putExtra("id", cat.userId);
                        mContext.startActivity(intent);
                    }

                });
            }
            return convertView;
        }
    }

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.button_right:
				finish();
				break;
//			case R.id.layout_group:
//				intent = new Intent(this, B02_SearchGroupActivity.class);
//				intent.putExtra("keyword", edit_keyword.getText().toString());
//				startActivity(intent);
//				break;
//			case R.id.layout_activity:
//				intent = new Intent(this, B02_SearchActivityActivity.class);
//				intent.putExtra("keyword", edit_keyword.getText().toString());
//				startActivity(intent);
//				break;
//			case R.id.layout_route:
//				intent = new Intent(this, B02_SearchRouteActivity.class);
//				intent.putExtra("keyword", edit_keyword.getText().toString());
//				startActivity(intent);
//				break;
			case R.id.text_type:
				showSelectDialog();
				break;
		}
	}

	Dialog mMenuDialog;
	private void showSelectDialog() {
		// TODO Auto-generated method stub

		if (mMenuDialog == null)
		{
			LayoutInflater inflater = LayoutInflater.from(this);

			View view = inflater.inflate(R.layout.b00_select_search_menus, null);
			mMenuDialog = new Dialog(this, R.style.transparentFrameWindowStyle);
			mMenuDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			Window window = mMenuDialog.getWindow();
//			window.setWindowAnimations(R.style.Animation_Top_Dialog);
			window.setGravity(Gravity.LEFT | Gravity.TOP);
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = layout_search.getHeight();
			wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
			wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			mMenuDialog.onWindowAttributesChanged(wl);
			mMenuDialog.setCanceledOnTouchOutside(true);

//			View layout_cancel = view.findViewById(R.id.layout_cancel);
//			layout_cancel.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					mMenuDialog.dismiss();
//					mMenuDialog = null;
//				}
//			});

			View text_question, text_diagnosis, text_article, text_video, text_expert, text_all;
			text_question = view.findViewById(R.id.text_question);
			text_question.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
                    text_type.setText("疑问解答");
					search_type = "3";
					requestData();
				}
			});
			text_diagnosis = view.findViewById(R.id.text_diagnosis);
			text_diagnosis.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					text_type.setText("诊断库");
					search_type = "0";
					requestData();
				}

			});
			text_article = view.findViewById(R.id.text_article);
			text_article.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					text_type.setText("文章库");
					search_type = "5";
					requestData();
				}

			});
			text_video = view.findViewById(R.id.text_video);
			text_video.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					text_type.setText("视频库");
					search_type = "2";
					requestData();
				}

			});
			text_expert = view.findViewById(R.id.text_expert);
			text_expert.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mMenuDialog.dismiss();
					mMenuDialog = null;
					text_type.setText("专家");
					search_type = "4";
					requestData();
				}

			});
            text_all = view.findViewById(R.id.text_all);
            text_all.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMenuDialog.dismiss();
                    mMenuDialog = null;
                    text_type.setText("全部");
                    search_type = "";
                    requestData();
                }
            });
		}

		mMenuDialog.show();
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	public void closeKeyBoard() {
		edit_keyword.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.HOME_SEARCH)) {
			updateData();
		}
	}

	@Override
	public void onRefresh(int id) {
		requestData();
	}

	@Override
	public void onLoadMore(int id) {

	}
}
