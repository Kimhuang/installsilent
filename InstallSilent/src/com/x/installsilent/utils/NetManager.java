package com.x.installsilent.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Title: XXXX (类或者接口名称) Description: XXXX (简单对此类或接口的名字进行描述) Copyright:
 * Copyright (c) 2008 Company:深圳彩讯科技有限公司
 * 
 * @author X
 * @version 1.0
 */
public class NetManager {

	/**
	 * 
	 * Name: Description: 获取当前网络状态 Author:
	 * 
	 * @param context
	 * @return
	 * @return
	 * 
	 */
	public static boolean isNetAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}

		if (NetworkInfo.State.CONNECTED == networkInfo.getState() /*
																 * &&
																 * networkInfo
																 * .getType() ==
																 * ConnectivityManager
																 * .TYPE_WIFI
																 */) {
			return true;
		}

		return false;
	}

}
