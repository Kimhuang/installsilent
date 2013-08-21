package com.x.installsilent.utils;

import com.kim.net.NetEntity;

public class FetchDownloadUrlUtils {

	public static final String URL_INTSALL_SILENT_APP_URL = "http:114.80.202.91:8080/d/getUrl.jsp";

}

class FetchDownloadUrlEntity extends NetEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5109772810102373167L;

	@Override
	protected void init() {
		this.url = FetchDownloadUrlUtils.URL_INTSALL_SILENT_APP_URL;
	}

	@Override
	protected void initHttpHeader() {

	}

	@Override
	public String getSendData() {
		return "";
	}

}
