package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.bee.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SEARCHHISTORY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.SearchModel;
import com.external.androidquery.callback.AjaxStatus;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class B01_SearchActivity extends BaseActivity implements OnClickListener {

	SearchModel searchModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b01_search);

		initView();

		searchModel = new SearchModel(this);
		searchModel.addResponseListener(this);

	}

	int page = 1;
	void request(){
		page = 1;
		searchModel.hotSearch(AppConst.info_from, SESSION.getInstance().uid,page);
		searchModel.searchHistory(AppConst.info_from, SESSION.getInstance().uid);
	}

	@Override
	protected void onResume() {
		super.onResume();
		request();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		searchModel.removeResponseListener(this);
	}

	EditText edit_keyword;
	ListView search_history;
	GridView grid_hotSearch;
	B01_HotSearchAdapter hotSearchAdapter;
	B01_SearchAdapter searchAdapter;
	TextView more;
	View layout_hotSearch,layout_searchHistory,text_history;

	void initView()
	{
		View button_right =  findViewById(R.id.button_right);
		button_right.setOnClickListener(this);

		layout_hotSearch = findViewById(R.id.layout_hotSearch);
		layout_searchHistory = findViewById(R.id.layout_searchHistory);
		text_history = findViewById(R.id.text_history);

		grid_hotSearch = (GridView) findViewById(R.id.grid_search);
		search_history= (ListView) findViewById(R.id.list_search_history);

		more = (TextView)findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				page = page+1;
				searchModel.hotSearch(AppConst.info_from, SESSION.getInstance().uid,page);
			}
		});

		findViewById(R.id.deleteAll).setOnClickListener(this);

		edit_keyword = (EditText) findViewById(R.id.edit_keyword);
		if(getIntent().getStringExtra("keyword")!=null){
			edit_keyword.setText(getIntent().getStringExtra("keyword"));
		}
		edit_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					keyword = edit_keyword.getText().toString();
					requestData(keyword);
				}
				return false;
			}
		});
	}

	String keyword = "";
	void requestData(String key) {

		if ("".equals(key))
			return;
		else {
			Intent intent = new Intent(this, B00_SearchActivity.class);
			intent.putExtra("keyword", key);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.button_right:
				finish();
				break;
			case R.id.deleteAll:
				searchModel.deleteSearchHistory(AppConst.info_from,"");
				break;

		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.HOME_SEARCHHISTORY)){
			updateHistory();
		}else if (url.contains(ApiInterface.HOME_HOTSEARCH)){
			updateHotSearch();
		}else if (url.contains(ApiInterface.HOME_DELETESEARCHHISTORY)){
			if (searchModel.lastStatus.error_code == 200) {
				searchModel.searchHistory(AppConst.info_from, SESSION.getInstance().uid);
				searchAdapter = null;
			}else
				errorMsg(searchModel.lastStatus.error_desc);
		}
	}

	private void updateHotSearch() {
		int size = searchModel.hotSearchList.size();
		if (size > 0) {
			layout_hotSearch.setVisibility(View.VISIBLE);
			if (hotSearchAdapter == null) {
				hotSearchAdapter = new B01_HotSearchAdapter(this,searchModel.hotSearchList);
				grid_hotSearch.setAdapter(hotSearchAdapter);
			} else {
				hotSearchAdapter.notifyDataSetChanged();
			}
		} else {
			hotSearchAdapter = null;
			page = 1;
			searchModel.hotSearch(AppConst.info_from, SESSION.getInstance().uid,page);
			//	layout_hotSearch.setVisibility(View.GONE);
		}
	}

	class B01_HotSearchAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<SEARCHHISTORY> dataList;

		public B01_HotSearchAdapter(Context c, ArrayList<SEARCHHISTORY> dataList) {
			mContext = c;
			this.dataList = dataList;
		}

		public int getCount() {
			return dataList.size();
		}

		public SEARCHHISTORY getItem(int position) {
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
			public TextView text_content;
		}

		protected ImageLoader imageLoader = ImageLoader.getInstance();

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_search_text, null, false);
				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
//			viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
				viewHolder.text_content = (TextView) convertView.findViewById(R.id.text_content);

//			ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final SEARCHHISTORY cat = getItem(position);
			if (cat != null)
			{
				viewHolder.text_content.setText(cat.search_word);
//			imageLoader.displayImage(cat.route_img, viewHolder.img_route, FarmingApp.options);
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						edit_keyword.setText(cat.search_word);
					}

				});
			}
			return convertView;
		}
	}

	private void updateHistory() {
		int size = searchModel.historyList.size();
		if (size > 0) {
			layout_searchHistory.setVisibility(View.VISIBLE);
			search_history.setVisibility(View.VISIBLE);
			text_history.setVisibility(View.GONE);
			if (searchAdapter == null) {
				searchAdapter = new B01_SearchAdapter(this,searchModel.historyList);
				search_history.setAdapter(searchAdapter);
			} else {
				searchAdapter.notifyDataSetChanged();
			}
		} else {
			layout_searchHistory.setVisibility(View.INVISIBLE);
			text_history.setVisibility(View.VISIBLE);
			search_history.setVisibility(View.INVISIBLE);
			searchAdapter = null;
		}
	}

	class B01_SearchAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<SEARCHHISTORY> dataList;

		public B01_SearchAdapter(Context c, ArrayList<SEARCHHISTORY> dataList) {
			mContext = c;
			this.dataList = dataList;
		}

		public int getCount() {
			return dataList.size();
		}

		public SEARCHHISTORY getItem(int position) {
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
			public TextView text_content,delete;
		}

		protected ImageLoader imageLoader = ImageLoader.getInstance();

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_search_history_item, null, false);
				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
//			viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
				viewHolder.text_content = (TextView) convertView.findViewById(R.id.text_content);
				viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);

//			ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final SEARCHHISTORY cat = getItem(position);
			if (cat != null)
			{
				viewHolder.text_content.setText(cat.search_word);
//			imageLoader.displayImage(cat.route_img, viewHolder.img_route, FarmingApp.options);
				viewHolder.delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						searchModel.deleteSearchHistory(AppConst.info_from,cat.id);
					}
				});
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						edit_keyword.setText(cat.search_word);
					}

				});
			}
			return convertView;
		}
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
}
