package org.jzs.mybaseapp.common.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * Created by Jzs on 2019/11/22.
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {
    val errMsg: MutableLiveData<String> = MutableLiveData()
    val finishPage: MutableLiveData<Boolean> = MutableLiveData()
    val showDialog: MutableLiveData<Boolean> = MutableLiveData()
    val refresh: MutableLiveData<Boolean> = MutableLiveData()

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    suspend fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.Default) {
            block
        }
    }

    suspend fun <T> executeResponse(res: ApiResponse<T>, successBlock: suspend CoroutineScope.() -> Unit, errorBlock: suspend CoroutineScope.() -> Unit) {
        showDialog.value = false
        coroutineScope {
            when (res.code) {
                200 -> {
                    successBlock()
                }
                401, 403 -> {
                    errorBlock()
                }
                else -> {
                    errMsg.value = res.message
                    errorBlock()
                }
            }
        }
    }

    //不消失dialog方法
    suspend fun <T> noExecuteResponse(res: ApiResponse<T>, successBlock: suspend CoroutineScope.() -> Unit, errorBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            when (res.code) {
                200 -> {
                    successBlock()
                }
                401, 403 -> {
                    errorBlock()
                }
                else -> {
                    errMsg.value = res.message
                    errorBlock()
                }
            }
        }
    }
}