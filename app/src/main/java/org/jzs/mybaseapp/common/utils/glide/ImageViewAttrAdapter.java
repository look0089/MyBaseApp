package org.jzs.mybaseapp.common.utils.glide;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jzs.mybaseapp.common.utils.GlideUtils;

import androidx.databinding.BindingAdapter;

/**
 * 在databinding中使用profileImage="@{data.img}"载入图片
 * Created by Jzs on 2017/12/13 0013.
 */

public class ImageViewAttrAdapter {
    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView view, String imageUrl) {
        GlideUtils.setImage(imageUrl,view);
    }
}
