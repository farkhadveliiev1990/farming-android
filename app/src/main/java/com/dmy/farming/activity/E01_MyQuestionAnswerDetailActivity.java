package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.dmy.farming.R;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

public class E01_MyQuestionAnswerDetailActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_question_answer_detail);
		initView();
	}

	XListView list_black;
	View null_pager;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		//null_pager = findViewById(R.id.null_pager);
		View more = findViewById(R.id.more);
		more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()){
					case R.id.more:
						showSelectDialog(view);
						break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}





	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}

	private void showSelectDialog(View view1) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.e01_my_question_answer_zuida, null);
		View zhuida = (View) view.findViewById(R.id.zhuida);
		zhuida.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(mContext, "button is pressed", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(E01_MyQuestionAnswerDetailActivity.this, E01_MyQuestionAnswerActivity.class);
				startActivity(intent);

			}
		});

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
		window.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.color_trans));

		window.showAsDropDown(view1);

	}


}