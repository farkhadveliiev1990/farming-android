package com.dmy.farming.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final boolean DEBUG = false;
    /**
     * 获取可读的文件大小
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;
        if(size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if(fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if(fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }
    /**
     * 获取文件的文件名(不包括扩展名)
     */
    public static String getFileNameWithoutExtension(String path) {
        if(path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        if(separatorIndex < 0) {
            separatorIndex = 0;
        }
        int dotIndex = path.lastIndexOf(".");
        if(dotIndex < 0) {
            dotIndex = path.length();
        } else if(dotIndex < separatorIndex) {
            dotIndex = path.length();
        }
        return path.substring(separatorIndex + 1, dotIndex);
    }
    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        if(path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
    /**
     * 获取扩展名
     */
    public static String getExtension(String path) {
        if(path == null) {
            return null;
        }
        int dot = path.lastIndexOf(".");
        if(dot >= 0) {
            return path.substring(dot);
        } else {
            return "";
        }
    }

    public static void openFile(File file, Activity context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        String mimeType = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), mimeType);

        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_LONG).show();
        }
    }

    public static String getMIMEType(File file) {
        String mimeType;
        String fileName = file.getName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt);
        return mimeType;
    }

    public static File getUriFile(Context context, Uri uri) {
        String path = getUriPath(context, uri);
        if(path == null) {
            return null;
        }
        return new File(path);
    }
    @SuppressLint("NewApi") public static String getUriPath(Context context, Uri uri) {
        if(uri == null) {
            return null;
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            if("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if(cursor != null) cursor.close();
        }
        return null;
    }

    public static void copyFile(Context context, File fromFile, File toFile,Boolean rewrite )
    {
        if (!fromFile.exists()) {
            return;
        }

        if (!fromFile.isFile()) {
            return;
        }

        if (!fromFile.canRead()) {
            return;
        }

        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }

        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
            java.io.FileOutputStream fosto = new FileOutputStream(toFile);

            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }

            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            //Log.e("readfile", ex.getMessage());
        }

        MediaScannerConnection.scanFile(context, new String[]{toFile.getAbsolutePath()}, null, null);
    }
}
