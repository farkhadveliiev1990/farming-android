package com.dmy.farming.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class C01_ExpertAnswerListItemAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;

	public Handler parentHandler;

	public C01_ExpertAnswerListItemAdapter(Context context) {
		this.mContext = context;
//		this.friends = friends;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {		
		return 1;
	}

//	@Override
//	public CHATUSER getItem(int position) {
//		return friends.get(position);
//	}


	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();
	TextView tv;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_answer_list_item, null);

		/*final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_question_item, null);

			holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
			holder.text_name = (TextView) convertView.findViewById(R.id.text_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CHATUSER item = getItem(position);
	//	imageLoader.displayImage(item.user_logo, holder.img_user, FarmingApp.options_head);
		holder.text_name.setText(item.user_name);

		holder.text_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (parentHandler != null) {
					Message msg = new Message();
					msg.what = 1;
					msg.arg1 = position;
					parentHandler.sendMessage(msg);
				}
			}
		});
		*/
		return convertView;
	}
	
	class ViewHolder {
		ImageView img_user;
		TextView text_name;
		View text_delete;
	}
}
