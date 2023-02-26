package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C01_ArticleAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<FARMARTICLE> dataList;

	public C01_ArticleAdapter(Context c, ArrayList<FARMARTICLE> dataList) {
		mContext = c;
		this.dataList = dataList;
	}

	public int getCount() {
		return dataList.size()+1;
	}

	public FARMARTICLE getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public TextView title,keyword,time,num;
		public ImageView close;
		public LinearLayout layout,layout_article_item;
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder  viewHolder1;
		if (convertView == null) {
			viewHolder1 = new ViewHolder();
            if(position+1==2) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_activity_agrotechnique_article_ad, null);
				viewHolder1.close = (ImageView) convertView.findViewById(R.id.close);
				viewHolder1.layout =(LinearLayout)convertView.findViewById(R.id.layout);
				viewHolder1.close.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						viewHolder1.layout.removeAllViews();
					}
				});
			}
            else{
                convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_activity_agrotechnique_article_item, null);
                viewHolder1.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder1.keyword = (TextView) convertView.findViewById(R.id.keyword);
                viewHolder1.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder1.num = (TextView) convertView.findViewById(R.id.num);
				viewHolder1.layout_article_item = (LinearLayout)convertView.findViewById(R.id.layout_article_item);
			}
			convertView.setTag(viewHolder1);
		}else {
			viewHolder1 = (ViewHolder) convertView.getTag();
		}
		if(position == 1){

		}else{
			if(position==0){
			 final 	FARMARTICLE cat = getItem(position);
				if (cat != null)
				{
					viewHolder1.title.setText(cat.title);
					String keywordname = "";
					for(int i =0;i< cat.keyWord.size();i++){
						keywordname += cat.keyWord.get(i).wordName+"  ";
					}
					viewHolder1.keyword.setText(keywordname);
					viewHolder1.time.setText(cat.publishTime);
					viewHolder1.num.setText(cat.pageView);
					viewHolder1.layout_article_item.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,C02_AgrotechniqueArticleDetailActivity.class);
							intent.putExtra("id", cat.id);
							intent.putExtra("cycle", cat.cycle);
							mContext.startActivity(intent);
						}
					});
				}
			}else {
				final	FARMARTICLE cat = getItem(position-1);
				if (cat != null)
				{
					viewHolder1.title.setText(cat.title);
					String keywordname = "";
					for(int i =0;i< cat.keyWord.size();i++){
						keywordname += cat.keyWord.get(i).wordName+"  ";
					}
					viewHolder1.keyword.setText(keywordname);
					viewHolder1.time.setText(cat.publishTime);
					viewHolder1.num.setText(cat.pageView);
					viewHolder1.layout_article_item.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,C02_AgrotechniqueArticleDetailActivity.class);
							intent.putExtra("id", cat.id);
							intent.putExtra("cycle", cat.cycle);
							mContext.startActivity(intent);
						}
					});
				}
			}
		}
		return convertView;
	}

}
