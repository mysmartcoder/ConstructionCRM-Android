package com.construction.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;

public class FileAdapter extends BaseAdapter {
	MyFragmentActivity mActivity;
	ArrayList<String> mArrayList;

	public FileAdapter(MyFragmentActivity mActivity2, ArrayList<String> mList) {
		this.mActivity = mActivity2;
		this.mArrayList= mList;

	}

	@Override
	public int getCount() {

		return mArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mActivity.getLayoutInflater().inflate(R.layout.row_files, null);

			holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.row_files_textview_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.mTextViewTitle.setText(mArrayList.get(position).toString());

		return convertView;
	}

	public class ViewHolder {
		TextView mTextViewTitle;
	}

}
