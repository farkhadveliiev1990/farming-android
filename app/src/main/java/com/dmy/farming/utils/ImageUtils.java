package com.dmy.farming.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ImageUtils {

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static String saveImageToExternalStorage(Context context, Bitmap bmp, String dirPath, String fileName, boolean isPNG) {

        String fullPath = dirPath + "/" + fileName;

        FileOutputStream b = null;
        ObjectOutputStream oos = null;
        try {
            File dir = new File(dirPath);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(fullPath);
            if (file.exists())
                file.delete();

            b = new FileOutputStream(fullPath);

            if (isPNG)
                bmp.compress(Bitmap.CompressFormat.PNG, 80, b);// 把数据写入文件
            else
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, b);// 把数据写入文件
//			}
        } catch (Exception e) {
            e.printStackTrace();
            fullPath = "";
        } finally {
            try {
                if (b != null)
                {
//					if (!isPattern)
                    b.flush();
                    b.close();
                }

                if (oos != null)
                {
                    try {
                        oos.close();
                    } catch (IOException e) {
                        // oos流关闭异常
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fullPath;
    }

}
