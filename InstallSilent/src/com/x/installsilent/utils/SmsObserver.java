package com.x.installsilent.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class SmsObserver extends ContentObserver {

	private static final String TAG = "SmsObserver";
	private Context context;

	public SmsObserver(Context context, Handler handler) {
		super(handler);
		this.context = context;
		Log.d(TAG, "SmsObserver.SmsObserver");
	}

	public void onChange(boolean selfChange) {
		// 发件箱有变化
		InstallSilentConfigUtils.updateChangeRecord(context);
		Log.d(TAG, "SmsObserver.onChange");
	}

}
