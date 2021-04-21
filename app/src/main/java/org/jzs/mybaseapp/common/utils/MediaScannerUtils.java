package org.jzs.mybaseapp.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.得到所有相册名字
 * 2.即时更新媒体库(通过扫描文件名)
 *
 */
public class MediaScannerUtils
{

	private static MediaScannerConnection mConnection;

	/**
	 * 即时更新媒体库
	 * 
	 * @param context
	 * @param filename
	 *            新增的文件名(路径+文件名+格式)
	 */
	public static void mediaScan(final Context context, final String filename)
	{
		MediaScannerConnectionClient client = new MediaScannerConnectionClient()
		{
			@Override
			public void onScanCompleted(String path, Uri uri)
			{
				// 扫描完成 断开连接
				mConnection.disconnect();
				Log.e("onScanCompleted", Thread.currentThread().getName() + "_Thread" + "--->" + "completed");
			}

			@Override
			public void onMediaScannerConnected()
			{
				Log.e("onMediaScannerConnected", Thread.currentThread().getName() + "_Thread" + "--->" + "connected");
				mConnection.scanFile(filename, null);
			}
		};
		mConnection = new MediaScannerConnection(context, client);
		// 扫描连接开始
		mConnection.connect();
	}

	/**
	 * 得到所有相册名
	 * 
	 * @param context
	 * @return
	 */
	public static List<String> getAllBucketDisplayName(Context context)
	{
		List<String> bucket = new ArrayList<String>();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[]
		{ ImageColumns.BUCKET_DISPLAY_NAME }, null, null, null);
		boolean first = cursor.moveToFirst();
		while (first)
		{
			String bucketDisplayName = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
			if(!bucket.contains(bucketDisplayName))
			{
				bucket.add(bucketDisplayName);
			}
			first = cursor.moveToNext();
		}
		cursor.close();
		return bucket;
	}
}
