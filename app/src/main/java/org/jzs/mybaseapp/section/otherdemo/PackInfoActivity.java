package org.jzs.mybaseapp.section.otherdemo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.databinding.ActivityPackinfoBinding;
import org.jzs.mybaseapp.databinding.ItemPackageBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Jzs created 2017/8/2
 */

public class PackInfoActivity extends AppCompatActivity {
    public static final String TAG = "PackInfoActivity";

    ActivityPackinfoBinding mBinding;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_packinfo);

        packageManager = this.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        List<PackageInfo> myApp = new ArrayList<>();

        for (PackageInfo info : installedPackages) {
            ApplicationInfo appInfo = info.applicationInfo;
            if (!((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)) {
                myApp.add(info);
            }
        }
        Log.e(TAG, "应用总数: " + myApp.size());
        ListAdapter mAdapter = new ListAdapter();
        mAdapter.setDataList(myApp);
        mBinding.rvMain.setAdapter(mAdapter);
    }

    /**
     * 列表适配器
     */
    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> {
        private List<PackageInfo> mList;

        public void setDataList(List<PackageInfo> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(PackInfoActivity.this), R.layout.item_package, parent, false));
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            final PackageInfo itemBean = mList.get(position);
            holder.mDataBinding.ivIcon.setImageDrawable(itemBean.applicationInfo.loadIcon(packageManager));
            holder.mDataBinding.tvPackageName.setText(itemBean.applicationInfo.loadLabel(packageManager).toString());
            holder.mDataBinding.tvPackageApplicationId.setText(itemBean.applicationInfo.packageName);
            holder.mDataBinding.executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            private ItemPackageBinding mDataBinding;

            public ItemViewHolder(ViewDataBinding dataBinding) {
                super(dataBinding.getRoot());
                mDataBinding = (ItemPackageBinding) dataBinding;
            }
        }

    }

}
