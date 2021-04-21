package org.jzs.mybaseapp.section.otherdemo.waterpic.photoinfodialog;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.section.otherdemo.waterpic.PhotoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决多个 EditText 在 ListView 滑动时数据错位
 * Created by Jzs on 2017/9/23 0023.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<PhotoEntity> mList = new ArrayList<>();

    public PhotoAdapter(List<PhotoEntity> list) {
        this.mList = list;
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
        // so that we don't have to do this expensive allocation in onBindViewHolder
        ViewHolder vh = new ViewHolder(v, new TitleListener(), new ContentListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // update MyCustomEditTextListener every time we bind a new item
        // so that it knows what item in mDataset to update
        holder.mTitleListener.updatePosition(holder.getAdapterPosition());
        holder.mContentListener.updatePosition(holder.getAdapterPosition());
        holder.mEt_title.setText(mList.get(holder.getAdapterPosition()).title);
        holder.mEt_content.setText(mList.get(holder.getAdapterPosition()).content);
        holder.mIv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getAdapterPosition() != -1) {
                    mList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(0, mList.size() - position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public EditText mEt_title;
        public EditText mEt_content;
        public ImageView mIv_del;
        public TitleListener mTitleListener;
        public ContentListener mContentListener;

        public ViewHolder(View v, TitleListener titleListener, ContentListener contentListener) {
            super(v);

            this.mEt_title = (EditText) v.findViewById(R.id.et_title);
            this.mEt_content = (EditText) v.findViewById(R.id.et_content);
            this.mIv_del = (ImageView) v.findViewById(R.id.iv_del);
            this.mTitleListener = titleListener;
            this.mContentListener = contentListener;
            this.mEt_title.addTextChangedListener(titleListener);
            this.mEt_content.addTextChangedListener(contentListener);
        }

    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class TitleListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mList.get(position).title = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class ContentListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mList.get(position).content = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
