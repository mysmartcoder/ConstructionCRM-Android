package com.construction.android.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.construction.android.MyApplication;
import com.construction.android.utils.CommonMethod;

public abstract class MyFragmentActivity extends FragmentActivity {

	abstract public CommonMethod getCommonMethod();
	
	abstract public void replaceFragment(Fragment mFragment, boolean addBackStack) ;
	
//	abstract public void replaceTabFragment(Fragment mFragment, boolean addBackStack, String tag);
	
	abstract public void setTitle(int title, int image);
	
	abstract public void setTitle(int title);
	
	abstract public MyApplication getMyApplication();
	
	abstract public void setTabSelectionDisable();
	
//	abstract public void setRightMenuAction(int image, OnClickListener mClickListener);
}
