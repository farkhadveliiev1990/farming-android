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

import com.dmy.farming.R;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.COLLECTION;
import com.dmy.farming.api.data.FINDHELPLIST;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.PAGINATED;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E01_MyCollectionExpertListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
//	List<CHATUSER> friends;
    ArrayList<FARMARTICLE> dataList;
	public PAGINATED paginated = new PAGINATED();

	public Handler parentHandler;

	public E01_MyCollectionExpertListAdapter(Context context, ArrayList<FARMARTICLE> dataList) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_collection_article_list_item, null);

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
		ImageView img_user;
		TextView text_name;
		View text_delete;
	}
}
