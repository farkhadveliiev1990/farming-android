
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class FINDHELPLIST
{
    public String id;
    public String title;
    public String helpType;
    public String publishTime;
    public String publish_username;
    public String price;
    public String unit;
    public String link_name;
    public String link_phone;
    public String provience;
    public String city;
    public String district;
    public String addressDetails;
    public String distance;
    public String user_phone;
    public String imgUrl;
    public String browse_num;
    public String collect_num;
    public String is_top;
    public String recommend;
    public String distrint;
    public String content;
    public String iscollection;
    public int deleted;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.helpType = jsonObject.optString("helpType");
        this.publishTime = jsonObject.optString("publishTime");
        this.publish_username = jsonObject.optString("publish_username");
        this.price = jsonObject.optString("price");
        this.unit = jsonObject.optString("unit");
        this.link_name = jsonObject.optString("linkUser");
        this.link_phone = jsonObject.optString("linkPhone");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.addressDetails = jsonObject.optString("addressDetails");
        this.distance = jsonObject.optString("distance");
        this.user_phone = jsonObject.optString("publishUser");
        this.imgUrl = jsonObject.optString("imgUrl");
        this.browse_num = jsonObject.optString("browse_num");
        this.collect_num = jsonObject.optString("collect_num");
        this.is_top = jsonObject.optString("is_top");
        this.recommend = jsonObject.optString("recommend");
        this.distrint = jsonObject.optString("distrint");
        this.content = jsonObject.optString("content");
        this.iscollection = jsonObject.optString("iscollection");
        this.deleted = jsonObject.optInt("deleted");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("helpType", helpType);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("publish_username", publish_username);
        localItemObject.put("price", price);
        localItemObject.put("unit", unit);
        localItemObject.put("link_name", link_name);
        localItemObject.put("link_phone", link_phone);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("addressDetails", addressDetails);
        localItemObject.put("distance", distance);
        localItemObject.put("user_phone", user_phone);
        localItemObject.put("imgUrl", imgUrl);
        localItemObject.put("browse_num", browse_num);
        localItemObject.put("collect_num", collect_num);
        localItemObject.put("is_top", is_top);
        localItemObject.put("recommend", recommend);
        localItemObject.put("distrint", distrint);
        localItemObject.put("content", content);
        localItemObject.put("iscollection", iscollection);
        localItemObject.put("deleted", deleted);

        return localItemObject;
    }
}
