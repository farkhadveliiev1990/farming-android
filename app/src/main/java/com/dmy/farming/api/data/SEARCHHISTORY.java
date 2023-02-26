
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SEARCHHISTORY
{
    //
    public String id;

    //
    public String search_word;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.search_word = jsonObject.optString("word_name");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("word_name", search_word);

        return localItemObject;
    }
}
