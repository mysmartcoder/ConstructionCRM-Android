package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.FilesParser;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class DelayAndStopTaskListFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;

    private TextView mTextViewTitle;
    private ListView mListView;
    private ProgressDialog mProgressDialog;

    private FilesParser mFilesParser;
    private ArrayList<String> mArrayListFiles;

    private ProjectListAdapter mProjectListAdapter;
    private BackGetProjects mBackGetProjects;

    private String mCurrentMethod = "";
    private String mMethdoGetDelayTask = "get_delay_task";
    private String mMethodGetStopTasks = "get_stop_task";


    private String mStringStartDate = "";
    private String mStringEndDate = "";


    public DelayAndStopTaskListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_delay_stop_task_list, container, false);

        mActivity = (MyFragmentActivity) getActivity();

        getWidgetRefrence(rootView);
        registerOnClick();


        mFilesParser = new FilesParser();

        mStringStartDate = getArguments().getString(getString(R.string.bunble_start_date));
        mStringEndDate = getArguments().getString(getString(R.string.bunble_end_date));


        if (getArguments().getString(getString(R.string.bunble_reason_for_delay_stop_list_screen)).equalsIgnoreCase(getString(R.string.bunble_reason_for_delay))) {

            mTextViewTitle.setText(getString(R.string.lbl_list_of_delayed_tasks));
            mBackGetProjects = new BackGetProjects();
            mBackGetProjects.execute(mMethdoGetDelayTask);
        } else {
            mTextViewTitle.setText(getString(R.string.lbl_stoped_tasks));
            mBackGetProjects = new BackGetProjects();
            mBackGetProjects.execute(mMethodGetStopTasks);
        }


        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     *
     * @param v
     */
    private void getWidgetRefrence(View v) {
        mTextViewTitle = (TextView) v.findViewById(R.id.fragment_delay_stop_list_textview_title);
        mListView = (ListView) v.findViewById(R.id.fragment_delay_stop_list);
    }

    /**
     * Method call will register OnClick() Events.
     */
    private void registerOnClick() {
    }

    /**
     * Method call will fire Click event.
     */
    @Override
    public void onClick(View v) {

    }


    /**
     * Set Project list Adapter
     *
     * @author ebaraiya
     */
    public class ProjectListAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mArrayListFiles.size();
        }

        @Override
        public Object getItem(int arg0) {

            return null;
        }

        @Override
        public long getItemId(int arg0) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_notifications, null);

                holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.row_notifications_textview_title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextViewTitle.setText(mArrayListFiles.get(position));
            holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if (position % 2 == 0) {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
            } else {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
            }


            return convertView;
        }

    }

    public class ViewHolder {
        TextView mTextViewTitle;
    }

    /**
     * Function call will get Project List from server.
     *
     * @author ebaraiya
     */
    public class BackGetProjects extends AsyncTask<String, Void, String> {
        String responseData = "";
        String mCurrentMethod = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];

            if (mCurrentMethod.equalsIgnoreCase(mMethdoGetDelayTask)) {

                mFilesParser = (FilesParser) mActivity.getCommonMethod().getDelayTaskListAPI(mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getProjecID(), mStringStartDate, mStringEndDate, mFilesParser);


            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetStopTasks)) {

                mFilesParser = (FilesParser) mActivity.getCommonMethod().getStopTaskListAPI(mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getProjecID(), mStringStartDate, mStringEndDate, mFilesParser);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            if (mActivity.getCommonMethod().isNetError) {
                mActivity.getCommonMethod().showDialog("", getString(R.string.validation_no_internet), true);
            } else if (mActivity.getCommonMethod().isError) {

            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethdoGetDelayTask)) {
                        if (!mFilesParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mFilesParser.getData().size() > 0) {

                                mArrayListFiles = new ArrayList<String>();
                                mArrayListFiles.addAll(mFilesParser.getData());
                                mProjectListAdapter = new ProjectListAdapter();
                                mListView.setAdapter(mProjectListAdapter);

                            } else {
                                mActivity.getCommonMethod().showDialog(getString(R.string.app_name), getString(R.string.alt_reports_data_not_founds), true);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mFilesParser.getMessage());
                        }
                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetStopTasks)) {


                        if (!mFilesParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mFilesParser.getData().size() > 0) {

                                mArrayListFiles = new ArrayList<String>();
                                mArrayListFiles.addAll(mFilesParser.getData());
                                mProjectListAdapter = new ProjectListAdapter();
                                mListView.setAdapter(mProjectListAdapter);

                            } else {

                                mActivity.getCommonMethod().showDialog(getString(R.string.app_name), getString(R.string.alt_reports_data_not_founds), true);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mFilesParser.getMessage());
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
