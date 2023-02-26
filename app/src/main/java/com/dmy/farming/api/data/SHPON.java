
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SHPON
{
    public String title;
    public String description;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.title = jsonObject.optString("typeName");
        this.description = jsonObject.optString("createuserName");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("title", title);
        localItemObject.put("description", description);

        return localItemObject;
    }
}
