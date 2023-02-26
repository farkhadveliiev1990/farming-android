package com.dmy.farming.view.comment;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.activity.ReportActivity;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.REPLY;
import com.dmy.farming.view.emojicon.EaseSmileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by huangziwei on 16-4-12.
 */
public class MomentAdapter extends BaseAdapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;

    private ArrayList<Moment> mList;
    private Context mContext;
    private CustomTagHandler mTagHandler;
    private QUESTION question;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public MomentAdapter(Context context, ArrayList<Moment> list, QUESTION q,CustomTagHandler tagHandler) {
        mList = list;
        mContext = context;
        mTagHandler = tagHandler;
        question = q;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
    }

    @Override
    public int getCount() {
        // heanderView
//        return mList.size() + 1;
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//            if (position == 0) {
//                convertView = View.inflate(mContext, R.layout.item_header, null);
//            } else {
                convertView = View.inflate(mContext, R.layout.item_moment, null);
                ViewHolder holder = new ViewHolder();
                holder.mCommentList = (LinearLayout) convertView.findViewById(R.id.comment_list);
                holder.mBtnInput = (TextView) convertView.findViewById(R.id.btn_input_comment);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.expert_type = (TextView) convertView.findViewById(R.id.expert_type);
                holder.mContent = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.likenum = (TextView) convertView.findViewById(R.id.likenum);
                holder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
                holder.img_thumbs_up = (ImageView) convertView.findViewById(R.id.img_thumbs_up);
                holder.img_more = (ImageView) convertView.findViewById(R.id.img_more);
                convertView.setTag(holder);
//            }
        }
        //防止ListView的OnItemClick与item里面子view的点击发生冲突
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//        if (position == 0) {
//
//        } else {
//            int index = position - 1;
            final int index = position;
        final   ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.name.setText(mList.get(index).reply.username);
            imageLoader.displayImage(mList.get(index).reply.headurl,holder.img_head, FarmingApp.options_head);
            if (mList.get(index).reply.reply_usertype == 0)
                holder.expert_type.setVisibility(View.GONE);
            else {
                holder.expert_type.setVisibility(View.VISIBLE);
                holder.expert_type.setText(mList.get(index).reply.expert_type);
            }

            Spannable span = EaseSmileUtils.getSmiledText(mContext, "\u3000\u3000" + mList.get(index).reply.content);
            holder.mContent.setText(span, TextView.BufferType.SPANNABLE);

            holder.time.setText(mList.get(index).reply.comment_time);
            if(mList.get(index).reply.islike==1){
                holder.img_thumbs_up.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icont_fabulous));
            }else{
                holder.img_thumbs_up.setBackground(convertView.getResources().getDrawable(R.drawable.adrtail_icon_default_fabulous));
            }
            holder.likenum.setText(mList.get(index).reply.like_num);

            if (!mList.get(index).reply.userPhone.equals(SESSION.getInstance().sid)){
                holder.img_thumbs_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 点赞
                        if (mList.get(index).reply.islike == 1) {
                            cancellike(mList.get(index).reply.id);
                        } else {
                            like(mList.get(index).reply.id, mList.get(index).reply.byreply_userPhone);
                        }
                    }
                });
            }

            holder.img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectDialog(v, mList.get(index).id, mList.get(index).reply, holder.mBtnInput);
                }
            });


            CommentFun.parseCommentList(mContext, mList.get(index),
                    holder.mCommentList, holder.mBtnInput, mTagHandler);
//        }
        return convertView;
    }

    private static class ViewHolder {
        LinearLayout mCommentList;
//        View mBtnInput;
        TextView mContent,name,expert_type,time,mBtnInput,likenum;
        ImageView img_more,img_thumbs_up,img_head;
    }

    PopupWindow popupWindow;
    private void showSelectDialog(View v, final String id, final REPLY reply, final View input) {
        //自定义布局，显示内容
        View view = LayoutInflater.from(mContext).inflate(R.layout.d00_chat_item_caina, null);
		View layout_dialog = view.findViewById(R.id.layout_dialog);
        View zhuiwen = view.findViewById(R.id.layout_zhuiwen);
        View line = view.findViewById(R.id.line);
		final View caina = view.findViewById(R.id.layout_caina);
        final View line1 = view.findViewById(R.id.line1);
        View jubao = view.findViewById(R.id.layout_jubao);
        View line2 = view.findViewById(R.id.line2);
        View delete = view.findViewById(R.id.layout_delete);

        if (D01_QuestionDetailActivity.type.equals("myquestion") || question.userPhone.equals(SESSION.getInstance().sid)){
            zhuiwen.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            zhuiwen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInputView(input,new User(reply.userPhone,""));
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }else {
            zhuiwen.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if(question.userPhone.equals(SESSION.getInstance().sid) && question.accept == 0 && reply.isadopt == 0){
            caina.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            caina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    D01_QuestionDetailActivity.adoptComment(id);
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }else{
            caina.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }

        if (reply.reply_usertype == 0){
            if (reply.userPhone.equals(SESSION.getInstance().sid)){
                jubao.setVisibility(View.GONE);
                zhuiwen.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                caina.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
            }else {
                jubao.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }else {
            if (reply.userPhone.equals(SESSION.getInstance().uid)){
                jubao.setVisibility(View.GONE);
                zhuiwen.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
                caina.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                line2.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
            }else {
                jubao.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }


        jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ReportActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("from","3");
                intent.putExtra("iscomment","1");
                intent.putExtra("about_user",reply.userPhone);
                mContext.startActivity(intent);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D01_QuestionDetailActivity.deleteComment(id,question.question_id);
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        //测量view 注意这里，如果没有测量  ，下面的popupHeight高度为-2  ,因为LinearLayout.LayoutParams.WRAP_CONTENT这句自适应造成的
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();    //  获取测量后的宽度
        int popupHeight = view.getMeasuredHeight();  //获取测量后的高度
        int[] location = new int[2];

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                //这里如果返回true的话，touch事件将被拦截
                //拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        //（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        // 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
        v.getLocationOnScreen(location);

        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以

        if (AppUtils.getScHeight(mContext) - location[1] > popupHeight) {
            layout_dialog.setBackgroundResource(R.drawable.down_bg);
            popupWindow.showAsDropDown(v);
        } else {
            layout_dialog.setBackgroundResource(R.drawable.up_bg);
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        }
    }

    private void showInputView(View view,User receiver) {
        ((D01_QuestionDetailActivity)mContext).inputComment(view,receiver);
    }

    private void like(String id,String byreplyId) {
            D01_QuestionDetailActivity.like(id,byreplyId);
    }

    private void cancellike(String id) {
        D01_QuestionDetailActivity.cancellike(id);
    }


}
