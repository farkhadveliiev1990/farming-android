package com.dmy.farming.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.activity.C02_AgrotechniqueVideoDetailActivity;
import com.dmy.farming.activity.C03_DiagmpsticLibDetailActivity;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.model.AgrotechniqueArticleDetailModel;

import java.util.ArrayList;

public class C03_DiagnosticCommentAdapter extends BaseAdapter{

	public static final int VIEW_HEADER = 0;
	public static final int VIEW_MOMENT = 1;
	Context mContext;
	ArrayList<COMMENT> dataList;

	public C03_DiagnosticCommentAdapter(Context c, ArrayList<COMMENT> dataList) {
		mContext = c;
		this.dataList = dataList;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
	}

	public int getCount() {
		return dataList.size();
	}

	public COMMENT getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public TextView name,time,content,num,like_num,id;
		public ImageView point,like;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final  ViewHolder  viewHolder1;
		if (convertView == null) {
			viewHolder1 = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.c02_comment_item, null,false);
				viewHolder1.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder1.content = (TextView) convertView.findViewById(R.id.content);
				viewHolder1.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder1.num = (TextView) convertView.findViewById(R.id.num);
				viewHolder1.like_num = (TextView) convertView.findViewById(R.id.like_num);
				viewHolder1.point = (ImageView) convertView.findViewById(R.id.point);
				viewHolder1.id = (TextView) convertView.findViewById(R.id.id);
				viewHolder1.like =(ImageView) convertView.findViewById(R.id.like);
				//viewHolder1.layout_article_item = (LinearLayout)convertView.findViewById(R.id.layout_article_item);
			viewHolder1.point.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showSelectDialog(v,viewHolder1.id.getText().toString());
				}
			});
			convertView.setTag(viewHolder1);
		}else {
			viewHolder1 = (ViewHolder) convertView.getTag();
		}
			 final 	COMMENT cat = getItem(position);
				if (cat != null)
				{
					viewHolder1.name.setText(cat.commentName);
					viewHolder1.content.setText(cat.content);
					viewHolder1.time.setText(cat.time);
					viewHolder1.num.setText(cat.reply_num);
					viewHolder1.like_num.setText(cat.likeNum);
					viewHolder1.id.setText(cat.id);
					if(cat.islike==1){
						viewHolder1.like.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
					}else{
						viewHolder1.like.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icon_default_fabulous));
					}
					viewHolder1.like.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(cat.islike==1){
								cancellike(viewHolder1.id.getText().toString());
							}else{
								like(viewHolder1.id.getText().toString(),cat.byreplyId);
							}

						}
					});
				}
		return convertView;
	}

	private void like(String id,String byreplyId) {
		C03_DiagmpsticLibDetailActivity.like(id,byreplyId);
	}
	private void cancellike(String id) {
		C03_DiagmpsticLibDetailActivity.cancellike(id);
	}



	PopupWindow window;
	private void showSelectDialog(View view1,final String id) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(mContext).inflate(R.layout.c02_articlecomment_item_deleted, null);
		View deleted = (View) view.findViewById(R.id.layout_deleted);
		deleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(mContext, "button is pressed", Toast.LENGTH_SHORT).show();
				deleteComment(id);
				if (window != null)
					window.dismiss();
			}
		});

		window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
		//测量view 注意这里，如果没有测量  ，下面的popupHeight高度为-2  ,因为LinearLayout.LayoutParams.WRAP_CONTENT这句自适应造成的
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
		int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
		int[] location = new int[2];
		window.setTouchable(true);
		window.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				//这里如果返回true的话，touch事件将被拦截
				//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
		window.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.color_white));

		view1.getLocationOnScreen(location);

		//这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
		window.showAtLocation(view1, Gravity.NO_GRAVITY, (location[0] + view1.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

	}

	private void deleteComment(String id) {
		C03_DiagmpsticLibDetailActivity.deleteComment(id);
	}

}
