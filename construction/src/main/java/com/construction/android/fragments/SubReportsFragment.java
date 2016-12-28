package com.construction.android.fragments;

import java.util.Calendar;

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
public class SubReportsFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;
	private TextView mTextViewPercentagePlane;
	private TextView mTextViewNonCompletion;
	private TextView mTextViewReasonDelay;
	private TextView mTextViewDelayTaskList;
	private TextView mTextViewStopTasks;

	public SubReportsFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_sub_reports, container, false);

		mActivity = (MyFragmentActivity) getActivity();
		mActivity.setTitle(R.string.lbl_report);

		getWidgetRefrence(rootView);
		registerOnClick();
		

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * @param v
	 */
	private void getWidgetRefrence(View v) {

		mTextViewPercentagePlane = (TextView) v.findViewById(R.id.fragment_sub_reports_textview_percentage_plan);
		mTextViewNonCompletion = (TextView) v.findViewById(R.id.fragment_sub_reports_textview_non_completion);
		mTextViewReasonDelay = (TextView) v.findViewById(R.id.fragment_sub_reports_textview_delay_reason);
		mTextViewDelayTaskList = (TextView) v.findViewById(R.id.fragment_sub_reports_textview_list_of_delay_task);
		mTextViewStopTasks = (TextView) v.findViewById(R.id.fragment_sub_reports_textview_stoped_tasks);
	}

	/**
	 * Method call will register OnClick() Events.
	 */
	private void registerOnClick() {

		mTextViewPercentagePlane.setOnClickListener(this);
		mTextViewNonCompletion.setOnClickListener(this);
		mTextViewReasonDelay.setOnClickListener(this);
		mTextViewDelayTaskList.setOnClickListener(this);
		mTextViewStopTasks.setOnClickListener(this);

	}

	/**
	 * Method call will fire onClick event;
	 */
	@Override
	public void onClick(View v) {

		if (v == mTextViewPercentagePlane) {

			Bundle mBundle = new Bundle();
			ReportsFragment mReportsFragment = new ReportsFragment();
			mBundle.putString(getString(R.string.bunble_report_screen), getString(R.string.bunble_percentage_plan_screen));
			mReportsFragment.setArguments(mBundle);
			mActivity.replaceFragment(mReportsFragment, true);

		} else if (v == mTextViewNonCompletion) {

			Bundle mBundle = new Bundle();
			ReportsFragment mReportsFragment = new ReportsFragment();
			mBundle.putString(getString(R.string.bunble_report_screen), getString(R.string.bunble_non_completion));
			mReportsFragment.setArguments(mBundle);
			mActivity.replaceFragment(mReportsFragment, true);

		} else if (v == mTextViewReasonDelay) {

			Bundle mBundle = new Bundle();
			ReportsFragment mReportsFragment = new ReportsFragment();
			mBundle.putString(getString(R.string.bunble_report_screen), getString(R.string.bunble_reason_for_delay_stop));
			mReportsFragment.setArguments(mBundle);
			mActivity.replaceFragment(mReportsFragment, true);

		} else if (v == mTextViewDelayTaskList) {

			Bundle mBundle = new Bundle();
			ReportsFragment mReportsFragment = new ReportsFragment();
			mBundle.putString(getString(R.string.bunble_report_screen), getString(R.string.bunble_reason_for_delay));
			mReportsFragment.setArguments(mBundle);
			mActivity.replaceFragment(mReportsFragment, true);

		} else if (v == mTextViewStopTasks) {

//			Bundle mBundle = new Bundle();
//			ReportsFragment mReportsFragment = new ReportsFragment();
//			mBundle.putString(getString(R.string.bunble_report_screen), getString(R.string.bunble_reason_for_stop));
//			mReportsFragment.setArguments(mBundle);
//			mActivity.replaceFragment(mReportsFragment, true);
			
			Calendar mCalendar = Calendar.getInstance();
			String mStringEndDate = String.valueOf(mCalendar.getTimeInMillis());
			
			Bundle mBundle = new Bundle();
			DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
			mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_stop));
			mBundle.putString(getString(R.string.bunble_start_date), mActivity.getMyApplication().getStartDate());
			mBundle.putString(getString(R.string.bunble_end_date),mStringEndDate);
			mDelayAndStopTaskListFragment.setArguments(mBundle);
			mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);

		}

	}
	
	

}
