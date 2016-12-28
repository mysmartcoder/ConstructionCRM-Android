package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.AutherParser;
import com.construction.android.parser.NotificationParser;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressLint("InflateParams")
public class NotificationsFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;

    private TextView mTextViewAll;
    private TextView mTextViewUnRead;
    private TextView mTextViewArchived;
    private ListView mListViewNotifications;

    private NotificationsAdapter mNotificationsAdapter;
//    private boolean isUnread = true;

    // private int mIntCurrentPosition = -1;

    List<NotificationParser> mList;

    String mStringReadPath = "";
    String mStringUnReadPath = "";

    public NotificationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_notification);

        getWidgetRefrence(rootView);
        registerOnClick();

        mList = new ArrayList<NotificationParser>();

        checkFolder();

        mStringReadPath = "/" + mActivity.getMyApplication().getUserID() + "/AllRead";
        mStringUnReadPath = "/" + mActivity.getMyApplication().getUserID() + "/UnRead";
        getfile(new File(mActivity.getFilesDir(), mStringUnReadPath), "true");
//        if (isUnread)
            getfile(new File(mActivity.getFilesDir(), mStringReadPath), "false");

        mNotificationsAdapter = new NotificationsAdapter();
        mListViewNotifications.setAdapter(mNotificationsAdapter);

        NotificationManager notifManager= (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();

        return rootView;
    }

    public void checkFolder() {
        File mFile = new File(mActivity.getFilesDir(), mActivity.getMyApplication().getUserID());

        if (!mFile.exists()) {
            mFile.mkdir();
        }

        File mFileUnRead = new File(mFile.getAbsolutePath(), "UnRead");

        if (!mFileUnRead.exists()) {
            mFileUnRead.mkdir();
        }

        File mFileAllRead = new File(mFile.getAbsolutePath(), "AllRead");

        if (!mFileAllRead.exists()) {
            mFileAllRead.mkdir();
        }
    }

    /**
     * Method call will get IDs from xml file.
     */
    private void getWidgetRefrence(View v) {
        mTextViewAll = (TextView) v.findViewById(R.id.fragment_notifications_textview_all);
        mTextViewAll.setTextColor(getResources().getColor(R.color.white));
        mTextViewAll.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewUnRead = (TextView) v.findViewById(R.id.fragment_notifications_textview_unread);
        mTextViewArchived = (TextView) v.findViewById(R.id.fragment_notifications_textview_archived);
        mListViewNotifications = (ListView) v.findViewById(R.id.fragment_notifications_listview);

    }

    /**
     * Method call will Register OnClick() Events for widgets.
     */
    private void registerOnClick() {
        mTextViewAll.setOnClickListener(this);
        mTextViewUnRead.setOnClickListener(this);
        mTextViewArchived.setOnClickListener(this);
        mListViewNotifications.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                // if (mIntCurrentPosition == position)
                // mIntCurrentPosition = -1;
                // else
                // mIntCurrentPosition = position;
                // mNotificationsAdapter.notifyDataSetChanged();


                if (mList.get(position).getIsUnReadNotification().equalsIgnoreCase("true")) {
                    File mFileUnread = new File(mActivity.getFilesDir(), mStringUnReadPath + "/" + mList.get(position).getFilename());
                    File mFileread = new File(mActivity.getFilesDir(), mStringReadPath + "/" + mList.get(position).getFilename());
                    boolean status = mFileUnread.renameTo(mFileread);
//                    Toast.makeText(mActivity, "Rename status : " + String.valueOf(status), Toast.LENGTH_SHORT).show();
//                    mList.remove(position);
                }

                Bundle mBundle = new Bundle();
                mBundle.putString(getString(R.string.bunble_project_guid), mList.get(position).getActivityDetails().getGuid());
                mBundle.putBoolean(getString(R.string.bunble_is_task_in_progress_screen), true);
                TaskCardFragment mTaskCardFragment = new TaskCardFragment();
                mTaskCardFragment.setArguments(mBundle);
                mActivity.replaceFragment(mTaskCardFragment, true);

                mNotificationsAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Method call OnClick Event fire.
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewAll) {

            mList = new ArrayList<NotificationParser>();
            getfile(new File(mActivity.getFilesDir(), "/" + mActivity.getMyApplication().getUserID() + "/UnRead"), "true");
            getfile(new File(mActivity.getFilesDir(), "/" + mActivity.getMyApplication().getUserID() + "/AllRead"), "false");

//            isUnread = false;
            mNotificationsAdapter = new NotificationsAdapter();
            mListViewNotifications.setAdapter(mNotificationsAdapter);

            setTabSelection();
            mTextViewAll.setTextColor(getResources().getColor(R.color.white));
            mTextViewAll.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));

        } else if (v == mTextViewUnRead) {

            mList = new ArrayList<NotificationParser>();
            getfile(new File(mActivity.getFilesDir(), "/" + mActivity.getMyApplication().getUserID() + "/UnRead"), "true");

//            isUnread = true;
            mNotificationsAdapter = new NotificationsAdapter();
            mListViewNotifications.setAdapter(mNotificationsAdapter);

            setTabSelection();
            mTextViewUnRead.setTextColor(getResources().getColor(R.color.white));
            mTextViewUnRead.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));

        } else if (v == mTextViewArchived) {

            setTabSelection();
            mTextViewArchived.setTextColor(getResources().getColor(R.color.white));
            mTextViewArchived.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));

        }

    }

    /**
     * Method call will Set Tab Selection : All, Unread, Archive
     */
    private void setTabSelection() {
        mTextViewAll.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewUnRead.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewArchived.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewAll.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
        mTextViewUnRead.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
        mTextViewArchived.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
    }

    /**
     * Method call will get mp3 files from recording dirctory.
     *
     * @param dir
     * @return
     */
    public void getfile(File dir, String isReadStatus) {

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                mList.add(readFile(listFile[i], isReadStatus));
            }
        }

        sortData();
    }

    public NotificationParser readFile(File mFile, String isStatus) {
        String mStringPreviousData = "";
        NotificationParser mNotificationParser = new NotificationParser();
        try {
            if (mFile.exists()) {
                int length = (int) mFile.length();

                byte[] bytes = new byte[length];

                FileInputStream in = new FileInputStream(mFile);
                try {
                    in.read(bytes);
                } finally {
                    in.close();
                }
                mStringPreviousData = new String(bytes);

                JSONObject mJsonObjectMain = new JSONObject(mStringPreviousData);

                //JSONObject mJsonObject = mJsonObjectMain.getJSONObject("payload");
                JSONObject mJsonObjectTASK = mJsonObjectMain.getJSONObject("activityDetails");
                JSONObject mJsonObjectActor = mJsonObjectMain.getJSONObject("creatorActorDetails");


                        System.out.println("====TYPE="+mJsonObjectMain.getString("objectEventType"));
                String notify_message = mJsonObjectMain.getString("comment");
                String timestamp = mJsonObjectMain.getString("timestamp");
                String notify_title = mJsonObjectTASK.getString("name");
                String task_id = mJsonObjectTASK.getString("guid");

                String actor_name = mJsonObjectActor.getString("name");
                String actor_id = mJsonObjectActor.getString("guid");

                mNotificationParser.setComment(notify_message);
                mNotificationParser.setTimestamp(timestamp);
                mNotificationParser.setFilename(mFile.getName());
                mNotificationParser.setIsUnReadNotification(isStatus);

                AutherParser mAutherParser = new AutherParser();
                mAutherParser.setName(notify_title);
                mAutherParser.setGuid(task_id);
                mNotificationParser.setActivityDetails(mAutherParser);

                AutherParser mAutherParserActor = new AutherParser();
                mAutherParserActor.setName(actor_name);
                mAutherParserActor.setGuid(actor_id);
                mNotificationParser.setCreatorActorDetails(mAutherParserActor);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mNotificationParser;
    }

    /**
     * Set Package Data Adapter.
     *
     * @author ebaraiya
     */
    public class NotificationsAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mList.size();
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
                convertView = mActivity.getLayoutInflater().inflate(R.layout.row_notifications, null);

                holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.row_notifications_textview_title);
                holder.mTextViewDate = (TextView) convertView.findViewById(R.id.row_notifications_textview_date);
                holder.mView = (View) convertView.findViewById(R.id.row_notifications_view);
                holder.mImageViewDelete = (ImageView) convertView.findViewById(R.id.row_notification_imageview_delete);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Calendar mCalendar = Calendar.getInstance();
            Calendar mCalendarEnd = Calendar.getInstance();

            if (position == 0) {

                holder.mTextViewDate.setVisibility(View.VISIBLE);
                mCalendar.setTimeInMillis(Long.parseLong(mList.get(position).getTimestamp()));
                holder.mTextViewDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy"));

            } else {

                String startDate = "";
                String endDate = "";

                try {
                    startDate = mList.get(position).getTimestamp().toString();
                    endDate = mList.get(position - 1).getTimestamp().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.mTextViewDate.setVisibility(View.GONE);
                    holder.mView.setVisibility(View.GONE);
                }

                if (position % 2 == 0) {
                    holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
                }



                if (startDate.length() > 0 && endDate.length() > 0) {


                    mCalendar.setTimeInMillis(Long.parseLong(mList.get(position).getTimestamp().toString()));
                    mCalendarEnd.setTimeInMillis(Long.parseLong(mList.get(position - 1).getTimestamp().toString()));

                    String mString1 = mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy");
                    String mString2 = mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendarEnd, "dd MMM yyyy");

                    if (mString1.equalsIgnoreCase(mString2)) {

                        holder.mTextViewDate.setVisibility(View.GONE);
                        holder.mView.setVisibility(View.GONE);

                    } else {
                        holder.mTextViewDate.setVisibility(View.VISIBLE);
                        holder.mView.setVisibility(View.VISIBLE);
                        mCalendar.setTimeInMillis(Long.parseLong(mList.get(position).getTimestamp()));
                        holder.mTextViewDate.setText(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy"));

                    }
                }
            }

            holder.mTextViewTitle.setText(mList.get(position).getComment());
            if (mList.get(position).getIsUnReadNotification().equalsIgnoreCase("true")) {
                holder.mTextViewTitle.setTypeface(holder.mTextViewTitle.getTypeface(), Typeface.BOLD);
            } else {
                holder.mTextViewTitle.setTypeface(holder.mTextViewTitle.getTypeface(), Typeface.NORMAL);
            }

            return convertView;
        }

    }

    public class ViewHolder {
        TextView mTextViewTitle;
        TextView mTextViewDate;
        ImageView mImageViewDelete;
        View mView;
    }

    /**
     * Method will sort data by debit
     */
    public void sortData() {
        Collections.sort(mList, new Comparator<NotificationParser>() {

            @SuppressLint("NewApi")
            @Override
            public int compare(NotificationParser o1, NotificationParser o2) {
                String first_amount = "";
                String second_amount = "";

                first_amount = o1.getTimestamp();

                second_amount = o2.getTimestamp();

                return Double.compare(Double.parseDouble(second_amount), Double.parseDouble(first_amount));
            }
        });
    }

}
