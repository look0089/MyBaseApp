package org.jzs.mybaseapp.section.otherdemo.eiface

enum class WffrStatus constructor(val value: Int, val mean: String) {
    Success(0, "成功"),
    Fail(1, "失败");

    companion object {
        open fun getName(index: Int): String? {
            for (c in WffrStatus.values()) {
                if (c.value == index) {
                    return c.mean
                }
            }
            return null
        }
    }
}