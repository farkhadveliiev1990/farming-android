package com.dmy.farming.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.adapter.E01_MyFollowListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.view.choosecrop.ChooseText;
import com.dmy.farming.view.choosecrop.OnChooseEditTextListener;
import com.dmy.farming.view.choosecrop.OnDeleteEditTextListener;
import com.external.androidquery.callback.AjaxStatus;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;
import com.dmy.farming.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChooseCropActivity extends BaseActivity implements View.OnClickListener,BusinessResponse {
	ChooseText chooseEditText;
	ImageView img_back;
	GridView gridView;
	ListView list_crop;
	DictionaryModel dictionaryModel;
	CropTypeAdapter cropTypeAdapter;
	public ArrayList<DICTIONARY> crop_type = new ArrayList<DICTIONARY>();
	public ArrayList<DICTIONARY> crop_sub_type = new ArrayList<DICTIONARY>();
	int currentItem = 0;
	Map<String,String> attentCrop = new LinkedHashMap<>();
	String code;
    ArrayList<String> attentname = new ArrayList<>();
    ArrayList<String> attentcode = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_crop);
		chooseEditText = (ChooseText) findViewById(R.id.chooseedittext);

        attentname = getIntent().getStringArrayListExtra("attentname");
        attentcode = getIntent().getStringArrayListExtra("attentcode");
        for (int i=0;i<attentname.size();i++) {
            chooseEditText.addItem(attentname.get(i));
            attentCrop.put(attentname.get(i), attentcode.get(i));
        }

		list_crop = (ListView) findViewById(R.id.listview_crop);

		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		gridView= (GridView)findViewById(R.id.GridView1);

		findViewById(R.id.text_sure).setOnClickListener(this);

		chooseEditText.setOnChooseEditTextListener(new OnChooseEditTextListener() {
			@Override
			public void onTextChangeed(String text) {
                String s = text;

			}
		});

		chooseEditText.setOnDeleteEditTextListener(new OnDeleteEditTextListener() {
			@Override
			public void onTextDeleted(String text) {
				attentCrop.remove(text);
				requestData(code, true);
			}
		});

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		dictionaryModel.cropType(true);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_sure:
				if (attentCrop.size() > 6){
					errorMsg("最多选择6个作物");
					return;
				}
				String attention = "";
				for (Map.Entry entry:attentCrop.entrySet()){
					attention += entry.getValue() + ",";
				}
				if (!"".equals(attention)) {
					dictionaryModel.saveAttention(AppConst.info_from, attention, false);

				}
                else
                    errorMsg("请选择至少一个作物");
				break;

		}
	}

	@Override
	protected void onResume() {
		//updateData();
		//requestData(true);
		super.onResume();

	}

	CropSubAdapter cropSubAdapter;

	private void updateData() {
			if (cropSubAdapter == null) {
				cropSubAdapter = new CropSubAdapter(this, crop_sub_type);
				gridView.setAdapter(cropSubAdapter);
			//	cropSubAdapter.notifyDataSetChanged();
			} else {
				cropSubAdapter.notifyDataSetChanged();
			}
	}

	public void requestData(String code_type,boolean bShow) {
		dictionaryModel.cropsubType(AppConst.info_from,code_type,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
			if (url.contains(ApiInterface.CROPTYPE)){
				if (dictionaryModel.data.crop_type.size() > 0)
				{
					crop_type = dictionaryModel.data.crop_type;
					updateCrop();
				}
			}else if (url.contains(ApiInterface.CROPSUB)){
				if (dictionaryModel.data.crop_sub_type.size() > 0)
				{
					crop_sub_type = dictionaryModel.data.crop_sub_type;
				}
				updateData();

			}else if (url.contains(ApiInterface.SAVEATTENTION)){
				if (dictionaryModel.lastStatus.error_code == 200){
					setResult(RESULT_OK);
					finish();
				}
				else
					errorMsg(dictionaryModel.lastStatus.error_desc);
			}

	}

	private void updateCrop() {
		if (dictionaryModel.data.crop_type.size() >= 0) {
			if (cropTypeAdapter == null) {
				cropTypeAdapter = new CropTypeAdapter(this, crop_type);
				list_crop.setAdapter(cropTypeAdapter);
				list_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						currentItem = position; //重新赋值
						cropSubAdapter = null;
						cropTypeAdapter.notifyDataSetChanged(); //通知adapter更新
					}
				});

			} else {
				cropTypeAdapter.notifyDataSetChanged();
			}
		}
		else {
			cropTypeAdapter = null;
			//gridView.setAdapter(null);
		}
	}


	class CropTypeAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;


		public CropTypeAdapter(Context context,ArrayList<DICTIONARY> datas) {
			mContext = context;
			dataList = datas;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
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


		String id;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.crop_button, null);
//				viewHolder.button = (Button) convertView.findViewById(R.id.btn);
				viewHolder.text = (TextView) convertView.findViewById(R.id.btn);
			//	viewHolder.id = (TextView) convertView.findViewById(R.id.id);

				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.text.setText(dataList.get(position).name);
		//	viewHolder.button.setSelected(false);
			//viewHolder.id.setText(dataList.get(position).code);

			if (position == currentItem) {
				viewHolder.text.setTextColor(getResources().getColor(R.color.green));
				code = dataList.get(position).code;
				requestData(code, true);
			}else {
				viewHolder.text.setTextColor(getResources().getColor(R.color.text_grey));
			//	viewHolder.text.setEnabled(false);
			}

			/*final DICTIONARY currentItem = getItem(position);
	 		  viewHolder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					id = viewHolder.id.getText().toString();
					requestData(id,true);
				//	setCurCheckItem(position);
					*//*if (currentItem.isCheck()){
						//	parent.setBackgroundColor(Color.parseColor("#3F51B5"));
//						viewHolder.button.setTextColor(getResources().getColor(R.color.green));
						viewHolder.button.setSelected(true);
					}else{
//						viewHolder.button.setTextColor(getResources().getColor(R.color.text_grey));
						viewHolder.button.setSelected(false);
					}*//*
				}
			});*/
			return convertView;
		}
		/**
		 * 设置当前选中的是那个item
		 * @param index
		 */
		public void setCurCheckItem(int index) {
			DICTIONARY currentItem = getItem(index);
			currentItem.setCheck(true);
		}


	}

	class ViewHolder {
		Button button;
		TextView content,id,text;

	}

	class ViewHolder1 {
		Button button;
		TextView content,id,text;

	}

	class CropSubAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;

		public CropSubAdapter(Context context,ArrayList<DICTIONARY> datas) {
			mContext = context;
			dataList = datas;
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

			final ViewHolder1 viewHolder1;
			if (convertView == null) {
				viewHolder1 = new ViewHolder1();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_crop_button, null);
				viewHolder1.content = (TextView) convertView.findViewById(R.id.content);

				convertView.setTag(viewHolder1);
			}else {
				viewHolder1 = (ViewHolder1) convertView.getTag();
			}

			final DICTIONARY cat = getItem(position);
			if (cat != null)
			{
				viewHolder1.content.setText(cat.name);
				String tag_first = chooseEditText.getText();
				if(tag_first != null) {
					if (tag_first.contains(cat.name)) {
						viewHolder1.content.setBackgroundResource(R.drawable.green);
						viewHolder1.content.setTextColor(getResources().getColor(R.color.white));
						viewHolder1.content.setEnabled(false);
					}else {
						viewHolder1.content.setBackgroundResource(R.drawable.gray);
						viewHolder1.content.setTextColor(getResources().getColor(R.color.text_grey));
						viewHolder1.content.setEnabled(true);
					}
				}
			}

			viewHolder1.content.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseEditText.addItem(viewHolder1.content.getText().toString());
					viewHolder1.content.setBackgroundResource(R.drawable.green);
					viewHolder1.content.setTextColor(getResources().getColor(R.color.white));
					viewHolder1.content.setEnabled(false);

					attentCrop.put(cat.name,cat.code);
				}
			});
			return convertView;
		}



	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
	}

}


