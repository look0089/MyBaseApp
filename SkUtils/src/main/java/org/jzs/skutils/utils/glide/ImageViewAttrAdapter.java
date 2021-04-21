package org.jzs.skutils.utils.glide;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * 在databinding中使用src="@{data.img}"载入图片
 * Created by Jzs on 2017/12/13 0013.
 */

public class ImageViewAttrAdapter {
    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView view, String url) {
        GlideUtils.setImage(url, view);
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
//        RequestOptions options = new RequestOptions()
//                .placeholder(holderDrawable)
//                .error(errorDrawable);
        Glide.with(imageView.getContext()).load(url)
//                .apply(options)
                .into(imageView);
    }
}
