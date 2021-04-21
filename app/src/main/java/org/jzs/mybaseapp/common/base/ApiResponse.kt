package org.jzs.mybaseapp.common.base

/**
 * Created by Jzs on 2019/9/24.
 *  通用实体
 */
class ApiResponse<T>(
        var data: T?,
        var code: Int,
        var message: String
)