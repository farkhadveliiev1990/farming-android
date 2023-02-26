package com.dmy.farming.utils.imagedrag;

import java.util.ArrayList;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnPictureDragListener {

    // 通知 开始发送图片
    void onSendImages(ArrayList<PHOTO_LOCAL> photos);

    // 滑动发送
    void onSendImage(PHOTO_LOCAL photo);

    // 点击拍照
    void onClickCamera();

    // 点击全部相册按钮
    void onClickAlbum();
}
