package org.jzs.mybaseapp.section.weightdemo.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.jzs.mybaseapp.R;

import java.util.List;


public class DialogiOS implements OnClickListener {
	private OnDialogItemClickListener listener;

	private Dialog dialog;

	private Context context;

	private RelativeLayout outer;

	private int colorRes;

	private int sp = 16;

	private List<String> list;

	private TextView cancel;

	public DialogiOS(Context context) {
		this.context = context;
		colorRes = context.getResources().getColor(R.color.txt_dialog);
	}

	public DialogiOS setTextColor(int colorRes) {
		this.colorRes = colorRes;
		return this;
	}

	public DialogiOS setTextSize(int sp) {
		this.sp = sp;
		return this;
	}

	public DialogiOS setTitles(List<String> list) {
		this.list = list;
		return this;
	}

	public String getItem(int position) {
		String string = this.list.get(position);
		return string;
	}

	public DialogiOS setOnItemClickListener(OnDialogItemClickListener listener) {
		this.listener = listener;
		return this;
	}

	public void show() {
		dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_bottom_layout);
		setMatchWidth();
		addTextView();
		cancel = (TextView) dialog.findViewById(R.id.cancel);
//		cancel.setTextColor(colorRes);
		outer = (RelativeLayout) dialog.findViewById(R.id.parent);
		outer.setOnClickListener(this);
		dialog.show();
	}

	@SuppressLint("NewApi")
	private void addTextView() {
		LinearLayout linear = (LinearLayout) dialog.findViewById(R.id.linear);
		for (int i = 0; i < list.size(); i++) {
			TextView tv = new TextView(context);
			tv.setId(i);
			tv.setGravity(Gravity.CENTER);
			int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources()
					.getDisplayMetrics());
			tv.setPadding(padding, padding, padding, padding);
			tv.setTextColor(colorRes);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
			tv.setText(list.get(i));
			tv.setBackground(context.getResources().getDrawable(R.drawable.shape_rec_coner));
			tv.setOnClickListener(this);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			tv.setLayoutParams(params);
			linear.addView(tv);

			if (i < list.size() - 1) {
				View line = new View(context);
				line.setBackgroundColor(context.getResources().getColor(R.color.line));
				int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1, context.getResources()
						.getDisplayMetrics());
				LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, px);
				param.weight = 0;
				line.setLayoutParams(param);
				linear.addView(line);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setMatchWidth() {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		dialog.getWindow().setAttributes(lp);
	}

	/**
	 * 数字顺序从上到下0，1，2，3
	 */
	@Override
	public void onClick(View v) {
		for (int i = 0; i < list.size(); i++) {
			if (v.getId() == i) {
				if (listener != null) {
					listener.onItemClick(i, ((TextView) v).getText().toString());
					dialog.dismiss();
					break;
				}
			}
		}

		if (v.getId() == R.id.parent) {
			dialog.dismiss();
		}
	}

	public interface OnDialogItemClickListener {
		public void onItemClick(int position, String str);
	}

	public void GoneCancle() {
		cancel.setVisibility(View.INVISIBLE);
		android.view.ViewGroup.LayoutParams p = cancel.getLayoutParams();
		p.height = 1;
		cancel.setLayoutParams(p);
	}
}
