package com.construction.android.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.HistoryDataParser;

public class LoadHistoryData{
	MyFragmentActivity mActivity;
	ArrayList<HistoryDataParser> mArrayList;
	ImageLoader mImageLoader;

	public LoadHistoryData(MyFragmentActivity mActivity2, ArrayList<HistoryDataParser> mHistoryDataParsers) {
		this.mActivity = mActivity2;
		this.mArrayList = mHistoryDataParsers;
		mImageLoader = ImageLoader.getInstance();
		System.out.println("SIZE=="+mArrayList.size());
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
	
	
	public void loadHistoryData(LinearLayout mLinearLayout)
	{
		mLinearLayout.removeAllViews();
		for (int position = 0; position < mArrayList.size(); position++) {
			ViewHolder holder;
			
			holder = new ViewHolder();
			View convertView = mActivity.getLayoutInflater().inflate(R.layout.row_notes, null);

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

			
			

			if (mArrayList.get(position).getCheckSender().equalsIgnoreCase("")) {
				if (mArrayList.get(position).getCreatorActorDetails()==null|| mArrayList.get(position).getCreatorActorDetails().getGuid().equalsIgnoreCase(mActivity.getMyApplication().getUserID())||mArrayList.get(position).getCreatorActorDetails().getGuid().equalsIgnoreCase("")) {

					holder.mTextViewSenderMessage.setText(mArrayList.get(position).getComment());
					holder.mTextViewSenderName.setText(mArrayList.get(position).getCreatorActorDetails()!=null ?mArrayList.get(position).getCreatorActorDetails().getName():"");
					Calendar mCalendar = Calendar.getInstance();
					mCalendar.setTimeInMillis(Long.parseLong(mArrayList.get(position).getTimestamp()));
					holder.mTextViewSenderDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar,"dd MMM yyyy"));
					holder.mRelativeLayoutSender.setVisibility(View.VISIBLE);
					holder.mRelativeLayoutReciever.setVisibility(View.GONE);
					
				} else {
					holder.mTextViewComments.setText(mArrayList.get(position).getComment());
					holder.mTextViewReciverName.setText(mArrayList.get(position).getCreatorActorDetails()!=null ?mArrayList.get(position).getCreatorActorDetails().getName():"");
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
			
			mLinearLayout.addView(convertView);
		}
	}

}
