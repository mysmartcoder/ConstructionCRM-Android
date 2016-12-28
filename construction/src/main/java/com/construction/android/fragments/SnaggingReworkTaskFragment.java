package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.parser.ProjectDetailsDataParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class SnaggingReworkTaskFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;
    private ProgressDialog mProgressDialog;

    private TextView mTextViewTaskName;
    private TextView mTextViewPlace;
    private TextView mTextViewDate;
    private TextView mTextViewInComplete;
    private TextView mTextViewDefective;
    private TextView mTextViewInspection;

    private ImageView mImageViewLogo;
    private ImageView mImageViewSendNote;
    private ImageView mImageViewPickImage;

    private EditText mEditTextNotes;

    private ProjectDetailsDataParser mDataParser;
    private String mStringGuID = "";

    private String mStringMessage = "";
    private String mStringStatus = "";
    private String mCurrentMethod = "";
    private String mMethodGetPredescessors = "getPredescessors";
    private String mMethodSetNoteMessage = "setNoteMessage";
    private String mMethodSendFile = "sendFile";
    private String mMethodInspectReject = "inspectReject";
    private String mStringSendFileMessage = "";
    private String mStringReason = "0";
    private String mStringComment = "";

    private BackProcessGetDetais mProcessGetDetails;

    private Intent mIntentPictureAction = null;
    private Bitmap mBitmap = null;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Dialog mDialog;
    private String mStringFilePath = "";
    private String mStringFileName = "";
    private long mIntTimeStamp = 0;

    public SnaggingReworkTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_snagging_rework_task_details, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_snagging);

        getWidgetRefrence(rootView);
        registerOnClick();

        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mStringGuID = mBundle.getString(getString(R.string.bunble_project_guid));
        }

        mDataParser = new ProjectDetailsDataParser();

        mProcessGetDetails = new BackProcessGetDetais();
        mProcessGetDetails.execute(mMethodGetPredescessors);

        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     */
    private void getWidgetRefrence(View v) {

        mImageViewLogo = (ImageView) v.findViewById(R.id.fragment_snagging_task_rework_imageview_logo);

        mTextViewTaskName = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_task_name);
        mActivity.getCommonMethod().setTitleFont(mTextViewTaskName);
        mTextViewPlace = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_task_place);
        mTextViewDate = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_task_date);

        mTextViewInComplete = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_incomplete);
        //mTextViewInComplete.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        //mTextViewInComplete.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
        mTextViewDefective = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_defactive);
        mTextViewInspection = (TextView) v.findViewById(R.id.fragment_snagging_task_rework_textview_inspection);

        mImageViewSendNote = (ImageView) v.findViewById(R.id.view_add_note_imageview_send);
        mImageViewPickImage = (ImageView) v.findViewById(R.id.view_add_note_imageview_camera);
        mEditTextNotes = (EditText) v.findViewById(R.id.view_add_note_edittex_text);

    }

    /**
     * Method call will Register OnClick() Events for widgets.
     */
    private void registerOnClick() {
        mTextViewInComplete.setOnClickListener(this);
        mTextViewDefective.setOnClickListener(this);
        mTextViewInspection.setOnClickListener(this);

        mImageViewPickImage.setOnClickListener(this);
        mImageViewSendNote.setOnClickListener(this);
    }

    /**
     * Method call OnClick Event fire.
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewInComplete) {

            setTabSelection();
            mTextViewInComplete.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
            mTextViewInComplete.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
            mTextViewInComplete.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_on));

            mStringReason = "0";
            showCommentDialog();

        } else if (v == mTextViewDefective) {

            setTabSelection();
            mTextViewDefective.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
            mTextViewDefective.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
            mTextViewDefective.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_on));
            mStringReason = "1";
            showCommentDialog();

        } else if (v == mTextViewInspection) {

            setTabSelection();
            mTextViewInspection.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
            mTextViewInspection.setBackgroundColor(getResources().getColor(R.color.bg_action_bar));
            mTextViewInspection.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_on));
            mStringReason = "2";
            showCommentDialog();

        } else if (v == mImageViewPickImage) {

            selectPhotoDialog();

        } else if (v == mImageViewSendNote) {
            mStringMessage = mEditTextNotes.getText().toString().trim();
            if (mStringMessage.length() > 0) {
                mProcessGetDetails = new BackProcessGetDetais();
                mProcessGetDetails.execute(mMethodSetNoteMessage);
            }
        }
    }

    /**
     * Method call will set Tab : Cmplete, Defective and Inspection.
     */
    private void setTabSelection() {
        mTextViewInComplete.setTextColor(getResources().getColor(R.color.white));
        mTextViewDefective.setTextColor(getResources().getColor(R.color.white));
        mTextViewInspection.setTextColor(getResources().getColor(R.color.white));
        mTextViewInComplete.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewDefective.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewInspection.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewInComplete.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_off));
        mTextViewDefective.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_off));
        mTextViewInspection.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.btn_redio_off));
    }

    /**
     * Method call will set Data from previous screen.
     */
    private void setData() {

        int drawableResourceId = this.getResources().getIdentifier("icon_snagging_task_" + mDataParser.getStatus(), "drawable",
                mActivity.getPackageName());
        mImageViewLogo.setImageResource(drawableResourceId);

        try {
            mTextViewTaskName.setText(mDataParser.getName());
            if (mDataParser.getActivityLocation().length() > 0) {
                mTextViewPlace.setText(mDataParser.getActivityLocation());
            } else {
                mTextViewPlace.setVisibility(View.GONE);
            }
        } catch (Exception e1) {
//			e1.printStackTrace();
        }

        try {
            Calendar mCalendarDuteDate = Calendar.getInstance();
            mCalendarDuteDate.setTimeInMillis(Long.parseLong(mDataParser.getEndDate()));
            mTextViewDate.setText(getString(R.string.lbl_markeed_complete) + " "
                    + mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendarDuteDate, "dd MMM yyyy"));
        } catch (Exception e) {
//			e.printStackTrace();
        }

    }

    /**
     * Method will display comment dialog
     */
    public void showCommentDialog() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_set_alert_comment);
        Window window = mDialog.getWindow();
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final TextView mTextViewSave = (TextView) mDialog.findViewById(R.id.dialog_set_alert_comment_textview_send);
        final EditText mEditTextComments = (EditText) mDialog.findViewById(R.id.dialog_set_alert_comment_editText_comments);
        mTextViewSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mStringComment = mEditTextComments.getText().toString().trim();
                if (mStringComment.length() > 0) {
                    mProcessGetDetails = new BackProcessGetDetais();
                    mProcessGetDetails.execute(mMethodInspectReject);
                } else {
                    mActivity.getCommonMethod().showDialog("", getString(R.string.validation_enter_comments), false);
                }

            }
        });
        mDialog.show();

    }

    /**
     * Function call will get image from gallary or camera.
     */
    private void selectPhotoDialog() {

        mIntTimeStamp = (long) System.currentTimeMillis();

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(getString(R.string.alt_msg_send_picture));
        myAlertDialog.setMessage(getString(R.string.alt_msg_send_picture_msessage));

        myAlertDialog.setPositiveButton(getString(R.string.lbl_gallery), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                mIntentPictureAction = new Intent(Intent.ACTION_GET_CONTENT, null);
                mIntentPictureAction.setType("image/*");
                mIntentPictureAction.putExtra("return-data", true);
                startActivityForResult(mIntentPictureAction, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton(getString(R.string.lbl_camera), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                File mFile = new File(mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png");
                mIntentPictureAction = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                mIntentPictureAction.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                startActivityForResult(mIntentPictureAction, CAMERA_REQUEST);

            }
        });
        myAlertDialog.show();
    }

    @SuppressWarnings("static-access")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICTURE) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    try {

                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        mBitmap = mActivity.getCommonMethod().resizeBitmap(mBitmap);

                        if (mBitmap != null) {
                            mActivity.getCommonMethod().getApplicationDirectory();
                            mActivity.getCommonMethod().copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);
                            mStringFilePath = mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png";
                            mStringFileName = mIntTimeStamp + ".png";
                            showImageDisplayDialog();
                        } else {
                            // mImageViewProfilePic.setImageResource(R.drawable.bg_user_pic);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                File imgFile = new File(mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png");
                mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mBitmap = mActivity.getCommonMethod().resizeBitmap(mBitmap);
                mStringFilePath = mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png";
                mStringFileName = mIntTimeStamp + ".png";
                showImageDisplayDialog();

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
            }
        }

    }


    /**
     * Method will display image after pick up from gallery or camera
     */
    public void showImageDisplayDialog() {
        mDialog = new Dialog(mActivity, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setContentView(R.layout.view_image);
        final CropImageView mImageViewRotate = (CropImageView) mDialog.findViewById(R.id.view_image_imageview_image);
        mImageViewRotate.setImageBitmap(mBitmap);

        TextView mTextViewDone = (TextView) mDialog.findViewById(R.id.view_image_textview_done);
        mTextViewDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
                mBitmap = mImageViewRotate.getCroppedImage();
                mActivity.getCommonMethod().copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);

                mProcessGetDetails = new BackProcessGetDetais();
                mProcessGetDetails.execute(mMethodSendFile);

            }
        });

        TextView mTextViewRotate = (TextView) mDialog.findViewById(R.id.view_image_textview_rotate);
        mTextViewRotate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mImageViewRotate.rotateImage(90);
            }
        });

        mDialog.show();
    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author ebaraiya
     */
    public class BackProcessGetDetais extends AsyncTask<String, Void, String> {
        String responseData = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            mCurrentMethod = params[0];

            if (mCurrentMethod.equalsIgnoreCase(mMethodGetPredescessors)) {

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().getPredescessorsAPI(mActivity.getMyApplication().getUserToken(),
                        mStringGuID, mDataParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().setNotesAPI(mActivity.getMyApplication().getUserToken(),
                        mStringGuID, mActivity.getMyApplication().getUserID(), mStringMessage, mDataParser);


            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

                mStringSendFileMessage = (String) mActivity.getCommonMethod().sendFileAPI(mActivity.getMyApplication().getUserToken(),
                        mStringFileName, mStringFilePath, mStringGuID, mStringSendFileMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodInspectReject)) {

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().setInspectRejectAPI(mStringGuID,
                        mActivity.getMyApplication().getUserToken(),
                        mStringReason, mStringComment, mDataParser);

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            if (mActivity.getCommonMethod().isNetError) {

                mActivity.getCommonMethod().showDialog("", getString(R.string.validation_no_internet), false);

            } else if (mActivity.getCommonMethod().isError) {

                //mActivity.getCommonMethod().showDialog("", getString(R.string.validation_failed), true);
                mActivity.getCommonMethod().sessionOut();

            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethodGetPredescessors)) {

                        if (!mDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParser.getConstrainingActivityRelations() != null) {

                                mActivity.getCommonMethod().writeToFile("");
                                setData();

                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

                        mEditTextNotes.setText("");
                        setData();


                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

                        Toast.makeText(mActivity, mStringSendFileMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodInspectReject)) {

                        if (!mDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {

                            if (mDataParser != null && mDataParser.getGuid().length() > 0) {
                                mDialog.cancel();
                                mActivity.onBackPressed();
                                mActivity.onBackPressed();
                            } else {
                                mActivity.getCommonMethod().showDialog("", mDataParser.getError(), false);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParser.getMessage());
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
