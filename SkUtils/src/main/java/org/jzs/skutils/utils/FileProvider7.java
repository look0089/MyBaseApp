package org.jzs.skutils.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * Created by zhanghongyang01 on 17/5/31.
 * 适配 Android 7.0 和 8.0
 * <p>
 * Change by jzs on 18/9/28
 * <p>
 * authority为context.getPackageName() + ".fileprovider"
 * 务必确认已在 AnroidManifest.xml添加了 provider 配置
 * 以及在xml文件夹添加了 file_paths_public 文件
 */
public class FileProvider7 {

    public static final String TAG = "FileProvider7";

    /**
     * 获取 Uri
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    public static Uri getUriForFile24(Context context, File file) {
        Log.e(TAG, "authority: " + context.getPackageName() + ".fileprovider");
        Uri fileUri = android.support.v4.content.FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", file);
        return fileUri;
    }

    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeAble) {
        int flag = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeAble) {
            flag |= Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }


    public static void setIntentData(Context context, Intent intent, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setData(getUriForFile(context, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setData(Uri.fromFile(file));
        }
    }

    /**
     * 安装 apk
     * <p>
     * FileProvider7.setIntentDataAndType(this,intent, "application/vnd.android.package-archive", file, true);
     *
     * @param context
     * @param intent
     * @param type
     * @param file
     * @param writeAble
     */
    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }
}