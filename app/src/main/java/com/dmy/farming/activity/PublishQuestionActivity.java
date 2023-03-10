package com.dmy.farming.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.illegalsRequest;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.IllegalModel;
import com.dmy.farming.api.model.PublishModel;
import com.dmy.farming.api.publishQuestionRequest;
import com.dmy.farming.photopicker.PhotoPickerActivity;
import com.dmy.farming.photopicker.PhotoPreviewActivity;
import com.dmy.farming.photopicker.SelectModel;
import com.dmy.farming.photopicker.intent.PhotoPickerIntent;
import com.dmy.farming.photopicker.intent.PhotoPreviewIntent;
import com.dmy.farming.view.JsonParser;
import com.dmy.farming.view.addressselector.BottomDialog;
import com.dmy.farming.view.addressselector.OnAddressSelectedListener;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.raizlabs.android.dbflow.sql.language.Condition;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PublishQuestionActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse, OnAddressSelectedListener {

	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private SpeechRecognizer mIat;
	// ????????????
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	int ret = 0; // ?????????????????????
	private boolean mTranslateEnable = false;
	private String str_lang = "mandarin";
	private Toast mToast;
	private int count = 0;
	PublishModel publishModel;
	CommonModel commonModel;
	String imgurl = "";
	IllegalModel illegalModel;
	illegalsRequest illrequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_push);

		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

		request = new publishQuestionRequest();
		illrequest = new illegalsRequest();

		initView();

		publishModel = new PublishModel(this);
		publishModel.addResponseListener(this);
		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);
		illegalModel = new IllegalModel(this);
		illegalModel.addResponseListener(this);
		illrequest.info_from = AppConst.info_from;
		illegalModel.getillegas(illrequest);
	}

	EditText mResultText;
	ImageView img_voice_recording_status;
	TextView text_crop,text_question,text_position;
	GridView gridView;
	GridAdapter gridAdapter;
	ArrayList<String> imagePaths = new ArrayList<>();
	private static final int REQUEST_CAMERA_CODE = 10;
	private static final int REQUEST_PREVIEW_CODE = 20;
	BottomDialog positionDialog;
	String province = "",city = "",county = "";
	private static final int REQUEST_CROP = 1;
	private static final int REQUEST_QUESTION = 2;
	publishQuestionRequest request;
	String crop_code;
	String questioncode;
	private List<String> list = new ArrayList<>();
	Boolean flag = false;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

        findViewById(R.id.img_voice_to_text).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);

		text_crop = (TextView) findViewById(R.id.text_crop);
		text_crop.setOnClickListener(this);
//		text_crop.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ArrayList<String> datalist = new ArrayList<String>();
//				for (DICTIONARY crop:crop_type){
//					datalist.add(crop.name);
//				}
//				showDialog(v,"??????????????????",datalist);
//			}
//		});

		text_question = (TextView) findViewById(R.id.text_question);
		text_question.setOnClickListener(this);
//		text_question.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ArrayList<String> datalist = new ArrayList<String>();
//				for (DICTIONARY question : question_type){
//					datalist.add(question.name);
//				}
//				showMultiChoiceDialog(datalist);
//			}
//		});

		text_position = (TextView) findViewById(R.id.text_position);
		text_position.setOnClickListener(this);
		text_position.setText(AppUtils.getFullAddr(this).length() > 2 ? AppUtils.getFullAddr(this).substring(2):AppUtils.getFullAddr(this));

		mResultText = (EditText) findViewById(R.id.result);

		img_voice_recording_status = (ImageView)findViewById(R.id.img_voice_recording_status);
		img_voice_recording_status.setVisibility(View.GONE);

//		gridView = (GridView) findViewById(R.id.gridView);
		gridView = (GridView) findViewById(R.id.grid_question_img);

		int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
		cols = cols < 3 ? 3 : cols;
		gridView.setNumColumns(cols);

		// preview
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String imgs = (String) parent.getItemAtPosition(position);
				if ("000000".equals(imgs) ){
					PhotoPickerIntent intent = new PhotoPickerIntent(PublishQuestionActivity.this);
					intent.setSelectModel(SelectModel.MULTI);
					intent.setShowCarema(true); // ??????????????????
					intent.setMaxTotal(9); // ????????????????????????????????????9
					intent.setSelectedPaths(imagePaths); // ??????????????????????????? ????????????????????????
					startActivityForResult(intent, REQUEST_CAMERA_CODE);
				}else{
					PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishQuestionActivity.this);
					intent.setCurrentItem(position);
					ArrayList<String> imgurls = new ArrayList<String>();
					for (String img:imagePaths){
						if (!"000000".equals(img))
							imgurls.add(img);
					}
					intent.setPhotoPaths(imgurls);
					startActivityForResult(intent, REQUEST_PREVIEW_CODE);
				}
			}
		});
		imagePaths.add("000000");
		gridAdapter = new GridAdapter(imagePaths);
		gridView.setAdapter(gridAdapter);

		// ??????????????????UI????????????
		// ??????SpeechRecognizer????????????????????????????????????????????????
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);



	}



	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.img_voice_to_text:
				btnVoice();
				break;
			case R.id.text_crop:
				intent = new Intent(this,C04_ChooseCropActivity.class);
				startActivityForResult(intent,REQUEST_CROP);
				break;
			case R.id.text_question:
				intent = new Intent(this,ChooseQuestionTypeActivity.class);
				startActivityForResult(intent,REQUEST_QUESTION);
				break;
			case R.id.text_position:
//				positionDialog = new BottomDialog(PublishQuestionActivity.this);
//				positionDialog.setOnAddressSelectedListener(PublishQuestionActivity.this);
//				positionDialog.show();
				break;
			case R.id.btn_submit:
				List<String> imgurl = new ArrayList();
				for (String img: imagePaths) {
					if (!"000000".equals(img))
						imgurl.add(img);
				}
				if (imgurl.size() > 0) {
					commonModel.uploadMultiFile(imgurl);
				}else {
					submit();
				}
				break;
		}
	}


	private void showDialog(final View v, String title, final ArrayList<String> datalist) {
		AlertDialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(PublishQuestionActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(PublishQuestionActivity.this).inflate(R.layout.dialog,null);
		builder.setView(view);
		((TextView)view.findViewById(R.id.text_title)).setText(title);
		ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(PublishQuestionActivity.this,datalist);
		listview.setAdapter(adapter);
		final AlertDialog finalDialog = dialog;
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (v == text_crop)
					text_crop.setText(datalist.get(position));
				else if (v == text_question)
					text_question.setText(datalist.get(position));
				finalDialog.dismiss();
			}
		});

		dialog = builder.create();

		Window dialogWindow = dialog.getWindow();

		dialog.show();

		Display d = getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.gravity = Gravity.CENTER;
		p.width = (int)(d.getWidth()*0.8);

		dialogWindow.setAttributes(p);
	}

	ArrayList<Boolean> yourChoices = new ArrayList<>();
	private void showMultiChoiceDialog(ArrayList<String> datas) {
		int size = datas.size();
		final String[] items = datas.toArray(new String[size]);
		// ????????????????????????????????????false??????????????????
		final boolean initChoiceSets[] = new boolean[size];
		for (int i = 0; i < size; i++) {
			initChoiceSets[i]= false;
		}
		yourChoices.clear();

		TextView title = new TextView(this);
		title.setText("??????????????????");
		title.setPadding(10,30, 10, 30);
		title.setGravity(Gravity.CENTER);
		title.setTextSize(18);

		AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(PublishQuestionActivity.this,R.style.myDialog);
		multiChoiceDialog.setCustomTitle(title);
		multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {

					}
				});
		multiChoiceDialog.setPositiveButton("??????",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						int size = yourChoices.size();
						int size = initChoiceSets.length;
						String str = "";
						for (int i = 0; i < size; i++) {
							if (initChoiceSets[i])
								str += items[i] + ",";
						}
						text_question.setText(str.substring(0,str.length()-1));
					}
				});
		AlertDialog dialog = multiChoiceDialog.create();
		dialog.show();

		int divierId = dialog.getContext().getResources().getIdentifier("android:id/titleDivider",null,null);
		View divider = dialog.findViewById(divierId);
		divider.setBackgroundColor(getResources().getColor(R.color.line_grey));
		divider.setMinimumHeight(1);

		Window dialogWindow = dialog.getWindow();
		Display d = getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.gravity = Gravity.CENTER;
		p.width = (int)(d.getWidth()*0.8);
		dialogWindow.setAttributes(p);

	}

	private void submit() {
		String crop = text_crop.getText().toString();
		String question = text_question.getText().toString();
		String content = mResultText.getText().toString();
		String position = text_position.getText().toString();

		if (TextUtils.isEmpty(crop))
			errorMsg("?????????????????????");
		else if (TextUtils.isEmpty(question))
			errorMsg("?????????????????????");
		else if (TextUtils.isEmpty(content))
			errorMsg("???????????????");
		else if (content.length() < 5 )
			errorMsg("????????????????????????5??????");
		else if (content.length() > 300 )
			errorMsg("????????????????????????300??????");
//		else if (TextUtils.isEmpty(position))
//			errorMsg("???????????????");
		else {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = formater.format(new Date());
			request.info_from = AppConst.info_from;
			request.user_phone = SESSION.getInstance().sid;
			request.crerate_time = date;
			request.img_url = imgurl;
			request.crop_type = crop_code;
			request.provience = AppUtils.getCurrProvince(this);
//			request.city = AppUtils.getCurrCity(this) + "???";
			request.city = GLOBAL_DATA.getInstance(this).currCity + "???";
			if (AppUtils.getCurrDistrict(this).contains("???"))
				request.district = AppUtils.getCurrDistrict(this);
			else
				request.district = AppUtils.getCurrDistrict(this) + "???";

			String textcontent = content;
			for (int i = 0; i < list.size(); i++) {
				String x = list.get(i);  //x???????????????
				if (textcontent.contains(x)){
					textcontent = textcontent.replaceAll(x, AppUtils.getXing(x));
					mResultText.setText(textcontent);
				}
			}
			request.content = textcontent;
			request.key_word = "";
			request.problem_type = questioncode;
			publishModel.publishQuestion(request);
		}
	}

	@Override
	public void onAddressSelected(Province province, City city, County county) {
		this.province = (province == null ? "" : province.name);
		this.city = (city == null ? "" : city.name);
		this.county = (county == null ? "" : county.name);

		text_position.setText(this.province + this.city + this.county);
		if (positionDialog != null)
			positionDialog.dismiss();
	}

	//TODO ???????????????
	private void btnVoice() {

		FlowerCollector.onEvent(this, "iat_recognize");
		mIatResults.clear();
		// ????????????
		setParam();
		boolean isShowDialog = false;// show dialog
		if (isShowDialog) {
			// ?????????????????????
			RecognizerDialog dialog = new RecognizerDialog(this,null);
			dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
			dialog.setListener(new RecognizerDialogListener() {
				@Override
				public void onResult(RecognizerResult recognizerResult, boolean b) {
					printResult(recognizerResult);
				}
				@Override
				public void onError(SpeechError speechError) {
				}
			});
			dialog.show();
			showTip("???????????????");

		} else {
			// ????????????????????????
			ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
//                    showTip("Dictation failed, error code???" + ret);
				showTip("????????????");
			} else {
				showTip("???????????????");
			}
		}

		img_voice_recording_status.setVisibility(View.VISIBLE);

	}

	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				showTip("???????????????");
			}
		}
	};

	/**
	 * ??????????????????
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// ??????????????????sdk??????????????????????????????????????????????????????????????????
			showTip("???????????????");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips???
			// ????????????10118(???????????????)????????????????????????????????????????????????????????????????????????????????????
			// ????????????????????????????????????????????????????????????????????????????????????
//            Button btn_record = (Button) findViewById(R.id.iat_recognize);
//            btn_record.setBackgroundResource(R.drawable.record);

			if(mTranslateEnable && error.getErrorCode() == 14002) {
				// showTip( error.getPlainDescription(true)+"\n Please confirm whether the translation function is enabled" );
			}else if (error.getErrorCode() == 20006) {
				errorMsg("?????????????????????");
			}
			else {
				errorMsg(error.getErrorDescription());
//				showTip(error.getPlainDescription(true));
			}

			img_voice_recording_status.setVisibility(View.GONE);

		}

		@Override
		public void onEndOfSpeech() {

//            Button btn_record = (Button) findViewById(R.id.iat_recognize);
//            btn_record.setBackgroundResource(R.drawable.record);
			// ??????????????????????????????????????????????????????????????????????????????????????????????????????
//			showTip("End speaking");
			img_voice_recording_status.setVisibility(View.GONE);
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			if( mTranslateEnable ){
				printTransResult( results );
			}else{
				printResult(results);
			}

			if (isLast) {
				// TODO ???????????????
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
		//	showTip("Currently talking, volume???" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// ??????????????????????????????????????????id??????????????????????????????id??????????????????????????????????????????????????????????????????????????????
			// ??????????????????????????????id???null
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
	};

	private void printTransResult (RecognizerResult results) {
		String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
		String oris = JsonParser.parseTransResult(results.getResultString(),"src");

		if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
		//	showTip( "The result of the analysis failed. Please confirm whether the translation function is enabled???" );
		}else{
//			mResultText.setText( "Original language:\n"+oris+"\n Target language:\n"+trans );
		}

	}

	//???????????????
//	private void printResult(RecognizerResult results) {
//		String text = parseIatResult(results.getResultString());
//		// ??????????????????
//		mResultText.append(text);
//	}

//	public static String parseIatResult(String json) {
//		StringBuffer ret = new StringBuffer();
//		try {
//			JSONTokener tokener = new JSONTokener(json);
//			JSONObject joResult = new JSONObject(tokener);
//			JSONArray words = joResult.getJSONArray("ws");
//			for (int i = 0; i < words.length(); i++) {
//				// ?????????????????????????????????????????????
//				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
//				JSONObject obj = items.getJSONObject(0);
//				ret.append(obj.getString("w"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ret.toString();
//	}

	private void printResult(RecognizerResult results) {

		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// ??????json????????????sn??????
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		if (resultBuffer.toString().length() > 0)
		{
			count++;
			if (count > 1)
			{
				count = 0;
				return;
			}
			img_voice_recording_status.setVisibility(View.GONE);

			String str_result = "";
			for (int i = 0; i < resultBuffer.toString().length(); i++) {
				if (resultBuffer.toString().charAt(i) != '???' || resultBuffer.toString().charAt(i) != '.' ) {
					str_result = str_result + resultBuffer.toString().charAt(i);
				} else {
					break;
				}
			}

			mResultText.append(str_result);

		} else {
			errorMsg("??????????????????????????????");
		}


	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	public void setParam() {
		// ????????????
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// ??????????????????
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// ????????????????????????
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		this.mTranslateEnable = false;
		if( mTranslateEnable ){
			mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
			mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
			mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
		}

//        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
		if (str_lang.equals("en_us")) {
			// ????????????
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
			mIat.setParameter(SpeechConstant.ACCENT, null);

			if( mTranslateEnable ){
				mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
				mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
			}
		} else {
			// ????????????
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ??????????????????
			mIat.setParameter(SpeechConstant.ACCENT, str_lang);

			if( mTranslateEnable ){
				mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
				mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
			}


		}

		mIat.setParameter(SpeechConstant.ASR_PTT, "1");


		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	}

	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_QUESTION) {
				String question = data.getStringExtra("question");
				questioncode = data.getStringExtra("questioncode");
				if (!"".equals(question))
					text_question.setText(question);
			}else if (requestCode == REQUEST_CROP){
				String crop = data.getStringExtra("crop");
				crop_code = data.getStringExtra("crop_code");
				if (!TextUtils.isEmpty(crop))
					text_crop.setText(crop);
			}
		}

		if(resultCode == RESULT_OK) {
			switch (requestCode) {
				// ????????????
				case REQUEST_CAMERA_CODE:
					ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
					loadAdpater(list);
					break;
				// ??????
				case REQUEST_PREVIEW_CODE:
					ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
					loadAdpater(ListExtra);
					break;
			}
		}

	}

	private void loadAdpater(ArrayList<String> paths){
		if (imagePaths!=null&& imagePaths.size()>0){
			imagePaths.clear();
		}
		if (paths.contains("000000")){
			paths.remove("000000");
		}
		paths.add("000000");
		imagePaths.addAll(paths);
		gridAdapter  = new GridAdapter(imagePaths);
		gridView.setAdapter(gridAdapter);
		try{
			JSONArray obj = new JSONArray(imagePaths);
			Log.e("--", obj.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private class GridAdapter extends BaseAdapter {
		private ArrayList<String> listUrls;
		private LayoutInflater inflater;
		public GridAdapter(ArrayList<String> listUrls) {
			this.listUrls = listUrls;
			if(listUrls.size() == 10){
				listUrls.remove(listUrls.size()-1);
			}
			inflater = LayoutInflater.from(PublishQuestionActivity.this);
		}

		public int getCount(){
			return  listUrls.size();
		}
		@Override
		public String getItem(int position) {
			return listUrls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_image, parent,false);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			final String path=listUrls.get(position);
			if (path.equals("000000")){
				holder.image.setImageResource(R.drawable.q_icon_add);
			}else {
				Glide.with(PublishQuestionActivity.this)
						.load(path)
						.placeholder(R.drawable.default_error)
						.error(R.drawable.default_error)
						.centerCrop()
						.crossFade()
						.into(holder.image);
			}
			return convertView;
		}
		class ViewHolder {
			ImageView image;
		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.PUBLISHQUESTION))
		{
			if (publishModel.lastStatus.error_code == 200)
			{
				errorMsg("????????????");
				setResult(RESULT_OK);
				finish();
			}
			else
				errorMsg(publishModel.lastStatus.error_desc);
		}else if (url.endsWith(ApiInterface.UPLOAD_FILE)) {
			if (commonModel.lastStatus.error_code == 200) {
				imgurl = commonModel.fullPath;
				submit();
			} else {
				errorMsg(commonModel.lastStatus.error_desc);
			}
		}else if(url.contains(ApiInterface.ILLEGALS)){
			for(int i = 0 ;i<illegalModel.data.illegals.size();i++){
				list.add(illegalModel.data.illegals.get(i).text);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		publishModel.removeResponseListener(this);
		commonModel.removeResponseListener(this);
	}

}