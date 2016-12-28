package com.construction.android.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hi3project.vineyard.comm.stomp.gozirraws.Listener;
import com.hi3project.vineyard.comm.stomp.gozirraws.WebSocketStomp;
import com.construction.android.MyApplication;
import com.construction.android.R;
import com.construction.android.fragments.HomeFragment;
import com.construction.android.fragments.SettigsFragment;
import com.construction.android.fragments.TaskCardFragment;
import com.construction.android.utils.CommonMethod;
import com.construction.android.view.ProjectListDialogFragment;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class MainActivity extends MyFragmentActivity implements OnClickListener {

	public MyFragmentActivity mActivity;
	public CommonMethod mCommonMethod;
	public MyApplication mApplication;

	private SharedPreferences mSharedPreferences;
	private Editor mEditor;

	private TextView mTextViewDate;
	private TextView mTextViewTitle;
	public TextView mTextViewHome;
	private TextView mTextViewSearch;
	public TextView mTextViewSettings;
	private TextView mTextViewBack;
	private ImageView mImageViewInformation;

	WebSocketStomp mWebSocketStomp;
	boolean isFromNotificationError = false;
	String task_id = "";
	String notification_file_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mActivity = this;
		mApplication = (MyApplication) getApplication();

		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();

		mCommonMethod = new CommonMethod(mActivity, getSharedPreferences(getString(R.string.sp_server_name_file), Context.MODE_PRIVATE).getString(
				getString(R.string.sp_server_name), ""));

		getWiegetReference();
		registerOnClick();
		setCurrentDate();

		// new RegisterGCM(mActivity).initGCM();

		if (getIntent().hasExtra(getString(R.string.bundle_from_notification))) {
			task_id = getIntent().getExtras().getString(getString(R.string.bundle_task_id));
			notification_file_name = getIntent().getExtras().getString(getString(R.string.bundle_notification_file_name));
			replaceFragment(new HomeFragment(), false);
			openNotification();
		} else {
			new ProjectListDialogFragment(mActivity).show(getSupportFragmentManager(), "");
			mActivity.replaceFragment(new HomeFragment(), false);
		}

		new ConnectSocketAsync().execute("");

	}

	public void openNotification() {
		// replaceFragment(new HomeFragment(), false);
		Bundle mBundle = new Bundle();
		mBundle.putString(getString(R.string.bunble_project_guid), task_id);
		mBundle.putBoolean(getString(R.string.bunble_is_task_in_progress_screen), true);
		TaskCardFragment mTaskCardFragment = new TaskCardFragment();
		mTaskCardFragment.setArguments(mBundle);
		replaceFragment(mTaskCardFragment, true);

		mActivity.getMyApplication().readNotification(notification_file_name);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.hasExtra(getString(R.string.bundle_from_notification))) {
			try {
				task_id = intent.getExtras().getString(getString(R.string.bundle_task_id));
				notification_file_name = intent.getExtras().getString(getString(R.string.bundle_notification_file_name));
				openNotification();
			} catch (Exception e) {
				isFromNotificationError = true;
				e.printStackTrace();
			}
		}
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {

		if (isFromNotificationError) {
			openNotification();
			isFromNotificationError = false;
		}
//		long last_active_time = mSharedPreferences.getLong(getString(R.string.sp_last_time), System.currentTimeMillis());
//		if (System.currentTimeMillis() - last_active_time > 1800000) {
//			// 1800000
//			mEditor.clear();
//			mEditor.commit();
//			Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
//			MainActivity.this.finish();
//			startActivity(mIntent);
//		}
		super.onResume();
	}

	@Override
	protected void onPause() {

		mEditor.putLong(getString(R.string.sp_last_time), System.currentTimeMillis());
		mEditor.commit();

		super.onPause();
	}

	/**
	 * Function call will set current date.
	 */
	@SuppressLint("SimpleDateFormat")
	private void setCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM");
		String formattedDate = df.format(c.getTime());
		// mTextViewDate.setText(setSpanText(getString(R.string.lbl_today) +
		// "\n" + formattedDate, formattedDate));
		mTextViewDate.setText(getString(R.string.lbl_today) + "\n" + Html.fromHtml("<b>" + formattedDate + "</b> "));
	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 */
	public void getWiegetReference() {

		mTextViewDate = (TextView) findViewById(R.id.view_actionbar_textview_date);
		mTextViewTitle = (TextView) findViewById(R.id.view_actionbar_textview_title);
		mTextViewHome = (TextView) findViewById(R.id.view_bottom_bar_textview_home);
		mTextViewHome.setBackgroundColor(getResources().getColor(R.color.transprant_blue));
		mTextViewSearch = (TextView) findViewById(R.id.view_bottom_bar_textview_search);
		mTextViewSettings = (TextView) findViewById(R.id.view_bottom_bar_textview_setting);
		mTextViewBack = (TextView) findViewById(R.id.view_bottom_bar_textview_back);

		mImageViewInformation = (ImageView) findViewById(R.id.view_actionbar_imageview_profile);
		mActivity.getCommonMethod().setTitleFont(mTextViewTitle);
		mActivity.getCommonMethod().setTitleFont(mTextViewDate);

	}

	/**
	 * Method call will Register OnClick() Events for widgets.
	 * 
	 */
	public void registerOnClick() {
		mTextViewHome.setOnClickListener(this);
		mTextViewSearch.setOnClickListener(this);
		mTextViewSettings.setOnClickListener(this);
		mTextViewBack.setOnClickListener(this);
		mImageViewInformation.setOnClickListener(this);

	}

	/**
	 * Method call OnClick Event fire.
	 * 
	 */
	@Override
	public void onClick(View v) {
		if (v == mTextViewHome) {

			setHomeScreen();

		} else if (v == mTextViewSearch) {

			setBottomTabSelection();
			mTextViewSearch.setBackgroundColor(getResources().getColor(R.color.transprant_blue));
			// mActivity.replaceFragment(new SubReportsFragment(), false);
			mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name), getString(R.string.alt_teature_will_be_abailable_soon));

		} else if (v == mTextViewSettings) {

			setBottomTabSelection();
			mTextViewSettings.setBackgroundColor(getResources().getColor(R.color.transprant_blue));
			mActivity.replaceFragment(new SettigsFragment(), false);

		} else if (v == mTextViewBack) {

			onBackPressed();

		} else if (v == mImageViewInformation) {

			showFinishDialog(true);

		}
	}

	/**
	 * Method will set home screen
	 */
	public void setHomeScreen() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
		setBottomTabSelection();
		mTextViewHome.setBackgroundColor(getResources().getColor(R.color.transprant_blue));
		mActivity.replaceFragment(new HomeFragment(), false);
	}

	/**
	 * Method call will selection of Bottom tab bar.
	 * 
	 */
	public void setBottomTabSelection() {
		mTextViewHome.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewSearch.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewSettings.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
	}

	/**
	 * Method will set title
	 * 
	 * @param resource
	 */
	@Override
	public void setTitle(int resource) {
		mTextViewTitle.setText(resource);
		mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	}

	/**
	 * Method will set title
	 * 
	 * @param resource
	 */
	@Override
	public void setTitle(int resource, int image) {
	}

	/**
	 * Method call will open exit dialog.
	 */
	public void showFinishDialog(final boolean isLogout) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setTitle(mActivity.getResources().getString(R.string.app_name));

		if (isLogout) {
			alertDialog.setMessage(getString(R.string.msg_logout));
		} else {
			alertDialog.setMessage(getString(R.string.msg_exit));
		}

		alertDialog.setPositiveButton(getString(R.string.lbl_yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				if (isLogout) {
					mEditor.clear();
					mEditor.commit();
					Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
					MainActivity.this.finish();
					startActivity(mIntent);
				} else {
					MainActivity.this.finish();
				}
			}
		});
		alertDialog.setNegativeButton(getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		alertDialog.show();
	}

	/**
	 * Method will handle back button
	 */
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			Fragment mFragmentCurrent = getSupportFragmentManager().findFragmentById(R.id.frame_container);
			if (mFragmentCurrent instanceof SettigsFragment) {
				setHomeScreen();
			} else {
				showFinishDialog(false);
			}

		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Method will return object of CommonMethod
	 */
	@Override
	public CommonMethod getCommonMethod() {

		return mCommonMethod;
	}

	/**
	 * Method will replace fragment
	 */
	@Override
	public void replaceFragment(Fragment mFragment, boolean addBackStack) {
		FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
		mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
		mFragmentTransaction.replace(R.id.frame_container, mFragment);
		if (addBackStack)
			mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();

	}

	/**
	 * Method will return MyApplication object
	 */
	@Override
	public MyApplication getMyApplication() {
		return mApplication;
	}

	/**
	 * Method will set bottom tab selection
	 */
	@Override
	public void setTabSelectionDisable() {

		setBottomTabSelection();
	}

	/**
	 * Method will read message from socket.
	 * 
	 * @param message
	 */
	public void readMessageFromSocket(String message) {

		try {
			JSONObject mJsonObjectMain = new JSONObject(message);

			// JSONObject mJsonObject =
			// mJsonObjectMain.getJSONObject("payload");
			JSONObject mJsonObjectTASK = mJsonObjectMain.getJSONObject("activityDetails");

			Random r = new Random();
			int Low = 1;
			int High = 100;
			int notify_id = r.nextInt(High - Low) + Low;

			String notiication_type = mJsonObjectMain.getString("objectEventType");
			String notify_message = mJsonObjectMain.getString("comment");
			String timestamp = mJsonObjectMain.getString("timestamp");
			String notify_title = mJsonObjectTASK.getString("name");
			String task_id = mJsonObjectTASK.getString("guid");

			if (saveDataInLocalStorage(message, timestamp + "_" + task_id)) {
				Intent mIntent = new Intent(this, MainActivity.class);
				// mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				// mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				mIntent.putExtra(getString(R.string.bundle_from_notification), true);
				mIntent.putExtra(getString(R.string.bundle_task_id), task_id);
				mIntent.putExtra(getString(R.string.bundle_notification_file_name), timestamp + "_" + task_id + ".txt");

				NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
				PendingIntent contentIntent = PendingIntent.getActivity(this, notify_id, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setDefaults(Notification.DEFAULT_ALL)
						.setSmallIcon(R.drawable.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
						.setStyle(new NotificationCompat.BigTextStyle().bigText(notify_message)).setTicker(notify_message)
						.setContentTitle(notify_title).setAutoCancel(true).setContentText(notify_title);

				// //System.out.println("not id.."+NOTIFICATION_ID);
				mBuilder.setContentIntent(contentIntent);
				if (mApplication.isNotificationOn()) {

					if (notiication_type.equalsIgnoreCase("ACTIVITY_NOTE_ADDED")) {

						if (mApplication.isNoteAddNotificationOn())
							mNotificationManager.notify(notify_id, mBuilder.build());

					} else
					if (notiication_type.equalsIgnoreCase("ACTIVITY_FILE_UPLOADED")) {

						if (mApplication.isFileNotificationOn())
							mNotificationManager.notify(notify_id, mBuilder.build());

					} else if (notiication_type.equalsIgnoreCase("ACTIVITY_END_DATE_CHANGE")) {

						if (mApplication.isStatusChangeNotificationOn())
							mNotificationManager.notify(notify_id, mBuilder.build());


					} else {

						mNotificationManager.notify(notify_id, mBuilder.build());
					}
//					if (notiication_type.equalsIgnoreCase("ACTIVITY_FORCED_READY")||notiication_type.equalsIgnoreCase("ACTIVITY_STARTED_LATE")||notiication_type.equalsIgnoreCase("ACTIVITY_WARNING")||notiication_type.equalsIgnoreCase("ACTIVITY_STOPPED")||notiication_type.equalsIgnoreCase("ACTIVITY_COMPLETED_EARLY")||notiication_type.equalsIgnoreCase("ACTIVITY_STARTED_EARLY")||notiication_type.equalsIgnoreCase("ACTIVITY_RESTARTED")||notiication_type.equalsIgnoreCase("ACTIVITY_ASSIGNED")||notiication_type.equalsIgnoreCase("ACTIVITY_REASSIGNED_FOR_APPROVAL")||notiication_type.equalsIgnoreCase("ACTIVITY_MADE_READY_LATE"))

				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method will store data in local storage
	 */
	public boolean saveDataInLocalStorage(String mJSONData, String file_name) {
		try {
			// File mFile = new File(getFilesDir(), "Tracking");
			File mFile = new File(getFilesDir(), getMyApplication().getUserID());

			if (!mFile.exists()) {
				mFile.mkdir();
			}

			File mFileUnRead = new File(mFile.getAbsolutePath(), "UnRead");

			if (!mFileUnRead.exists()) {
				mFileUnRead.mkdir();
			}

			String filename = file_name + ".txt";

			if (new File(mFileUnRead.getAbsolutePath() + "/" + filename).exists())
				return false;

			BufferedWriter bos = new BufferedWriter(new FileWriter(mFileUnRead.getAbsolutePath() + "/" + filename));
			bos.write(mJSONData);
			bos.flush();
			bos.close();

			// mEditor.putBoolean(getString(R.string.sp_is_updated), false);
			// mEditor.commit();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Method will connect web socket
	 */
	private void connectWebSocket() {

		String server_host = getSharedPreferences(getString(R.string.sp_server_name_file), Context.MODE_PRIVATE).getString(
				getString(R.string.sp_server_name), "");

		final String wsuri = "ws://" + server_host + ":80/constructionAPI/update/websocket?at=" + mActivity.getMyApplication().getUserToken();
		final String TAG = "SOCKET";

		try {
			mWebSocketStomp = new WebSocketStomp(new URI(wsuri), "x", "x");
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("id", "sub-3");
			mWebSocketStomp.subscribe("/user/queue/activityHistory", new Listener() {

				@Override
				public void message(Map headers, String body) {

					System.out.println(body);
					Log.d(TAG, "Message recieved... " + body);
					readMessageFromSocket(body);
				}

			}, headers);
			mWebSocketStomp.addErrorListener(new Listener() {

				@Override
				public void message(Map arg0, String arg1) {

					Log.d(TAG, "Error... " + arg1);
				}
			});
			if (mWebSocketStomp.isConnected()) {
				Log.d(TAG, "Socket connected... ");
			} else {
				Log.d(TAG, "Socket not connected... ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Back process to connect web socket
	 * 
	 * @author npatel
	 * 
	 */
	public class ConnectSocketAsync extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				// ProjectsParser mProjectsParser = new ProjectsParser();
				// mProjectsParser = (ProjectsParser)
				// mActivity.getCommonMethod().getHistory(mActivity.getMyApplication().getUserToken(),
				// mProjectsParser);

				connectWebSocket();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			mWebSocketStomp.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
