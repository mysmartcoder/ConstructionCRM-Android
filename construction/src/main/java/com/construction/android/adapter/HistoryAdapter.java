package com.construction.android.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.HistoryDataParser;

public class HistoryAdapter extends BaseAdapter {
	MyFragmentActivity mActivity;
	ArrayList<HistoryDataParser> mArrayList;
	ImageLoader mImageLoader;

	public HistoryAdapter(MyFragmentActivity mActivity2, ArrayList<HistoryDataParser> mHistoryDataParsers) {
		this.mActivity = mActivity2;
		this.mArrayList = mHistoryDataParsers;
		mImageLoader = ImageLoader.getInstance();
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.row_notes, null);

			holder.mTextViewComments = (TextView) convertView.findViewById(R.id.row_notes_textview_title);
			holder.mTextViewDate = (TextView) convertView.findViewById(R.id.row_notes_textview_date);
			holder.mTextViewSenderMessage = (TextView) convertView.findViewById(R.id.row_notes_textview_sender_title);
			holder.mTextViewSenderDate = (TextView) convertView.findViewById(R.id.row_notes_textview_sender_date);
			holder.mTextViewSenderName = (TextView) convertView.findViewById(R.id.row_notes_textview_sender_name);
			holder.mTextViewReciverName = (TextView) convertView.findViewById(R.id.row_notes_textview_name);
			holder.mTextViewSenderName.setVisibility(View.VISIBLE);
			holder.mTextViewReciverName.setVisibility(View.VISIBLE);
			
			holder.mImageViewSender = (ImageView) convertView.findViewById(R.id.row_notes_imageview_sender);

			holder.mRelativeLayoutReciever = (RelativeLayout) convertView.findViewById(R.id.row_notes_relative_reciver_message);
			holder.mRelativeLayoutSender = (RelativeLayout) convertView.findViewById(R.id.row_notes_relative_sender_message);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mArrayList.get(position).getCheckSender().equalsIgnoreCase("")) {
			if (mArrayList.get(position).getCreatorActorDetails().getGuid().equalsIgnoreCase(mActivity.getMyApplication().getUserID())||mArrayList.get(position).getCreatorActorDetails().getGuid().equalsIgnoreCase("")) {

				holder.mTextViewSenderMessage.setText(mArrayList.get(position).getComment());
				holder.mTextViewSenderName.setText(mArrayList.get(position).getCreatorActorDetails().getName());
				Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTimeInMillis(Long.parseLong(mArrayList.get(position).getTimestamp()));
				holder.mTextViewSenderDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar,"dd MMM yyyy"));
				holder.mRelativeLayoutSender.setVisibility(View.VISIBLE);
				holder.mRelativeLayoutReciever.setVisibility(View.GONE);
				
			} else {
				holder.mTextViewComments.setText(mArrayList.get(position).getComment());
				holder.mTextViewReciverName.setText(mArrayList.get(position).getCreatorActorDetails().getName());
				Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTimeInMillis(Long.parseLong(mArrayList.get(position).getTimestamp()));
				holder.mTextViewSenderDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar,"dd MMM yyyy"));
				holder.mRelativeLayoutSender.setVisibility(View.GONE);
			}
		} else {
			holder.mTextViewSenderMessage.setText(mArrayList.get(position).getComment());
			holder.mTextViewSenderDate.setText(mArrayList.get(position).getTimestamp());

			if (mArrayList.get(position).getSenderImagePath().equalsIgnoreCase("")) {
				holder.mImageViewSender.setVisibility(View.GONE);
				holder.mTextViewSenderMessage.setVisibility(View.VISIBLE);
			} else {
				holder.mImageViewSender.setVisibility(View.VISIBLE);
				holder.mTextViewSenderMessage.setVisibility(View.GONE);
				mImageLoader.displayImage("file:/" + mArrayList.get(position).getSenderImagePath(), holder.mImageViewSender,
						mActivity.getMyApplication().profileOption);
			}
			holder.mRelativeLayoutSender.setVisibility(View.VISIBLE);
			holder.mRelativeLayoutReciever.setVisibility(View.GONE);
		}

		return convertView;
	}

	public class ViewHolder {
		
		TextView mTextViewReciverName;
		TextView mTextViewComments;
		TextView mTextViewDate;
		TextView mTextViewSenderName;
		TextView mTextViewSenderMessage;
		TextView mTextViewSenderDate;
		ImageView mImageViewReciver;
		ImageView mImageViewSender;
		ImageView mImageViewReciverDP;
		ImageView mImageViewSenderDP;
		RelativeLayout mRelativeLayoutReciever;
		RelativeLayout mRelativeLayoutSender;
	}
}
