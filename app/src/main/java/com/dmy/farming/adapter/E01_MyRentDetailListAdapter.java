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
import com.dmy.farming.api.data.chat.RENTLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class E01_MyRentDetailListAdapter extends BaseAdapter {

	Context mContext;
    List<RENTLIST> dataList;

	public Handler parentHandler;

	public E01_MyRentDetailListAdapter(Context context, List<RENTLIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {		
		return 1;
	}

	@Override
	public RENTLIST getItem(int position) {
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
		 
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_rent_detail, null);

			holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.phone = convertView.findViewById(R.id.phone);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

	 final 	RENTLIST item = getItem(position);
		if (item != null) {
			imageLoader.displayImage(item.imgUrl, holder.img_user, FarmingApp.options_head);
			holder.title.setText(item.title);
			holder.phone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.phone:
							call(item.link_phone);
							break;
					}
				}
			});
		}

		return convertView;
	}
	
	class ViewHolder {
		ImageView img_user;
		TextView title;
		View phone;
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
