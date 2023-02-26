package com.dmy.farming.utils.imagedrag;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Interface to listen for a move or dismissal event from a {@link ItemTouchHelper.Callback}.
 *
 * @author Paul Burke (ipaulpro)
 */
public interface ItemTouchHelperAdapter {

    // 拖动开始
    void onDragStart(RecyclerView.ViewHolder viewHolder, float dY);

    // 拖动中
    void onDragging(RecyclerView.ViewHolder viewHolder, float dY);

    // 拖动停止
    void onDragEnd(RecyclerView.ViewHolder viewHolder, float dY);

    // 拖动删除
    void onDismiss();

}
