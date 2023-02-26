package com.dmy.farming.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_MyQuestionAnswerAdapter;
import com.dmy.farming.fragment.E01_MyAnswerFragment;
import com.dmy.farming.fragment.E01_MyQuestionFragment;
import com.external.maxwin.view.XListView;
import android.support.v4.app.FragmentTransaction;
import org.bee.activity.BaseActivity;

public class E01_MyQuestionAnswerActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_question_answer_item);
		initView();

	}

	XListView list_black;
	View null_pager;
	E01_MyQuestionAnswerAdapter listAdapter1;

	TextView text_tab_0, text_tab_1;
	View img_line_0, img_line_1;


	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

//		list_black = (XListView) findViewById(R.id.list_black);
//
//		listAdapter1 = new E01_MyQuestionAnswerAdapter(this);
//		list_black.setAdapter(listAdapter1);
//
//		list_black.setXListViewListener(this, 1);
//		list_black.setPullRefreshEnable(true);
//		list_black.setPullLoadEnable(false);
//		list_black.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			Intent intent;
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				intent = new Intent( E01_MyQuestionAnswerActivity.this, E01_MyQuestionAnswerDetailActivity.class);
//				startActivity(intent);
//			}
//		});


		findViewById(R.id.layout_0).setOnClickListener(this);
		findViewById(R.id.layout_1).setOnClickListener(this);

		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView)findViewById(R.id.text_tab_1);

		img_line_0 = findViewById(R.id.img_line_0);
		img_line_1 = findViewById(R.id.img_line_1);

		clickTab(1);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_0:
				clickTab(0);
				break;
			case R.id.layout_1:
				clickTab(1);
				break;
		}
	}

	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
			tabSelected(cur_tab);
		}
	}

	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);

		if (cur_tab == 0) {
			img_line_0.setVisibility(View.VISIBLE);
			img_line_1.setVisibility(View.GONE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
		}
		else if (cur_tab == 1) {
			img_line_0.setVisibility(View.GONE);
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
		}

	}

	E01_MyQuestionFragment questionFragment;
	E01_MyAnswerFragment answerFragment;

	int cur_index = -1;
	public void tabSelected(int index)
	{
		if (cur_index == index) return;
		cur_index = index;

		if (index == 0)
		{
			answerFragment = new E01_MyAnswerFragment();
			FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
			localFragmentTransaction.replace(R.id.fragment_container, answerFragment, "myquestion_one");
			localFragmentTransaction.commitAllowingStateLoss();
		}
		else if (index == 1)
		{
			questionFragment = new E01_MyQuestionFragment();
			FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
			localFragmentTransaction.replace(R.id.fragment_container, questionFragment, "myquestion_two");
			localFragmentTransaction.commitAllowingStateLoss();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}


}