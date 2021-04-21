package org.jzs.mybaseapp.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 常用工具类
 * <p>
 * Description: 包括 1. 判断网络是否连接正常 2. 隐藏键盘 3. 显示EditText错误信息 4. 创建快捷方式 5. 删除快捷方式
 * 6. 图片切换特效 7. px,dip,sp的相互转换 8. 截屏
 *
 * @author zhouyujing
 * @version v1.0
 * @e-mail 1032668839@qq.com
 * @copyright 2010-2015
 * @create-time 2015年10月19日 下午2:29:34
 */
public class CommonUtils {
    /**
     * 判断网络是否可用
     *
     * @param context
     * @return true可用 false 不可用
     */
    public static boolean isNetConnectionAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isAvailable()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean is3g2g(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 显示键盘
     *
     * @param mContext
     * @param v
     */
    public static void showKeyboard(Context mContext, View v) {
        v.requestFocus();
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v, 0);
    }

    /**
     * 隐藏键盘
     *
     * @param a
     */
    public static void hideKeyboard(final Activity a) {
        if (a == null || a.getCurrentFocus() == null)
            return;
        InputMethodManager inputManager = (InputMethodManager) a.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * EidtText控件显示错误信息 参数以String传入
     *
     * @param et
     * @param error     字符串参数
     * @param animation
     */
    public static void showErrorByEditText(EditText et, String error, Animation animation) {
        et.requestFocus();
        SpannableString ss = new SpannableString(error);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, error.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setError(ss);
        et.startAnimation(animation);
    }

    /**
     * EditText控件显示错误信息 参数以R.string.xxx传入
     *
     * @param et
     * @param resId     资源ID
     * @param animation
     */
    public static void showErrorByEditText(EditText et, int resId, Animation animation) {
        String error = et.getResources().getString(resId);
        et.requestFocus();

        SpannableString ss = new SpannableString(error);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, error.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setError(ss);
        et.startAnimation(animation);
    }

    /**
     * 隐藏EditText控件错误信息
     *
     * @param et
     */
    public static void hideErrorByEditText(EditText et) {
        et.requestFocus();
        et.setError(null);
    }


    /**
     * 判断是否存在快捷方式
     *
     * @param ctx
     * @return
     */
    public static boolean hasShortcut(Context ctx, int app_name) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = ctx.getContentResolver();
        final String AUTHORITY;
        // 在andriod 2.1即SDK7以上，是读取launcher.settings中的favorites表的数据；
        // 在andriod 2.2即SDK8以上，是读取launcher2.settings中的favorites表的数据。
        if (AppHelper.getSystemVersion() < 8) {
            AUTHORITY = "com.android.launcher.settings";
        } else {
            AUTHORITY = "com.android.launcher2.settings";
        }

        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{ctx.getString(app_name).trim()}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }

    /**
     * 添加快捷方式
     */
    public static void createShortcut(Context ctx, int app_name, int icon, Class<?> clazz) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(app_name));
        shortcut.putExtra("duplicate", false); // 不允许重复创建
        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
        // String appClass = act.getPackageName() + "." + launchActivity;
        // ComponentName comp = new ComponentName(act.getPackageName(), clazz);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setClass(ctx, clazz));
        // 快捷方式的图标
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(ctx, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        ctx.sendBroadcast(shortcut);
    }

    /**
     * 删除快捷方式
     */
    public static void removeShortcut(Context ctx, int app_name, Class<?> clazz) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(app_name));
        // 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
        // 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
        // String appClass = act.getPackageName() + ".WelcomeIndexActivity";
        // ComponentName comp = new ComponentName(act.getPackageName(),
        // appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setClass(ctx, clazz));
        ctx.sendBroadcast(shortcut);
    }

    /**
     * 获取网络制式
     *
     * @param context
     * @return
     */
    public static String getAccessType(Context context) {
        // 网络类型
        String access = "";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null) {
            int netType = info.getType();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                access = "wifi";
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                access = "2G/3G";
            }
        }

        return access;
    }

    /**
     * 获取屏幕密度比例
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取文字密度比例
     *
     * @param context
     * @return
     */
    public static float getScaledDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 转换dip为px
     *
     * @param context
     * @param dip     值
     * @return
     */
    public static int convertDipToPx(Context context, double dip) {
        float scale = getScreenDensity(context);
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip
     *
     * @param context
     * @param px      值
     * @return
     */
    public static int convertPxToDip(Context context, int px) {
        float scale = getScreenDensity(context);
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 将PX转SP
     *
     * @param context
     * @param pxValue px值
     * @return
     */
    public static int convertPxToSp(Context context, float pxValue) {
        float fontScale = getScaledDensity(context);
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将SP转PX
     *
     * @param context
     * @param spValue sp值
     * @return
     */
    public static int convertSpToPx(Context context, float spValue) {
        float fontScale = getScaledDensity(context);
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 保存图片路径
     *
     * @param bitmap
     * @param dir
     * @param fileName
     * @return
     */
    public static File save(final Bitmap bitmap, String dir, String fileName) {
        if (bitmap == null)
            return null;

        String absolutePath = dir;
        AppLog.greenLog("", "absolutePath " + absolutePath);
        AppLog.greenLog("", "fileName " + fileName);

        File f = new File(absolutePath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                AppLog.redLog("", "mkdirs error:" + absolutePath);
            }
        }

        File mf = new File(absolutePath + "/" + fileName);
        OutputStream outputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
        byte[] jpegData = out.toByteArray();

        try {
            outputStream = new FileOutputStream(mf);
            outputStream.write(jpegData);
        } catch (FileNotFoundException e) {
            AppLog.redLog("", "FileNotFoundException error message:" + e.getMessage());
        } catch (IOException e) {
            AppLog.redLog("", "IOException error desc:" + e.getMessage());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mf;
    }

    /**
     * 获取根路径
     */
    public static String getSdcardDir() {
        return Environment.getExternalStorageDirectory().toString();
    }

    /**
     * 打电话
     *
     * @param context
     * @param tel     电话号码
     */
    public static void call(Context context, String tel) {
        // Intent intent = new Intent();
        // intent.setAction(Intent.ACTION_CALL);
        // intent.setData(Uri.parse("tel" + tel));// 传入一个URI Uri.parse(“tel:” +
        // 电话号码)
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        context.startActivity(intent);
    }

    /**
     * 发短信
     *
     * @param context
     * @param tel     电话号码
     * @param content 短信内容
     */
    public static void sendMsg(Context context, String tel, String content) {
        // 发送短信息
        Uri uri = Uri.parse("smsto:" + tel);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * 保存通讯录
     *
     * @param context
     * @param name
     * @param tel
     * @param company
     */
    public static void saveAdressList(Context context, String name, String tel, String company) {
        // 插入raw_contacts表，并获取_id属性
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        // 插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        // add Name
        values.put("raw_contact_id", contact_id);
        values.put(Data.MIMETYPE, "vnd.android.cursor.item/name");
        values.put("data1", name);// 姓名
        resolver.insert(uri, values);
        values.clear();
        // add Phone
        values.put("raw_contact_id", contact_id);
        values.put(Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
        values.put("data1", tel); // 手机
        resolver.insert(uri, values);
        values.clear();
        // add email
        values.put("raw_contact_id", contact_id);
        values.put(Data.MIMETYPE, "vnd.android.cursor.item/organization");
        values.put("data1", company); // 单位
        resolver.insert(uri, values);
    }


    /**
     * 查找联系人，判断是否已经添加
     *
     * @param context
     * @return
     */
    public static boolean getReadAllContacts(Context context, String phone) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null);
        int contactIdIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            /*
             * 查找该联系人的phone信息
			 */
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                if (phone.equals(phoneNumber)) {
                    return false;
                }
            }
        }
        return true;
    }


    private static long lastClickTime;// 最后点击时间

    /**
     * 防止重复点击
     *
     * @return
     */
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;

    }


    public static void setRecyclerManage(Context context, RecyclerView rv, int orientation) {
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        // LinearLayoutManager.VERTICAL
        // LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(orientation);
        // 设置布局管理器
        rv.setLayoutManager(layoutManager);
        //分割线
//        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    public static void setRecyclerGridManage(Context context, RecyclerView rv, int spanCount) {
        // 创建一个Grid布局管理器
        GridLayoutManager mgr = new GridLayoutManager(context, spanCount);
        mgr.setOrientation(GridLayoutManager.VERTICAL);
        // 设置布局管理器
        rv.setLayoutManager(mgr);
        //分割线
        int spacing = 10;//每一个矩形的间距
        //设置每个item间距
        rv.addItemDecoration(new GridSpacingItemDecoration(spacing));
//        rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public GridSpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.bottom = spacing;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = spacing;
            } else {
                outRect.top = 0;
            }
        }
    }
}
