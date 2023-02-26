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

import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_FarmNewsItemDetailActivity;
import com.dmy.farming.activity.C01_SellDetailActivity;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.chat.SALELIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class C01_ArticleListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
	ArrayList<ARTICLELIST> dataList;

	public Handler parentHandler;

	public C01_ArticleListAdapter(Context context, ArrayList<ARTICLELIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

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
		public ImageView img_type,img;
		public TextView title,time,browse_num;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	String pattern ="yyyy-MM-dd HH:mm:ss";
	String display = "";
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			if(position+1==2) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_article_item_ad, null);

			} else {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_article_item, null, false);

				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
				viewHolder.img_type = (ImageView) convertView.findViewById(R.id.img_type);
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.browse_num = (TextView) convertView.findViewById(R.id.browse_num);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

			}
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(position == 1){

		}else{
			if(position==0){
				final ARTICLELIST cat  = getItem(position);
				viewHolder.title.setText(cat.title);

				if(cat.level.equals("0")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag1);
				}else if (cat.level.equals("1")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag2);
				}else if (cat.level.equals("2")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag3);
				}else if (cat.level.equals("3")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag4);
				}
				imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options_small_with_text);
				viewHolder.time.setText(AppUtils.time(cat.publish_time));
				viewHolder.browse_num.setText(cat.page_view);
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//ARTICLELIST data = getItem(position);
						if (cat != null)
						{
							Intent intent = new Intent(mContext, C01_FarmNewsItemDetailActivity.class);
							intent.putExtra("id", cat.id);
							mContext.startActivity(intent);
						}
					}

				});
			}else {
				final ARTICLELIST cat=getItem(position-1);
				viewHolder.title.setText(cat.title);
				if(cat.level.equals("0")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag1);
				}else if (cat.level.equals("1")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag2);
				}else if (cat.level.equals("2")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag3);
				}else if (cat.level.equals("3")){
					viewHolder.img_type.setBackgroundResource(R.drawable.n_icon_tag4);
				}
				imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options_small_with_text);
				viewHolder.time.setText(AppUtils.time(cat.publish_time));
				viewHolder.browse_num.setText(cat.page_view);
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						//ARTICLELIST data = getItem(position);
						if (cat != null)
						{
							Intent intent = new Intent(mContext, C01_FarmNewsItemDetailActivity.class);
							intent.putExtra("id", cat.id);
							mContext.startActivity(intent);
						}
					}

				});
			}
			}

		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.phone:
				call("1111111");
				break;

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
}
