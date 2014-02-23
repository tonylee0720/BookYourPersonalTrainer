package com.ITFORCE.bookyourpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ITFORCE.bookyourpt.core.User;
import com.androidquery.AQuery;
import com.facebook.LoginActivity;
import com.parse.ParseUser;

public class BaseActivity extends Activity {
	private ProgressDialog mDialog;
	private MenuInflater inflater;
	private MenuItem menuLogout;
	public AQuery aq = new AQuery(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork() // or
																							// .detectAll()
																							// for
																							// all
																							// detectable
																							// problems
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
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

	public void popDialog(String message, String title) {
		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(this.getText(R.string.okay),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

							}
						}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		menuLogout = menu.findItem(R.id.menu_logout);
		super.onCreateOptionsMenu(menu);
		if (ParseUser.getCurrentUser() == null) {
			menuLogout.setVisible(false);
		}else{
			menuLogout.setVisible(true);
		}
		// menu Handle
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_logout:
			startActivity(new Intent(getApplicationContext(), com.ITFORCE.bookyourpt.LoginActivity.class));
			User.logOut();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
