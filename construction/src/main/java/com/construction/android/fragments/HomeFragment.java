package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;

@SuppressLint("InflateParams")
public class HomeFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;

	private TextView mTextViewTaskInProgress;
	private TextView mTextViewMakeReadyTask;
	private TextView mTextViewReassignments;
	private TextView mTextViewNotifications;
	private TextView mTextViewSnagging;
	private TextView mTextViewReports;

	public HomeFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_construction_home);

		getWidgetRefrence(rootView);
		registerOnClick();

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * @param v
	 */
	private void getWidgetRefrence(View v) {
		mTextViewTaskInProgress = (TextView) v.findViewById(R.id.fragment_home_textview_task_in_progress);
		mTextViewMakeReadyTask = (TextView) v.findViewById(R.id.fragment_home_textview_make_ready_task);
		mTextViewReassignments = (TextView) v.findViewById(R.id.fragment_home_textview_reassignments);
		mTextViewNotifications = (TextView) v.findViewById(R.id.fragment_home_textview_notifications);
		mTextViewSnagging = (TextView) v.findViewById(R.id.fragment_home_textview_snagging);
		mTextViewReports = (TextView) v.findViewById(R.id.fragment_home_textview_reports);

		mActivity.getCommonMethod().setTitleFont(mTextViewTaskInProgress);
		mActivity.getCommonMethod().setTitleFont(mTextViewMakeReadyTask);
		mActivity.getCommonMethod().setTitleFont(mTextViewReassignments);
		mActivity.getCommonMethod().setTitleFont(mTextViewNotifications);
		mActivity.getCommonMethod().setTitleFont(mTextViewSnagging);
		mActivity.getCommonMethod().setTitleFont(mTextViewReports);

	}

	/**
	 * Method call will Register OnClick() Events for widgets.
	 */
	private void registerOnClick() {
		mTextViewTaskInProgress.setOnClickListener(this);
		mTextViewMakeReadyTask.setOnClickListener(this);
		mTextViewReassignments.setOnClickListener(this);
		mTextViewNotifications.setOnClickListener(this);
		mTextViewSnagging.setOnClickListener(this);
		mTextViewReports.setOnClickListener(this);
	}

	/**
	 * Method call OnClick Event fire.
	 */
	@Override
	public void onClick(View v) {

		mActivity.setTabSelectionDisable();

		if (v == mTextViewTaskInProgress) {

			mActivity.replaceFragment(new TaskInProgressFragment(), true);

		} else if (v == mTextViewMakeReadyTask) {

			mActivity.replaceFragment(new MakeReadyTaskFragment(), true);

		} else if (v == mTextViewReassignments) {

			mActivity.replaceFragment(new ReassignmentsFragment(), true);

		} else if (v == mTextViewNotifications) {

			mActivity.replaceFragment(new NotificationsFragment(), true);

		} else if (v == mTextViewSnagging) {

			mActivity.replaceFragment(new SnaggingFragment(), true);

		} else if (v == mTextViewReports) {

			mActivity.replaceFragment(new SubReportsFragment(), true);
		}

	}
	
	
}
