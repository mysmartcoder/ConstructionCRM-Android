package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ReportParser;

import java.util.Calendar;

@SuppressLint("InflateParams")
public class ReportsFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;
    private Dialog mDialog;
    private Calendar mCalendarFirst;
    private Calendar mCalendarLast;

    private TextView mTextViewLastMonth;
    private TextView mTextViewSixMonth;
    private TextView mTextViewCustom;
    private TextView mTextViewTitle;

    private BackProcessGetReason mBackProcessGetReason;
    private ProgressDialog mProgressDialog;
    private ReportParser mReportParser;

    private String mCurrentMethod = "";
    private String mMethdoGetReasonStopDelay = "get_reason_delay_stop";
    private String mMethodGetReasonNonCompletion = "get_reason_non_completion";
    private String mMethodGetPercentagePlan = "get_percentage_plan";

    private String mStringStartDate = "";
    private String mStringEndDate = "";
    private String mStringPercentagePlanData = "";

    public ReportsFragment() {

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reports, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_report);

        getWidgetRefrence(rootView);
        registerOnClick();

        mReportParser = new ReportParser();

        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     *
     * @param v
     */
    private void getWidgetRefrence(View v) {

        mTextViewTitle = (TextView) v.findViewById(R.id.fragment_report_textview_title);
        mTextViewLastMonth = (TextView) v.findViewById(R.id.fragment_reports_textview_lastmonth);
        mTextViewSixMonth = (TextView) v.findViewById(R.id.fragment_reports_textview_six_month);
        mTextViewCustom = (TextView) v.findViewById(R.id.fragment_reports_textview_custom);
    }

    /**
     * Method call will register OnClick() Events.
     */
    private void registerOnClick() {

        mTextViewLastMonth.setOnClickListener(this);
        mTextViewSixMonth.setOnClickListener(this);
        mTextViewCustom.setOnClickListener(this);

        if (getArguments().getString(getString(R.string.bunble_report_screen))
                .equalsIgnoreCase(getString(R.string.bunble_percentage_plan_screen))) {

            mTextViewTitle.setText(getString(R.string.lbl_percentage_plan_complete));

        } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(getString(R.string.bunble_non_completion))) {

            mTextViewTitle.setText(getString(R.string.lbl_reason_for_non_completion));

        } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                getString(R.string.bunble_reason_for_delay_stop))) {

            mTextViewTitle.setText(getString(R.string.lbl_reason_for_delay));

        } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                getString(R.string.bunble_reason_for_delay))) {

            mTextViewTitle.setText(getString(R.string.lbl_list_of_delayed_tasks));

        } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                getString(R.string.bunble_reason_for_stop))) {

            mTextViewTitle.setText(getString(R.string.lbl_stoped_tasks));
        }


    }


    /**
     * Method call will fire onClick event;
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewLastMonth) {

            getLastMonth();

            if (getArguments().getString(getString(R.string.bunble_report_screen))
                    .equalsIgnoreCase(getString(R.string.bunble_percentage_plan_screen))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethodGetPercentagePlan);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(getString(R.string.bunble_non_completion))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethodGetReasonNonCompletion);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_delay_stop))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethdoGetReasonStopDelay);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_delay))) {

                Bundle mBundle = new Bundle();
                DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_delay));
                mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                mDelayAndStopTaskListFragment.setArguments(mBundle);
                mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_stop))) {

                Bundle mBundle = new Bundle();
                DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_stop));
                mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                mDelayAndStopTaskListFragment.setArguments(mBundle);
                mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);
            }

        } else if (v == mTextViewSixMonth) {

            getSixMonth();

            if (getArguments().getString(getString(R.string.bunble_report_screen))
                    .equalsIgnoreCase(getString(R.string.bunble_percentage_plan_screen))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethodGetPercentagePlan);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(getString(R.string.bunble_non_completion))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethodGetReasonNonCompletion);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_delay_stop))) {

                mBackProcessGetReason = new BackProcessGetReason();
                mBackProcessGetReason.execute(mMethdoGetReasonStopDelay);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_delay))) {

                Bundle mBundle = new Bundle();
                DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_delay));
                mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                mDelayAndStopTaskListFragment.setArguments(mBundle);
                mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);

            } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                    getString(R.string.bunble_reason_for_stop))) {

                Bundle mBundle = new Bundle();
                DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_stop));
                mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                mDelayAndStopTaskListFragment.setArguments(mBundle);
                mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);
            }
        } else if (v == mTextViewCustom) {

            showDialogForSelectDate();

        }

    }

    /**
     *
     */
    public void getLastMonth() {
        Calendar mCalendarFirst = Calendar.getInstance();
        Calendar mCalendarLast = Calendar.getInstance();
        mCalendarFirst.add(Calendar.MONTH, -1);
//		mCalendarFirst.add(Calendar.DAY_OF_MONTH, 1);
        mCalendarFirst.set(Calendar.DAY_OF_MONTH, 1);

        mCalendarLast.add(Calendar.MONTH, -1);
        mCalendarLast.set(Calendar.DAY_OF_MONTH, mCalendarFirst.getActualMaximum(Calendar.DAY_OF_MONTH));

        mStringStartDate = String.valueOf(mCalendarFirst.getTimeInMillis());
        mStringEndDate = String.valueOf(mCalendarLast.getTimeInMillis());

        System.out.println("DATE====" + mCalendarFirst.getTime().toString());
    }

    /**
     *
     */
    public void getSixMonth() {
        Calendar mCalendarFirst = Calendar.getInstance();
        Calendar mCalendarLast = Calendar.getInstance();
        mCalendarFirst.add(Calendar.MONTH, -6);
//		mCalendarFirst.add(Calendar.DAY_OF_MONTH, 1);
        mCalendarFirst.set(Calendar.DAY_OF_MONTH, 1);

        mCalendarLast.add(Calendar.MONTH, -1);
        mCalendarLast.set(Calendar.DAY_OF_MONTH, mCalendarFirst.getActualMaximum(Calendar.DAY_OF_MONTH));

        mStringStartDate = String.valueOf(mCalendarFirst.getTimeInMillis());
        mStringEndDate = String.valueOf(mCalendarLast.getTimeInMillis());

        System.out.println("DATE====" + mCalendarFirst.getTime().toString());
    }

    /**
     *
     */
    public void showDialogForSelectDate() {

        mCalendarFirst = Calendar.getInstance();
        mCalendarLast = Calendar.getInstance();

        mDialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        View mView = getLayoutInflater(null).inflate(R.layout.view_select_from_to_date, null);
        final DatePicker mDatePickerFrom = (DatePicker) mView.findViewById(R.id.view_select_from_to_date_datepicker_from);
        mDatePickerFrom.updateDate(mCalendarFirst.get(Calendar.YEAR), mCalendarFirst.get(Calendar.MONTH), mCalendarFirst.get(Calendar.DAY_OF_MONTH));
        final DatePicker mDatePickerTo = (DatePicker) mView.findViewById(R.id.view_select_from_to_date_datepicker_to);
        mDatePickerTo.updateDate(mCalendarLast.get(Calendar.YEAR), mCalendarLast.get(Calendar.MONTH), mCalendarLast.get(Calendar.DAY_OF_MONTH));
        TextView mTextViewDone = (TextView) mView.findViewById(R.id.view_select_from_to_date_textview_done);
        mTextViewDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCalendarFirst = Calendar.getInstance();
                mCalendarFirst.set(Calendar.MONTH, mDatePickerFrom.getMonth());
                mCalendarFirst.set(Calendar.YEAR, mDatePickerFrom.getYear());
                mCalendarFirst.set(Calendar.DAY_OF_MONTH, mDatePickerFrom.getDayOfMonth());
                mStringStartDate = String.valueOf(mCalendarFirst.getTimeInMillis());

                mCalendarLast = Calendar.getInstance();
                mCalendarLast.set(Calendar.MONTH, mDatePickerTo.getMonth());
                mCalendarLast.set(Calendar.YEAR, mDatePickerTo.getYear());
                mCalendarLast.set(Calendar.DAY_OF_MONTH, mDatePickerTo.getDayOfMonth());
                mStringEndDate = String.valueOf(mCalendarLast.getTimeInMillis());

                if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                        getString(R.string.bunble_percentage_plan_screen))) {

                    mBackProcessGetReason = new BackProcessGetReason();
                    mBackProcessGetReason.execute(mMethodGetPercentagePlan);

                } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                        getString(R.string.bunble_non_completion))) {

                    mBackProcessGetReason = new BackProcessGetReason();
                    mBackProcessGetReason.execute(mMethodGetReasonNonCompletion);

                } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                        getString(R.string.bunble_reason_for_delay_stop))) {

                    mBackProcessGetReason = new BackProcessGetReason();
                    mBackProcessGetReason.execute(mMethdoGetReasonStopDelay);

                } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                        getString(R.string.bunble_reason_for_delay))) {

                    Bundle mBundle = new Bundle();
                    DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                    mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_delay));
                    mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                    mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                    mDelayAndStopTaskListFragment.setArguments(mBundle);
                    mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);

                } else if (getArguments().getString(getString(R.string.bunble_report_screen)).equalsIgnoreCase(
                        getString(R.string.bunble_reason_for_stop))) {

                    Bundle mBundle = new Bundle();
                    DelayAndStopTaskListFragment mDelayAndStopTaskListFragment = new DelayAndStopTaskListFragment();
                    mBundle.putString(getString(R.string.bunble_reason_for_delay_stop_list_screen), getString(R.string.bunble_reason_for_stop));
                    mBundle.putString(getString(R.string.bunble_start_date), mStringStartDate);
                    mBundle.putString(getString(R.string.bunble_end_date), mStringEndDate);
                    mDelayAndStopTaskListFragment.setArguments(mBundle);
                    mActivity.replaceFragment(mDelayAndStopTaskListFragment, true);
                }
                mDialog.dismiss();
            }
        });
        TextView mTextViewCancel = (TextView) mView.findViewById(R.id.view_select_from_to_date_textview_cancel);
        mTextViewCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(mView);
        mDialog.show();
    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author ebaraiya
     */

    public class BackProcessGetReason extends AsyncTask<String, Void, String> {
        String responseData = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];
            if (mCurrentMethod.equalsIgnoreCase(mMethdoGetReasonStopDelay)) {

                mReportParser = (ReportParser) mActivity.getCommonMethod().getReasonForDealyStopTasksAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getProjecID(), mStringStartDate,
                        mStringEndDate, mReportParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetReasonNonCompletion)) {

                mReportParser = (ReportParser) mActivity.getCommonMethod().getReasonForNonCompletionTasksAPI(
                        mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getProjecID(),
                        mStringStartDate, mStringEndDate, mReportParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetPercentagePlan)) {

                mStringPercentagePlanData = (String) mActivity.getCommonMethod().getPercentagePlanCompleteAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getProjecID(), mStringStartDate, mStringEndDate, mStringPercentagePlanData);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            if (mActivity.getCommonMethod().isNetError) {
                mActivity.getCommonMethod().showDialog("", getString(R.string.validation_no_internet), false);
            } else if (mActivity.getCommonMethod().isError) {
                //mActivity.getCommonMethod().showDialog("", getString(R.string.validation_failed), true);
                mActivity.getCommonMethod().sessionOut();
            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethdoGetReasonStopDelay)) {

                        if (!mReportParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mReportParser != null) {
                                Bundle mBundle = new Bundle();
                                PieChartReportFragment mPieChartReportFragment = new PieChartReportFragment();
                                mBundle.putParcelable(getString(R.string.bunble_report_data), mReportParser);
                                mPieChartReportFragment.setArguments(mBundle);
                                mActivity.replaceFragment(mPieChartReportFragment, true);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mReportParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetReasonNonCompletion)) {

                        if (!mReportParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mReportParser != null) {
                                Bundle mBundle = new Bundle();
                                PieChartReportFragment mPieChartReportFragment = new PieChartReportFragment();
                                mBundle.putParcelable(getString(R.string.bunble_report_data), mReportParser);
                                mPieChartReportFragment.setArguments(mBundle);
                                mActivity.replaceFragment(mPieChartReportFragment, true);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mReportParser.getMessage());
                        }


                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetPercentagePlan)) {

                        if (!mReportParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mReportParser != null) {
                                Bundle mBundle = new Bundle();
                                LineChartReportFragment mLineChartReportFragment = new LineChartReportFragment();
                                mBundle.putString(getString(R.string.bunble_report_data), mStringPercentagePlanData);
                                mLineChartReportFragment.setArguments(mBundle);
                                mActivity.replaceFragment(mLineChartReportFragment, true);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mReportParser.getMessage());
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }
}
