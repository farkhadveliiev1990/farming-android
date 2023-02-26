package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
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
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class D00_QuestionListAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<QUESTIONLIST> dataList;

	public Handler parentHandler;

	public D00_QuestionListAdapter(Context context,ArrayList<QUESTIONLIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {		
		return dataList.size();
	}

	@Override
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

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.d00_chat_item_list, null);
			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.text_username = (TextView) convertView.findViewById(R.id.text_username);
			viewHolder.text_time = (TextView) convertView.findViewById(R.id.text_time);
			viewHolder.text_position = (TextView) convertView.findViewById(R.id.text_position);
			viewHolder.text_content = (TextView) convertView.findViewById(R.id.text_content);
			viewHolder.text_keyword = (TextView) convertView.findViewById(R.id.text_keyword);
			viewHolder.text_comment = (TextView) convertView.findViewById(R.id.text_comment);
			viewHolder.text_browse = (TextView) convertView.findViewById(R.id.text_browse);
			viewHolder.text_answer = (TextView) convertView.findViewById(R.id.text_answer);
			viewHolder.solve = (ImageView) convertView.findViewById(R.id.solve);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final QUESTIONLIST item = getItem(position);
		if (item != null)
		{
			viewHolder.text_username.setText(item.userName);
			imageLoader.displayImage(item.headurl, viewHolder.img_head, FarmingApp.options_head);
			if (TextUtils.isEmpty(item.imgUrl))
				viewHolder.img.setVisibility(View.GONE);
			else {
				viewHolder.img.setVisibility(View.VISIBLE);
                if (item.imgUrl.contains(","))
                    imageLoader.displayImage(item.imgUrl.substring(0,item.imgUrl.indexOf(",")),viewHolder.img, FarmingApp.options);
                else
				    imageLoader.displayImage(item.imgUrl, viewHolder.img, FarmingApp.options);
			}
			viewHolder.text_time.setText(AppUtils.time(item.createTime));
			viewHolder.text_position.setText(item.city + item.district);
			if(item.content.length()>14){
				viewHolder.text_content.setText(item.content.substring(0,14)+"...");
			}else{
				viewHolder.text_content.setText(item.content);
			}
			String a[] = item.problemType.split(",");
			String keyword = "";
			for(int i = 0;i<a.length;i++){
				keyword =keyword+a[i]+"   ";
			}
			viewHolder.text_keyword.setText(keyword);
			viewHolder.text_comment.setText(item.commentNum);
			viewHolder.text_browse.setText(item.pageView +"");
			viewHolder.text_answer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, D01_QuestionDetailActivity.class);
					intent.putExtra("id", item.id);
					intent.putExtra("answer", true);
					intent.putExtra("type","questiontypr");
					mContext.startActivity(intent);
				}
			});

			if(item.accept.equals("1")){
				viewHolder.solve.setVisibility(View.VISIBLE);
			}else{
				viewHolder.solve.setVisibility(View.GONE);
			}
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					if (item != null)
					{
						Intent intent = new Intent(mContext, D01_QuestionDetailActivity.class);
						intent.putExtra("id", item.id);
						intent.putExtra("answer", false);
						intent.putExtra("type","questiontypr");
						mContext.startActivity(intent);
					}
				}

			});
		}

		return convertView;
	}
	
	class ViewHolder {
		ImageView img_head,img,solve;
		TextView text_username,text_time,text_position,text_content,text_keyword,text_comment,text_browse,text_answer;
		View text_delete,layout_frame;
	}
}
