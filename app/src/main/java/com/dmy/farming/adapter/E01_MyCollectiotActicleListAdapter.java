package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.C01_FarmNewsItemDetailActivity;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E01_MyCollectiotActicleListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
	ArrayList<ARTICLELIST> dataList;

	public Handler parentHandler;

	public E01_MyCollectiotActicleListAdapter(Context context,ArrayList<ARTICLELIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {
		return dataList.size();
	}

//	@Override
//	public CHATUSER getItem(int position) {
//		return friends.get(position);
//	}


	public ARTICLELIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder1;
		if (convertView == null) {
			viewHolder1 = new  ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_activity_agrotechnique_article_item, null);
				viewHolder1.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder1.keyword = (TextView) convertView.findViewById(R.id.keyword);
				viewHolder1.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder1.num = (TextView) convertView.findViewById(R.id.num);
				viewHolder1.layout_article_item = (LinearLayout)convertView.findViewById(R.id.layout_article_item);
			convertView.setTag(viewHolder1);
		}else {
			viewHolder1 = (ViewHolder) convertView.getTag();
		}
				final 	ARTICLELIST cat = getItem(position);
				if (cat != null)
				{
					viewHolder1.title.setText(cat.title);
					/*String keywordname = "";
					for(int i =0;i< cat.key_word.size();i++){
						keywordname += cat.key_word.get(i).wordName+"  ";
					}*/
					viewHolder1.keyword.setText(cat.key_word);
					viewHolder1.time.setText(cat.publish_time);
					viewHolder1.num.setText(cat.page_view);
					viewHolder1.layout_article_item.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,C01_FarmNewsItemDetailActivity.class);
							intent.putExtra("id", cat.id);
							mContext.startActivity(intent);
						}
					});
				}
		return convertView;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}

	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}


	private class ViewHolder {
		public TextView title,keyword,time,num;
		public ImageView close;
		public LinearLayout layout,layout_article_item;
	}
}
