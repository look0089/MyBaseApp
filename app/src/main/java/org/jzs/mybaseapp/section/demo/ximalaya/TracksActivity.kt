package org.jzs.mybaseapp.section.demo.ximalaya

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingAdapter
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingVH
import org.jzs.mybaseapp.common.base.BaseActivity
import org.jzs.mybaseapp.databinding.ActivityTrackBinding
import org.jzs.mybaseapp.databinding.ItemTrackBinding

/**
 * 专辑
 *
 * @author Jzs created 2019/1/2
 * @email 355392668@qq.com
 */
class TracksActivity : BaseActivity() {
    lateinit var mBinding: ActivityTrackBinding
    var mList = arrayListOf<Track>()
    lateinit var mAdapter: BaseBindingAdapter<Track, ItemTrackBinding>
    var mPage = 1
    lateinit var player: XmPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_track)

        player = XmPlayerManager.getInstance(this)
        getTracks(itemId)

        //设置刷新控件
        mBinding.refreshLayout.setOnRefreshListener { refreshlayout ->
            mList.clear()
            mPage = 1
            getTracks(itemId)
            mBinding.refreshLayout.setNoMoreData(false)
            refreshlayout.finishRefresh()
        }
        mBinding.refreshLayout.setOnLoadMoreListener { refreshlayout ->
            mPage++
            getTracks(itemId)
            refreshlayout.finishLoadMore()
        }

        mAdapter = object : BaseBindingAdapter<Track, ItemTrackBinding>(this, mList, R.layout.item_track) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemTrackBinding>, position: Int) {
                //★super一定不要删除
                super.onBindViewHolder(holder, position)
                val binding = holder.binding
                var item = mList[position]
                binding.position = position
                binding.tvNum.text = (position + 1).toString()

            }
        }
        mBinding.rv.adapter = mAdapter
        mAdapter.itemPresenter = SingleItemPresenter()


    }


    /**
     * ★ Item点击事件P
     */
    inner class SingleItemPresenter {
        fun onItemClick(data: Track, position: Int) {
            player.playList(mList, position)
        }
    }

    /**
     * 浏览专辑内歌曲
     */
    fun getTracks(id: String) {
        var map = HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, id);
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, mPage.toString());
        CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
            override fun onSuccess(entity: TrackList?) {
                mBinding.data = entity
                mList.addAll(entity!!.tracks)
                if (entity.tracks.size < 20) {
                    mBinding.refreshLayout.setNoMoreData(true)
                }
                mAdapter.notifyDataSetChanged()

            }

            override fun onError(code: Int, message: String?) {

            }
        })
    }

}
