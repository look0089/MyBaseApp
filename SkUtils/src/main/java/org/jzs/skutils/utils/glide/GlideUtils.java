package org.jzs.skutils.utils.glide;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jzs.skutils.AppContext.Applications;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GlideUtils {
    static RequestOptions options = new RequestOptions()
            .placeholder(org.jzs.skutils.R.drawable.ic_default_picture)
            .error(org.jzs.skutils.R.drawable.ic_default_picture)
            .dontAnimate();

    static RequestOptions circleCropOptions = new RequestOptions()
            .circleCrop()
            .placeholder(org.jzs.skutils.R.drawable.ic_default_picture)
            .error(org.jzs.skutils.R.drawable.ic_default_picture)
            .dontAnimate();

    private static RequestOptions getroundedCornersOptions(int angle) {
        return new RequestOptions()
                .transform(new RoundedCorners(10))
                .placeholder(org.jzs.skutils.R.drawable.ic_default_picture)
                .error(org.jzs.skutils.R.drawable.ic_default_picture)
                .dontAnimate();
    }


    private RequestOptions getRequestOptions() {
        RequestOptions options = new RequestOptions();
        //options.format(DecodeFormat.PREFER_ARGB_8888)
        //options.centerCrop()//图片显示类型
        //options.placeholder(loadingRes)//加载中图片
        //options.error(errorRes)//加载错误的图片
        //options.error(new ColorDrawable(Color.RED))//或者是个颜色值
        //options.priority(Priority.HIGH)//设置请求优先级
        //options.diskCacheStrategy(DiskCacheStrategy.ALL);
        //options.diskCacheStrategy(DiskCacheStrategy.RESOURCE)//仅缓存原图片
        //options.diskCacheStrategy(DiskCacheStrategy.ALL)//全部缓存
        //options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)缓存缩略过的
        //options.onlyRetrieveFromCache(true)//仅从缓存加载
        //options.skipMemoryCache(true)//禁用缓存,包括内存和磁盘
        //options.diskCacheStrategy(DiskCacheStrategy.NONE)仅跳过磁盘缓存
        //options.override(200,200)加载固定大小的图片
        //options.dontTransform()不做渐入渐出的转换
        //options.transition(new DrawableTransitionOptions().dontTransition())//同上
        //options.circleCrop()设置成圆形头像<这个是V4.0新增的>
        //options.transform(new RoundedCorners(10))设置成圆角头像<这个是V4.0新增的>
        return options;
    }

    /**
     * 加载图片(无动画)
     */
    public static void setImage(String url, ImageView iv) {
        Glide.with(Applications.context())
                .load(url)
                .thumbnail(0.1f)
                .apply(options)
                .into(iv);
    }


    public static void setImage(Bitmap url, ImageView iv) {
        Glide.with(Applications.context())
                .load(url)
                .thumbnail(0.1f)
                .apply(options)
                .into(iv);
    }

    /**
     * 加载图片(有淡入淡出)
     *
     * @param url
     * @param iv
     */
    public static void setCrossFadeImage(String url, ImageView iv) {
        Glide.with(Applications.context())
                .load(url)
                .apply(options)
                .transition(withCrossFade())
                .into(iv);
    }

    /**
     * 圆形图片
     */
    public static void setCircleCropImage(String url, ImageView iv) {
        Glide.with(Applications.context())
                .load(url)
                .apply(circleCropOptions)
                .into(iv);
    }

    /**
     * 圆角图片
     */
    public static void setRoundedCornersImage(String url, ImageView iv, int angle) {
        Glide.with(Applications.context())
                .load(url)
                .apply(getroundedCornersOptions(angle))
                .into(iv);
    }

    /**
     * 自定义options
     */
    public static void setOptionsImage(String url, ImageView iv, RequestOptions options) {
        Glide.with(Applications.context())
                .load(url)
                .thumbnail(0.1f)
                .apply(options)
                .into(iv);
    }
}
