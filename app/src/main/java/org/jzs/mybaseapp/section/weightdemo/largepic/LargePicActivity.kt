package org.jzs.mybaseapp.section.weightdemo.largepic

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.jzs.mybaseapp.R
import org.jzs.mybaseapp.databinding.ActivityLargePicBinding

class LargePicActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityLargePicBinding
    private val KEY_X = "X"
    private val KEY_Y = "Y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_large_pic)
        with(mBinding.ivLarge) {
            setInputStream(assets.open("world.jpg"))
            post {
                val p = savedInstanceState?.takeIf { it.containsKey(KEY_X) && it.containsKey(KEY_Y) }?.let {
                    Point(it.getInt(KEY_X), it.getInt(KEY_Y))
                }
                if (p != null) {
                    mBinding.ivLarge.setViewport(p)
                } else {
                    mBinding.ivLarge.setViewportCenter()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val p = Point().apply { mBinding.ivLarge.getViewport(this) }
        outState.putInt(KEY_X, p.x)
        outState.putInt(KEY_Y, p.y)
        super.onSaveInstanceState(outState)
    }
}
