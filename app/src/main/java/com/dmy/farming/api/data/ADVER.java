package com.dmy.farming.api.data;

import com.dmy.farming.protocol.PHOTO;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.ContactsContract.CommonDataKinds.Photo.PHOTO;

/**
 * Created by victory1989 on 2017/10/7.
 */


public class ADVER
{
    public String adver_id;

    public String adver_name;

    public PHOTO adver_img;

    // 0:Link, 1:GoodID 2:ArticleID 3:形象设计师 4:服装设计师 5:店铺 6: DIY定制 , 7:纺品定制
    public int target_type;

    public String target;

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }

        PHOTO  adver_img = new PHOTO();
        adver_img.fromJson(jsonObject.optJSONObject("adver_img"));
        this.adver_img = adver_img;

        this.adver_name = jsonObject.optString("adver_name");
        this.adver_id = jsonObject.optString("adver_id");
        this.target = jsonObject.optString("target");
        this.target_type = jsonObject.optInt("target_type");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null!=adver_img)
        {
            localItemObject.put("adver_img", adver_img.toJson());
        }

        localItemObject.put("adver_name", adver_name);
        localItemObject.put("adver_id",adver_id);
        localItemObject.put("target_type", target_type);
        localItemObject.put("target", target);
        return localItemObject;
    }
}

