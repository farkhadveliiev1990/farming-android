
package com.dmy.farming.api.data;

import org.json.JSONException;
import org.json.JSONObject;

public class BUYLIST
{
    public String id;
    public String title;
    public String buy_type;
    public String publish_time;
    public String publish_username;
    public String price;
    public String unit;
    public String link_name;
    public String link_phone;
    public String provience;
    public String city;
    public String district;
    public String address_details;
    public String distance;
    public String user_phone;
    public String img_url;
    public String browse_num;
    public String collect_num;
    public String is_top;
    public String recommend;
    public String distrint;
    public String content;
    public int deleted;
    public String iscollection;



    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.buy_type = jsonObject.optString("buy_type");
        this.publish_time = jsonObject.optString("publish_time");
        this.publish_username = jsonObject.optString("publish_username");
        this.price = jsonObject.optString("price");
        this.unit = jsonObject.optString("unit");
        this.link_name = jsonObject.optString("link_name");
        this.link_phone = jsonObject.optString("link_phone");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.address_details = jsonObject.optString("address_details");
        this.distance = jsonObject.optString("distance");
        this.user_phone = jsonObject.optString("publish_user");
        this.img_url = jsonObject.optString("img_url");
        this.browse_num = jsonObject.optString("browse_num");
        this.collect_num = jsonObject.optString("collect_num");
        this.is_top = jsonObject.optString("is_top");
        this.recommend = jsonObject.optString("recommend");
        this.distrint = jsonObject.optString("distrint");
        this.content = jsonObject.optString("content");
        this.deleted = jsonObject.optInt("deleted");
        this.iscollection = jsonObject.optString("iscollection");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("buy_type", buy_type);
        localItemObject.put("publish_time", publish_time);
        localItemObject.put("publish_username", publish_username);
        localItemObject.put("price", price);
        localItemObject.put("unit", unit);
        localItemObject.put("link_name", link_name);
        localItemObject.put("link_phone", link_phone);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("address_details", address_details);
        localItemObject.put("distance", distance);
        localItemObject.put("user_phone", user_phone);
        localItemObject.put("img_url", img_url);
        localItemObject.put("browse_num", browse_num);
        localItemObject.put("collect_num", collect_num);
        localItemObject.put("is_top", is_top);
        localItemObject.put("recommend", recommend);
        localItemObject.put("distrint", distrint);
        localItemObject.put("content", content);
        localItemObject.put("deleted", deleted);
        localItemObject.put("iscollection", iscollection);

        return localItemObject;
    }
}
