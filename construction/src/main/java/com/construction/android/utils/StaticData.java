package com.construction.android.utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

public class StaticData {

	public static String DATE_FORMAT_1 = "dd/MM/yyyy";
	public static String DATE_FORMAT_2 = "MM/dd/yyyy";
	public static String DATE_FORMAT_4 = "yyyyMMdd";
	public static String DATE_FORMAT_3 = "dd/MM/yyyy HH:mm:ss";
	
	public static int END_LIMIT = 15;
//	public static final String BROADCAST_ACTION_FORUM = "com.tunedinn.android.forum";
//	public static final String BROADCAST_ACTION_MESSAGE = "com.tunedinn.android.message";
//	public static final String BROADCAST_ACTION_NOTIFY = "com.tunedinn.android.notify";
	
	
	public static <P, T extends AsyncTask<P, ?, ?>> void executeAsyncTask(T task) {
        executeAsyncTask(task, (P[]) null);
    }

    @SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void executeAsyncTask(T task, P... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
	
}
