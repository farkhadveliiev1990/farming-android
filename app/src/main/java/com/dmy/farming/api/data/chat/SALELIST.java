
package com.dmy.farming.api.data.chat;

import org.json.JSONException;
import org.json.JSONObject;

public class SALELIST
{
    public String id;
    public String title;
    public String sale_type;
    public String publishTime;
    public String publish_username;
    public String price;
    public String unit;
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
    public String content;
    public String linkPhone;
    public String linkUser;
    public String iscollection;
    public int deleted;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.title = jsonObject.optString("title");
        this.sale_type = jsonObject.optString("saleType");
        this.publishTime = jsonObject.optString("publishTime");
        this.publish_username = jsonObject.optString("publish_username");
        this.price = jsonObject.optString("price");
        this.unit = jsonObject.optString("unit");
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
        this.content = jsonObject.optString("content");
        this.linkPhone = jsonObject.optString("linkPhone");
        this.linkUser = jsonObject.optString("linkUser");
        this.iscollection = jsonObject.optString("iscollection");
        this.deleted = jsonObject.optInt("deleted");

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("title", title);
        localItemObject.put("sale_type", sale_type);
        localItemObject.put("publishTime", publishTime);
        localItemObject.put("publish_username", publish_username);
        localItemObject.put("price", price);
        localItemObject.put("unit", unit);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", distance);
        localItemObject.put("addressDetails", addressDetails);
        localItemObject.put("distance", distance);
        localItemObject.put("user_phone", user_phone);
        localItemObject.put("imgUrl", imgUrl);
        localItemObject.put("browse_num", browse_num);
        localItemObject.put("collect_num", collect_num);
        localItemObject.put("is_top", is_top);
        localItemObject.put("recommend", recommend);
        localItemObject.put("distrint", district);
        localItemObject.put("content", content);
        localItemObject.put("linkPhone", linkPhone);
        localItemObject.put("linkUser", linkUser);
        localItemObject.put("deleted", deleted);

        return localItemObject;
    }
}
