
package com.dmy.farming.api.data;

import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.PAGINATED;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HELPCENTER
{
    public Long lastUpdateTime = 0L;

    public String id ;
    public String type;
    public String app ;
    public String appid ;
    public String createUser;
    public String createTime ;
    public  String updataTime ;
    public String deleted ;
    public String content ;
    public String title;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.type = jsonObject.optString("type");
        this.app = jsonObject.optString("app");
        this.appid = jsonObject.optString("appid");
        this.createUser = jsonObject.optString("createUser");
        this.updataTime = jsonObject.optString("updataTime");
        this.deleted = jsonObject.optString("deleted");
        this.content = jsonObject.optString("content");
        this.title = jsonObject.optString("title");


        //
        lastUpdateTime = jsonObject.optLong("lastUpdateTime");
        //
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("type", type);
        localItemObject.put("app", app);
        localItemObject.put("appid", appid);
        localItemObject.put("createUser", createUser);
        localItemObject.put("updataTime", updataTime);
        localItemObject.put("deleted", deleted);
        localItemObject.put("content", content);
        localItemObject.put("title", title);

        localItemObject.put("lastUpdateTime", lastUpdateTime);
        return localItemObject;
    }

}
