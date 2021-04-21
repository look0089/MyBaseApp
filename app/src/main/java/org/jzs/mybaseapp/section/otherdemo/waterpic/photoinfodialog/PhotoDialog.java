package org.jzs.mybaseapp.section.otherdemo.waterpic.photoinfodialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.section.otherdemo.waterpic.PhotoEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 图片水印设置dialog
 */
public class PhotoDialog implements OnClickListener {
    public static final String TAG = "PhotoDialog";
    private Dialog dialog;

    private Context context;

    private DialogCallBack mCallBack;
    private RecyclerView rv;
    public static List<PhotoEntity> mList = new ArrayList<>();
    private PhotoAdapter mAdapter;

    public Dialog initDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.custom_dialog);
        dialog.setContentView(R.layout.dialog_photo);
        dialog.findViewById(R.id.btn_other_add).setOnClickListener(this);
        dialog.findViewById(R.id.btn_other_cancel).setOnClickListener(this);
        dialog.findViewById(R.id.btn_other_ok).setOnClickListener(this);
        initRecycle();
        return dialog;
    }

    public void show() {
        dialog.show();
    }

    public void initRecycle() {
        rv = (RecyclerView) dialog.findViewById(R.id.rv);
        for (int i = 0; i < 5; i++) {
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.title = "项目" + i;
            photoEntity.content = "内容" + i;
            mList.add(photoEntity);
        }
        rv.setItemViewCacheSize(mList.size());
        mAdapter = new PhotoAdapter(mList);
        rv.setAdapter(mAdapter);
    }


    public void setCancleBackground(int res) {
        ((Button) dialog.findViewById(R.id.btn_other_cancel)).setBackground(ContextCompat.getDrawable(context, res));
    }

    public void setCallBack(PhotoDialog.DialogCallBack dialogCallBack) {
        this.mCallBack = dialogCallBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_other_cancel:
                if (mCallBack != null) {
                    mCallBack.cancel();
                }
                dialog.dismiss();
                break;
            case R.id.btn_other_ok:
                if (mCallBack != null) {
                    mCallBack.sure();
                }
                dialog.dismiss();
                break;
            case R.id.btn_other_add:
                PhotoEntity photoEntity = new PhotoEntity();
                mList.add(photoEntity);
                mAdapter.notifyItemInserted(mList.size());
                mAdapter.notifyItemRangeChanged(0, mList.size() - 1);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public List<PhotoEntity> getList() {
        return mList;
    }

    public interface DialogCallBack {

        void sure();

        void cancel();
    }

}
