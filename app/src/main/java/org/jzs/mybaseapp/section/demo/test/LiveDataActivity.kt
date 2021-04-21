package org.jzs.mybaseapp.section.demo.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.databinding.ActivityLivedataBinding

class LiveDataActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityLivedataBinding
    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_livedata)
        initView()
        initViewModel()
    }

    fun initView() {
        mBinding.apply {
            btn1.setOnClickListener {
                Log.e("livedata", "btn1:" + etText.text.toString())
                Log.e("livedata", "btn1:" + homeViewModel.searchText)
            }
            btn2.setOnClickListener {
                etText.setText("33333")
            }
            btn3.setOnClickListener {
                homeViewModel.searchText.value = "4444"
            }
        }
    }

    fun initViewModel() {
        mBinding.viewModel = homeViewModel
//        homeViewModel.searchText.observe(this, Observer {
//            Log.e("livedata", "observer searchText:" + it)
//        })
        homeViewModel.mList.observe(this, Observer {
            Log.e("livedata", "observer mList:" + it.size)
        })
    }
}
