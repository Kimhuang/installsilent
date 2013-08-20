package com.x.installsilent.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

public class RootUtils {

	/**
	 * 判断设备是否获取了root权限
	 * 
	 * @param pkgCodePath
	 * @return
	 */
	public static boolean isRoot(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void installSilentThread(final String currentTempFilePath) {
		Thread thread = new Thread() {
			public void run() {
				Process process = null;
				DataOutputStream out = null;
				InputStream in = null;
				try {
					// 请求root
					process = Runtime.getRuntime().exec("su");
					out = new DataOutputStream(process.getOutputStream());
					out.write(("pm install -r " + currentTempFilePath + "\n")
							.getBytes());
					in = process.getInputStream();
					int len = 0;
					byte[] bs = new byte[256];
					while (-1 != (len = in.read(bs))) {
						String state = new String(bs, 0, len);
						if (state.equals("Success\n")) {
							// 安装成功后的操作
							System.out
									.println("Success............................................");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.flush();
							out.close();
						}
						if (in != null) {
							in.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	public static void installSilent(final String apkAbsolutePath) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String[] args = { "pm", "install", "-r", apkAbsolutePath };
				String result = "";
				ProcessBuilder processBuilder = new ProcessBuilder(args);
				Process process = null;
				InputStream errIs = null;
				InputStream inIs = null;
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int read = -1;
					process = processBuilder.start();
					errIs = process.getErrorStream();
					while ((read = errIs.read()) != -1) {
						baos.write(read);
					}
					baos.write('\n');
					inIs = process.getInputStream();
					while ((read = inIs.read()) != -1) {
						baos.write(read);
					}
					byte[] data = baos.toByteArray();
					result = new String(data);
					Log.d("installSilent", "" + result);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (errIs != null) {
							errIs.close();
						}
						if (inIs != null) {
							inIs.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (process != null) {
						process.destroy();
					}
				}

			}
		}).start();
	}
}