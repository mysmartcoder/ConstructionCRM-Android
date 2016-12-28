package com.construction.android.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract.PhoneLookup;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.construction.android.R;
import com.construction.android.activity.LoginActivity;

@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class CommonMethod {

	private SharedPreferences mSharedPreferences;
	private Editor mEditor;
	public boolean isError = false;
	public boolean isNetError = false;
	public Bitmap bitmap;
	public String mStringResponse = "";
	Activity mActivity;
	Gson mGson;
	// String mStringWSMainURL = "[prod_API]";
	String mStringWSMainURL = "[prod_API]";

	String posfixURL = "";
	String mStringToken = "";
	String mStringHeaderValue = "construction-android-native/phone";

	public CommonMethod(Activity activity, String url) {
		mActivity = activity;
		mGson = new Gson();
		mStringWSMainURL = "http://"+url+"/constructionAPI/";
		mSharedPreferences = mActivity.getSharedPreferences(activity.getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public CommonMethod() {
		mGson = new Gson();
	}

	/**
	 * function will check internet avail or not
	 * 
	 * @return boolean
	 */
	public boolean check_Internet() {
		ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Method will print data
	 * 
	 * @param data
	 */
	public void printData(String data) {
		//System.out.println(data);
	}

	/**
	 * Method will return application directory path.
	 * 
	 * @return directory path
	 */
	public String getApplicationDirectory() {

		String appPath = "";
		try {

			File SDCardRoot = Environment.getExternalStorageDirectory();
			File folder = new File(SDCardRoot, mActivity.getString(R.string.app_name));
			if (!folder.exists())
				folder.mkdirs();

			appPath = folder.getAbsolutePath();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return appPath;
	}

	/**
	 * Function will resize bitmap
	 * 
	 * @param mBitmap
	 * @return Bitmap
	 */
	public Bitmap resizeBitmap(Bitmap mBitmap) {
		try {
			mBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (mBitmap.getWidth() * 0.5), (int) (mBitmap.getHeight() * 0.5), true);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mBitmap;
	}

	/**
	 * Function will convert date into dd-MM-yyyy format
	 * 
	 * @param calendar
	 * @param format
	 * @return date in dd-MM-yyyy format
	 */
	@SuppressLint("SimpleDateFormat")
	public String getDateFormatFromCalendar(Calendar calendar, String format) {
		String formatedDate = "";
		Date mDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		formatedDate = sdf.format(mDate);
		return formatedDate;
	}

	/**
	 * Function will convert date format
	 * 
	 * @param mStringDate
	 * @return date in new format
	 */
	@SuppressLint("SimpleDateFormat")
	public String getDateInFormateReverse(String mStringDate, String oldFormat, String newFormat) {
		String formatedDate = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);
		Date myDate = null;
		try {
			myDate = dateFormat.parse(mStringDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
		formatedDate = timeFormat.format(myDate);

		return formatedDate;
	}

	/**
	 * 
	 * @return
	 */
	public String getTimeAgo(String date) {

		final int SECOND_MILLIS = 1000;
		final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
		final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
		final int DAY_MILLIS = 24 * HOUR_MILLIS;
		long time = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy");
		Date myDate = null;
		try {
			myDate = dateFormat.parse(date);
			date = timeFormat.format(myDate);
			time = myDate.getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (time == 0) {
			time = System.currentTimeMillis();
		}

		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();

		if (time > now || time <= 0) {
			return date;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + " minutes ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "an hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + " hours ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "yesterday";
		} else if (diff / DAY_MILLIS < 30) {
			return diff / DAY_MILLIS + " days ago";
		} else {
			return date;
		}

	}

	/**
	 * Function will check value is empty or not.
	 * 
	 * @param value
	 * @return value
	 */
	public String checkEmpty(String value) {
		if (value == null || value.trim().length() == 0 || value.trim().equalsIgnoreCase("null"))
			return "";
		else
			return value;
	}

	/**
	 * Function will check text match email patter or not
	 * 
	 * @param email
	 * @return true or false
	 */
	public boolean checkValidEmail(String email, EditText mEditText) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		if (pattern.matcher(email).matches()) {
			return pattern.matcher(email).matches();
		} else {
			mEditText.requestFocus();
			return pattern.matcher(email).matches();
		}
	}

	/**
	 * Function will check text match url patter or not
	 * 
	 * @param url
	 * @return true or false
	 */
	public boolean checkValidWebURL(String url) {
		Pattern pattern = Patterns.WEB_URL;
		if (pattern.matcher(url).matches()) {
			return pattern.matcher(url).matches();
		} else {
			return pattern.matcher(url).matches();
		}
	}

	public SpannableString setSpanText(String real_text, final String span_text) {
		SpannableString spannable = new SpannableString(real_text);
		int start = real_text.toLowerCase().indexOf(span_text.toLowerCase());
		if (start == -1) {
			return spannable;
		}
		int end = start + span_text.length();
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Toast.makeText(mActivity, span_text, Toast.LENGTH_SHORT).show();
			}
		};
		ClickableSpan clickableSpan1 = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Toast.makeText(mActivity, "niranj", Toast.LENGTH_SHORT).show();
			}
		};

		// spannable.setSpan(new ForegroundColorSpan(Color.RED), start, end,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(clickableSpan1, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * Function will return contact name from number
	 * 
	 * @param context
	 * @param phoneNumber
	 * @return contact name
	 */
	public String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri, new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}

	/**
	 * Function will remove Extra Characters From Number
	 * 
	 * @param phoneNumber
	 * @return contact number
	 */
	public String removeExtraCharactersFromNumber(String phoneNumber) {
		try {
			phoneNumber = phoneNumber.replace("(", "");
			phoneNumber = phoneNumber.replace(")", "");
			phoneNumber = phoneNumber.replace("-", "");
			phoneNumber = phoneNumber.replace(" ", "");
			phoneNumber = phoneNumber.replace("+", "");
			phoneNumber = phoneNumber.substring(phoneNumber.length() - 10);

		} catch (Exception e) {
			phoneNumber = "";
			Log.e("Cal Reciever_R", e.toString());
		}
		return phoneNumber;
	}

	/**
	 * Method will display alert dialog
	 * 
	 * @param title
	 * @param message
	 * @param isFinish
	 */
	public void showDialog(String title, String message, final boolean isFinish) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setTitle(title);
		alertDialog.setCancelable(false);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(mActivity.getResources().getString(R.string.lbl_ok), new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (isFinish) {
					//Intent mIntent = new Intent(mActivity,LoginActivity.class);
					mActivity.onBackPressed();
					//mActivity.startActivity(mIntent);
					
				}
			}
		});

		alertDialog.show();
	}

	/**
	 * Function will hide softkeyboard
	 * 
	 * @param mEditText
	 */
	public void HideKeyboard(final View mEditText) {
		final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			// imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
				}
			}, 200);
		}
	}

	/**
	 * Function will show softkeyboard
	 * 
	 * @param mEditText
	 */
	public void showKeyboard(final EditText mEditText) {
		final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					imm.showSoftInput(mEditText, 0);
				}
			}, 200);

		}
	}

	/**
	 * Function will download file from URL
	 * 
	 * @param mStringUrl
	 * @return local file path
	 */
	public String downloadFile(String mStringUrl, String filename, String token) {
		isError = false;
		String file_path = "";
		try {
			// set the download URL, a url that points to a file on the internet
			// this is the file to be downloaded
			String file_name = filename;
			// if(isPdf)
			// file_name = "reports.pdf";
			// else
			// file_name = "reports.xls";

			URL url = new URL(mStringUrl);

			// create the new connection
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestProperty("x-auth-token", token);
			urlConnection.setRequestProperty("User-Agent", mStringHeaderValue);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// set up some things on the connection
			urlConnection.setRequestMethod("GET");
			// urlConnection.setUseCaches(false);
			// urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);

			// and connect!
			urlConnection.connect();

			// set the path where we want to save the file
			// in this case, going to save it on the root directory of the
			// sd card.
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// create a new file, specifying the path, and the filename
			// which we want to save the file as.
			File folder = new File(SDCardRoot, "construction");
			if (!folder.exists())
				folder.mkdirs();
			File file = new File(SDCardRoot, "construction/" + file_name);
			file_path = file.getAbsolutePath();

			//System.out.println("size.." + urlConnection.getContentLength());

			// this will be used to write the downloaded data into the file we
			// created
			FileOutputStream fileOutput = new FileOutputStream(file);

			// this will be used in reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();

			// this is the total size of the file
			// int totalSize = urlConnection.getContentLength();
			// variable to store total downloaded bytes
			// int downloadedSize = 0;

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0; // used to store a temporary size of the
									// buffer

			// now, read through the input buffer and write the contents to the
			// file
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				// add the data in the buffer to the file in the file output
				// stream (the file on the sd card
				fileOutput.write(buffer, 0, bufferLength);
				// add up the size so we know how much is downloaded
				// downloadedSize += bufferLength;
				// this is where you would do something to report the prgress,
				// like this maybe
				// updateProgress(downloadedSize, totalSize);

			}
			// close the output stream when done
			fileOutput.close();

			// catch some possible errors...
		} catch (MalformedURLException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		}
		return file_path;
	}

	/**
	 * Function will call webservice
	 * 
	 * @param url
	 * @param mNameValuePair
	 * @param mObject
	 * @return webservice response
	 */
	public Object callWebService(String url, String mToken, List<NameValuePair> mNameValuePair, Object mObject) {
		Object mFillObject = null;

		String responseData = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}

		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpPost httppost = new HttpPost(url);

		//System.out.println("url .." + url);

		if (mToken.length() > 0)
			httppost.setHeader("x-auth-token", mToken);

		httppost.setHeader("User-Agent", mStringHeaderValue);
		MultipartEntity mEntity = new MultipartEntity();
		Charset chars = Charset.forName("UTF-8");
		

		try {

			for (int i = 0; i < mNameValuePair.size(); i++) {
				if (mNameValuePair.get(i).getName().equalsIgnoreCase("file")) {

					if (mNameValuePair.get(i).getValue().length() > 0) {
						File mFile = new File(mNameValuePair.get(i).getValue());
						mEntity.addPart(mNameValuePair.get(i).getName(), new FileBody(mFile));
					}
				} else {
					mEntity.addPart(mNameValuePair.get(i).getName(), new StringBody(mNameValuePair.get(i).getValue(),chars));
				}
				//System.out.println(mNameValuePair.get(i).getName() + ":" + mNameValuePair.get(i).getValue());
			}

			mFillObject = mObject.getClass().newInstance();
			// httppost.setEntity(mEntity);

			// Execute HTTP Post Request
			HttpResponse response;
			response = httpclient.execute(httppost);
			try {

				String headers = response.getFirstHeader("x-auth-token").toString();
				if (headers != null) {
					String[] token = headers.split(":");
					mStringToken = token[1].trim();
					//System.out.println("token..." + mStringToken);
					Header[] header = response.getAllHeaders();
					for (int i = 0; i < header.length; i++) {
						//System.out.println("header..." + header[i].toString());
					}
				}
			} catch (Exception e) {
//				e.printStackTrace();
			}

			responseData = EntityUtils.toString(response.getEntity());
			if (responseData.startsWith("["))
				responseData = "{ data : " + responseData + "}";

			System.out.println("RESPONSE==" + responseData);

			mFillObject = mGson.fromJson(responseData, mFillObject.getClass());

		} catch (ClientProtocolException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = true;
			e.printStackTrace();
		} catch (InstantiationException e) {
			isError = true;
			e.printStackTrace();
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return mFillObject;
	}

	/**
	 * Function will call webservice
	 * 
	 * @param url
	 * @param mNameValuePair
	 * @param mObject
	 * @return webservice response
	 */
	public Object callMultipartWebService(String url, String mToken, List<NameValuePair> mNameValuePair, Object mObject) {
		Object mFillObject = null;

		String responseData = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}

		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpPost httppost = new HttpPost(url);

		//System.out.println("url .." + url);

		if (mToken.length() > 0)
			httppost.setHeader("x-auth-token", mToken);

		httppost.setHeader("User-Agent", mStringHeaderValue);
		MultipartEntity mEntity = new MultipartEntity();
		Charset chars = Charset.forName("UTF-8");
		
		try {

			for (int i = 0; i < mNameValuePair.size(); i++) {
				if (mNameValuePair.get(i).getName().equalsIgnoreCase("file")) {

					if (mNameValuePair.get(i).getValue().length() > 0) {
						File mFile = new File(mNameValuePair.get(i).getValue());
						//System.out.println("IMAGE VALUE===" + mFile.length());

						mEntity.addPart(mNameValuePair.get(i).getName(), new FileBody(mFile));
					}
				} else {
					mEntity.addPart(mNameValuePair.get(i).getName(), new StringBody(mNameValuePair.get(i).getValue(),chars));
				}
				//System.out.println(mNameValuePair.get(i).getName() + ":" + mNameValuePair.get(i).getValue());
			}

			mFillObject = mObject.getClass().newInstance();
			httppost.setEntity(mEntity);

			// Execute HTTP Post Request
			HttpResponse response;
			response = httpclient.execute(httppost);
			try {

				String headers = response.getFirstHeader("x-auth-token").toString();
				if (headers != null) {
					String[] token = headers.split(":");
					mStringToken = token[1].trim();
					System.out.println("token..." + mStringToken);
					Header[] header = response.getAllHeaders();
					for (int i = 0; i < header.length; i++) {
						//System.out.println("header..." + header[i].toString());
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			responseData = EntityUtils.toString(response.getEntity());
			if (responseData.startsWith("["))
				responseData = "{ data : " + responseData + "}";

			System.out.println("RESPONSE==" + responseData);

			mFillObject = mGson.fromJson(responseData, mFillObject.getClass());

		} catch (ClientProtocolException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = true;
			e.printStackTrace();
		} catch (InstantiationException e) {
			isError = true;
			e.printStackTrace();
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return mFillObject;
	}

	/**
	 * Function will call webservice
	 * 
	 * @param url
	 * @param mNameValuePair
	 * @param mObject
	 * @return webservice response
	 */
	public Object callGetWebService(String url, String mToken, List<NameValuePair> mNameValuePair, Object mObject) {
		Object mFillObject = null;

		String responseData = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}

		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpGet httppost = new HttpGet(url);

		//System.out.println("url .." + url);

		if (mToken.length() > 0)
			httppost.setHeader("x-auth-token", mToken);
		
		httppost.setHeader("User-Agent", mStringHeaderValue);

		MultipartEntity mEntity = new MultipartEntity();
		Charset chars = Charset.forName("UTF-8");

		try {

			for (int i = 0; i < mNameValuePair.size(); i++) {
				if (mNameValuePair.get(i).getName().equalsIgnoreCase("file")) {

					if (mNameValuePair.get(i).getValue().length() > 0) {
						File mFile = new File(mNameValuePair.get(i).getValue());
						mEntity.addPart(mNameValuePair.get(i).getName(), new FileBody(mFile));
					}
				} else {
					mEntity.addPart(mNameValuePair.get(i).getName(), new StringBody(mNameValuePair.get(i).getValue(),chars));
				}
				//System.out.println(mNameValuePair.get(i).getName() + ":" + mNameValuePair.get(i).getValue());
			}

			mFillObject = mObject.getClass().newInstance();
			// httppost.setEntity(mEntity);

			// Execute HTTP Post Request
			HttpResponse response;
			response = httpclient.execute(httppost);
			try {

				String headers = response.getFirstHeader("x-auth-token").toString();
				if (headers != null) {
					String[] token = headers.split(":");
					mStringToken = token[1].trim();
					System.out.println("token..." + mStringToken);
					Header[] header = response.getAllHeaders();
					for (int i = 0; i < header.length; i++) {
						//System.out.println("header..." + header[i].toString());
					}
				}
			} catch (Exception e) {
//				 e.printStackTrace();
			}

			responseData = EntityUtils.toString(response.getEntity());
			if (responseData.startsWith("["))
				responseData = "{ data : " + responseData + "}";

//			writeToFileTemp(responseData);
			System.out.println("RESPONSE==" + responseData);
			mStringResponse = responseData;
			try {
				mFillObject = mGson.fromJson(responseData, mFillObject.getClass());
			} catch (Exception e) {
				e.printStackTrace();
				if (url.contains("percentageOfActivities")) {
					return mStringResponse;
				} else {
					isError = true;
				}

			}

		} catch (ClientProtocolException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = true;
			e.printStackTrace();
		} catch (InstantiationException e) {
			isError = true;
			e.printStackTrace();
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return mFillObject;
	}

	/**
	 * Method call will get picture from server.
	 * 
	 * @param url
	 * @param mToken
	 * @param mObject
	 * @return
	 */
	public Object callGetImageWebService(String url, String mToken, Object mObject) {
		Bitmap decodedByte = null;

		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}
		try {
			final HttpClient client = AndroidHttpClient.newInstance("Android");
			final HttpGet getRequest = new HttpGet(url);
			getRequest.setHeader("x-auth-token", mToken);
			getRequest.setHeader("User-Agent", mStringHeaderValue);
			// getRequest.setHeader("Content-Type", "image/jpeg");

			try {
				HttpResponse response = client.execute(getRequest);
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					try {
						String StringResponse = EntityUtils.toString(entity, HTTP.UTF_8);
						if (StringResponse != null) {
							//System.out.println("response=== " + StringResponse);
							if (StringResponse.length() > 0) {
								byte[] decodedString = Base64.decode(StringResponse, Base64.DEFAULT);
								// decodedByte =
								// BitmapFactory.decodeByteArray(decodedString,
								// 0, decodedString.length);
								return decodedString;
							}
						}
					} finally {
						entity.consumeContent();
					}
				}

			} catch (Exception e) {
				System.err.println(e.toString());
			} finally {
				if ((client instanceof AndroidHttpClient)) {
					((AndroidHttpClient) client).close();
				}
			}
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return decodedByte;
	}

	/**
	 * Method call will webservice.
	 * 
	 * @param url
	 * @param mToken
	 * @return
	 */
	public Object callGetFilePostWebService(String url, String mToken, List<NameValuePair> mNameValuePair) {
		String path = "";
		String filename = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return path;
		}
		try {
			final HttpClient client = AndroidHttpClient.newInstance("Android");
			final HttpPost httppost = new HttpPost(url);
			httppost.setHeader("x-auth-token", mToken);
			// httppost.setHeader("Content-Type", "image/jpeg");
			httppost.setHeader("User-Agent", mStringHeaderValue);
			Charset chars = Charset.forName("UTF-8");
			try {
				MultipartEntity mEntity = new MultipartEntity();
				for (int i = 0; i < mNameValuePair.size(); i++) {
					if (mNameValuePair.get(i).getName().equalsIgnoreCase("file")) {
						if (mNameValuePair.get(i).getValue().length() > 0) {
							File mFile = new File(mNameValuePair.get(i).getValue());
							mEntity.addPart(mNameValuePair.get(i).getName(), new FileBody(mFile));
						}
					} else {
						if (mNameValuePair.get(i).getName().equalsIgnoreCase("filename"))
							filename = mNameValuePair.get(i).getValue();
						mEntity.addPart(mNameValuePair.get(i).getName(), new StringBody(mNameValuePair.get(i).getValue(),chars));
					}
					//System.out.println(mNameValuePair.get(i).getName() + ":" + mNameValuePair.get(i).getValue());
				}
				httppost.setEntity(mEntity);

				HttpResponse response = client.execute(httppost);
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					try {
						String StringResponse = EntityUtils.toString(entity, HTTP.UTF_8);
						if (StringResponse != null) {
							//System.out.println("response=== " + StringResponse);
							if (StringResponse.length() > 0) {
								byte[] decodedString = Base64.decode(StringResponse, Base64.DEFAULT);
								path = getApplicationDirectory() + "/" + filename;
								FileOutputStream mOutputStream = new FileOutputStream(new File(path));
								mOutputStream.write(decodedString);
								return path;
							}
						}
					} finally {
						entity.consumeContent();
					}
				}

			} catch (Exception e) {
				System.err.println(e.toString());
			} finally {
				if ((client instanceof AndroidHttpClient)) {
					((AndroidHttpClient) client).close();
				}
			}
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * Function will call webservice
	 * 
	 * @param url
	 * @param mObject
	 * @return webservice response
	 */
	public Object callPostWebService(String url, String mToken, String mStringJson, Object mObject) {
		Object mFillObject = null;

		String responseData = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}

		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpPost httppost = new HttpPost(url);

		//System.out.println("url .." + url);
		//System.out.println("token .." + mToken);

		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept", "application/json");

		if (mToken.length() > 0)
			httppost.setHeader("x-auth-token", mToken);
		
		httppost.setHeader("User-Agent", mStringHeaderValue);

		try {

			mFillObject = mObject.getClass().newInstance();
			httppost.setEntity(new StringEntity(mStringJson,"UTF-8"));

			// Execute HTTP Post Request
			HttpResponse response;
			response = httpclient.execute(httppost);

			responseData = EntityUtils.toString(response.getEntity());
			if (responseData.startsWith("["))
				responseData = "{ data : " + responseData + "}";

			System.out.println("RESPONSE==" + responseData);

			mFillObject = mGson.fromJson(responseData, mFillObject.getClass());

		} catch (ClientProtocolException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = true;
			e.printStackTrace();
		} catch (InstantiationException e) {
			isError = true;
			e.printStackTrace();
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return mFillObject;
	}

	/**
	 * Function will call webservice
	 * 
	 * @param url
	 * @param mObject
	 * @return webservice response
	 */
	public Object callPutWebService(String url, String mToken, String mStringJson, Object mObject) {
		Object mFillObject = null;

		String responseData = "";
		isError = false;
		isNetError = false;
		if (!check_Internet()) {
			isNetError = true;
			isError = true;
			return mObject;
		}

		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		HttpClient httpclient = new DefaultHttpClient(params);
		HttpPost httppost = new HttpPost(url);

		//System.out.println("url .." + url);
		//System.out.println("token .." + mToken);

		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("Accept", "application/json");

		if (mToken.length() > 0)
			httppost.setHeader("x-auth-token", mToken);
		
		httppost.setHeader("User-Agent", mStringHeaderValue);

		try {

			mFillObject = mObject.getClass().newInstance();
			httppost.setEntity(new StringEntity(mStringJson,"UTF-8"));

			// Execute HTTP Post Request
			HttpResponse response;
			response = httpclient.execute(httppost);

			responseData = EntityUtils.toString(response.getEntity());
			if (responseData.startsWith("["))
				responseData = "{ data : " + responseData + "}";

			System.out.println("RESPONSE==" + responseData);

			mFillObject = mGson.fromJson(responseData, mFillObject.getClass());

		} catch (ClientProtocolException e) {
			isError = true;
			e.printStackTrace();
		} catch (IOException e) {
			isError = true;
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			isError = true;
			e.printStackTrace();
		} catch (InstantiationException e) {
			isError = true;
			e.printStackTrace();
		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
		}
		return mFillObject;
	}

	/**
	 * Function will call login WebService
	 * 
	 * @param username
	 * @param password
	 * @return webservice response
	 */
	public Object loginAPI(String username, String password, Object mObject) {

		String mStringURL = mStringWSMainURL + "authenticate?username=" + username + "&password=" + password;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		System.out.println("===LOGIN===	"+mStringURL);
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	public String setToken() {
		return mStringToken;
//		return "1234";

	}

	/**
	 * Function call will Forgote Password Webservice.
	 * 
	 * @param email
	 * @param mObject
	 * @return
	 */
	public Object forgotPasswordAPI(String email, Object mObject) {

		String mStringURL = mStringWSMainURL + "login/forgotpassword";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("email", email));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Function call will user Change Password webservice.
	 * 
	 * @param email
	 * @param oldpassword
	 * @param newpassword
	 * @param mObject
	 * @return
	 */
	public Object changePasswordAPI(String email, String oldpassword, String newpassword, String token, Object mObject) {

		String mStringURL = mStringWSMainURL + "user/chnagepassword/token/" + token;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("email", email));
		nameValuePair.add(new BasicNameValuePair("oldpassword", oldpassword));
		nameValuePair.add(new BasicNameValuePair("newpassword", newpassword));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Function call will get All Project List.
	 * 
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object getProjects(String token, String user_guiid, Object mObject) {

//		String mStringURL = mStringWSMainURL + "projects?depth=0";
//		String mStringURL = mStringWSMainURL + "projects";
		String mStringURL = mStringWSMainURL + "actor/"+user_guiid+"/projects";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		System.out.println("URL-==="+mStringURL);
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Function call will set notes.
	 * 
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object setNotesAPI(String token, String mStringGuId, String mStringUserId, String mStringMessage, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + mStringGuId + "/addNote";

		JSONObject mJsonObject = new JSONObject();
		try {
			mJsonObject.put("message", mStringMessage);
			mJsonObject.put("authorGuid", mStringUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will set Check box selection API.
	 * 
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object setSelectionAPI(String taskID, String token, String mStringJSON, boolean matrial, boolean equipment, boolean workers,
			boolean space, boolean design,boolean exteranl, String stauts, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + taskID;
		// String mStringURL = mStringWSMainURL + "project/" + projectID +
		// "/responsibleActor/" + userID + "/activities/status/" +
		// mStringStatus;
		JSONObject mJsonObject = null;

		try {

			mJsonObject = new JSONObject(mStringJSON);
			mJsonObject.put("materialConstraint", matrial);
			mJsonObject.put("equipmentConstraint", equipment);
			mJsonObject.put("manpowerConstraint", workers);
			mJsonObject.put("spaceConstraint", space);
			mJsonObject.put("designConstraint", design);
			mJsonObject.put("externalConstraint", exteranl);
			mJsonObject.put("status", stauts);

//			System.out.println("CHANGE JSON DATA=====" + mJsonObject.toString());
//			System.out.println("CHANGE JSON DATA 	=====" + mJsonObject.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will set Action Start in (Make Ready Screen)
	 * 
	 * @param guID
	 * @param mStringDateComments
	 * @param mStringReason
	 * @param token
	 * @param mStringDate
	 * @param mObject
	 * @return
	 */
	public Object setActionStartAPI(String guID, String mStringDateComments, ArrayList<String> mStringReason, String token, String mStringDate,
			Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + guID + "/start";
		JSONObject mJsonObject = null;
		try {
			JSONArray mJsonArray = new JSONArray();
			for (int i = 0; i < mStringReason.size(); i++) {
				mJsonArray.put(Integer.parseInt(mStringReason.get(i)));
			}

			mJsonObject = new JSONObject();
			mJsonObject.put("comment", mStringDateComments);
			mJsonObject.put("reasons", mJsonArray);
			mJsonObject.put("statusChangeDate", Long.parseLong(mStringDate));
			//System.out.println("json data..." + mJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will set Action Warn in (Make Ready Screen)
	 * 
	 * @param guID
	 * @param mStringDateComments
	 * @param mStringReason
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object setActionWarnAPI(String guID, String mStringDateComments, ArrayList<String> mStringReason, String token, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + guID + "/warn";
		JSONObject mJsonObject = null;
		try {
			JSONArray mJsonArray = new JSONArray();
			for (int i = 0; i < mStringReason.size(); i++) {
				mJsonArray.put(Integer.parseInt(mStringReason.get(i)));
			}

			mJsonObject = new JSONObject();

			mJsonObject.put("comment", mStringDateComments);
			mJsonObject.put("reasons", mJsonArray);
			//System.out.println("json data..." + mJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will set Action Stop in (Make Ready Screen)
	 * 
	 * @param guID
	 * @param mStringDateComments
	 * @param mStringReason
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object setActionStopAPI(String guID, String mStringDateComments, ArrayList<String> mStringReason, String token, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + guID + "/stop";
		JSONObject mJsonObject = null;
		try {
			JSONArray mJsonArray = new JSONArray();
			for (int i = 0; i < mStringReason.size(); i++) {
				mJsonArray.put(Integer.parseInt(mStringReason.get(i)));
			}

			mJsonObject = new JSONObject();
			mJsonObject.put("comment", mStringDateComments);
			mJsonObject.put("reasons", mJsonArray);
			//System.out.println("json data..." + mJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will set Action Complete in (Make Ready Screen)
	 * 
	 * @param guID
	 * @param mStringDateComments
	 * @param mStringReason
	 * @param token
	 * @param mStringDate
	 * @param mObject
	 * @return
	 */
	public Object setActionCompleteAPI(String guID, String approveGuID, String mStringDateComments, ArrayList<String> mStringReason, String token,
			String mStringDate, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + guID + "/complete";
		JSONObject mJsonObject = null;
		try {

			JSONArray mJsonArray = new JSONArray();
			for (int i = 0; i < mStringReason.size(); i++) {
				mJsonArray.put(Integer.parseInt(mStringReason.get(i)));
			}

			mJsonObject = new JSONObject();
			mJsonObject.put("comment", mStringDateComments);
			mJsonObject.put("approverGuid", approveGuID);
			mJsonObject.put("reasons", mJsonArray);
			mJsonObject.put("statusChangeDate", Long.parseLong(mStringDate));
			//System.out.println("json data..." + mJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
	}

	/**
	 * Function call will get All Approver Guid.
	 * 
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object getApproverGuidAPI(String token, String guID, Object mObject) {

		String mStringURL = mStringWSMainURL + "project/" + guID + "/approverActors";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Method call will set InspectRecject.
	 * 
	 * @param guID
	 * @param reason
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object setInspectRejectAPI(String guID, String token, String reason, String comment, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + guID + "/inspectReject";

		JSONArray mJsonArray = new JSONArray();
		mJsonArray.put(Integer.parseInt(reason));

		JSONObject mJsonObject = null;
		try {
			mJsonObject = new JSONObject();
			mJsonObject.put("comment", comment);
			mJsonObject.put("reasons", mJsonArray);
			//System.out.println("json data..." + mJsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return callPostWebService(mStringURL, token, mJsonObject.toString(), mObject);
		
		
	}

	/**
	 * Function call will get Task in Progress List.
	 * 
	 * @param token
	 * @return
	 */
	public Object getTaskInProgress(String token, String projectID, String userID, String mStringStatus, Object mObject) {
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/responsibleActor/" + userID + "/activities/status/" + mStringStatus;
		System.out.println("URL==="+mStringURL);
		System.out.println("TOKEN==" + token);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Function call will get Predescessores list.
	 * 
	 * @param token
	 * @param projectID
	 * @param mObject
	 * @return
	 */
	public Object getPredescessorsAPI(String token, String projectID, Object mObject) {
		String mStringURL = mStringWSMainURL + "activity/" + projectID;
		System.out.println("PREDESESSOR URL=="+mStringURL);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Functinon call will send file
	 * 
	 * @param token
	 * @param filename
	 * @param projectID
	 * @return
	 */
	public Object sendFileAPI(String token, String filename, String filePath, String projectID, String mString) {
		String mStringURL = mStringWSMainURL + "activity/" + projectID + "/fileUpload";

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("filename", filename));
		nameValuePair.add(new BasicNameValuePair("file", filePath));
		// nameValuePair.add(new BasicNameValuePair("activityGuid", projectID));
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Function call will get file list..
	 * 
	 * @param token
	 * @param projectID
	 * @param mObject
	 * @return
	 */
	public Object getFileListAPI(String token, String projectID, Object mObject) {
		String mStringURL = mStringWSMainURL + "activity/" + projectID + "/files";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Method call Download Actvity File
	 * 
	 * @param token
	 * @param projectID
	 * @return
	 */
	public Object getActivityFileDownloadAPI(String token, String projectID, String filename) {

		String mStringURL = mStringWSMainURL + "activity/" + projectID + "/fileDownload";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("filename", filename));
		return callGetFilePostWebService(mStringURL, token, nameValuePair);
	}

	/**
	 * Function call will set InspectAccept (Snagging)
	 * 
	 * @param token
	 * @param projectID
	 * @return
	 */
	public Object setInspectAcceptAPI(String token, String projectID, Object mObject) {

		String mStringURL = mStringWSMainURL + "activity/" + projectID + "/inspectAccept";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callPostWebService(mStringURL, token, "", mObject);
	}

	/**
	 * Function call get History of task.
	 * 
	 * @param token
	 * @param projectID
	 * @param mObject
	 * @return
	 */
	public Object getHistoryAPI(String token, String projectID, Object mObject) {
		String mStringURL = mStringWSMainURL + "activity/" + projectID + "/history";
		System.out.println("HISTORY URL=="+mStringURL);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Method call will update Profile Picture.
	 * 
	 * @param token
	 * @param filename
	 * @param filePath
	 * @param mStringUserId
	 * @return
	 */
	public Object updateProfilePicAPI(String token, String filename, String filePath, String mStringUserId, String mStringResponse) {
		String mStringURL = mStringWSMainURL + "user/" + mStringUserId + "/pictureUpload";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("filename", filename));
		nameValuePair.add(new BasicNameValuePair("file", filePath));
		return callMultipartWebService(mStringURL, token, nameValuePair, mStringResponse);
	}

	/**
	 * Method call will update First name and Family name API.
	 * 
	 * @param token
	 * @param mStringFname
	 * @param mStringLname
	 * @param mStringUserId
	 * @param mStringResponse
	 * @return
	 */
	public Object updateUserNameAPI(String token, String mStringFname, String mStringLname, String mStringUserId, String mStringResponse) {
		String mStringURL = mStringWSMainURL + "person/" + mStringUserId + "/updateProfile";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("givenname", mStringFname));
		nameValuePair.add(new BasicNameValuePair("familyname", mStringLname));
		return callMultipartWebService(mStringURL, token, nameValuePair, mStringResponse);
	}

	/**
	 * Method call will get user information.
	 * 
	 * @param token
	 * @param mStringUserName
	 * @param mObject
	 * @return
	 */
	public Object getUserDetailsAPI(String token, String mStringUserName, Object mObject) {
		String mStringURL = mStringWSMainURL + "user/" + mStringUserName + "/details";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * 
	 * @param token
	 * @param mObject
	 * @return
	 */
	public Object getUserPictureAPI(String token, String userGUID, Object mObject) {
		String mStringURL = mStringWSMainURL + "user/" + userGUID + "/fileDownload";
		return callGetImageWebService(mStringURL, token, mObject);
	}

	/**
	 * Method call will get list of delay tasks.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringStartDate
	 * @param mStringEndDate
	 * @param mObject
	 * @return
	 */
	public Object getDelayTaskListAPI(String token, String projectID, String mStringStartDate, String mStringEndDate, Object mObject) {
		// String mStringURL = mStringWSMainURL + "project/" + projectID +
		// "/reasonsForNoneCompletion/status/6/rangeStart/" + mStringStartDate
		// + "/rangeEnd/" + mStringEndDate;
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/namesOfActivities/status/5/rangeStart/" + mStringStartDate + "/rangeEnd/"
				+ mStringEndDate;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Method call wll giet stop tasks.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringStartDate
	 * @param mStringEndDate
	 * @param mObject
	 * @return
	 */
	public Object getStopTaskListAPI(String token, String projectID, String mStringStartDate, String mStringEndDate, Object mObject) {
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/namesOfActivities/status/6/rangeStart/" + mStringStartDate + "/rangeEnd/"
				+ mStringEndDate;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Method call will get Reason for Delay and Stop Tasks.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringStartDate
	 * @param mStringEndDate
	 * @param mObject
	 * @return
	 */
	public Object getReasonForDealyStopTasksAPI(String token, String projectID, String mStringStartDate, String mStringEndDate, Object mObject) {
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/reasonsForNoneCompletion/status/6/rangeStart/" + mStringStartDate
				+ "/rangeEnd/" + mStringEndDate;

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Method call will get Reason for non-completion tasks.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringStartDate
	 * @param mStringEndDate
	 * @param mObject
	 * @return
	 */
	public Object getReasonForNonCompletionTasksAPI(String token, String projectID, String mStringStartDate, String mStringEndDate, Object mObject) {
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/reasonsForNoneCompletion/status/5/rangeStart/" + mStringStartDate
				+ "/rangeEnd/" + mStringEndDate;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Method call will get Percentage Plan Complete API.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringStartDate
	 * @param mStringEndDate
	 * @param mObject
	 * @return
	 */
	public Object getPercentagePlanCompleteAPI(String token, String projectID, String mStringStartDate, String mStringEndDate, String mObject) {
		String mStringURL = mStringWSMainURL + "project/" + projectID + "/percentageOfActivities/status/7/rangeStart/" + mStringStartDate
				+ "/rangeEnd/" + mStringEndDate;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		return callGetWebService(mStringURL, token, nameValuePair, mObject);

	}

	/**
	 * Function will call Register WebService
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @param mObject
	 * @return
	 */
	public Object registerAPI(String username, String email, String password, Object mObject) {
		String mStringURL = mStringWSMainURL + "register/createuser";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("name", username));
		nameValuePair.add(new BasicNameValuePair("email", email));
		nameValuePair.add(new BasicNameValuePair("password", password));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Function call will set user profile picture.
	 * 
	 * @param userid
	 * @param profile_picture
	 * @param mStringToken
	 * @param mObject
	 * @return
	 */
	public Object setUserProfilePicAPI(String userid, String profile_picture, String mStringToken, Object mObject) {
		String mStringURL = mStringWSMainURL + "user/changeprofilepicture/token/" + mStringToken;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("uid", userid));
		nameValuePair.add(new BasicNameValuePair("profile_picture", profile_picture));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Function call will get user profile data.
	 * 
	 * @param userid
	 * @param mStringToken
	 * @param mObject
	 * @return
	 */
	public Object getUserProfileAPI(String userid, String mStringToken, Object mObject) {
		String mStringURL = mStringWSMainURL + "user/getuserbyid/token/" + mStringToken;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("uid", userid));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Method call will get All Notification Status.
	 * 
	 * @param token
	 * @param projectID
	 * @param mObject
	 * @return
	 */
	public Object getAllNotificationAPI(String token, String projectID, Object mObject) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callGetWebService(mStringURL, token, nameValuePair, mObject);
	}

	/**
	 * Method call update all show notification api
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @return
	 */
	public Object updateShowNotification(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications/" + mStringValue + "/update";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call update Show notificaton for everyone.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object updateShowNotificationEveryOne(String token, String projectID, boolean mStringValue, String mString) {
		// String mStringURL = mStringWSMainURL + "person/" + projectID +
		// "/notificatons/" + mStringValue + "/showFromEveryone";
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notificationsShownFrom/everyone";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call will update show notification for only me.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object showNotificationOnlyMe(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notificationsShownFrom/onlyMe";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call to update Activity Data Notification API.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object updateDataNotificationAPI(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications/" + mStringValue + "/showActivityDateChange";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call to update Status Notification API.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object updateStatusNotificationAPI(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications/" + mStringValue + "/showActivityStatusChange";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call will update Note Notification API.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object updateNoteNotificationAPI(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications/" + mStringValue + "/showActivityNotesChange";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Method call will update show referred to me notification API.
	 * 
	 * @param token
	 * @param projectID
	 * @param mStringValue
	 * @param mString
	 * @return
	 */
	public Object updateReferedMeNotificationAPI(String token, String projectID, boolean mStringValue, String mString) {
		String mStringURL = mStringWSMainURL + "person/" + projectID + "/notifications/" + mStringValue + "/showActivityReferred";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callMultipartWebService(mStringURL, token, nameValuePair, mString);
	}

	/**
	 * Function call will update device token ws.
	 * 
	 * @param user_id
	 * @param token
	 * @param token
	 * @param mObject
	 * @return ws response
	 */
	public Object updateDeviceToken(String user_id, String token, String device_token, Object mObject) {

		String mStringURL = mStringWSMainURL + "user/updatepushnotificationtoken/token/" + token;
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("uid", user_id));
		nameValuePair.add(new BasicNameValuePair("ntoken", device_token));
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	public Object getHistory(String token, Object mObject) {
		String mStringURL = mStringWSMainURL + "user/queue/activityHistory";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		return callWebService(mStringURL, "", nameValuePair, mObject);
	}

	/**
	 * Method call will check invalid mail address in edittex
	 * 
	 */
	public boolean emailValidator(String email, Context context, EditText editText) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			showAlert(context, mActivity.getString(R.string.app_name), "Enter valid email address");
			editText.requestFocus();
		}
		return matcher.matches();

	}

	/**
	 * Show Alert Dialog for select text
	 */
	public void showAlertDialog(final String title, final TextView mtTextView, final CharSequence[] items, final Activity mActivity) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		AlertDialog alertDialog;
		builder.setTitle("Select " + title);
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@SuppressLint("ResourceAsColor")
			public void onClick(DialogInterface dialog, int item) {

				mtTextView.setText(items[item]);
				dialog.dismiss();
			}
		});
		alertDialog = builder.create();
		alertDialog.show();

	}

	/**
	 * Method call will display Alert Dialog
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 * @param listener
	 */
	public void showDeleteAlert(Context context, String title, String msg, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton(mActivity.getString(R.string.lbl_ok), listener);
		alertDialogBuilder.setNegativeButton(mActivity.getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * Function will return application path
	 * 
	 * @return application path
	 */
	public String getAppPath() {
		File folder = new File(Environment.getExternalStorageDirectory(), mActivity.getString(R.string.app_name));
		if (!folder.exists())
			folder.mkdirs();

		return folder.getAbsolutePath();
	}

	/**
	 * Method will write file
	 * 
	 * @param mStringImagePath
	 * @param mBitmap
	 */
	public void copyImageFile(String mStringImagePath, Bitmap mBitmap) {
		OutputStream fOut = null;
		//System.out.println("CREATE FOLDER:::::");
		File file = new File(mStringImagePath);
		try {
			fOut = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			// mBitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method will call resize image
	 * 
	 * @param source
	 * @param angle
	 * @return
	 */
	public Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	/**
	 * Method call when check blank field validation for edittex
	 * 
	 * @param et
	 * @param context
	 * @param msg
	 * @return
	 */
	public boolean validateBlankField(final EditText et, final Context context, final String msg) {

		if (et.getText().toString().trim().length() == 0) {
			// CommanMethod.showToast(context, msg);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(R.string.app_name);
			alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					et.requestFocus();
					dialog.dismiss();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return false;
		}
		return true;
	}

	/**
	 * Function will check blank field
	 * 
	 * @param et
	 * @param context
	 * @param mStringCompare
	 * @param msg
	 * @return booleans
	 */
	public static boolean validateSelectkField(final TextView et, final Context context, final String mStringCompare, final String msg) {

		if (et.getText().toString().equalsIgnoreCase(mStringCompare)) {
			// CommanMethod.showToast(context, msg);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle(R.string.app_name);
			alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					et.setSelected(true);
					et.setFocusableInTouchMode(true);
					et.requestFocus();
					dialog.dismiss();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return false;
		}
		return true;
	}

	/**
	 * Method call will display Alert Dialog
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 */
	public void showAlert(Context context, String title, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	
	/**
	 * Method call will display Alert Dialog
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 */
	public void showSystemErrorAlert(Context context, String title, String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mActivity.onBackPressed();
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


	public void showSessionExpireAlert(Context context, String title, String msg) {
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//		alertDialogBuilder.setTitle(title);
//		alertDialogBuilder.setMessage(msg).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
				Intent mIntent = new Intent(mActivity, LoginActivity.class);
				mActivity.startActivity(mIntent);
				mActivity.finish();
//				dialog.dismiss();
//			}
//		});
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		alertDialog.show();
	}




	/**
	 * function will find out left time
	 * 
	 * @param end_time
	 * @return string
	 */
	public String findOutTimeLeft(long end_time) {

		long diffInMillisec = end_time - System.currentTimeMillis();
		long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
		int seconds = (int) diffInSec % 60;
		diffInSec /= 60;
		int minutes = (int) diffInSec % 60;
		diffInSec /= 60;
		int hours = (int) diffInSec % 24;
		diffInSec /= 24;
		int days = (int) diffInSec;

		return days + "d: " + hours + "h: " + minutes + "m: " + seconds + "s left";
	}

	/**
	 * function will find out day diff
	 * 
	 * @param end_time
	 * @return string
	 */
	public int findOutDayDiff(long start_time, long end_time) {

		long diffInMillisec = end_time - start_time;
		long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
		// int seconds = (int) diffInSec % 60;
		// diffInSec /= 60;
		// int minutes = (int) diffInSec % 60;
		// diffInSec /= 60;
		// int hours = (int) diffInSec % 24;
		// diffInSec /= 24;
		int days = (int) diffInSec;
		return days;
	}

	public void setTitleFont(TextView view) {
		Typeface type = Typeface.createFromAsset(mActivity.getAssets(), "fonts/title.ttf");
		view.setTypeface(type);
	}

	/**
	 * Function call will create Text file in system folder.
	 * 
	 * @param data
	 */
	public void writeToFile(String data) {
		data = mStringResponse;
		try {
			File myFile = new File(getAppPath() + "/Temp.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			//System.out.println("WRITE DATA=====" + data);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
		}
	}
	
	public void writeToFileTemp(String data) {
		data = mStringResponse;
		try {
			File myFile = new File(getAppPath() + "/Temp_response.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(data);
			//System.out.println("WRITE DATA=====" + data);
			myOutWriter.close();
			fOut.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Function call will Read Data from Temp.txt file.
	 * 
	 * @return
	 */
	public String readFile() {
		String aDataRow = "";
		String aBuffer = "";
		try {
			File myFile = new File(getAppPath() + "/Temp.txt");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));

			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			//System.out.println("READ DATA======" + aBuffer);
			myReader.close();
		} catch (Exception e) {
		}
		return aBuffer;
	}

	/**
	 * Method call return Time Ago, Left.
	 * 
	 * @param time
	 * @return
	 */
	public String relatvieTimveSpan(long time) {
		return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString();
	}

	/**
	 * Method call will set scrollview down selection.
	 * 
	 * @param mScrollView
	 */
	public void setScrollDown(final ScrollView mScrollView) {
		mScrollView.post(new Runnable() {
			@Override
			public void run() {
				mScrollView.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	/**
	 * Function will return extension of file
	 * 
	 * @param url
	 * @return file extension
	 */
	public String fileExt(String url) {
		if (url.indexOf("?") > -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		if (url.lastIndexOf(".") == -1) {
			return null;
		} else {
			String ext = url.substring(url.lastIndexOf("."));
			if (ext.indexOf("%") > -1) {
				ext = ext.substring(0, ext.indexOf("%"));
			}
			if (ext.indexOf("/") > -1) {
				ext = ext.substring(0, ext.indexOf("/"));
			}
			return ext.toLowerCase();

		}
	}

	/**
	 * Function call will return current system Date.
	 * 
	 * @return
	 */
	public String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}
	

	/**
	 * Method call will session out application
	 */
	public void sessionOut(){
		showSystemErrorAlert(mActivity,mActivity.getString(R.string.app_name) , mActivity.getString(R.string.validation_failed));
//		SharedPreferences mSharedPreferences = mActivity.getSharedPreferences(mActivity.getString(R.string.sp_file_name), Context.MODE_PRIVATE);
//		Editor mEditor = mSharedPreferences.edit();
//		mEditor.clear();
//		mEditor.commit();
//		Intent mIntent = new Intent(mActivity,LoginActivity.class);
//		mActivity.finish();
//		mActivity.startActivity(mIntent);

	}
}
