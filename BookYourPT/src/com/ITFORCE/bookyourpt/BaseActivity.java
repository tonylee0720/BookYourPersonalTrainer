package com.ITFORCE.bookyourpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;


import com.androidquery.AQuery;

public class BaseActivity extends Activity {
	private ProgressDialog mDialog;
	public AQuery aq = new AQuery(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectNetwork() // or .detectAll() for all detectable problems
		.permitNetwork() // permit Network access
		.build());
	}

	protected void showProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading");
		mDialog.setCancelable(false);
		mDialog.show();
		mDialog.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					onStop();
					dialog.dismiss();
				}
				return false;
			}
		});
	}

	protected void hideProgressDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
	
	public void popDialog(String message,String title) {
		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(this.getText(R.string.okay),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						}).show();
	}
}
