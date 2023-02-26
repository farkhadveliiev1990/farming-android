package com.dmy.farming.view.comment;

import com.dmy.farming.api.data.chat.REPLY;

import java.util.ArrayList;

/**
 * 评论对象
 */
public class Moment {

    public String id;
//    public String mContent;
    public REPLY reply;
    public ArrayList<Comment> mComment; // 评论列表

    public Moment(String id,REPLY reply, ArrayList<Comment> mComment) {
        this.id = id;
        this.mComment = mComment;
        this.reply = reply;
    }
}
