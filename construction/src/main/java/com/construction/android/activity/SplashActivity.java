package com.construction.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.construction.android.MyApplication;
import com.construction.android.R;
import com.construction.android.utils.CommonMethod;

public class SplashActivity extends MyFragmentActivity {
	protected boolean _isActive = true;
	protected int _splashTime = 3000;
	final String TAG = "SplashActivity";
	private SharedPreferences mSharedPreferences;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		final Thread splashThread = new Thread() {
			public void run() {
				try {
					int wait = 0;
					while (_isActive && (_splashTime > wait)) {
						sleep(100);
						if (_isActive) {
							wait += 100;
						}
					}
				} catch (InterruptedException e) {
					Log.d(TAG, e.getMessage());

				} finally {
					mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
					mSharedPreferences.edit();
					if (mSharedPreferences.getBoolean(getString(R.string.sp_is_login), false)) {

						Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(mainActivity);
						finish();

					} else {
						if (_isActive) {
							Intent mainActivity = new Intent(SplashActivity.this, LoginActivity.class);
							startActivity(mainActivity);
							finish();
						}
					}
				}
			}
		};
		splashThread.start();
	}

	@Override
	protected void onPause() {
		_isActive = false;
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		_isActive = false;
		super.onDestroy();
	}

	@Override
	public CommonMethod getCommonMethod() {
		return null;
	}

	@Override
	public void replaceFragment(Fragment mFragment, boolean addBackStack) {

	}

	@Override
	public void setTitle(int title, int image) {

	}

	@Override
	public void setTitle(int title) {

	}

	@Override
	public MyApplication getMyApplication() {
		return null;
	}

	@Override
	public void setTabSelectionDisable() {

	}

}