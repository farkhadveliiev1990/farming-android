package com.dmy.farming.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_ExpertDatailActivity;
import com.dmy.farming.activity.C01_ExpertListActivity;
import com.dmy.farming.activity.C02_ExpertVideoActivity;
import com.dmy.farming.adapter.E01_NoticeListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.EXPERTINFO;
import com.dmy.farming.api.model.ExpertModel;
import com.dmy.farming.api.model.NoticeModel;
import com.dmy.farming.api.model.RentResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class E01_HuDongFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;
	ExpertModel expertModel;
	/**
	 * 是否创建
	 */
	protected boolean isCreate = false;


	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		expertModel = new ExpertModel(getContext());
		expertModel.addResponseListener(this);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_my_collection_article,null);
		mActivity = (BaseActivity) getActivity();

		isCreate = true;

		initView(mainView);

		return mainView;
	}

	XListView list_black;
	View null_pager;
	ExpertListAdapter listAdapter;

	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);

//		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		//list_black.addFooterView(footerView);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	int page = 1;

	void requestData(){
		expertModel.getExpertList(AppConst.info_from, AppUtils.getCurrProvince(getContext()),AppUtils.getCurrCity(getContext()),AppUtils.getCurrDistrict(getContext()),"","",page+"",true);
	}

	private void updateExpert() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (expertModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new ExpertListAdapter(getContext(),expertModel.data.data);

				list_black.setAdapter(listAdapter);
			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == expertModel.data.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}
	}

	class ExpertListAdapter extends BaseAdapter {

		ArrayList<EXPERTINFO> dataList;
		Context mContext;
		ImageLoader imageLoader = ImageLoader.getInstance();

		public ExpertListAdapter(Context context,ArrayList<EXPERTINFO> datas) {
			mContext = context;
			dataList = datas;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public EXPERTINFO getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_expert_chat_item, null);
				viewHolder.layout = convertView.findViewById(R.id.layout);
				viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
				viewHolder.text_content = (TextView) convertView.findViewById(R.id.text_content);
				viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
				viewHolder.img_level = (ImageView) convertView.findViewById(R.id.img_level);


				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final EXPERTINFO cat = getItem(position);
			if (cat != null)
			{
				viewHolder.text_name.setText(cat.nick_name);
				if (cat.online_status == 1) {
					imageLoader.displayImage(cat.img_url,viewHolder.img_head, FarmingApp.options_head);
					// 头像变亮
					ColorMatrix matrix = new ColorMatrix();
					matrix.setSaturation(1);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
					viewHolder.img_head.setColorFilter(filter);

				} else {
					imageLoader.displayImage(cat.img_url,viewHolder.img_head, FarmingApp.options_head);
					// 头像变灰
					ColorMatrix matrix = new ColorMatrix();
					matrix.setSaturation(0);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
					viewHolder.img_head.setColorFilter(filter);
				}

				if(cat.levels.equals("0")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag1);
				}else if (cat.levels.equals("1")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag2);
				}else if (cat.levels.equals("2")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag3);
				}else if (cat.levels.equals("3")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag4);
				}

				viewHolder.layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getContext(),C01_ExpertDatailActivity.class);
						intent.putExtra("id",cat.userId);
						startActivity(intent);
					}
				});
			}

			return convertView;
		}



	}

	class ViewHolder {
		TextView text_name,text_content;
		ImageView img_head,img_level;
		View layout;
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
        {

		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isCreate) {
			//相当于Fragment的onResume
			requestData();
		} else {
			//相当于Fragment的onPause
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.EXPERT)) {
			updateExpert();
		}
	}

	@Override
	public void onDestroyView() {
		listAdapter = null;
		super.onDestroyView();
	}

	@Override
	public void onRefresh(int id) {
		requestData();
	}

	@Override
	public void onLoadMore(int id) {
		expertModel.getExpertListMore(AppConst.info_from, AppUtils.getCurrProvince(getContext()),AppUtils.getCurrCity(getContext()),AppUtils.getCurrDistrict(getContext()),"","",(++page)+ "",true);
	}
}
