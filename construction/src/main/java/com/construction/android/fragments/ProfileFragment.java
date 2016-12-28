package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.construction.android.parser.StatusData;
import com.construction.android.parser.UserDetailParser;

import java.io.File;
import java.util.StringTokenizer;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class ProfileFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;

    private ImageView mImageViewProfilePic;
    private TextView mTextViewUserName;
    private TextView mTextViewStatus;
    private TextView mTextViewRole;
    private TextView mTextViewEmail;
    private TextView mTextViewOrg;
    private TextView mTextViewUpdate;

    private Intent mIntentPictureAction = null;
    private Bitmap mBitmap = null;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Dialog mDialog;
    private String mStringProfilePicturePath = "";

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;

    private BackProcessUpdateProfile mBackProcessUpdateProfile;
    private ProgressDialog mProgressDialog;

    private String mCurrentMethod = "";
    private String mMethdoUpdateProfilePic = "profile_pic";
    private String mMethodGetProfilePic = "get_profile_pic";
    private String mMethodUpdteUserName = "update_username";
    private String mMethodGetUserDetails = "get_user_details";

    private StatusData mStatusData;
    private UserDetailParser mUserDetailParser;
    private String mStringUpdateProfilePicMessage = "";
    private String mStringFirstName = "";
    private String mStringFamilyName = "";
    private Bitmap mBitmapUserPicture = null;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mActivity = (MyFragmentActivity) getActivity();

        getWidgetRefrence(rootView);
        registerOnClick();

        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mStatusData = new StatusData();
        mUserDetailParser = new UserDetailParser();

        mBackProcessUpdateProfile = new BackProcessUpdateProfile();
        mBackProcessUpdateProfile.execute(mMethodGetProfilePic);

        return rootView;
    }

    /**
     * Fuction call will get IDs from xml file.
     *
     * @param v
     */
    private void getWidgetRefrence(View v) {

        mImageViewProfilePic = (ImageView) v.findViewById(R.id.fragment_profile_imageview_profile_picture);
        mTextViewUserName = (TextView) v.findViewById(R.id.fragment_profile_textview_user_name);
        mTextViewUserName.setText(mActivity.getMyApplication().getUserName());
        mActivity.getCommonMethod().setTitleFont(mTextViewUserName);
        mTextViewStatus = (TextView) v.findViewById(R.id.fragment_profile_textview_status);
        if (mActivity.getMyApplication().getUserStatus().equalsIgnoreCase("true")) {

            mTextViewStatus.setText(getString(R.string.lbl_active));
            mTextViewStatus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_profile_active), null, null, null);
        } else {

            mTextViewStatus.setText(getString(R.string.lbl_in_active));
            mTextViewStatus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_profile_unactive), null, null, null);

        }
        mTextViewRole = (TextView) v.findViewById(R.id.fragment_profile_textview_role);
        mTextViewEmail = (TextView) v.findViewById(R.id.fragment_profile_textview_email);
        mTextViewOrg = (TextView) v.findViewById(R.id.fragment_profile_textview_organization);
        mTextViewUpdate = (TextView) v.findViewById(R.id.fragment_profile_textview_edit_profile);
    }

    /**
     * Function call will register OnClick() Events.
     */
    private void registerOnClick() {

        mImageViewProfilePic.setOnClickListener(this);
        mTextViewUserName.setOnClickListener(this);
        mTextViewUpdate.setOnClickListener(this);
        mTextViewStatus.setOnClickListener(this);

    }

    /**
     * Function call will fire onClick event;
     */
    @Override
    public void onClick(View v) {

        if (v == mImageViewProfilePic) {

            selectPhotoDialog();

        } else if (v == mTextViewUserName) {

        } else if (v == mTextViewUpdate) {

            showUsernameUpdateDialog();

        } else if (v == mTextViewStatus) {

            setUSerStatus();
        }

    }

    /**
     * Function call will get image from gallary or camera.
     */
    private void selectPhotoDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(getString(R.string.alt_msg_Profile_pic));
        myAlertDialog.setMessage(getString(R.string.alt_msg_set_profile_picture));

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
                File mFile = new File(mActivity.getCommonMethod().getAppPath() + "/profile.png");
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
                            mActivity.getCommonMethod()
                                    .copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/profile.png", mBitmap);
                            mStringProfilePicturePath = mActivity.getCommonMethod().getAppPath() + "/profile.png";
                            showImageDisplayDialog();
                        } else {
                            mImageViewProfilePic.setImageResource(R.drawable.bg_user_pic);
                        }

                    } catch (Exception e) {
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
                File imgFile = new File(mActivity.getCommonMethod().getAppPath() + "/profile.png");
                mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mBitmap = mActivity.getCommonMethod().resizeBitmap(mBitmap);
                mStringProfilePicturePath = mActivity.getCommonMethod().getAppPath() + "/profile.png";
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
                mActivity.getCommonMethod().copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/profile.png", mBitmap);
                mImageViewProfilePic.setImageBitmap(mBitmap);

                mBackProcessUpdateProfile = new BackProcessUpdateProfile();
                mBackProcessUpdateProfile.execute(mMethdoUpdateProfilePic);
//				new ImageUploadTask().execute("");

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
     * Method call will showing Username update dialog.
     */
    private void showUsernameUpdateDialog() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_username_update);
        Window window = mDialog.getWindow();
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        final EditText mEditTextFname = (EditText) mDialog.findViewById(R.id.dialog_username_update_editText_fname);
        final EditText mEditTextLname = (EditText) mDialog.findViewById(R.id.dialog_username_update_editText_lname);
        final TextView mTextViewCancel = (TextView) mDialog.findViewById(R.id.dialog_username_updatee_textview_cancel);
        final TextView mTextViewUpdate = (TextView) mDialog.findViewById(R.id.dialog_username_updatee_textview_send);

        StringTokenizer mStringTokenizer = new StringTokenizer(mTextViewUserName.getText().toString(), " ");

        try {
            mEditTextFname.setText(mStringTokenizer.nextToken().toString());
            mEditTextLname.setText(mStringTokenizer.nextToken().toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mTextViewCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDialog.cancel();
            }
        });
        mTextViewUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mStringFirstName = mEditTextFname.getText().toString().trim();
                mStringFamilyName = mEditTextLname.getText().toString().trim();

                if (mActivity.getCommonMethod().validateBlankField(mEditTextFname, mActivity, getString(R.string.validation_fname))
                        && mActivity.getCommonMethod().validateBlankField(mEditTextLname, mActivity, getString(R.string.validation_lname))) {
                    mActivity.getCommonMethod().HideKeyboard(mEditTextFname);

                    mBackProcessUpdateProfile = new BackProcessUpdateProfile();
                    mBackProcessUpdateProfile.execute(mMethodUpdteUserName);
                }
            }
        });

        mDialog.show();
    }

    /**
     * Method call will set user status :Active or In-Active
     */
    public void setUSerStatus() {
        int position = 0;
        if (mTextViewStatus.getText().toString().equalsIgnoreCase(getString(R.string.lbl_active))) {
            position = 0;
        } else {
            position = 1;

        }

        final AlertDialog.Builder singlechoicedialog = new AlertDialog.Builder(mActivity);
        final CharSequence[] Report_items = {getString(R.string.lbl_active), getString(R.string.lbl_in_active)};
        singlechoicedialog.setTitle(getString(R.string.lbl_user_status));
        singlechoicedialog.setSingleChoiceItems(Report_items, position, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                mTextViewStatus.setText(Report_items[item].toString());

                if (Report_items[item].toString().equalsIgnoreCase(getString(R.string.lbl_active))) {

                    mTextViewStatus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_profile_active), null, null,
                            null);
                    mEditor.putString(getString(R.string.sp_user_status), "true");
                    mEditor.commit();

                } else {
                    mTextViewStatus.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_profile_unactive), null, null,
                            null);
                    mEditor.putString(getString(R.string.sp_user_status), "false");
                    mEditor.commit();

                }

                dialog.cancel();

            }
        });
        AlertDialog alert_dialog = singlechoicedialog.create();
        alert_dialog.show();
    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author npatel
     */
    public class BackProcessUpdateProfile extends AsyncTask<String, Void, String> {
        String responseData = "";
//		String mCurrentMethod = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];
            if (mCurrentMethod.equalsIgnoreCase(mMethdoUpdateProfilePic)) {

                mStringUpdateProfilePicMessage = (String) mActivity.getCommonMethod().updateProfilePicAPI(
                        mActivity.getMyApplication().getUserToken(), String.valueOf(System.currentTimeMillis()) + ".png", mStringProfilePicturePath, mActivity.getMyApplication().getUserID(),
                        mStringUpdateProfilePicMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodUpdteUserName)) {

                mStringUpdateProfilePicMessage = (String) mActivity.getCommonMethod().updateUserNameAPI(mActivity.getMyApplication().getUserToken(),
                        mStringFirstName, mStringFamilyName, mActivity.getMyApplication().getUserID(), mStringUpdateProfilePicMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetUserDetails)) {

                mUserDetailParser = (UserDetailParser) mActivity.getCommonMethod().getUserDetailsAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserName(), mUserDetailParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetProfilePic)) {

                byte[] mdata = (byte[]) mActivity.getCommonMethod().getUserPictureAPI(mActivity.getMyApplication().getUserToken(),
                        mActivity.getMyApplication().getUserID(), mBitmapUserPicture);
                if (mdata != null)
                    mBitmapUserPicture = BitmapFactory.decodeByteArray(mdata, 0, mdata.length);
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
                //Activity.getCommonMethod().showDialog("", getString(R.string.validation_failed), true);
                mActivity.getCommonMethod().sessionOut();
            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethdoUpdateProfilePic)) {

                        Toast.makeText(mActivity, mStringUpdateProfilePicMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodUpdteUserName)) {

                        mDialog.cancel();
                        Toast.makeText(mActivity, mStringUpdateProfilePicMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetUserDetails)) {

                        if (!mUserDetailParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mUserDetailParser != null) {

                                mTextViewUserName.setText(mUserDetailParser.getGivenName() + " " + mUserDetailParser.getFamilyName());
                                mTextViewEmail.setText(mUserDetailParser.getContactInfo().getEmailAddress());
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mUserDetailParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetProfilePic)) {

                        if (mBitmapUserPicture != null)
                            mImageViewProfilePic.setImageBitmap(mBitmapUserPicture);
                        mBackProcessUpdateProfile = new BackProcessUpdateProfile();
                        mBackProcessUpdateProfile.execute(mMethodGetUserDetails);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }


}