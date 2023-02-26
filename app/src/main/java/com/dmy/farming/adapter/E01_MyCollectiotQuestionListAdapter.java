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
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E01_MyCollectiotQuestionListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
//	List<CHATUSER> friends;
   ArrayList<QUESTIONLIST> dataList;

	public Handler parentHandler;

	public E01_MyCollectiotQuestionListAdapter(Context context, ArrayList<QUESTIONLIST> dataList) {
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


	/*@Override
	public Object getItem(int position) {
		return null;
	}*/
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
			imageLoader.displayImage(item.imgUrl, viewHolder.img, FarmingApp.options);
			viewHolder.text_time.setText(item.createTime);
			viewHolder.text_position.setText(item.provience + item.city + item.district);
			viewHolder.text_content.setText(item.content);
			viewHolder.text_keyword.setText(item.keyWord);
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


	class ViewHolder {
		ImageView img_head,img,solve;
		TextView text_username,text_time,text_position,text_content,text_keyword,text_comment,text_browse,text_answer;
		View text_delete,layout_frame;
	}
}
