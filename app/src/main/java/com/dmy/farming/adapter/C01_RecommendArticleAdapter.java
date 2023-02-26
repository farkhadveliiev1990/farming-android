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
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_FarmNewsItemDetailActivity;
import com.dmy.farming.api.data.ARTICLELIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class C01_RecommendArticleAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<ARTICLELIST> dataList;

	public Handler parentHandler;

	public C01_RecommendArticleAdapter(Context context, ArrayList<ARTICLELIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

	public ARTICLELIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public int getCount() {
		return dataList.size()+1;
	}

	private class ViewHolder {
		public View layout_frame;
		public ImageView img;
		public TextView title,time;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			if(position+1==2) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_article_item_ad, null);

			} else {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_recommend_article_item, null, false);

				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);

			}
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(position == 1){

		}else{
			final ARTICLELIST cat;
			if(position==0){
				cat = getItem(position);
			}else {
				cat = getItem(position-1);
			}

			if (cat != null)
			{
				viewHolder.title.setText(cat.title);

				imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options_small_with_text);
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						ARTICLELIST data = getItem(position);
						if (data != null)
						{
							Intent intent = new Intent(mContext, C01_FarmNewsItemDetailActivity.class);
							intent.putExtra("id", data.id);
							mContext.startActivity(intent);
						}
					}

				});

				viewHolder.time.setText(cat.publish_time);

			}

		}

		return convertView;
	}


}
