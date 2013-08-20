package com.x.installsilent.net;

import com.kim.net.NetEntity;

public class DownloadUrlEntity extends NetEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 134522071590229777L;

	@Override
	protected void init() {
		url = "http://www.baidu.com";
	}

	@Override
	protected void initHttpHeader() {
	}

	@Override
	public String getSendData() {
		return "";
	}

}
