package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.a_QUESTION_LIST;
import com.dmy.farming.api.expertRequest;
import com.dmy.farming.api.questionListResponse;
import com.dmy.farming.protocol.PAGINATION;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class QuestionListModel extends BaseModel {

	public a_QUESTION_LIST data = new a_QUESTION_LIST();
	int PAGE_COUNT = 10;

	String fileName;

	public long tag_id;


	public QuestionListModel(Context context, long tag_id) {
		super(context);

		this.tag_id = tag_id;
		fileName = "/questionL_" + tag_id + ".dat";
		readCache();
	}

	public void readCache() {
		String path = DataCleanManager.getCacheDir(mContext) + fileName;
		File f1 = new File(path);
		if (f1.exists()) {
			try {
				InputStream is = new FileInputStream(f1);
				InputStreamReader input = new InputStreamReader(is, "UTF-8");
				BufferedReader bf = new BufferedReader(input);

				parseCache(bf.readLine());

				bf.close();
				input.close();
				is.close();

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void parseCache(String result) {
		try {
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				data.fromJson(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 缓存数据
	private PrintStream ps = null;

	public void fileSave() {
		data.lastUpdateTime = System.currentTimeMillis();

		String path = DataCleanManager.getCacheDir(mContext);
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File file = new File(filePath + fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			ps = new PrintStream(fos);
			ps.print(data.toJson().toString());
			ps.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	///////////////////////////
	public void getQuestionList(String user_attention, int page,int positon, boolean bShow) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				QuestionListModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						questionListResponse response = new questionListResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
//							data.keyword = request.keyword;
							data.questions.clear();
							data.paginated = response.paginated;
							if (response.data.size() > 0)
							{
								data.questions.addAll(response.data);
							}

							fileSave();
							QuestionListModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject localItemObject = new JSONObject();
			localItemObject.put("info_from", AppConst.info_from);
			localItemObject.put("user_attention", user_attention);
			localItemObject.put("page", page);
			localItemObject.put("position", positon);
			params.put("json", localItemObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.QUESTIONLIST).type(JSONObject.class).params(params);

		if (bShow) {
			MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
			aq.progress(pd.mDialog).ajax(cb);
		} else {
			aq.ajax(cb);
		}
	}

	///////////////////////////
	public void getQuestionListmore(String user_attention, int page,int positon, boolean bShow) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				QuestionListModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						questionListResponse response = new questionListResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
//							data.keyword = request.keyword;
							data.paginated = response.paginated;
							if (response.data.size() > 0)
							{
								data.questions.addAll(response.data);
							}

							fileSave();
							QuestionListModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject localItemObject = new JSONObject();
			localItemObject.put("info_from", AppConst.info_from);
			localItemObject.put("user_attention", user_attention);
			localItemObject.put("page", page);
			localItemObject.put("position", positon);
			params.put("json", localItemObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.QUESTIONLIST).type(JSONObject.class).params(params);

		if (bShow) {
			MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
			aq.progress(pd.mDialog).ajax(cb);
		} else {
			aq.ajax(cb);
		}
	}

	public void API_getMysolved(expertRequest request, boolean bShow) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				QuestionListModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						questionListResponse response = new questionListResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
//							data.keyword = request.keyword;
							data.questions.clear();
							data.paginated = response.paginated;
							if (response.data.size() > 0)
							{
								data.questions.addAll(response.data);
							}

							fileSave();
							QuestionListModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("json", request.toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.EXPERTSOLVED).type(JSONObject.class).params(params);
		MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
		aq.progress(pd.mDialog).ajax(cb);
	}
}
