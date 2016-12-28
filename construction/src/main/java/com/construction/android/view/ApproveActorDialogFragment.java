package com.construction.android.view;

import java.util.ArrayList;
import java.util.List;

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

import com.construction.android.R;
import com.construction.android.activity.LoginActivity;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ProjectDetailsDataParser;
import com.construction.android.parser.ProjectListDataParser;
import com.construction.android.parser.ProjectsParser;

@SuppressWarnings("unused")
@SuppressLint("InflateParams")
public class ApproveActorDialogFragment extends DialogFragment {

	private MyFragmentActivity mActivity;

	private ListView mListViewActores;
	private TextView mTextViewTitle;

	private int mIntCurrentPosition = -1;

	private ActoreListAdapter mActoreListAdapter;
	private ProjectsParser mProjectsParser;
	private List<ProjectListDataParser> mPackageListDataParsers;
	private BackGetProjects mBackGetProjects;

	private Dialog mDialog;
	private ImageView mImageViewClose;
	private ProgressDialog mProgressDialog;

	private String mCurrentMethod = "";
	private String mMethodGetActores = "getActores";
	private String mStringActoreID = "";
	private String mStringActoreName = "";

	public ApproveActorDialogFragment() {

	}

	public ApproveActorDialogFragment(MyFragmentActivity activity) {
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

		mTextViewTitle = (TextView)mDialog.findViewById(R.id.dialog_project_list_textview_title);
		mTextViewTitle.setText(getString(R.string.lbl_select_actore));
		
		mListViewActores = (ListView) mDialog.findViewById(R.id.dialog_project_list_projects);

		mBackGetProjects = new BackGetProjects();
		mBackGetProjects.execute(mMethodGetActores);

		mListViewActores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				mIntCurrentPosition = position;
				mActoreListAdapter.notifyDataSetChanged();
				mStringActoreID = mPackageListDataParsers.get(position).getGuid();
				mStringActoreName = mPackageListDataParsers.get(position).getName();
				
				System.out.println("PROJECT ID" + mPackageListDataParsers.get(position).getGuid());
				System.out.println("PROJECT NAME" + mPackageListDataParsers.get(position).getName());
			
				mDialog.cancel();

			}
		});

		return mDialog;
	}


	/**
	 * Set Project list Adapter
	 * 
	 * @author ebaraiya
	 * 
	 */
	public class ActoreListAdapter extends BaseAdapter {

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
//			if (mIntCurrentPosition == position) {
//				holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_box_on), null, null, null);
//			} else {
//				holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_box_off), null, null, null);
//			}
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
	 * 
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

			if (mCurrentMethod.equalsIgnoreCase(mMethodGetActores)) {


				mProjectsParser = (ProjectsParser) mActivity.getCommonMethod().getApproverGuidAPI(
						mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getProjecID(), mProjectsParser);
				
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
					if (mCurrentMethod.equalsIgnoreCase(mMethodGetActores)) {
						if (mProjectsParser.getData().size() > 0) {

							mPackageListDataParsers = new ArrayList<ProjectListDataParser>();
							mPackageListDataParsers.addAll(mProjectsParser.getData());
							mActoreListAdapter = new ActoreListAdapter();
							mListViewActores.setAdapter(mActoreListAdapter);

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
