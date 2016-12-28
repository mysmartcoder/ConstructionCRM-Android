package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ProjectDetailsDataParser;
import com.construction.android.parser.ProjectsDetailParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("InflateParams")
public class TaskInProgressFragment extends Fragment {

    private MyFragmentActivity mActivity;
    private ProgressDialog mProgressDialog;

    private ListView mListView;
    private TextView mTextViewDataNotFound;

    private String mCurrentMethod = "";
    private String mMethodGetTaskInProgress = "getTaskInProgress";
    private BackProcessGetTaskInProgress mBackProcessGetTaskInProgress;

    private ProjectsDetailParser mProjectsParser;
    private List<ProjectDetailsDataParser> mProjectDetailsDataParsers;
    private ProjectDetailsAdapter mProjectDetailsAdapter;

    public TaskInProgressFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_in_progress, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_task_in_progress);

        getWidgetReference(rootView);

        mProjectsParser = new ProjectsDetailParser();

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Bundle mBundle = new Bundle();
                mBundle.putString(getString(R.string.bunble_project_guid), mProjectDetailsDataParsers.get(position).getGuid());
                mBundle.putBoolean(getString(R.string.bunble_is_task_in_progress_screen), true);
                TaskCardFragment mTaskCardFragment = new TaskCardFragment();
                mTaskCardFragment.setArguments(mBundle);
                mActivity.replaceFragment(mTaskCardFragment, true);

            }
        });

        mBackProcessGetTaskInProgress = new BackProcessGetTaskInProgress();
        mBackProcessGetTaskInProgress.execute(mMethodGetTaskInProgress);
        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     *
     * @param v
     */
    private void getWidgetReference(View v) {
        mListView = (ListView) v.findViewById(R.id.fragment_task_in_progress_listview);
        mTextViewDataNotFound = (TextView) v.findViewById(R.id.fragment_task_in_progress_textview_datanotfound);
    }

    /**
     * Method call will set Adapter.
     *
     * @author ebaraiya
     */
    public class ProjectDetailsAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mProjectDetailsDataParsers.size();
        }

        @Override
        public Object getItem(int arg0) {

            return null;
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mActivity.getLayoutInflater().inflate(R.layout.row_comman_date, null);

                holder.mTextViewDate = (TextView) convertView.findViewById(R.id.row_comman_adapter_textview_sdate);
                holder.mTextViewTaskName = (TextView) convertView.findViewById(R.id.row_comman_adapter_textview_name);
                holder.mView = (View) convertView.findViewById(R.id.row_comman_date_view);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Calendar mCalendar = Calendar.getInstance();
            Calendar mCalendarEnd = Calendar.getInstance();

            holder.mTextViewTaskName.setText(mProjectDetailsDataParsers.get(position).getName());

            if (position == 0) {
                mCalendar.setTimeInMillis(Long.parseLong(mProjectDetailsDataParsers.get(0).getStartDate()));
                holder.mTextViewDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy"));
                holder.mTextViewDate.setVisibility(View.VISIBLE);

            } else {
                mCalendar.setTimeInMillis(Long.parseLong(mProjectDetailsDataParsers.get(position).getStartDate().toString()));
                mCalendarEnd.setTimeInMillis(Long.parseLong(mProjectDetailsDataParsers.get(position - 1).getStartDate().toString()));

                String mString1 = mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy");
                String mString2 = mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendarEnd, "dd MMM yyyy");

                if (mString1.equalsIgnoreCase(mString2)) {

                    holder.mTextViewDate.setVisibility(View.GONE);
                    holder.mView.setVisibility(View.GONE);

                    if (position % 2 == 0) {
                        holder.mTextViewTaskName.setBackgroundColor(getResources().getColor(R.color.white));
                    } else {
                        holder.mTextViewTaskName.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
                    }

                } else {
                    holder.mTextViewDate.setVisibility(View.VISIBLE);
                    holder.mView.setVisibility(View.VISIBLE);
                    mCalendar.setTimeInMillis(Long.parseLong(mProjectDetailsDataParsers.get(position).getStartDate()));
                    holder.mTextViewDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy"));
                }
            }

            int drawableResourceId = mActivity.getResources().getIdentifier("icon_task_dot_" + mProjectDetailsDataParsers.get(position).getStatus(),
                    "drawable", mActivity.getPackageName());
            holder.mTextViewTaskName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(drawableResourceId), null);

            return convertView;
        }

    }

    public class ViewHolder {
        TextView mTextViewDate;
        TextView mTextViewTaskName;
        View mView;
    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author ebaraiya
     */
    public class BackProcessGetTaskInProgress extends AsyncTask<String, Void, String> {
        String responseData = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];
            if (mCurrentMethod.equalsIgnoreCase(mMethodGetTaskInProgress)) {
                mProjectsParser = (ProjectsDetailParser) mActivity.getCommonMethod().getTaskInProgress(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getProjecID(), mActivity.getMyApplication().getUserID(), "23456", mProjectsParser);
                if (mProjectsParser != null && mProjectsParser.getData() != null && mProjectsParser.getData().size() > 0) {
                    mProjectDetailsDataParsers = new ArrayList<ProjectDetailsDataParser>();

                    for (int i = 0; i < mProjectsParser.getData().size(); i++) {
                        // if (mProjectsParser.getData().get(i)!=null &&
                        // mProjectsParser.getData().get(i).getName().length()
                        // >0) {
                        // mProjectDetailsDataParsers.add(mProjectsParser.getData().get(i));
                        // }
                        checkChildData(mProjectsParser.getData().get(i));
                    }
                }
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
                // mActivity.getCommonMethod().showDialog("",
                // getString(R.string.validation_failed), true);
                mActivity.getCommonMethod().sessionOut();
            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethodGetTaskInProgress)) {
                        if (!mProjectsParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mProjectsParser.getData().size() > 0) {
                                // mProjectDetailsDataParsers = new
                                // ArrayList<ProjectDetailsDataParser>();
                                // mProjectDetailsDataParsers.addAll(mProjectsParser.getData());
                                Collections.sort(mProjectDetailsDataParsers, new Comparator<ProjectDetailsDataParser>(){
                                    public int compare(ProjectDetailsDataParser emp1, ProjectDetailsDataParser emp2) {
                                        return emp1.getStartDate().compareToIgnoreCase(emp2.getStartDate()); // To compare string values
                                    }
                                });
                                mProjectDetailsAdapter = new ProjectDetailsAdapter();
                                mListView.setAdapter(mProjectDetailsAdapter);

                            } else {
                                // mActivity.getCommonMethod().showDialog(getString(R.string.app_name),
                                // getString(R.string.alt_msg_data_not_found),
                                // true);
                                mTextViewDataNotFound.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.GONE);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mProjectsParser.getMessage());
                            //Toast.makeText(mActivity,mProjectsParser.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    public void checkChildData(ProjectDetailsDataParser mObject) {

        if (mObject.getChildren() == null || mObject.getChildren().size() == 0) {

            if (mObject.getName().length() > 0)
                mProjectDetailsDataParsers.add(mObject);
        } else {
            for (int i = 0; i < mObject.getChildren().size(); i++) {
                checkChildData(mObject.getChildren().get(i));
            }
        }
    }

}
