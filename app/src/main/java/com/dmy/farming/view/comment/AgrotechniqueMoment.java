package com.dmy.farming.view.comment;

import com.dmy.farming.api.data.a_COMMENT_LIST;
import com.dmy.farming.api.data.chat.COMMENT;
import com.dmy.farming.api.data.chat.REPLY;

import java.util.ArrayList;

/**
 * 评论对象
 */
public class AgrotechniqueMoment {

    public String id;
    public a_COMMENT_LIST a_comment_list;
    public ArrayList<Comment> mComment; // 评论列表

    public AgrotechniqueMoment(String id, a_COMMENT_LIST a_comment_list, ArrayList<Comment> mComment) {
        this.id = id;
        this.a_comment_list = a_comment_list;
        this.mComment = mComment;
    }
}
