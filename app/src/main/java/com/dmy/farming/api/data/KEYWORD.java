package com.dmy.farming.api.data;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;


public class KEYWORD  {
		public String id;
		public String wordName;
		public String wordCode;

		public void fromJson(JSONObject jsonObject)  throws JSONException
		{
			if (null == jsonObject){
				return ;
			}

			this.id = jsonObject.optString("id");
			this.wordName = jsonObject.optString("wordName");
			this.wordCode = jsonObject.optString("wordCode");

			return ;
		}

		public JSONObject  toJson() throws JSONException
		{
			JSONObject localItemObject = new JSONObject();

			localItemObject.put("id", id);
			localItemObject.put("wordName", wordName);
			localItemObject.put("wordCode", wordCode);
			return localItemObject;
		}

}
