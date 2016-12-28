package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.NotificationDataParser;

@SuppressLint("InflateParams")
public class SettingNotificationFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;

    private TextView mTextViewAllowNotification;
    private TextView mTextViewShowAlertFromEveryone;
    private TextView mTextViewShowAlertFrom;
    private TextView mTextViewShowAlertFromValue;
    private TextView mTextViewActiveStatusChange;
    private TextView mteTextViewActiveDataChange;
    private TextView mTextViewNoteUpdate;
    private TextView mTextViewWhenIAm;

    private boolean isAllNotification = false;
    private boolean isAlertFromEveryone = false;
    private boolean isActiveStatusChange = false;
    private boolean isActiveDataChange = false;
    private boolean isActiveNoteUpdate = false;
    private boolean isWhenIRefered = false;

    private BackProcessGetNotification mBackProcessGetNotification;
//	private ProgressDialog mProgressDialog;

    private String mCurrentMethod = "";
    private String mMethdoGetNotification = "get_notification";
    private String mMethodSetShowAllNotification = "set_show_all_notification";
    private String mMethodShowNotificationForEveryone = "show_everyone";
    private String mMethodShowNotificationOnlyMe = "shwo_only_me";
    private String mMethodActivityStatusChange = "activity_status_change";
    private String mMethodActivityDataChange = "activity_data_change";
    private String mMethodActivityNoteUpdate = "activity_note_update";
    private String mMethodReferdToMe = "refered_to_me";

    private NotificationDataParser mNotificationDataParser;
    private String mStringMessage = "";

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;

    public SettingNotificationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings_notification, container, false);

        mActivity = (MyFragmentActivity) getActivity();

        getWidgetRefrence(rootView);
        registerOnClick();


        mSharedPreferences = mActivity.getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mNotificationDataParser = new NotificationDataParser();

        mBackProcessGetNotification = new BackProcessGetNotification();
        mBackProcessGetNotification.execute(mMethdoGetNotification);

        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     *
     * @param v
     */
    private void getWidgetRefrence(View v) {
        mTextViewAllowNotification = (TextView) v.findViewById(R.id.fragment_settings_textveiw_notifications_allow_notification);
        mTextViewShowAlertFromEveryone = (TextView) v.findViewById(R.id.fragment_settings_notifications_show_alert_from_everyone);
        mTextViewShowAlertFrom = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_show_alert_value);
        mTextViewShowAlertFromValue = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_show_alert);
        mTextViewActiveStatusChange = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_active_status_change);
        mteTextViewActiveDataChange = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_active_data_change);
        mTextViewNoteUpdate = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_active_note_update);
        mTextViewWhenIAm = (TextView) v.findViewById(R.id.fragment_settings_notifications_textview_when_i_am_reffered);
    }

    /**
     * Method call will register OnClick() Events.
     */
    private void registerOnClick() {

        mTextViewAllowNotification.setOnClickListener(this);
        mTextViewShowAlertFromEveryone.setOnClickListener(this);
        mTextViewShowAlertFrom.setOnClickListener(this);
        mTextViewShowAlertFromValue.setOnClickListener(this);
        mTextViewActiveStatusChange.setOnClickListener(this);
        mteTextViewActiveDataChange.setOnClickListener(this);
        mTextViewNoteUpdate.setOnClickListener(this);
        mTextViewWhenIAm.setOnClickListener(this);

    }

    /**
     * Method call will fire Click event.
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewAllowNotification) {

            // isAllNotification = !isAllNotification;
            if (isAllNotification) {
                mTextViewAllowNotification.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
                isAllNotification = false;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodSetShowAllNotification);
                mEditor.putBoolean(getString(R.string.sp_is_notification), false);
                mEditor.apply();

            } else {

                mTextViewAllowNotification.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
                isAllNotification = true;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodSetShowAllNotification);
                mEditor.putBoolean(getString(R.string.sp_is_notification), true);
                mEditor.apply();

            }

        } else if (v == mTextViewShowAlertFromEveryone) {

            // isAlertFromEveryone = !isAlertFromEveryone;
            if (isAlertFromEveryone) {
                mTextViewShowAlertFromEveryone.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.check_box_off), null);
                isAlertFromEveryone = false;
                mTextViewShowAlertFrom.setEnabled(true);
                mTextViewShowAlertFromValue.setEnabled(true);
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodShowNotificationForEveryone);
                mEditor.putBoolean(getString(R.string.sp_is_notification_everyone), false);
                mEditor.apply();
            } else {
                mTextViewShowAlertFromEveryone.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                        .getDrawable(R.drawable.check_box_on), null);
                isAlertFromEveryone = true;
                mTextViewShowAlertFrom.setEnabled(false);
                mTextViewShowAlertFromValue.setEnabled(false);
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodShowNotificationForEveryone);
                mEditor.putBoolean(getString(R.string.sp_is_notification_everyone), true);
                mEditor.apply();
            }

        } else if (v == mTextViewShowAlertFrom) {

            setAlertPrivacy();

        } else if (v == mTextViewShowAlertFromValue) {

            setAlertPrivacy();

        } else if (v == mTextViewActiveStatusChange) {
            // isActiveStatusChange = !isActiveStatusChange;
            if (isActiveStatusChange) {
                mTextViewActiveStatusChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
                isActiveStatusChange = false;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityStatusChange);
                mEditor.putBoolean(getString(R.string.sp_is_status_notification), false);
                mEditor.apply();
            } else {
                mTextViewActiveStatusChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
                isActiveStatusChange = true;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityStatusChange);
                mEditor.putBoolean(getString(R.string.sp_is_status_notification), true);
                mEditor.apply();
            }

        } else if (v == mteTextViewActiveDataChange) {
            // isActiveDataChange = !isActiveDataChange;
            if (isActiveDataChange) {
                mteTextViewActiveDataChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
                isActiveDataChange = false;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityDataChange);
                mEditor.putBoolean(getString(R.string.sp_is_file_notification), false);
                mEditor.apply();
            } else {
                mteTextViewActiveDataChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
                isActiveDataChange = true;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityDataChange);
                mEditor.putBoolean(getString(R.string.sp_is_file_notification), true);
                mEditor.apply();
            }

        } else if (v == mTextViewNoteUpdate) {
            // isActiveNoteUpdate = !isActiveNoteUpdate;
            if (isActiveNoteUpdate) {
                mTextViewNoteUpdate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
                isActiveNoteUpdate = false;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityNoteUpdate);
                mEditor.putBoolean(getString(R.string.sp_is_not_notification), false);
                mEditor.apply();
            } else {
                mTextViewNoteUpdate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
                isActiveNoteUpdate = true;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodActivityNoteUpdate);
                mEditor.putBoolean(getString(R.string.sp_is_not_notification), true);
                mEditor.apply();
            }

        } else if (v == mTextViewWhenIAm) {

            // isWhenIRefered = !isWhenIRefered;
            if (isWhenIRefered) {
                mTextViewWhenIAm.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
                isWhenIRefered = false;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodReferdToMe);
            } else {
                mTextViewWhenIAm.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
                isWhenIRefered = true;
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodReferdToMe);
            }
        }

    }

    /**
     * Method call will set Storage Limitation.
     */
    public void setAlertPrivacy() {
        final AlertDialog.Builder singlechoicedialog = new AlertDialog.Builder(mActivity);
        final CharSequence[] Report_items = getResources().getStringArray(R.array.show_alert_from_array);
        singlechoicedialog.setTitle(getString(R.string.lbl_set_alert_privacy));
        singlechoicedialog.setSingleChoiceItems(Report_items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mTextViewShowAlertFromValue.setText(Report_items[item].toString());
                dialog.cancel();
                mBackProcessGetNotification = new BackProcessGetNotification();
                mBackProcessGetNotification.execute(mMethodShowNotificationOnlyMe);
            }
        });
        AlertDialog alert_dialog = singlechoicedialog.create();
        alert_dialog.show();
    }

    /**
     * Method call will set all notification data.
     */
    private void setNotificationData() {
        if (mNotificationDataParser.getNotificationsAllowed().equalsIgnoreCase("false")) {
            mTextViewAllowNotification.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
            isAllNotification = false;
        } else {
            mTextViewAllowNotification.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
            isAllNotification = true;
        }

        if (mActivity.getMyApplication().isShowNotifcatoinForAll()) {
            mTextViewShowAlertFromEveryone.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.check_box_on),
                    null);
            isAlertFromEveryone = true;
            mTextViewShowAlertFrom.setEnabled(false);
            mTextViewShowAlertFromValue.setEnabled(false);
        } else {
            mTextViewShowAlertFromEveryone.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.check_box_off),
                    null);
            isAlertFromEveryone = false;
            mTextViewShowAlertFrom.setEnabled(true);
            mTextViewShowAlertFromValue.setEnabled(true);


        }

        if (mNotificationDataParser.getShowNotificationFromMeOnly().equalsIgnoreCase("false")) {

        }

        if (mNotificationDataParser.getShowActivityStatusChange().equalsIgnoreCase("false")) {
            mTextViewActiveStatusChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
            isActiveStatusChange = false;
        } else {
            mTextViewActiveStatusChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
            isActiveStatusChange = true;
        }
        if (mNotificationDataParser.getShowActivityDateChange().equalsIgnoreCase("false")) {
            mteTextViewActiveDataChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
            isActiveDataChange = false;
        } else {
            mteTextViewActiveDataChange.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
            isActiveDataChange = true;
        }

        if (mNotificationDataParser.getShowActivityNotesChange().equalsIgnoreCase("false")) {
            mTextViewNoteUpdate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
            isActiveNoteUpdate = false;
        } else {
            mTextViewNoteUpdate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
            isActiveNoteUpdate = true;
        }

        if (mNotificationDataParser.getShowWhenReferred().equalsIgnoreCase("false")) {
            mTextViewWhenIAm.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_off), null);
            isWhenIRefered = false;
        } else {
            mTextViewWhenIAm.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.btn_on), null);
            isWhenIRefered = true;
        }
    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author ebaraiya
     */
    public class BackProcessGetNotification extends AsyncTask<String, Void, String> {
        String responseData = "";

        @Override
        protected void onPreExecute() {
//			mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];
            if (mCurrentMethod.equalsIgnoreCase(mMethdoGetNotification)) {

                mNotificationDataParser = (NotificationDataParser) mActivity.getCommonMethod().getAllNotificationAPI(
                        mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getUserID(), mNotificationDataParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetShowAllNotification)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateShowNotification(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isAllNotification, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodShowNotificationForEveryone)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateShowNotificationEveryOne(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isAlertFromEveryone, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodShowNotificationOnlyMe)) {

                mStringMessage = (String) mActivity.getCommonMethod().showNotificationOnlyMe(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isAlertFromEveryone, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityStatusChange)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateStatusNotificationAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isActiveStatusChange, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityDataChange)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateDataNotificationAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isActiveDataChange, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityNoteUpdate)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateNoteNotificationAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isActiveNoteUpdate, mStringMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodReferdToMe)) {

                mStringMessage = (String) mActivity.getCommonMethod().updateReferedMeNotificationAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), isWhenIRefered, mStringMessage);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

//			if (mProgressDialog != null)
//				mProgressDialog.dismiss();

            if (mActivity.getCommonMethod().isNetError) {
                mActivity.getCommonMethod().showDialog("", getString(R.string.validation_no_internet), false);
            } else if (mActivity.getCommonMethod().isError) {
                mActivity.getCommonMethod().showDialog("", getString(R.string.validation_failed), true);
                //mActivity.getCommonMethod().sessionOut();
            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethdoGetNotification)) {

                        if (!mNotificationDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            setNotificationData();

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mNotificationDataParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetShowAllNotification)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodShowNotificationForEveryone)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodShowNotificationOnlyMe)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityStatusChange)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityDataChange)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodActivityNoteUpdate)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodReferdToMe)) {

                        Toast.makeText(mActivity, mStringMessage, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }
}
