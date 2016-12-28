package com.construction.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.PredescessorsParser;

public class PredescessorsAdapter extends BaseAdapter {
	MyFragmentActivity mActivity;
	ArrayList<PredescessorsParser> mPredescessorsParsers;

	public PredescessorsAdapter(MyFragmentActivity mActivity2, ArrayList<PredescessorsParser> mArrayList) {
		this.mActivity = mActivity2;
		this.mPredescessorsParsers = mArrayList;
	}

	@Override
	public int getCount() {

		return mPredescessorsParsers.size();
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.row_predescessors, null);

			holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.row_predescessor_textview_title);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTextViewTitle.setText(mPredescessorsParsers.get(position).getName());
		
		int drawableResourceId = mActivity.getResources().getIdentifier("icon_task_dot_"+mPredescessorsParsers.get(position).getStatus(), "drawable", mActivity.getPackageName());
		holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, mActivity.getResources().getDrawable(drawableResourceId), null);
		

		return convertView;
	}

	public class ViewHolder {
		TextView mTextViewTitle;
	}

}
