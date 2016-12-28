package com.construction.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.android.R;

@SuppressLint({ "ValidFragment", "SimpleDateFormat", "InlinedApi" })
public class CustomDatePickerFragment extends DialogFragment implements OnDateSetListener, OnDateChangedListener {

	TextView mTextView;
	boolean isClick = false;
	Activity mActivity;
	InterfaceDatePicker mInterfaceDatePicker;

	public CustomDatePickerFragment(TextView mTextView, Activity activity) {
		this.mTextView = mTextView;
		isClick = false;
		mActivity = activity;
		showDatePickerDialog();
	}

	public CustomDatePickerFragment(InterfaceDatePicker interfaceDatePicker, Activity activity) {
		this.mInterfaceDatePicker = interfaceDatePicker;
		isClick = false;
		mActivity = activity;
		showDatePickerDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		System.out.println("onCreateDialog**********");

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), DatePickerDialog.THEME_HOLO_LIGHT, this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker arg0, int year, int month, int day) {
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_MONTH, day);
		String date = getDateInFormate(c);

		System.out.println("onDateSet**********");

		mTextView.setText(date);
	}

	public String getDateInFormate(Calendar calendar) {
		String formatedDate = "";
		Date mDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		formatedDate = sdf.format(mDate);

		System.out.println("getDateInFormate**********");
		return formatedDate;
	}

	@SuppressLint("NewApi")
	public void showDatePickerDialog() {
		final DatePicker mDatePicker = new DatePicker(mActivity);
		mDatePicker.setCalendarViewShown(false);
		Calendar c = Calendar.getInstance();
		mDatePicker.setMaxDate(c.getTimeInMillis());

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setTitle(mActivity.getResources().getString(R.string.app_name));
		alertDialog.setView(mDatePicker);
		alertDialog.setPositiveButton(mActivity.getResources().getString(R.string.lbl_ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				final Calendar c = Calendar.getInstance();
				c.set(Calendar.MONTH, mDatePicker.getMonth());
				c.set(Calendar.YEAR, mDatePicker.getYear());
				c.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());
				String date = getDateInFormate(c);

				final Calendar cs = Calendar.getInstance();
				int year = cs.get(Calendar.YEAR);
				int month = cs.get(Calendar.MONTH);
				int day = cs.get(Calendar.DAY_OF_MONTH);

				if (mDatePicker.getYear() > year) {
					Toast.makeText(mActivity, "Do not select future date", Toast.LENGTH_SHORT).show();
					if (mDatePicker.getMonth() > month && mDatePicker.getYear() > year) {
						Toast.makeText(mActivity, "Do not select future date", Toast.LENGTH_SHORT).show();
						if (mDatePicker.getDayOfMonth() > day && mDatePicker.getYear() > year && mDatePicker.getMonth() > month) {
							System.out.println("Please select propar date");
							Toast.makeText(mActivity, "Do not select future date", Toast.LENGTH_SHORT).show();

						}
					}

				} else {

					if (mTextView != null) {
						mTextView.setText(date);
						mTextView.setTextColor(Color.BLACK);
						// mTextView.setTextColor(mActivity.getResources().getColor(R.color.black_text));
					}

					if (mInterfaceDatePicker != null)
						mInterfaceDatePicker.onDateSelected(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());

				}
				System.out.println("Current month ====" + mDatePicker.getYear());
				System.out.println("Click event");

			}
		});
		alertDialog.setNegativeButton(mActivity.getResources().getString(R.string.lbl_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}

	public void setClick() {
		System.out.println("setClick**********");
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

		// final Calendar c = Calendar.getInstance();
		// int years = c.get(Calendar.YEAR);
		// int month = c.get(Calendar.MONTH);
		// int day = c.get(Calendar.DAY_OF_MONTH);
		//
		// if (year > years)
		// view.updateDate(year, month, day);
		// view.setEnabled(false);
		System.out.println("onDateChanged**********");

	}

}
