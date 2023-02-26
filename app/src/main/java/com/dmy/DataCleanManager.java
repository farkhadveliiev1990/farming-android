package com.dmy;

import android.content.Context;

import org.bee.Utils.FileSizeUtil;

import com.dmy.farming.FarmingApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by K on 2016/10/24.
 */

public class DataCleanManager {

    public static File cacheDir = null;
    public DataCleanManager(Context context) {
        String filePath = StorageUtils.getCacheDirectory(context, false).toString();
        cacheDir = new File(filePath + FarmingApp.CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context){//, String... filepath) {
        //cleanInternalCache(context);
        //cleanExternalCache(context);
        //cleanDatabases(context);
        //cleanSharedPreference(context);
        //cleanFiles(context);

        String cacheFolder = getCacheDir(context);
        cleanCustomCache(cacheFolder);
        ImageLoader.getInstance().clearMemoryCache();
        /*
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
        */
    }

    public static String getCacheSize(Context context)
    {
        String cacheFolder = getCacheDir(context);
        return FileSizeUtil.getAutoFileOrFilesSize(cacheFolder);
    }

    public static String getCacheDir(Context context)
    {
        String rootPath = context.getCacheDir() + FarmingApp.CACHE_DIR;
        return rootPath;
    }
}
