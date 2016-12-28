package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class SettingDataFragment extends Fragment implements OnClickListener {

	private MyFragmentActivity mActivity;
	private View rootView;

	private TextView mTextViewSyncAuto;
	private TextView mTextViewDataDuration;

	private boolean isSynchronise = false;

	public SettingDataFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_settings_data, container, false);

		mActivity = (MyFragmentActivity) getActivity();

		getWidgetRefrence(rootView);
		registerOnClick();

		return rootView;
	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 * @param v
	 */
	private void getWidgetRefrence(View v) {

		mTextViewSyncAuto = (TextView) v.findViewById(R.id.fragment_settings_data_textview_sync_auto);
		mTextViewDataDuration = (TextView) v.findViewById(R.id.fragment_settings_data_textview_load_data_duration);
	}

	/**
	 * Method call will register OnClick() Events.
	 */
	private void registerOnClick() {

		mTextViewSyncAuto.setOnClickListener(this);
		mTextViewDataDuration.setOnClickListener(this);
		mTextViewDataDuration.setEnabled(false);

	}

	/**
	 * Method call will fire Click event.
	 */
	@Override
	public void onClick(View v) {

		if (v == mTextViewSyncAuto) {

			isSynchronise = !isSynchronise;
			if (isSynchronise) {

				mTextViewSyncAuto.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
				mTextViewDataDuration.setEnabled(true);

			} else {

				mTextViewSyncAuto.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
				mTextViewDataDuration.setEnabled(false);
			}

		} else if (v == mTextViewDataDuration) {

			setLoadDataDuration();

		}

	}

	/**
	 * Method call will set Load data duration.
	 * 
	 */
	public void setLoadDataDuration() {
		int dataDurationPosition = 0;
		if (mTextViewDataDuration.getText().toString().equalsIgnoreCase("1 Week")) {
			dataDurationPosition = 0;
		} else if (mTextViewDataDuration.getText().toString().equalsIgnoreCase("2 Week")) {
			dataDurationPosition = 1;
		} else if (mTextViewDataDuration.getText().toString().equalsIgnoreCase("3 Week")) {
			dataDurationPosition = 2;
		} else if (mTextViewDataDuration.getText().toString().equalsIgnoreCase("4 Week")) {
			dataDurationPosition = 3;
		} else {
			dataDurationPosition = 4;
		}

		final AlertDialog.Builder singlechoicedialog = new AlertDialog.Builder(mActivity);
		final CharSequence[] Report_items = getResources().getStringArray(R.array.week_array);
		singlechoicedialog.setTitle(getString(R.string.lbl_load_data_duration));
		singlechoicedialog.setSingleChoiceItems(Report_items, dataDurationPosition, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				mTextViewDataDuration.setText(Report_items[item].toString());
				dialog.cancel();
			}
		});
		AlertDialog alert_dialog = singlechoicedialog.create();
		alert_dialog.show();
	}

}
