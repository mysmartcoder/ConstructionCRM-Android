package com.construction.android.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.android.R;
import com.construction.android.activity.LoginActivity;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ProjectListDataParser;
import com.construction.android.parser.ProjectsParser;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@SuppressLint("InflateParams")
public class ProjectListDialogFragment extends DialogFragment {

    private MyFragmentActivity mActivity;

    private ListView mListViewProjects;
    private TextView mTextViewSelect;

    private int mIntCurrentPosition = -1;

    private ProjectListAdapter mProjectListAdapter;
    private ProjectsParser mProjectsParser;
    private List<ProjectListDataParser> mPackageListDataParsers;
    private BackGetProjects mBackGetProjects;

    private Dialog mDialog;
    private ImageView mImageViewClose;
    private ProgressDialog mProgressDialog;

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;

    private String mCurrentMethod = "";
    private String mMethodGetProjects = "getProjects";
    private String mStringProjectID = "";
    private String mStringProjectName = "";

    public ProjectListDialogFragment() {

    }

    public ProjectListDialogFragment(MyFragmentActivity activity) {
        mActivity = activity;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDialog = new Dialog(mActivity);
        View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_projects_list, null);

        mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(view);
        setCancelable(false);
        mDialog.show();

        mProjectsParser = new ProjectsParser();

        mSharedPreferences = mActivity.getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mListViewProjects = (ListView) mDialog.findViewById(R.id.dialog_project_list_projects);
        mTextViewSelect = (TextView) mDialog.findViewById(R.id.dialog_project_list_textview_select);

        mBackGetProjects = new BackGetProjects();
        mBackGetProjects.execute(mMethodGetProjects);

        mListViewProjects.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                mIntCurrentPosition = position;
                mProjectListAdapter.notifyDataSetChanged();
                mStringProjectID = mPackageListDataParsers.get(position).getGuid();
                mStringProjectName = mPackageListDataParsers.get(position).getName();
                System.out.println("PROJECT ID" + mPackageListDataParsers.get(position).getGuid());
                System.out.println("PROJECT NAME" + mPackageListDataParsers.get(position).getName());
                mEditor.putString(getString(R.string.sp_project_id), mPackageListDataParsers.get(position).getGuid());
                mEditor.putString(getString(R.string.sp_start_date), mPackageListDataParsers.get(position).getStartDate());
                mEditor.putString(getString(R.string.sp_end_date), mPackageListDataParsers.get(position).getEndDate());
                mEditor.commit();
                mDialog.cancel();

            }
        });

        mTextViewSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mStringProjectName.equalsIgnoreCase("")) {

                    mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name), getString(R.string.alt_msg_select_project));

                } else {

                    mDialog.cancel();
                }

            }
        });

        return mDialog;
    }

    /**
     * Method call will display Finish App Alert Dialog
     */
    public void showTokenExpiredDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setMessage(getString(R.string.validation_token_expired)).setCancelable(false)
                .setPositiveButton(getString(R.string.lbl_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent mIntent = new Intent(mActivity, LoginActivity.class);
                        mEditor.clear();
                        mEditor.commit();
                        startActivity(mIntent);
                        mActivity.finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Set Project list Adapter
     *
     * @author ebaraiya
     */
    public class ProjectListAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mPackageListDataParsers.size();
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

            holder.mTextViewTitle.setText(mPackageListDataParsers.get(position).getName());
            holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if (position % 2 == 0) {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
            } else {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
            }
            // if (mIntCurrentPosition == position) {
            // holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_box_on),
            // null, null, null);
            // } else {
            // holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_box_off),
            // null, null, null);
            // }
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

            if (mCurrentMethod.equalsIgnoreCase(mMethodGetProjects)) {

                mProjectsParser = (ProjectsParser) mActivity.getCommonMethod().getProjects(mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getUserID(), mProjectsParser);

                if (mProjectsParser.getData() != null && mProjectsParser.getData().size() > 0) {

                    mPackageListDataParsers = new ArrayList<ProjectListDataParser>();
                    try {
                        for (int i = 0; i < mProjectsParser.getData().size(); i++) {
                            mPackageListDataParsers.add(mProjectsParser.getData().get(i));
//							for (int j = 0; j < mProjectsParser.getData().get(i).getProjectRoleHolderRelations().size(); j++) {
//								if (mProjectsParser.getData().get(i).getProjectRoleHolderRelations().get(j).getEnd().getGuid()
//										.equalsIgnoreCase(mActivity.getMyApplication().getUserID())) {
//									mPackageListDataParsers.add(mProjectsParser.getData().get(i));
//								}
//							}
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                if (mProgressDialog != null)
                    mProgressDialog.dismiss();

                if (mActivity.getCommonMethod().isNetError) {
                    mActivity.getCommonMethod().showDialog("", getString(R.string.validation_no_internet), true);
                } else if (mActivity.getCommonMethod().isError) {
//					Intent mIntent = new Intent(mActivity, LoginActivity.class);
//					mEditor.clear();
//					mEditor.commit();
//					startActivity(mIntent);
//					mActivity.finish();
                    mActivity.getCommonMethod().showDialog("", getString(R.string.validation_failed), true);
                    mDialog.cancel();

                } else {
                    try {
                        if (mCurrentMethod.equalsIgnoreCase(mMethodGetProjects)) {

                            if (!mProjectsParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {

                                if (mProjectsParser.getData() != null && mProjectsParser.getData().size() > 0) {

                                    // mPackageListDataParsers = new
                                    // ArrayList<ProjectListDataParser>();
                                    // mPackageListDataParsers.addAll(mProjectsParser.getData());

                                    if (mPackageListDataParsers.size() == 1) {
                                        mEditor.putString(getString(R.string.sp_project_id), mPackageListDataParsers.get(0).getGuid());
                                        mEditor.putString(getString(R.string.sp_start_date), mPackageListDataParsers.get(0).getStartDate());
                                        mEditor.putString(getString(R.string.sp_end_date), mPackageListDataParsers.get(0).getEndDate());
                                        mEditor.commit();
                                        mDialog.cancel();
                                    } else {
                                        mProjectListAdapter = new ProjectListAdapter();
                                        mListViewProjects.setAdapter(mProjectListAdapter);
                                    }

//
                                } else {
                                    Intent mIntent = new Intent(mActivity, LoginActivity.class);
                                    mEditor.clear();
                                    mEditor.commit();
                                    startActivity(mIntent);
                                    mActivity.finish();
                                }

                            } else {
//                                Toast.makeText(mActivity, mProjectsParser.getMessage(), Toast.LENGTH_SHORT).show();
                                mActivity.getCommonMethod().showSessionExpireAlert(mActivity,getString(R.string.app_name),mProjectsParser.getMessage());
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.onPostExecute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}


