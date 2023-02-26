package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChooseQuestionTypeActivity extends BaseActivity implements View.OnClickListener,BusinessResponse {

	ImageView img_back;
	GridView gridView;
	DictionaryModel dictionaryModel;
	public ArrayList<DICTIONARY> question_type = new ArrayList<DICTIONARY>();
	TextView text_finish;
    String question = "";
	String questioncode="";
	int currentItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_question_type);

		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		gridView= (GridView)findViewById(R.id.gridView);

		text_finish = (TextView)findViewById(R.id.text_finish);
		text_finish.setOnClickListener(this);


		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		dictionaryModel.questionType();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_finish:
				 for (Map.Entry<Integer,Boolean> entry : map.entrySet()){
					 if (entry.getValue() == true){
						 question += question_type.get(entry.getKey()).name+ ",";
						 questioncode+=question_type.get(entry.getKey()).code+",";
					 }
				 }

				Intent intent = new Intent();
				if (questioncode.equals(""))
					errorMsg("请选择问题类型");
				else{
					intent.putExtra("question",question.substring(0,question.length()-1));
					intent.putExtra("questioncode",questioncode.substring(0,questioncode.length()-1));
					setResult(Activity.RESULT_OK,intent);
					finish();
				}
				break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	QuestionAdapter questionAdapter;

	private void updateData() {
		if (questionAdapter == null) {
			questionAdapter = new QuestionAdapter(this, question_type);
			gridView.setAdapter(questionAdapter);
		} else {
			questionAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.QUESTIONTYPE)){
			if (dictionaryModel.data.question_type.size() > 0)
			{
				question_type = dictionaryModel.data.question_type;
				updateData();
			}
		}

	}


	class ViewHolder {
		Button button;
		TextView content,id,text;

	}

	Map<Integer,Boolean> map ;
	class QuestionAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;
		boolean select = false;

		public QuestionAdapter(Context context,ArrayList<DICTIONARY> datas) {
			mContext = context;
			dataList = datas;
			map = new LinkedHashMap<>();
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public DICTIONARY getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_question_button, null);
				viewHolder.button = (Button) convertView.findViewById(R.id.btn);

				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			DICTIONARY cat = getItem(position);
			if (cat != null)
			{
				viewHolder.button.setText(cat.name);
				viewHolder.button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						select = !select;
						if (select) {
							viewHolder.button.setTextColor(getResources().getColor(R.color.green));
							viewHolder.button.setSelected(true);
							map.put(position,true);
						}else {
							viewHolder.button.setTextColor(getResources().getColor(R.color.black));
							viewHolder.button.setSelected(false);
							map.put(position,false);
						}
					}
				});
			}


			return convertView;
		}



	}




}


