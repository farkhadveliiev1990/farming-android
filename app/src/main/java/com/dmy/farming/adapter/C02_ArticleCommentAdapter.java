package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.activity.C02_AgrotechniqueVideoDetailActivity;
import com.dmy.farming.activity.ReportActivity;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.model.AgrotechniqueArticleDetailModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C02_ArticleCommentAdapter extends BaseAdapter{

	public static final int VIEW_HEADER = 0;
	public static final int VIEW_MOMENT = 1;
	Context mContext;
	ArrayList<COMMENT> dataList;
//	private ArrayList<Moment> mList;
	AgrotechniqueArticleDetailModel agrotechniqueArticleDetailModel;
	int type; // 1: 文章 2: 视频
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public C02_ArticleCommentAdapter(Context c, ArrayList<COMMENT> dataList,int type) {
		mContext = c;
		this.dataList = dataList;
		this.type = type;
	}

	@Override
	public int getViewTypeCount() {
		return  dataList.size();
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
		public ImageView point,like,img_head;
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
			viewHolder1.img_head = (ImageView) convertView.findViewById(R.id.img_head);
			//viewHolder1.layout_article_item = (LinearLayout)convertView.findViewById(R.id.layout_article_item);

			convertView.setTag(viewHolder1);
		}else {
			viewHolder1 = (ViewHolder) convertView.getTag();
		}
			 final 	COMMENT cat = getItem(position);
				if (cat != null)
				{
//					imageLoader.displayImage(cat.,viewHolder1.img_head, FarmingApp.options_head);
					viewHolder1.name.setText(cat.commentName);
					viewHolder1.content.setText(cat.content);
					viewHolder1.time.setText(cat.time);
					viewHolder1.num.setText(cat.reply_num);
					viewHolder1.like_num.setText(cat.likeNum);
					viewHolder1.id.setText(cat.id);
					viewHolder1.point.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							showSelectDialog(v,viewHolder1.id.getText().toString(),cat);
						}
					});
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


	PopupWindow window;
	private void showSelectDialog(View view1, final String id, final COMMENT com) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_list_dialog, null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_question_type);
		if(com.commentId.equals(SESSION.getInstance().uid)){
			View item = LayoutInflater.from(mContext).inflate(R.layout.c01_article_comment, null);
			TextView textview = (TextView)item.findViewById(R.id.text_name);
			textview.setText("删除");
			ImageView textimg = (ImageView)item.findViewById(R.id.text_img);
			textimg.setBackground(mContext.getResources().getDrawable(R.drawable.ser_icon_delete));
			layout.addView(item);
			textview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteComment(id);
					if (window != null)
						window.dismiss();
				}
			});
		}else{
			View item = LayoutInflater.from(mContext).inflate(R.layout.c01_article_comment, null);
			TextView textview = (TextView)item.findViewById(R.id.text_name);
			textview.setText("举报");
			ImageView textimg = (ImageView)item.findViewById(R.id.text_img);
			textimg.setBackground(mContext.getResources().getDrawable(R.drawable.icon_report));
			layout.addView(item);
			textview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,ReportActivity.class);
					intent.putExtra("id",id);
					intent.putExtra("from",type);
					intent.putExtra("iscomment","0");
					intent.putExtra("about_user",com.commentId);
					mContext.startActivity(intent);
					if (window != null)
						window.dismiss();
				}
			});
		}

		View item = LayoutInflater.from(mContext).inflate(R.layout.c01_article_comment, null);
		TextView textview = (TextView)item.findViewById(R.id.text_name);
		textview.setText("回复");
		ImageView textimg = (ImageView)item.findViewById(R.id.text_img);
		textimg.setBackground(mContext.getResources().getDrawable(R.drawable.icon_report));
		layout.addView(item);
		textview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//answerComment(com);
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
		if (type == 1)
			C02_AgrotechniqueArticleDetailActivity.deleteComment(id);
		else
			C02_AgrotechniqueVideoDetailActivity.deleteComment(id);
	}
	private void like(String id,String byreplyId) {
		if (type == 1)
			C02_AgrotechniqueArticleDetailActivity.like(id,byreplyId);
		else
			C02_AgrotechniqueVideoDetailActivity.like(id,byreplyId);
	}
	private void cancellike(String id) {
		if (type == 1)
			C02_AgrotechniqueArticleDetailActivity.cancellike(id);
		else
			C02_AgrotechniqueVideoDetailActivity.cancellike(id);
	}

/*	private void answerComment(COMMENT com) {

		if (type == 1)
		//	C02_AgrotechniqueArticleDetailActivity.answerComment(com.id,com.commentId,"123456");
		else
			((C02_AgrotechniqueVideoDetailActivity)mContext).answerComment(com.id,com.commentId,"123456");
	}*/

}
