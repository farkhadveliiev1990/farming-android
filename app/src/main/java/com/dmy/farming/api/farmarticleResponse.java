
package com.dmy.farming.api;

import com.dmy.farming.api.data.ARTICLE;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.KEYWORD;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.dmy.farming.protocol.PAGINATED;
import com.dmy.farming.protocol.STATUS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class farmarticleResponse
{
    public STATUS status;
    public ArrayList<FARMARTICLE> data = new ArrayList<>();
  /*  public  FARMARTICLE farmarticle;*/
//    public ArrayList<COMMENT> comment = new ArrayList<>();
    public ArrayList<KEYWORD> keyWord = new ArrayList<KEYWORD>();
    public PAGINATED paginated;


    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        STATUS  status = new STATUS();
//        status.fromJson(jsonObject.optJSONObject("status"));
        status.succeed = jsonObject.optInt("state");
        status.error_code = jsonObject.optInt("errCode");
        status.error_desc = jsonObject.optString("errMsg");
        this.status = status;

        PAGINATED  paginated = new PAGINATED();
//          paginated.fromJson(jsonObject.optJSONObject("ismore"));
        paginated.more = jsonObject.optInt("ismore");
        paginated.count = jsonObject.optInt("count");
        this.paginated = paginated;

        JSONArray subItemArray = jsonObject.optJSONArray("datalist");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                FARMARTICLE subItem = new FARMARTICLE();
                subItem.fromJson(subItemObject);
                this.data.add(subItem);
            }
        }
       /* FARMARTICLE farmarticle = new FARMARTICLE();
        farmarticle.fromJson(jsonObject.optJSONObject("data"));
        this.farmarticle = farmarticle;*/

//         subItemArray = jsonObject.optJSONArray("datalist");
//        if(null != subItemArray)
//        {
//            for(int i = 0;i < subItemArray.length();i++)
//            {
//                JSONObject subItemObject = subItemArray.getJSONObject(i);
//                COMMENT subItem = new COMMENT();
//                subItem.fromJson(subItemObject);
//                this.comment.add(subItem);
//            }
//        }

        subItemArray = jsonObject.optJSONArray("keyword");
        if(null != subItemArray)
        {
            for(int i = 0;i < subItemArray.length();i++)
            {
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                KEYWORD subItem = new KEYWORD();
                subItem.fromJson(subItemObject);
                this.keyWord.add(subItem);
            }
        }

        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        if(null != status)
        {
            localItemObject.put("status", status.toJson());
        }

        JSONArray itemJSONArray = new JSONArray();
        for(int i=0; i< data.size(); i++)
        {
            FARMARTICLE itemData = data.get(i);
            JSONObject itemJSONObject = itemData.toJson();
            itemJSONArray.put(itemJSONObject);
        }
        localItemObject.put("data", itemJSONArray);




        return localItemObject;
    }

}
