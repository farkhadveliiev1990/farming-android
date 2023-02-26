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
import com.dmy.farming.activity.E01_MyQuestionActivity;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.view.SwipeMenuLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C01_ExpertAnswerDeatilListItemAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;
    ArrayList<QUESTIONLIST> dataList;


	public C01_ExpertAnswerDeatilListItemAdapter(Context context,ArrayList<QUESTIONLIST> dataList) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_answer_detail_list_item, null);
			viewHolder.layout_1 = convertView.findViewById(R.id.layout_1);
			viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.position = (TextView) convertView.findViewById(R.id.position);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.keyword = (TextView) convertView.findViewById(R.id.keyword);

			convertView.setTag(viewHolder);
		} else {
			viewHolder =(ViewHolder) convertView.getTag();
		}

		final QUESTIONLIST item = getItem(position);
		if (item != null)
		{
			viewHolder.nickname.setText(item.userName);
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
			viewHolder.time.setText(AppUtils.time(item.createTime));
			viewHolder.position.setText(item.city + item.district);
			if(item.content.length()>14){
				viewHolder.content.setText(item.content.substring(0,14)+"...");
			}else{
				viewHolder.content.setText(item.content);
			}
			/*String a[] = item.problemType.split(",");
			String keyword = "";
			for(int i = 0;i<a.length;i++){
				keyword =keyword+a[i]+ "&nbsp;&nbsp;&nbsp;"+"是对方是否是";
			}*/

			viewHolder.keyword.setText(item.problemType);
			viewHolder.layout_1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					if (item != null)
					{
						Intent intent = new Intent(mContext, D01_QuestionDetailActivity.class);
						intent.putExtra("id", item.id);
						intent.putExtra("type","myquestion");
						intent.putExtra("answer", false);
						mContext.startActivity(intent);
					}
				}

			});
		}

		return convertView;
	}
	
	class ViewHolder {
		ImageView img_head,img;
		TextView nickname,time,position,content,keyword;
		View layout_1;
	}
}
