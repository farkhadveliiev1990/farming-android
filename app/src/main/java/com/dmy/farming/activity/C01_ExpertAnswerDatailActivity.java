package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_ExpertAnswerDeatilListItemAdapter;
import com.dmy.farming.adapter.C01_ExpertAnswerListItemAdapter;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

public class C01_ExpertAnswerDatailActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_question_answer);

		initView();

	}

	ListView list_answer;
	View null_pager;
//	C01_ExpertAnswerDeatilListItemAdapter listAdapter;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		View home = findViewById(R.id.home);
		home.setOnClickListener(this);
		View answer = findViewById(R.id.answer);
		answer.setOnClickListener(this);
		list_answer = (ListView)findViewById(R.id.list_answer);
		/*list_answer.setXListViewListener(this, 1);
		list_answer.setPullRefreshEnable(true);
		list_answer.setPullLoadEnable(false);*/

	/*	listAdapter = new C01_ExpertAnswerDeatilListItemAdapter(this);
		list_answer.setAdapter(listAdapter);*/

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);
	}

	Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.home:
				intent = new Intent(C01_ExpertAnswerDatailActivity.this,C01_ExpertDatailActivity.class);
				startActivity(intent);
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


}