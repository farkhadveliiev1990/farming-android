
package com.dmy.farming.api.data.chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PRICELIST implements Serializable
{
    public String id;
    public String  address_details;
    public String  crop_type;
    public String provience;
    public String city;
    public String district;
    public String low;
    public String upper;
    public String turn_price;
    public String publish_time;
    public String remark;
    public String user_id;
    public String user_name;
    public String info_from;
    public String deleted;
    public String price_type;
    public String user_phone;
    public String crop_code;


    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.id = jsonObject.optString("id");
        this.address_details = jsonObject.optString("address_details");
        this.crop_type = jsonObject.optString("crop_type");
        this.upper = jsonObject.optString("upper");
        this.turn_price = jsonObject.optString("turn_price");
        this.publish_time = jsonObject.optString("publish_time");
        this.remark = jsonObject.optString("remark");
        this.user_id = jsonObject.optString("user_id");
        this.user_name = jsonObject.optString("user_name");
        this.provience = jsonObject.optString("provience");
        this.city = jsonObject.optString("city");
        this.district = jsonObject.optString("district");
        this.price_type = jsonObject.optString("price_type");
        this.user_phone = jsonObject.optString("user_phone");
        this.low = jsonObject.optString("low");
        this.info_from = jsonObject.optString("info_from");
        this.deleted = jsonObject.optString("deleted");
        this.crop_code = jsonObject.optString("corp_code");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("id", id);
        localItemObject.put("address_details", address_details);
        localItemObject.put("crop_type", crop_type);
        localItemObject.put("upper", upper);
        localItemObject.put("turn_price", turn_price);
        localItemObject.put("publish_time", publish_time);
        localItemObject.put("remark", remark);
        localItemObject.put("user_id", user_id);
        localItemObject.put("user_name", user_name);
        localItemObject.put("provience", provience);
        localItemObject.put("city", city);
        localItemObject.put("district", district);
        localItemObject.put("price_type", price_type);
        localItemObject.put("user_phone", user_phone);
        localItemObject.put("low", low);
        localItemObject.put("info_from", info_from);
        localItemObject.put("deleted", deleted);
        localItemObject.put("crop_code", crop_code);

        return localItemObject;
    }
}
