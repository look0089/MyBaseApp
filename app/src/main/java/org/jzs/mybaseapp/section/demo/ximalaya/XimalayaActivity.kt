package org.jzs.mybaseapp.section.demo.ximalaya

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.AlbumList
import com.ximalaya.ting.android.opensdk.model.category.Category
import com.ximalaya.ting.android.opensdk.model.category.CategoryList
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingAdapter
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingVH
import org.jzs.mybaseapp.common.system.Constant
import org.jzs.mybaseapp.common.utils.AppLog
import org.jzs.mybaseapp.common.utils.GlideUtils
import org.jzs.mybaseapp.databinding.ActivityXimalayaBinding
import org.jzs.mybaseapp.databinding.ItemXmlyBinding

/**
 * 喜马拉雅sdk
 *
 * @author Jzs created 2019/1/2
 * @email 355392668@qq.com
 */
class XimalayaActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityXimalayaBinding
    var mList = arrayListOf<Category>()
    lateinit var mAdapter: BaseBindingAdapter<Category, ItemXmlyBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ximalaya)
        getAllCate()
        mAdapter = object : BaseBindingAdapter<Category, ItemXmlyBinding>(this, mList, R.layout.item_xmly) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemXmlyBinding>, position: Int) {
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
        fun onItemClick(data: Category) {
            val intent = Intent(this@XimalayaActivity, AlbumActivity::class.java)
            intent.putExtra(Constant.EXTRA.EXTRA_ITEM_ID, data.id.toString())
            startActivity(intent)
        }
    }


    /**
     * 获取分类
     */
    fun getAllCate() {
        var map = HashMap<String, String>();
        CommonRequest.getCategories(map, object : IDataCallBack<CategoryList> {
            override fun onSuccess(list: CategoryList?) {
                mList.addAll(list!!.categories)
                mAdapter.notifyDataSetChanged()
            }


            override fun onError(code: Int, message: String?) {
                AppLog.e("code:" + code.toString() + ",mes:" + message)
            }
        })
    }


    /**
     * 获取专辑
     */
    fun getAlbum(id: String) {
        var map = HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, id);//专辑id
        map.put(DTransferConstants.CALC_DIMENSION, "1");//最火（1），最新（2），经典或播放最多（3）
        CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
            override fun onSuccess(list: AlbumList?) {
            }


            override fun onError(code: Int, message: String?) {
                AppLog.e("code:" + code.toString() + ",mes:" + message)
            }
        })
    }
}
