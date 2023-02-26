
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class DICTIONARY
{
    public String id;
    public String name;
    public String code;
    public String aboutCode;
    public String attentionType;
    private boolean isCheck = false;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.name = jsonObject.optString("dicName");
        this.code = jsonObject.optString("dicCode");
        this.aboutCode = jsonObject.optString("aboutCode");
        this.attentionType = jsonObject.optString("attentionType");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("dicName", name);
        localItemObject.put("dicCode", code);
        localItemObject.put("aboutCode", aboutCode);
        localItemObject.put("attentionType", attentionType);
        return localItemObject;
    }


    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
