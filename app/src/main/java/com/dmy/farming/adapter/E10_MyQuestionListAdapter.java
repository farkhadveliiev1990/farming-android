package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.B01_WarnDetailActivity;
import com.dmy.farming.activity.E01_MyQuestionItemDetailActivity;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.api.data.chat.CHATUSER;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class E10_MyQuestionListAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;
	ArrayList<QUESTIONLIST> dataList;


	public Handler parentHandler;

	public E10_MyQuestionListAdapter(Context context, ArrayList<QUESTIONLIST> dataList) {
		this.mContext = context;
//		this.friends = friends;
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

	public QUESTIONLIST getItem(int position) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		//convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_warning_item, null,false);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_question_answer, null,false);

			viewHolder.mychat = convertView.findViewById(R.id.mychat);
			//viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.position = (TextView) convertView.findViewById(R.id.position);
			viewHolder.num = (TextView) convertView.findViewById(R.id.num);
			viewHolder.read = (TextView) convertView.findViewById(R.id.read);
			//viewHolder.address_detail = (TextView) convertView.findViewById(R.id.address_detail);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		QUESTIONLIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.name.setText(cat.userName);
			viewHolder.position.setText(cat.provience);
			viewHolder.content.setText(cat.content);
			viewHolder.time.setText(cat.createTime);
			viewHolder.num.setText(cat.commentNum);
			viewHolder.read.setText(cat.pageView);
			//	viewHolder.levels.setText(cat.levels);
			//imageLoader.displayImage(cat.img_url, viewHolder.img_route, FarmingApp.options_long_with_text);
			viewHolder.mychat.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					QUESTIONLIST data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, E01_MyQuestionItemDetailActivity.class);

						intent.putExtra("id", data.id);
					/*	intent.putExtra("price", data.price);
						intent.putExtra("time", data.publish_time);
						intent.putExtra("address_detail",data.address_details);
						intent.putExtra("content",data.content);*/
						mContext.startActivity(intent);
					}
				}

			});
		}
		return convertView;
	}
	
	class ViewHolder {
		ImageView img_user;
		TextView text_name;
		View mychat;
		public TextView name,time,position,content,keyword,num,read;
	}
}
