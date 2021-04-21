package org.jzs.mybaseapp.common.BindingAdapter;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jzs on 2018/4/8.
 */

public class BaseBindingVH<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    protected final T mBinding;

    public BaseBindingVH(T t) {
        super(t.getRoot());
        mBinding = t;
    }

    public T getBinding() {
        return mBinding;
    }
}