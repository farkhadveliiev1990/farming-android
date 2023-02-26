package com.dmy.farming.adapter;

import android.content.Context;
import android.os.Handler;
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
import com.nostra13.universalimageloader.core.ImageLoader;

public class E01_MyQuestionAnswerAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;

	public Handler parentHandler;

	public E01_MyQuestionAnswerAdapter(Context context) {
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


		convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_question_answer, null);

			/*View more = convertView.findViewById(R.id.more);
			more.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					switch (view.getId()){
						case R.id.more:
							showSelectDialog(view);
							break;
					}
				}
			});*/

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

	private void showSelectDialog(View view1) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(mContext).inflate(R.layout.e01_my_question_item_zuiwen, null);
		/*View more = (View) view.findViewById(R.id.more);
		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "button is pressed", Toast.LENGTH_SHORT).show();
			}
		});*/

		PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

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

		window.showAsDropDown(view1);

	}
}
