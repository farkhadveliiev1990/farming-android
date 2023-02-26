
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class COLLECTION
{
    public String id;
    public String collection_type;
    public String collction_id;
    public String userid;
    public String info_from;
    private boolean isCheck = false;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.collection_type = jsonObject.optString("collection_type");
        this.collction_id = jsonObject.optString("collction_id");
        this.userid = jsonObject.optString("userid");
        this.info_from = jsonObject.optString("info_from");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("collection_type", collection_type);
        localItemObject.put("collction_id", collction_id);
        localItemObject.put("userid", userid);
        localItemObject.put("info_from", info_from);
        return localItemObject;
    }


    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
