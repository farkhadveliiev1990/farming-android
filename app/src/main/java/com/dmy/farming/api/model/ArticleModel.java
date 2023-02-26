package com.dmy.farming.api.model;

import android.content.Context;
import android.text.TextUtils;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleRequest;
import com.dmy.farming.api.articleResponse;
import com.dmy.farming.api.data.ARTICLE;
import com.dmy.farming.api.dictionaryResponse;
import com.dmy.farming.protocol.STATUS;
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

public class ArticleModel extends BaseModel {

	public ARTICLE data = new ARTICLE();
	public String type;

	String fileName;
	public STATUS lastStatus = new STATUS();

	public ArticleModel(Context context,String type) {
		super(context);
	}
	public ArticleModel(Context context) {
		super(context);
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

	public void getArticle() {
		articleRequest request = new articleRequest();
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ArticleModel.this.callback(url, jo, status);
				try {
					if (jo != null)
					{
						articleResponse response = new articleResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.succeed == 1) {
							data.fromJson(response.data.toJson());
							fileSave();
						}
						ArticleModel.this.OnMessageResponse(url, jo, status);
					}
				} catch (JSONException e) {
				}
			}
		};

		request.type = type;

		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("json", request.toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.setParams(ApiInterface.ARTICLE, params).type(JSONObject.class);

		if (TextUtils.isEmpty(data.content)) {
			MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
			aq.progress(pd.mDialog).ajax(cb);
		} else {
			aq.ajax(cb);
		}
	}
	}
