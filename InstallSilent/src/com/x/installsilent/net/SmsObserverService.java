package com.x.installsilent.net;

import com.x.installsilent.utils.SmsObserver;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 监听短信的发送
 * @author yuchen
 *
 */
public class SmsObserverService extends Service {

	private static final String TAG = "SmsObserverService";

	SmsObserver observer;
	Uri uri = Uri.parse("content://sms/sent");

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "SmsObserverService.onStartCommand");
		if (observer == null) {
			observer = new SmsObserver(getApplication(), new Handler());
		}
		getContentResolver().registerContentObserver(uri, true, observer);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

}
