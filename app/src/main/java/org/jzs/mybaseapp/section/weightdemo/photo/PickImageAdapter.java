package org.jzs.mybaseapp.section.weightdemo.photo;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.Applications;
import org.jzs.mybaseapp.common.utils.GlideUtils;
import org.jzs.mybaseapp.section.weightdemo.permission.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.FragmentActivity;

/**
 * 多张图片选择
 */
public class PickImageAdapter extends BaseAdapter {
    private int MAX_IMAGES = 8;// 最大选择数量
    public static final int REQUEST_CODE_PICK_IMAGE = 1024;
    public static final int REQUEST_CODE_CAMERA = 2048;
    public static final int REQUEST_CODE_CUT = 4096;
    private List<LocalMedia> paths = new ArrayList<LocalMedia>();
    private FragmentActivity mContext;
    private List<String> imagetitles = Arrays.asList("拍照", "从相册选择");
    private DialogiOS imageDialog;
    private File dir;
    public static String imgPath = "";

    /**
     * @param context
     * @param paths   图片路径
     */
    public PickImageAdapter(FragmentActivity context, List<LocalMedia> paths, int imagesNum) {
        this.mContext = context;
        this.paths = paths;
        this.MAX_IMAGES = imagesNum;
    }

    public void notifyDataSetChanged(List<LocalMedia> paths) {
        this.paths = paths;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (paths.size() >= MAX_IMAGES) {
            return MAX_IMAGES;
        }
        return paths.size() + 1;

    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_img_pick, container, false);
            viewHolder.ivPic = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tvDel = (TextView) convertView.findViewById(R.id.tv_del);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.ivPic.setOnClickListener(null);
            viewHolder.ivPic.setOnLongClickListener(null);
            viewHolder.tvDel.setOnClickListener(null);
            viewHolder.tvDel.setOnLongClickListener(null);
        }
        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_default_picture).error(R.drawable.ic_default_picture).dontAnimate();

        if (position == paths.size()) {
            viewHolder.tvDel.setVisibility(View.GONE);
            Glide.with(Applications.context())
                    .load(R.drawable.ic_paizhao)
                    .apply(options)
                    .thumbnail(0.1f)
                    .into(viewHolder.ivPic);

            viewHolder.ivPic.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 选择图片
                    chooseImages();
                }
            });
            return convertView;
        } else {
            viewHolder.tvDel.setVisibility(View.VISIBLE);
        }

        if (!paths.isEmpty()) {
            if (!TextUtils.isEmpty(paths.get(position).getPath())) {
                try {
                    if (paths.get(position).getPath().startsWith("http")) {
                        GlideUtils.setImage(paths.get(position).getPath(), viewHolder.ivPic);
                    } else {
                        File file = new File(paths.get(position).getPath().replace("sdcard://", ""));
                        Glide.with(Applications.context())
                                .load(file)
                                .apply(options)
                                .thumbnail(0.1f)
                                .into(viewHolder.ivPic);
                    }
                } catch (Exception e) {
                    File file = new File(paths.get(position).getPath().replace("sdcard://", ""));
                    Glide.with(Applications.context())
                            .load(file)
                            .apply(options)
                            .thumbnail(0.1f)
                            .into(viewHolder.ivPic);
                }

            }

            viewHolder.tvDel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//                    NoTitleDialog myDialog = new NoTitleDialog("确定删除吗？");
//                    myDialog.setCallBack(new NoTitleDialog.DialogCallBack() {
//                        @Override
//                        public void sure() {
//                            paths.remove(paths.get(position));
//                            notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void cancel() {
//
//                        }
//                    });
//                    myDialog.show(mContext.getFragmentManager(), "del");
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {

        ImageView ivPic;
        TextView tvDel;
    }

    /**
     * 选择图片
     */
    private void chooseImages() {

        if (imageDialog == null) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("拍照");
            list.add("从相册选择");
            imageDialog = new DialogiOS(mContext);
            imageDialog.setTitles(list);
        }

        imageDialog.setOnItemClickListener(new DialogiOS.OnDialogItemClickListener() {

            @Override
            public void onItemClick(int position, String str) {
                PermissionUtils.requestStorage(mContext, () -> {
                    switch (position) {
                        case 0:
                            // 拍照
                            PictureSelector.create(mContext)
                                    .openCamera(PictureMimeType.ofImage())
                                    .forResult(PictureConfig.SINGLE);
                            break;
                        case 1:
                            // 选择图片
                            PictureSelector.create(mContext)
                                    .openGallery(PictureMimeType.ofImage())
                                    .isCamera(false)
                                    .imageSpanCount(3)
                                    .maxSelectNum(MAX_IMAGES)
                                    .forResult(PictureConfig.SINGLE);
                            break;
                    }
                });
            }
        }).show();

    }


}
