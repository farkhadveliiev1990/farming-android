
package com.dmy.farming.api.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DIAGNOSTICITEM
{
    public String id;
    public String title;
    public String content;
    public String num;
    public String state;
    public String diagnoseId;
    public String deleted;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.content = jsonObject.optString("content");
        this.num = jsonObject.optString("num");
        this.state = jsonObject.optString("state");
        this.diagnoseId = jsonObject.optString("diagnoseId");
        this.deleted = jsonObject.optString("deleted");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("content", content);
        localItemObject.put("num", num);
        localItemObject.put("state", state);
        localItemObject.put("diagnoseId", diagnoseId);
        localItemObject.put("deleted", deleted);

        return localItemObject;
    }
}
