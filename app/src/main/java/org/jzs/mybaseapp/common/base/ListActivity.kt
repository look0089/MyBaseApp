package org.jzs.mybaseapp.common.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingAdapter
import org.jzs.mybaseapp.common.BindingAdapter.BaseBindingVH
import org.jzs.mybaseapp.common.widget.CommonDialog
import org.jzs.mybaseapp.common.widget.TestDialogFragment
import org.jzs.mybaseapp.databinding.ActivityListBinding
import org.jzs.mybaseapp.databinding.ItemListBinding

/**
 * list模板
 */

class ListActivity : BaseActivity() {

    lateinit var mBinding: ActivityListBinding

    var mList = arrayListOf<BaseEntity>()
    lateinit var mAdapter: BaseBindingAdapter<BaseEntity, ItemListBinding>
    var mPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list)
        initView()

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: EventBusEntity) {
    }

    fun initView() {
//        mBinding.titleBar!!.titleName.text = "list模板"

        //设置刷新控件
        mBinding.refreshLayout.setOnRefreshListener { refreshlayout ->
            mList.clear()
            mPage = 1
            getData()
            mBinding.refreshLayout.setNoMoreData(false)
            refreshlayout.finishRefresh()
        }
        mBinding.refreshLayout.setOnLoadMoreListener { refreshlayout ->
            mPage++
            getData()
            refreshlayout.finishLoadMore()
        }

        mAdapter = object : BaseBindingAdapter<BaseEntity, ItemListBinding>(this, mList, R.layout.item_list) {
            override fun onBindViewHolder(holder: BaseBindingVH<ItemListBinding>, position: Int) {
                //★super一定不要删除
                super.onBindViewHolder(holder, position)
                val binding = holder.binding
                var item = mList[position]
                binding.tvName.setOnClickListener {
                    val myDialog = TestDialogFragment()
                    val args = Bundle()
//                    args.putSerializable(Constant.EXTRA.EXTRA_ITEM, item)
                    myDialog.arguments = args
                    myDialog.show(fragmentManager, "manage")
                }

                EventBus.getDefault().post(mList[position])
                EventBus.getDefault().postSticky(mList[position])
            }
        }
        mBinding.rv.adapter = mAdapter

        mAdapter.itemPresenter = SingleItemPresenter()
    }


    /**
     * ★ Item点击事件P
     */
    inner class SingleItemPresenter {
        fun onItemClick(data: BaseEntity, position: Int) {
        }

        fun onBuyClick(data: BaseEntity) {
        }
    }

    fun openDialog() {
        val myDialog = CommonDialog("打开对话框")
        myDialog.setCallBack(object : CommonDialog.DialogCallBack {
            override fun sure() {
            }

            override fun cancel() {

            }
        })
        myDialog.show(fragmentManager, "manage")
    }


    fun getData() {
        for (i in 0..9) {
            mList.add(BaseEntity())
            mAdapter.notifyDataSetChanged()
        }

//        MyRequestUtils.getRequestServices(HomeService::class.java)
//                .homeProductList
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : MySubscriber<BaseEntity>(this) {
//
//                    override fun taskSuccess(entity: BaseEntity) {
//                        mList.addAll(entity.list)
//                        if (entity.list.size < 10) {
//                            mBinding.refreshLayout.setNoMoreData(true)
//                        }
//                        mAdapter!!.notifyDataSetChanged()
//                    }
//                })
    }

}
