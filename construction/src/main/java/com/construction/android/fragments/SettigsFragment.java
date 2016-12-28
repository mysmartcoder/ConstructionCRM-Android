package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;

@SuppressLint("InflateParams")
public class SettigsFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;

	private TextView mTextViewProfile;
	private TextView mTextViewNotification;
	private TextView mTextViewData;
	private TextView mTextViewProjects;

	public SettigsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_settings, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_setting);

		getWidgetRefrence(rootView);
		registerOnClick();

		replaceFragment(new ProfileFragment(), false);

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 */
	private void getWidgetRefrence(View v) {
		mTextViewProfile = (TextView) v.findViewById(R.id.view_stting_actionbar_textview_profile);
		mTextViewProfile.setTextColor(getResources().getColor(R.color.white));
		mTextViewProfile.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewNotification = (TextView) v.findViewById(R.id.view_stting_actionbar_textview_notification);
		mTextViewData = (TextView) v.findViewById(R.id.view_stting_actionbar_textview_data);
		mTextViewProjects = (TextView)v.findViewById(R.id.view_stting_actionbar_textview_projects);

	}

	/**
	 * Method call will Register OnClick() Events for widgets.
	 */
	private void registerOnClick() {
		mTextViewProfile.setOnClickListener(this);
		mTextViewNotification.setOnClickListener(this);
		mTextViewData.setOnClickListener(this);
		mTextViewProjects.setOnClickListener(this);
	}

	/**
	 * Method call OnClick Event fire.
	 */
	@Override
	public void onClick(View v) {

		if (v == mTextViewProfile) {

			setTabSelection();
			mTextViewProfile.setTextColor(getResources().getColor(R.color.white));
			mTextViewProfile.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
			replaceFragment(new ProfileFragment(), false);

		} else if (v == mTextViewNotification) {

			setTabSelection();
			mTextViewNotification.setTextColor(getResources().getColor(R.color.white));
			mTextViewNotification.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
			replaceFragment(new SettingNotificationFragment(), false);

		} else if (v == mTextViewData) {

			setTabSelection();
			mTextViewData.setTextColor(getResources().getColor(R.color.white));
			mTextViewData.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
			replaceFragment(new SettingDataFragment(), false);

		}else if(v==mTextViewProjects){
			
			setTabSelection();
			mTextViewProjects.setTextColor(getResources().getColor(R.color.white));
			mTextViewProjects.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
			replaceFragment(new SettingProjectsFragment(), false);
			
		}
	}

	/**
	 * Method call will set Tab selection : Profile, Notification and Data.
	 */
	private void setTabSelection() {
		mTextViewProfile.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewNotification.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewData.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewProfile.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
		mTextViewNotification.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
		mTextViewData.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
		mTextViewProjects.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
		mTextViewProjects.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
		
	}

	/**
	 * Method call will replace sub fragments.
	 * @param mFragment
	 * @param addBackStack
	 */
	public void replaceFragment(Fragment mFragment, boolean addBackStack) {
		FragmentTransaction mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
		mFragmentTransaction.replace(R.id.setting_frame_container, mFragment);
		if (addBackStack)
			mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();

	}

}
