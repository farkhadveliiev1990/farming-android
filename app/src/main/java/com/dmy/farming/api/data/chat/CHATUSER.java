
package com.dmy.farming.api.data.chat;

import android.text.TextUtils;

import com.droid.PingYinUtil;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;
import com.external.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "CHATUSER")
public class CHATUSER extends Model
{
    public final static long CHATUSER_HUODONG = 2;
    public final static long CHATUSER_SYSTEM = 3;

    // 聊天账号
    @Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long user_id;

    // 问题图片
    @Column(name = "questionimg")
    public String question_img;

    // 时间
    @Column(name = "user_name")
    public String user_name;

    // 位置
    @Column(name = "user_rename")
    public String user_rename;

    // 技术
    @Column(name = "user_tag")
    public String user_tag;

    // 标题
    @Column(name = "friend_status")
    public int friend_status;

    // 黑名单状态(0:默认 a1:我黑他 2:他黑我 3:相互黑)
    @Column(name = "black_status")
    public int black_status;

    // 免打扰(0:默认 a1:已设置)
    @Column(name = "is_mute")
    public int is_mute;

    // tmp
    @Column(name = "user_chat_pinyin")
    public String user_chat_pinyin;

    // tmp
    public int chk_state; //0:normal, a1:checked 2:disabled
    //

    public String getConvId() {
        return "0_" + user_id;
    }

    public void setPinyin() {
        user_chat_pinyin = PingYinUtil.getPingYin(getChatName());
    }

    public void fromJson(JSONObject jsonObject)  throws JSONException
    {
        if (null == jsonObject){
            return ;
        }

        this.user_id = jsonObject.optLong("user_id");
        this.question_img = jsonObject.optString("question_img");
        this.user_name = jsonObject.optString("user_name");
        this.user_rename = jsonObject.optString("user_rename");
        this.user_tag = jsonObject.optString("user_tag");
        if (user_tag == null)
            user_tag = "";
        this.friend_status = jsonObject.optInt("friend_status");
        this.black_status = jsonObject.optInt("black_status");
        this.is_mute = jsonObject.optInt("is_mute");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();

        localItemObject.put("user_id", user_id);
        localItemObject.put("user_logo", question_img);
        localItemObject.put("user_name", user_name);
        localItemObject.put("user_rename", user_rename);
        localItemObject.put("user_tag", user_tag);
        localItemObject.put("friend_status", friend_status);
        localItemObject.put("black_status", black_status);
        localItemObject.put("is_mute", is_mute);
        return localItemObject;
    }

    public static List<CHATUSER> getFriends() {
        return new Select().from(CHATUSER.class).execute();//.where("user_status = ?", user_status).execute();
    }

    public String getChatName() {
        if (!TextUtils.isEmpty(user_rename))
            return user_rename;
        else
            return user_name;
    }

    public static CHATUSER findById(long user_id) {
        return new Select().from(CHATUSER.class).where("user_id = ?", user_id).executeSingle();
    }
}
