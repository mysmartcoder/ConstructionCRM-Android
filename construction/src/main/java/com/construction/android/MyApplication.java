package com.construction.android;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.construction.android.utils.TypefaceUtil;

public class MyApplication extends Application {

	private SharedPreferences mSharedPreferences;
	
	public DisplayImageOptions profileOption;
	public ImageLoader imageLoader;

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		super.onCreate();
		
		imageLoader = ImageLoader.getInstance();
		
		initImageLoader(getApplicationContext());
//		TypefaceUtil.overrideFont(getApplicationContext(), "normal", "fonts/title.ttf");
		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/regular.ttf");
//		TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/title.ttf");

		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
        

		profileOption = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(0)).cacheInMemory(true).cacheOnDisk(true)
				.showImageOnLoading(R.drawable.bg_user_pic).showImageForEmptyUri(R.drawable.bg_user_pic).showImageOnFail(R.drawable.bg_user_pic)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		
	}
	
	
	/**
	 * Function will return logged user's email id
	 * @return email id
	 */
	public String getUserID()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_user_id), "");
	}
	
	/**
	 * Function call will return user's name
	 * @return
	 */
	public String getUserName()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_user_name), "");
	}

	public String getPassword()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_password), "");
	}

	public boolean isLogin()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_login), false);
	}


	/**
	 * Function call will returen user's status : active OR in-active
	 * @return
	 */
	public String getUserStatus()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_user_status), "true");
	}
	/**
	 * Function call will getProject id.
	 * @return
	 */
	public String getProjecID()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_project_id), "");
	}
	
	/**
	 * Function will return logged user's token
	 * @return token
	 */
	public String getUserToken()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_user_token), "");
	}
	
	/**
	 * Function will return logged user's token
	 * @return token
	 */
	public String getUserImage()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_user_image), "");
	}
	
	/**
	 * Function will return project start date
	 * @return
	 */
	public String getStartDate()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_start_date), "");
	}
	
	/**
	 * Function will return project end date
	 * @return
	 */
	public String getEndDate()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getString(getString(R.string.sp_end_date), "");
	}
	
	
	/**
	 * Method call will set notification on or off
	 * @return
	 */
	public boolean isNotificationOn()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_notification), true);
	}
	
	public boolean isNoteAddNotificationOn()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_not_notification), true);
	}
	
	public boolean isFileNotificationOn()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_file_notification), true);
	}
	
	public boolean isStatusChangeNotificationOn()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_status_notification), true);
	}

	public boolean isShowNotifcatoinForAll()
	{
		mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(getString(R.string.sp_is_notification_everyone), true);
	}
	
	/**
	 * Method will read notification
	 * @param file_name
	 */
	public void readNotification(String file_name)
	{
		try {
			File mFileUnRead = new File(getFilesDir()+"/"+getUserID()+"/"+"UnRead", file_name );
			File mFileRead = new File(getFilesDir()+"/"+getUserID(), "Read");
			if (!mFileRead.exists()) {
				mFileRead.mkdir();
			}
			
			File mFileReadMove = new File(mFileRead.getAbsolutePath(), file_name);
			mFileUnRead.renameTo(mFileReadMove);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Function will return logged user's email id is verify or not
	 * @return email id
	 */
	

	public static void initImageLoader(Context context) {

		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;

	}
}
