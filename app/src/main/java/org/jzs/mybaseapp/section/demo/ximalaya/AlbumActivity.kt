package org.jzs.mybaseapp.section.demo.ximalaya

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.AlbumList
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingAdapter
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingVH
import org.jzs.mybaseapp.common.base.BaseActivity
import org.jzs.mybaseapp.common.system.Constant
import org.jzs.mybaseapp.common.utils.AppLog
import org.jzs.mybaseapp.common.utils.GlideUtils
import org.jzs.mybaseapp.databinding.ActivityListBinding
import org.jzs.mybaseapp.databinding.ItemAlbumBinding

/**
 * 专辑
 *
 * @author Jzs created 2019/1/2
 * @email 355392668@qq.com
 */
class AlbumActivity : BaseActivity() {
    lateinit var mBinding: ActivityListBinding
    var mList = arrayListOf<Album>()
    lateinit var mAdapter: BaseBindingAdapter<Album, ItemAlbumBinding>
    var mPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        mBinding.titleBar!!.visibility = View.GONE

        getAlbum(itemId)

        //设置刷新控件
        mBinding.refreshLayout.setOnRefreshListener { refreshlayout ->
            mList.clear()
            mPage = 1
            getAlbum(itemId)
            mBinding.refreshLayout.setNoMoreData(false)
            refreshlayout.finishRefresh()
        }
        mBinding.refreshLayout.setOnLoadMoreListener { refreshlayout ->
            mPage++
            getAlbum(itemId)
            refreshlayout.finishLoadMore()
        }

        mAdapter = object : BaseBindingAdapter<Album, ItemAlbumBinding>(this, mList, R.layout.item_album) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemAlbumBinding>, position: Int) {
                //★super一定不要删除
                super.onBindViewHolder(holder, position)
                val binding = holder.binding
                var item = mList[position]
                GlideUtils.setImage(item.coverUrlMiddle, binding.ivIcon)
            }
        }
        mBinding.rv.adapter = mAdapter
        mAdapter.itemPresenter = SingleItemPresenter()
    }


    /**
     * ★ Item点击事件P
     */
    inner class SingleItemPresenter {
        fun onItemClick(data: Album) {
            val intent = Intent(this@AlbumActivity, TracksActivity::class.java)
            intent.putExtra(Constant.EXTRA.EXTRA_ITEM_ID, data.id.toString())
            startActivity(intent)
        }
    }


    /**
     * 获取专辑
     */
    fun getAlbum(id: String) {
        var map = HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, id);//专辑id
        map.put(DTransferConstants.CALC_DIMENSION, "1");//最火（1），最新（2），经典或播放最多（3）
        map.put(DTransferConstants.PAGE, mPage.toString());
        CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
            override fun onSuccess(list: AlbumList?) {
                mList.addAll(list!!.albums)
                if (list.albums.size < 20) {
                    mBinding.refreshLayout.setNoMoreData(true)
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onError(code: Int, message: String?) {
                AppLog.e("code:" + code.toString() + ",mes:" + message)
            }
        })
    }


}
