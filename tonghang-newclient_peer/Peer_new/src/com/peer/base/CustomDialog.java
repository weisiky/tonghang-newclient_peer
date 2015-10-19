package com.peer.base;

import android.app.Dialog;
import android.content.Context;

/**
 * 自定义dialog
 * @author zhzhg
 * @version 1.0.0
 */
public class CustomDialog extends Dialog {

	private pBaseActivity baseActivity;

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		baseActivity = (pBaseActivity) context;
	}

	public CustomDialog(Context context) {
		super(context, 0);
		baseActivity = (pBaseActivity) context;
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void cancel() {
		if (this.isShowing()) {
			super.cancel();
		}

	}
}
