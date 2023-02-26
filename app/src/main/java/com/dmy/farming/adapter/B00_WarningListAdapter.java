package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.B01_WarnDetailActivity;
import com.dmy.farming.activity.C01_RentDetailActivity;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class B00_WarningListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
//	List<WARNLIST> friends;
	ArrayList<WARNLIST> dataList;

	public Handler parentHandler;

	public B00_WarningListAdapter(Context context, ArrayList<WARNLIST> dataList) {
		this.mContext = context;
//		this.friends = friends;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	public WARNLIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public void onClick(View v) {

	}

/*	@Override
	public Object getItem(int position) {
		return null;
	}*/

	private class ViewHolder {
		public View layout_frame;
		public TextView title,levels,content,time,detail,readstatus;
		public ImageView type;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		//convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_warning_item, null,false);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_warning_item, null,false);

			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			//viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.type = (ImageView) convertView.findViewById(R.id.type);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
			viewHolder.readstatus = (TextView) convertView.findViewById(R.id.readstatus);
			//viewHolder.address_detail = (TextView) convertView.findViewById(R.id.address_detail);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final WARNLIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			switch (cat.warning_type){
				case "虫害":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_pest));
					break;
				case "病害":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_disease));
					break;
				case "植保":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_zhibao));
					break;
				case "气象":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_weather));
					break;
				/*case "CHONGHAI_CODE":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_disease));
					break;*/

			}

			if(cat.content.length()>20){
				viewHolder.content.setText((Html.fromHtml(cat.content)));
			}else {
				viewHolder.content.setText(Html.fromHtml(cat.content));
			}
			if(cat.readStatus.equals("0")){
				viewHolder.readstatus.setText("[未读]");
				viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.text_red));
			}else{
				viewHolder.readstatus.setText("[已读]");
				viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.green));
			}
			viewHolder.time.setText(AppUtils.time(cat.publishTime));
		//	viewHolder.levels.setText(cat.levels);
			//imageLoader.displayImage(cat.img_url, viewHolder.img_route, FarmingApp.options_long_with_text);
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
				Intent intent = new Intent(mContext, B01_WarnDetailActivity.class);
				intent.putExtra("id", cat.id);
				mContext.startActivity(intent);

				}

			});
		}
		return convertView;
	}


}
