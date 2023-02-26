package com.dmy.farming.utils.imagedrag;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnBottomImageListener {

    // 通知 开始发送图片
    void onSendImage(PHOTO_LOCAL photo);

    // 通知 转发图片
    void onShareImage(PHOTO_LOCAL photo);

    // 通知 有变化点选状态
    void onChangeChecked(int checkedCount);
}
