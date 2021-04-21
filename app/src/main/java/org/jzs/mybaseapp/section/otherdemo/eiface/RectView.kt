package org.jzs.mybaseapp.section.otherdemo.eiface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager

/**
 * 画人脸识别框
 */
class RectView : View {
    private var rect: Rect? = null
    private val screenWidth: Int
    private val screenHeight: Int
    private var mPaint: Paint? = null
    private var mTPaint: Paint? = null
    private var tempTPaint: Paint? = null
    private var IdCardPaint: Paint? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        screenWidth = wm.defaultDisplay.width
        screenHeight = wm.defaultDisplay.height
        initPaint()
        mTPaint = Paint().apply {
            isAntiAlias = true
            color = Color.GREEN
            strokeWidth = 15F
            textAlign = Paint.Align.LEFT
            textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    18F, context.resources.displayMetrics
            )
        }

        tempTPaint = Paint().apply {
            isAntiAlias = true
            color = Color.YELLOW
            strokeWidth = 15F
            textAlign = Paint.Align.LEFT
            textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    21F, context.resources.displayMetrics
            )
        }

        IdCardPaint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
            strokeWidth = 16F
            textAlign = Paint.Align.LEFT
            textSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    20F, context.resources.displayMetrics
            )
        }
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.strokeWidth = 6f
        mPaint!!.color = Color.GREEN
    }

    /**
     * 开始画矩形框
     *
     * @param rect1
     */
    private var hint_str: String = ""
    private var temp_str: String = ""

    fun drawFaceRect(rect1: Rect, hint_str: String, temp_str: String, color: Int) {
        this.rect = rect1
        this.mPaint!!.color = color
        this.hint_str = hint_str
        this.temp_str = temp_str
        //if(System.currentTimeMillis()%2 > 0)
//        setMeteringAreas_rk()
//        setMeteringAreas()
        postInvalidate()
    }

    fun setMeteringAreas_rk() {
//        val parameters1 = videoUtilBack!!.mCamera!!.getParameters();
//        val parameters2 = videoUtilFront!!.mCamera!!.getParameters();
        var focusList = ArrayList<Camera.Area>()
        val tmprect = Rect()
        tmprect.left = rect!!.left * 2000 / width - 1000
        tmprect.right = rect!!.right * 2000 / width - 1000
        tmprect.top = rect!!.top * 2000 / height - 1000
        tmprect.bottom = rect!!.bottom * 2000 / height - 1000
        focusList.add(Camera.Area(tmprect, 1000))
//        parameters1.setMeteringAreas(focusList)
//        parameters1.setFocusAreas(focusList)
//        parameters2.setMeteringAreas(focusList)
//        parameters2.setFocusAreas(focusList)
        //videoUtilBack!!.mCamera!!.parameters = parameters1
//        videoUtilFront!!.mCamera!!.parameters = parameters2
        Log.d("debug", "setMeteringAreas_rk")
    }

    fun clearRect() {
        rect = null
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rect != null) {
            canvas.drawLine(
                    rect!!.left.toFloat(),
                    rect!!.top.toFloat(),
                    rect!!.left.toFloat(),
                    (rect!!.top + 26).toFloat(),
                    mPaint!!
            )
            canvas.drawLine(
                    rect!!.left.toFloat(),
                    rect!!.top.toFloat(),
                    (rect!!.left + 26).toFloat(),
                    rect!!.top.toFloat(),
                    mPaint!!
            )

            canvas.drawLine(
                    rect!!.right.toFloat(),
                    rect!!.top.toFloat(),
                    (rect!!.right - 26).toFloat(),
                    rect!!.top.toFloat(),
                    mPaint!!
            )
            canvas.drawLine(
                    rect!!.right.toFloat(),
                    rect!!.top.toFloat(),
                    rect!!.right.toFloat(),
                    (rect!!.top + 26).toFloat(),
                    mPaint!!
            )
            canvas.drawLine(
                    rect!!.left.toFloat(),
                    rect!!.bottom.toFloat(),
                    rect!!.left.toFloat(),
                    (rect!!.bottom - 26).toFloat(),
                    mPaint!!
            )
            canvas.drawLine(
                    rect!!.left.toFloat(),
                    rect!!.bottom.toFloat(),
                    (rect!!.left + 26).toFloat(),
                    rect!!.bottom.toFloat(),
                    mPaint!!
            )

            canvas.drawLine(
                    rect!!.right.toFloat(),
                    rect!!.bottom.toFloat(),
                    rect!!.right.toFloat(),
                    (rect!!.bottom - 26).toFloat(),
                    mPaint!!
            )
            canvas.drawLine(
                    rect!!.right.toFloat(),
                    rect!!.bottom.toFloat(),
                    (rect!!.right - 26).toFloat(),
                    rect!!.bottom.toFloat(),
                    mPaint!!
            )
        }
//        canvas.drawText(str, 20F, 260F, mTPaint!!)
//        if (temp_str != "" && rect != null) {
//            if (UsbTemp.getStrLastTemp() != "" && rect != null) {
//                canvas.drawText("当前体温: ${UsbTemp.getStrLastTemp()}", 20F, 280F, tempTPaint!!)
//            }
//        }
//        if (temp_str != "" && rect != null){
//            canvas.drawText(this.temp_str, (rect!!.left + 60).toFloat(), (rect!!.top - 20).toFloat(), tmepTPaint!!)
//        }

        if (rect != null) {
            canvas.drawText(this.hint_str, 320F, 900F, IdCardPaint!!)
//            canvas.drawText(hint_str, (rect!!.left + 26).toFloat(), (rect!!.bottom + 30).toFloat(), mTPaint!!)  //跟随矩形框
        }
    }
}
