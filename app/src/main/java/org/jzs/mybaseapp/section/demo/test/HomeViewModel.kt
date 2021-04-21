package org.jzs.mybaseapp.section.demo.test

import androidx.lifecycle.MutableLiveData
import org.jzs.mybaseapp.common.base.BaseViewModel

class HomeViewModel : BaseViewModel() {
    val searchText: MutableLiveData<String> = MutableLiveData()
    val mList: MutableLiveData<MutableList<String>> = MutableLiveData()

    init {
        val list = arrayListOf<String>()
        for (i in 0..10) {
            list.add("aaa" + i)
        }
        mList.value = list
    }
}