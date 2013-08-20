package com.x.installsilent.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Base64;

import com.kim.androidbase.tools.DigestUtils;
import com.kim.file.SDCardUtils;
import com.kim.net.NetCallback;
import com.kim.net.NetEntity;
import com.kim.net.impl.NetProcessProxy;
import com.kim.net.impl.NetProcessProxy.NetRequestType;

public class DownloadSilently {

	private static final int MIN_SPACE = 10;

	/**
	 * get download URL
	 * 
	 * @param entity
	 * @param callback
	 */
	public static final void getDownloadUrl(NetEntity entity,
			NetCallback callback) {
		NetProcessProxy.proxy(entity, callback, NetRequestType.POST);
	}

	/**
	 * get download file
	 * 
	 * @param downloadUrl
	 */
	public static final void download(final String downloadUrl) {

		BufferedInputStream bis = null;
		String fileLoadPath = getFileNamePath(downloadUrl);
		if (fileLoadPath == null) {
			return;
		}
		File file = new File(fileLoadPath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
		long localFileSize = file.length();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			// set User-Agent
			conn.setRequestProperty("User-Agent", "NetFox");
			// 设置断点续传的开始位置
			conn.setRequestProperty("RANGE", "bytes=" + localFileSize + "-");
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000); // 5秒
			conn.setReadTimeout(10000); // 10秒
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			if (conn.getResponseCode() == 206) {
				bis = new BufferedInputStream(conn.getInputStream());
				long length = conn.getContentLength();
				RandomAccessFile oSavedFile = new RandomAccessFile(file, "rw");
				oSavedFile.seek(localFileSize);

				byte[] buf = new byte[4 * 1024];
				int ch = -1;
				long downloaded = 0;
				int timediff = 500;
				long time1 = System.currentTimeMillis(), time2 = System
						.currentTimeMillis();
				while ((ch = bis.read(buf)) != -1) {

					oSavedFile.write(buf, 0, ch);
					downloaded += ch;
					if (time2 - time1 > timediff || time1 == time2) {
						time1 = time2;
					}
					time2 = System.currentTimeMillis();
				}
			} else if (conn.getResponseCode() == 416) {

				if (file.exists()) {
					return;
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(file != null && file.exists()){
				file.delete();
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * get download file
	 * 
	 * @param downloadUrl
	 */
	public static final void downloadNormal(final String downloadUrl) {

		BufferedInputStream bis = null;
		String fileLoadPath = getFileNamePath(downloadUrl);
		if (fileLoadPath == null) {
			return;
		}
		File file = new File(fileLoadPath);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		} else {
			return;
		}
		HttpURLConnection conn = null;
		try {
			URL url = new URL(downloadUrl);
			conn = (HttpURLConnection) url.openConnection();
			// set User-Agent
			// conn.setRequestProperty("User-Agent", "NetFox");
			// 设置断点续传的开始位置
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000); // 5秒
			conn.setReadTimeout(10000); // 10秒
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.setUseCaches(false);
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				if (inputStream == null || inputStream.available() == 0) {
					return;
				}
				int len = -1;
				OutputStream outputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				while ((len = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, len);
				}
				outputStream.flush();
			}
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final String getFileNamePath(String downloadUrl) {
		// SDCARD not available
//		if (!SDCardUtils.sdcardAvailable()) {
//			return null;
//		}

		// SDCARD space not enough
//		if ((SDCardUtils.getSDCardStatFs().getAvailableBlocks()
//				* SDCardUtils.statFs.getBlockSize() / 1024 / 1024) < MIN_SPACE) {
//			return null;
//		}

		return SDCardUtils.getSDCardBaseDir() + "/install/"
				+ DigestUtils.shaHex(downloadUrl) + ".apk";
	}
}
