
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class CROPCYCLE
{
    public String id;
    public String cropDicid;
    public String maturityType;
    public String cycleType;
    public String dicname;
    public String cropName;
    private boolean isCheck = false;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.cropDicid = jsonObject.optString("cropDicid");
        this.maturityType = jsonObject.optString("maturityType");
        this.cycleType = jsonObject.optString("cycleType");
        this.dicname = jsonObject.optString("dicname");
        this.cropName = jsonObject.optString("cropName");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("cropDicid", cropDicid);
        localItemObject.put("maturityType", maturityType);
        localItemObject.put("cycleType", cycleType);
        localItemObject.put("cropName", cropName);
        localItemObject.put("dicname", dicname);
        return localItemObject;
    }


    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
