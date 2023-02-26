
package com.dmy.farming.utils.imagedrag;
import android.graphics.Bitmap;

public class PHOTO_LOCAL implements Comparable<PHOTO_LOCAL>
{
	// photo id
	public long id;

	// file name
	public String title;

	// file last modified time
	public long lastModified;

	// image width
	public int width;

	// image height
	public int height;

	// image mime type
	public String mime;

	// file local path
	public String localPath;

	// file size
	public double fileSize;

	// photo id
	public long idv;

	// memory bitmap
	public Bitmap bitmap;

	// is already checked?
	public boolean isChecked;

	//滑动发送类型 1 图片 2 视频s
	public int mediaType;

	public String path;

	public String videoTime;

	@Override
	public int compareTo(PHOTO_LOCAL photo_local) {
		return (int) (photo_local.lastModified - this.lastModified);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PHOTO_LOCAL that = (PHOTO_LOCAL) o;

		return id == that.id;

	}

	@Override
	public int hashCode() {
		return (int) (idv ^ (idv >>> 32));
	}
}
