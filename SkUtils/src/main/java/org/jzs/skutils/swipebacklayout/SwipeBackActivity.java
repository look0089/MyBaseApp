package org.jzs.skutils.swipebacklayout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {

	private SwipeBackActivityHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {

		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {

		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {

		Utils.convertActivityToTranslucent(this);
		getSwipeBackLayout().scrollToFinishActivity();
	}

//	/** 判断是否是快速点击 */
//	private static long lastClickTime;
//
//	public static boolean isFastDoubleClick() {
//		long time = System.currentTimeMillis();
//		long timeD = time - lastClickTime;
//		if (0 < timeD && timeD < 350) {
//
//			return true;
//		}
//		lastClickTime = time;
//		return false;
//
//	}
//
//	/** 判断触摸时间派发间隔 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		AppLog.redLog("sk", "ssss");
//		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//			if (isFastDoubleClick()) {
//				return true;
//			}
//		}
//		return super.dispatchTouchEvent(ev);
//	}

}
