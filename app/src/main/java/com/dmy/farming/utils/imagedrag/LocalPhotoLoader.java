package com.dmy.farming.utils.imagedrag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

import org.bee.Utils.TimeUtil;

public class LocalPhotoLoader {

	private static LocalPhotoLoader photoLoader;
	private static ContentResolver contentResolver;

	public  ArrayList<PHOTO_LOCAL> localPhotoes = new ArrayList<PHOTO_LOCAL>();//图片
	public static ArrayList<PHOTO_LOCAL> localPhotoesv = new ArrayList<PHOTO_LOCAL>();//视频

	private Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private Uri contentUriv = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;//视频
	Bitmap vbitmap;
	Bitmap bitmap;
	File file;

//	ContentUris

	private String[] projection = {
			Media._ID,
			Media.DISPLAY_NAME,
			Media.DATA,
			Media.DATE_MODIFIED,
			Media.WIDTH,
			Media.HEIGHT,
			Media.MIME_TYPE,
			Media.SIZE,
	};

	private String[] projectionv = {
			MediaStore.Video.Media._ID,
			MediaStore.Video.Media.DISPLAY_NAME,
			MediaStore.Video.Media.DATA,
			MediaStore.Video.Media.DATE_MODIFIED,
			MediaStore.Video.Media.WIDTH,
			MediaStore.Video.Media.HEIGHT,
			MediaStore.Video.Media.MIME_TYPE,
			MediaStore.Video.Media.SIZE,
			MediaStore.Video.Thumbnails._ID,
			MediaStore.Video.Thumbnails.DATA,
			MediaStore.Video.Media.DURATION
	};

	private String where =  "mime_type in ('image/jpeg', 'image/png') and _data not like '%/Bang/video%' " ;
	private String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc limit 20 offset 0";

	private String wherev =  "mime_type in ('video/mp4') " ;
	private String sortOrderv = MediaStore.Video.Media.DATE_MODIFIED + " desc limit 20 offset 0";

	public static LocalPhotoLoader instance(ContentResolver pContentResolver){
		if(photoLoader == null){
			contentResolver = pContentResolver;
			photoLoader = new LocalPhotoLoader();

		}
		return photoLoader;
	}

	private LocalPhotoLoader(){

	}

	public void refresh(){
		Cursor cursor = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
			cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(cursor == null){

		}else if(!cursor.moveToFirst()){
			//Log.v(TAG,"Line(39  )   Music Loader cursor.moveToFirst() returns false.");
		}else{
			int idCol = cursor.getColumnIndex(Media._ID);
			int titleCol = cursor.getColumnIndex(Media.DISPLAY_NAME);
			int timeCol = cursor.getColumnIndex(Media.DATE_MODIFIED);
			int widthCol = cursor.getColumnIndex(Media.WIDTH);
			int heightCol = cursor.getColumnIndex(Media.HEIGHT);
			int mimeCol = cursor.getColumnIndex(Media.MIME_TYPE);
			int dataCol = cursor.getColumnIndex(Media.DATA);
			int sizeCol = cursor.getColumnIndex(Media.SIZE);

			localPhotoes.clear();

			do{
				long id = cursor.getLong(idCol);
				String title = cursor.getString(titleCol);
				long time = cursor.getLong(timeCol);
				int width = cursor.getInt(widthCol);
				int height = cursor.getInt(heightCol);
				String mimeType = cursor.getString(mimeCol);
				String data = cursor.getString(dataCol);
				double size = cursor.getDouble(sizeCol);
				boolean bool = title != null && title.contains("bimage");

				if (width > 30 && height > 30 && bool == false)
				{
					PHOTO_LOCAL music = new PHOTO_LOCAL();
//					int degree = Utils.readPictureDegree(data);
//					if (degree == 0){
//						music.bitmap = null;
//						music.id = id;
//						music.title = title;
//						music.lastModified = time;
//						music.width = width;
//						music.height = height;
//						music.mime = mimeType;
//						music.localPath = "file://" + data;
//						music.path = data;
//						music.fileSize = size;
//
//						music.isChecked = false;
//						music.mediaType = a1;
//						localPhotoes.add(music);
//					}else{
//						bitmap = BitmapFactory.decodeFile(data, options);
//						bitmap = Utils.rotaingImageView(degree, bitmap);
//						music.bitmap = bitmap;
//						music.id = id;
//						music.title = title;
//						music.lastModified = time;
//						music.width = height;
//						music.height = width;
//						music.mime = mimeType;
//						music.localPath = "file://" + data;
//						music.path = data;
//						music.fileSize = size;
//
//						music.isChecked = false;
//						music.mediaType = a1;
//						localPhotoes.add(music);
//					}

					music.id = id;
					music.title = title;
					music.lastModified = time;
					music.width = width;
					music.height = height;
					music.mime = mimeType;
					music.localPath = "file://" + data;
					music.path = data;
					music.fileSize = size;
					music.bitmap = null;
					music.isChecked = false;
					music.mediaType = 1;
					localPhotoes.add(music);
				}

			}while(cursor.moveToNext());
		}
//		ArrayList<PHOTO_LOCAL> localPhotoesvTempView = new ArrayList<PHOTO_LOCAL>();
//		try{
//			cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);
//			while (cursor.moveToNext()){
//			int idCol = cursor.getColumnIndex(Media._ID);
//			int titleCol = cursor.getColumnIndex(Media.DISPLAY_NAME);
//			int timeCol = cursor.getColumnIndex(Media.DATE_MODIFIED);
//			int widthCol = cursor.getColumnIndex(Media.WIDTH);
//			int heightCol = cursor.getColumnIndex(Media.HEIGHT);
//			int mimeCol = cursor.getColumnIndex(Media.MIME_TYPE);
//			int dataCol = cursor.getColumnIndex(Media.DATA);
//			int sizeCol = cursor.getColumnIndex(Media.SIZE);
//
//			long id = cursor.getLong(idCol);
//			String title = cursor.getString(titleCol);
//			long time = cursor.getLong(timeCol);
//			int width = cursor.getInt(widthCol);
//			int height = cursor.getInt(heightCol);
//			String mimeType = cursor.getString(mimeCol);
//			String data = cursor.getString(dataCol);
//			double size = cursor.getDouble(sizeCol);
//			boolean bool = title.contains("bimage");
//
//			if (width > 30 && height > 30&&bool == false)
//				{
//					PHOTO_LOCAL music = new PHOTO_LOCAL();
//					music.id = id;
//					music.title = title;
//					music.lastModified = time;
//					music.width = width;
//					music.height = height;
//					music.mime = mimeType;
//					music.localPath = "file://" + data;
//					music.path = data;
//					music.fileSize = size;
//					music.bitmap = null;
//					music.isChecked = false;
//					music.mediaType = a1;
//					localPhotoesvTempView.add(music);
//				}
//			}
//			//目标localPhotoesv里面没有对应的id缩略图，生成缩略图，并放置到目标localPhotoesv里
//			for (int i = 0 ; i < localPhotoesvTempView.size() ; i++) {
//				PHOTO_LOCAL per = localPhotoesvTempView.get(i);
//				if (!localPhotoes.contains(per)) {
//					int degree = Utils.readPictureDegree(per.path);
//					if (degree != 0){
//						//需要旋转 并将 生成的bitmap保存起来
//						bitmap = BitmapFactory.decodeFile(per.path, options);
//						bitmap = Utils.rotaingImageView(degree, bitmap);
//						per.bitmap = bitmap;
//						localPhotoes.add(per);
//					}else{
//						//不需要旋转 正常 保存即可
//						localPhotoes.add(per);
//					}
//				}
//			}
//
//			//目标localPhotoesv里有多余的id缩略图，删除对应的id缩略图
//			ArrayList<PHOTO_LOCAL> localPhotoesvRemoveView = new ArrayList<PHOTO_LOCAL>();
//			for (int i = 0 ; i < localPhotoes.size() ; i++) {
//				PHOTO_LOCAL per = localPhotoes.get(i);
//				if (!localPhotoesvRemoveView.contains(per)) {
//					localPhotoesvRemoveView.add(per);
//				}
//			}
//			localPhotoes.removeAll(localPhotoesvRemoveView);
//
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}

		ArrayList<PHOTO_LOCAL> localPhotoesvTemp = new ArrayList<PHOTO_LOCAL>();
		try {
			Cursor cursorv = contentResolver.query(contentUriv, projectionv, wherev, null, sortOrderv);
			while (cursorv.moveToNext()) {
				int idColv = cursorv.getColumnIndex(MediaStore.Video.Media._ID);
				int titleColv = cursorv.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
				int timeColv = cursorv.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED);
				int widthColv = cursorv.getColumnIndex(MediaStore.Video.Media.WIDTH);
				int heightColv = cursorv.getColumnIndex(MediaStore.Video.Media.HEIGHT);
				int mimeColv = cursorv.getColumnIndex(MediaStore.Video.Media.MIME_TYPE);
				int dataColv = cursorv.getColumnIndex(MediaStore.Video.Media.DATA);
				int sizeColv = cursorv.getColumnIndex(MediaStore.Video.Media.SIZE);
				int ThumbnailsId = cursorv.getColumnIndex(MediaStore.Video.Thumbnails._ID);
				//			int ThumbnailsImageId = cursorv.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
				int ThumbnailsData = cursorv.getColumnIndex(MediaStore.Video.Thumbnails.DATA);

				int duration = cursorv.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

				long idv = cursorv.getLong(idColv);
				String titlev = cursorv.getString(titleColv);
				long timev = cursorv.getLong(timeColv);
				int widthv = cursorv.getInt(widthColv);
				int heightv = cursorv.getInt(heightColv);
				String mimeTypev = cursorv.getString(mimeColv);
				String datav = cursorv.getString(dataColv);
				double sizev = cursorv.getDouble(sizeColv);
				boolean bool = titlev.contains("bimage");
				int _id = cursorv.getInt(ThumbnailsId);
				String _data = cursorv.getString(ThumbnailsData);
				int videoTime = cursorv.getInt(duration);


				//widthv > 30 && heightv > 30 &&
				if (sizev<=20971520) {
					PHOTO_LOCAL musicv = new PHOTO_LOCAL();
					musicv.id = _id;
					musicv.title = titlev;
//					file = new File("file://"+datav);
//					musicv.lastModified = file.lastModified();
					musicv.lastModified = timev;
					musicv.width = widthv;
					musicv.height = heightv;
					musicv.mime = mimeTypev;
					musicv.localPath = "file://" + _data;
					musicv.fileSize = sizev;
					musicv.path = datav;
					musicv.bitmap = null;
					musicv.isChecked = false;
					musicv.mediaType = 2;
					musicv.videoTime = TimeUtil.formatVideoTime(videoTime);
					localPhotoesvTemp.add(musicv);
				}
			}

			//目标localPhotoesv里面没有对应的id缩略图，生成缩略图，并放置到目标localPhotoesv里
			for (int i = 0 ; i < localPhotoesvTemp.size() ; i++) {
				PHOTO_LOCAL per = localPhotoesvTemp.get(i);
				if (!localPhotoesv.contains(per)) {
					Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, per.id, MediaStore.Images.Thumbnails.MINI_KIND, options);
					per.bitmap = bitmap;
					localPhotoesv.add(per);
				}
			}

			//目标localPhotoesv里有多余的id缩略图，删除对应的id缩略图
			ArrayList<PHOTO_LOCAL> localPhotoesvRemove = new ArrayList<PHOTO_LOCAL>();
			for (int i = 0 ; i < localPhotoesv.size() ; i++) {
				PHOTO_LOCAL per = localPhotoesv.get(i);
				if (!localPhotoesvTemp.contains(per)) {
					localPhotoesvRemove.add(per);
				}
			}
			localPhotoesv.removeAll(localPhotoesvRemove);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// kcm
		//localPhotoes.addAll(localPhotoesv);

		Collections.sort(localPhotoes);
//		ArrayList<PHOTO_LOCAL> list = new ArrayList<PHOTO_LOCAL>();
//		if (localPhotoes.size()>20){
//			for (int i = 0 ;i <20; i++){
//				list.add(localPhotoes.get(i));
//			}
//			localPhotoes = list;
//		}

	}
}
