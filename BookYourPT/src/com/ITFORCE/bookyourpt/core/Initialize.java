package com.ITFORCE.bookyourpt.core;

import android.app.Application;

import com.ITFORCE.bookyourpt.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Initialize extends Application {

	private final static String PARSE_APPID = "PMO0FytspDIR6zFY9HjNw9TXFMvqoCXEzgWLMTPU";
	private final static String PARSE_CLIENT = "T0C8sHjPbisEAt3DkejNMJxtrLrPaqlYNDtt8bJh";

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, PARSE_APPID, PARSE_CLIENT);
		ParseFacebookUtils.initialize(getString(R.string.fb_appid));

	}
	
}
