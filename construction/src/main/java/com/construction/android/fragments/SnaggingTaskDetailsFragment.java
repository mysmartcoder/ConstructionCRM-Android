package com.construction.android.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.construction.android.R;
import com.construction.android.activity.MyFragmentActivity;
import com.construction.android.activity.NoteActivity;
import com.construction.android.adapter.FileAdapter;
import com.construction.android.adapter.HistoryAdapter;
import com.construction.android.adapter.LoadHistoryData;
import com.construction.android.adapter.LoadNotesAdapter;
import com.construction.android.adapter.NotesAdapter;
import com.construction.android.adapter.PredescessorsAdapter;
import com.construction.android.helper.Helper;
import com.construction.android.parser.FilesParser;
import com.construction.android.parser.HistoryDataParser;
import com.construction.android.parser.HistoryParser;
import com.construction.android.parser.NotesParser;
import com.construction.android.parser.PredescessorsParser;
import com.construction.android.parser.ProjectDetailsDataParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class SnaggingTaskDetailsFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private View rootView;
    private ProgressDialog mProgressDialog;

    private TextView mTextViewTaskName;
    private TextView mTextViewPlace;
    private TextView mTextViewDate;
    private TextView mTextViewComplete;
    private TextView mTextViewRework;
    private TextView mTextViewDescription;
    private TextView mTextViewPredescessor;
    private TextView mTextViewFile;
    private TextView mTextViewNote;
    private TextView mTextViewHistory;
    private TextView mTextViewDescriptionValue;

    private ImageView mImageViewLogo;
    private ImageView mImageViewSendNote;
    private ImageView mImageViewPickImage;
    private TextView mTextViewMore;

    private TextView mTextViewPredecessorNotFound;
    private TextView mTextViewFileNotFound;
    private TextView mTextViewNoteNotFound;
    private TextView mTextViewHistoryNotFound;

    private EditText mEditTextNotes;

    private RelativeLayout mRelativeLayout;

    private ListView mListViewPredescessor;
    private ListView mListViewFiles;
    private LinearLayout mListViewHistory;
    private LinearLayout mListViewNotes;

    private boolean isPredecessor = false;
    private boolean isDescription = false;
    private boolean isFile = false;
    private boolean isNotes = false;
    private boolean isHistory = false;

    private ScrollView mScrollView;

    private PredescessorsAdapter mPredescessorsAdapter;
    private FileAdapter mFileAdapter;
    private HistoryAdapter mHistoryAdapter;
    private NotesAdapter mNotesAdapter;

    private ProjectDetailsDataParser mDataParser;
    private ArrayList<PredescessorsParser> mPredescessorsParsers;
    private ArrayList<NotesParser> mNotesParsersList;
    private ArrayList<HistoryDataParser> mHistoryDataParsers;
    private HistoryParser mHistoryParser;

    private FilesParser mFilesParser;
    private ArrayList<String> mArrayListFiles;

    private String mStringGuID = "";

    private String mStringMessage = "";
    private String mStringStatus = "";
    private String mCurrentMethod = "";
    private String mMethodGetPredescessors = "getPredescessors";
    private String mMethodSetNoteMessage = "setNoteMessage";
    private String mMethodGetHistory = "getHistory";
    private String mMethodSendFile = "sendFile";
    private String mMethodGetFileList = "getFiles";
    private String mMethodGetFileDownload = "getFileDownload";
    private String mMethodInspectAccept = "insepectAccept";
    private String mStringSendFileMessage = "";
    private String mStringFileNameForDownload = "";

    private BackProcessGetDetails mBackProcessGetDetails;

    private Intent mIntentPictureAction = null;
    private Bitmap mBitmap = null;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Dialog mDialog;
    private String mStringFilePath = "";
    private String mStringFileName = "";
    private long mIntTimeStamp = 0;

    public SnaggingTaskDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_snagging_task_details, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_snagging);

        getWidgetRefrence(rootView);
        registerOnClick();

        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mStringGuID = mBundle.getString(getString(R.string.bunble_project_guid));
        }

        mDataParser = new ProjectDetailsDataParser();
        mHistoryParser = new HistoryParser();
        mFilesParser = new FilesParser();

        mBackProcessGetDetails = new BackProcessGetDetails();
        mBackProcessGetDetails.execute(mMethodGetPredescessors);

        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     */
    private void getWidgetRefrence(View v) {

        mImageViewLogo = (ImageView) v.findViewById(R.id.fragment_snagging_task_details_imagview_logo);

        mTextViewTaskName = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_task_name);
        mActivity.getCommonMethod().setTitleFont(mTextViewTaskName);
        mTextViewPlace = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_task_place);
        mTextViewDate = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_task_date);

        mTextViewComplete = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_complete);
        mTextViewRework = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_rework);

        mTextViewDescription = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_description);
        mTextViewPredescessor = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_predescessor);
        mTextViewFile = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_files);
        mTextViewNote = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_notes);
        mTextViewHistory = (TextView) v.findViewById(R.id.fragment_snagging_task_details_textview_history);

        mTextViewDescriptionValue = (TextView) rootView.findViewById(R.id.fragment_snagging_task_details_textview_description_value);
        mListViewPredescessor = (ListView) rootView.findViewById(R.id.fragment_snagging_task_details_listview_predescessor);
        mListViewFiles = (ListView) rootView.findViewById(R.id.fragment_snagging_task_details_listview_files);
        mListViewHistory = (LinearLayout) rootView.findViewById(R.id.fragment_snagging_task_details_linear_history);
        mListViewNotes = (LinearLayout) rootView.findViewById(R.id.fragment_snagging_task_details_linear_notes);

        mTextViewMore = (TextView) v.findViewById(R.id.fragment_snagging_task_details_imageview_more);

        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.fragment_snagging_task_details_relative);

        mImageViewSendNote = (ImageView) v.findViewById(R.id.view_add_note_imageview_send);
        mImageViewPickImage = (ImageView) v.findViewById(R.id.view_add_note_imageview_camera);
        mEditTextNotes = (EditText) v.findViewById(R.id.view_add_note_edittex_text);

        mScrollView = (ScrollView) rootView.findViewById(R.id.fragment_snagging_task_details_scrollview);

        mTextViewPredecessorNotFound = (TextView) rootView.findViewById(R.id.fragment_snagging_task_details_textview_predescessor_not_found);
        mTextViewFileNotFound = (TextView) rootView.findViewById(R.id.fragment_snagging_task_details_textview_file_not_found);
        mTextViewNoteNotFound = (TextView) rootView.findViewById(R.id.fragment_snagging_task_details_textview_notes_not_found);
        mTextViewHistoryNotFound = (TextView) rootView.findViewById(R.id.fragment_snagging_task_details_textview_history_not_found);

    }

    /**
     * Method call will Register OnClick() Events for widgets.
     */
    private void registerOnClick() {
        mTextViewComplete.setOnClickListener(this);
        mTextViewRework.setOnClickListener(this);
        mTextViewDescription.setOnClickListener(this);
        mTextViewPredescessor.setOnClickListener(this);
        mTextViewFile.setOnClickListener(this);
        mTextViewNote.setOnClickListener(this);
        mTextViewHistory.setOnClickListener(this);
        mTextViewMore.setOnClickListener(this);
        mImageViewPickImage.setOnClickListener(this);
        mImageViewSendNote.setOnClickListener(this);
        mListViewFiles.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                mStringFileNameForDownload = mArrayListFiles.get(pos);
                File mFile = new File(mActivity.getCommonMethod().getApplicationDirectory(), mStringFileNameForDownload);

                if (mFile.exists()) {
                    openFileUsingIntent(mFile.getAbsolutePath());
                } else {
                    mActivity.getCommonMethod().showDeleteAlert(mActivity, getString(R.string.app_name),
                            getString(R.string.alt_download_file), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mBackProcessGetDetails = new BackProcessGetDetails();
                                    mBackProcessGetDetails.execute(mMethodGetFileDownload);
                                }
                            });
                }

                // Toast.makeText(mActivity, mArrayListFiles.get(pos),
                // Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Method call OnClick Event fire.
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewComplete) {

            mBackProcessGetDetails = new BackProcessGetDetails();
            mBackProcessGetDetails.execute(mMethodInspectAccept);

        } else if (v == mTextViewRework) {

            Bundle mBundle = new Bundle();
            mBundle.putString(getString(R.string.bunble_project_guid), mStringGuID);
            SnaggingReworkTaskFragment mSnaggingReworkTaskFragment = new SnaggingReworkTaskFragment();
            mSnaggingReworkTaskFragment.setArguments(mBundle);
            mActivity.replaceFragment(mSnaggingReworkTaskFragment, true);

        } else if (v == mTextViewDescription) {

            isDescription = !isDescription;
            if (isDescription) {
                hideButtons();
                mTextViewDescriptionValue.setVisibility(View.VISIBLE);
                mTextViewDescription.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.icon_expand_on), null);
                mTextViewDescription.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
                mTextViewDescription.setTextColor(getResources().getColor(R.color.white));
            } else {
                hideButtons();
            }

        } else if (v == mTextViewPredescessor) {

            Helper.getListViewSize(mListViewPredescessor);
            isPredecessor = !isPredecessor;
            if (isPredecessor) {
                hideButtons();
                mTextViewPredescessor.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(R.drawable.icon_expand_on), null);

                if (mListViewPredescessor.getCount() > 0)
                    mListViewPredescessor.setVisibility(View.VISIBLE);
                else
                    mTextViewPredecessorNotFound.setVisibility(View.VISIBLE);

                mTextViewPredescessor.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
                mTextViewPredescessor.setTextColor(getResources().getColor(R.color.white));
            } else {
                hideButtons();
            }

        } else if (v == mTextViewFile) {

            Helper.getListViewSize(mListViewFiles);
            isFile = !isFile;
            if (isFile) {
                mBackProcessGetDetails = new BackProcessGetDetails();
                mBackProcessGetDetails.execute(mMethodGetFileList);

                hideButtons();
                mTextViewFile.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_expand_on),
                        null);
                mListViewFiles.setVisibility(View.VISIBLE);

                mTextViewFile.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
                mTextViewFile.setTextColor(getResources().getColor(R.color.white));
            } else {
                hideButtons();
            }

        } else if (v == mTextViewNote) {

            isNotes = !isNotes;
            if (isNotes) {
                if (mDataParser.getData().size() > 0) {
                    if (mDataParser.getData().get(0).getMessage().length() > 0) {

                        hideButtons();
                        mTextViewNote.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.drawable.icon_expand_on), null);
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        mTextViewNote.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
                        mTextViewNote.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        mRelativeLayout.setVisibility(View.GONE);
                        mTextViewNoteNotFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    mRelativeLayout.setVisibility(View.GONE);
                    mTextViewNoteNotFound.setVisibility(View.VISIBLE);
                }
            } else {
                hideButtons();
            }

        } else if (v == mTextViewHistory) {

            isHistory = !isHistory;
            if (isHistory) {

                mBackProcessGetDetails = new BackProcessGetDetails();
                mBackProcessGetDetails.execute(mMethodGetHistory);

                hideButtons();
                mTextViewHistory.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_expand_on),
                        null);
                mTextViewHistory.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
                mTextViewHistory.setTextColor(getResources().getColor(R.color.white));
                mListViewHistory.setVisibility(View.VISIBLE);
            } else {
                hideButtons();
            }

        } else if (v == mImageViewSendNote) {

            mStringMessage = mEditTextNotes.getText().toString().trim();
            if (mStringMessage.length() > 0) {

                mBackProcessGetDetails = new BackProcessGetDetails();
                mBackProcessGetDetails.execute(mMethodSetNoteMessage);
            }
        } else if (v == mImageViewPickImage) {

            selectPhotoDialog();

        } else if (v == mTextViewMore) {

            Intent mIntent = new Intent(mActivity, NoteActivity.class);
            mIntent.putExtra(getString(R.string.bunble_project_guid), mStringGuID);
            startActivity(mIntent);
        }
    }

    /**
     * Method call will check ListView visibility.
     *
     * @return boolean
     */
    private void hideButtons() {

        mTextViewDescriptionValue.setVisibility(View.GONE);
        mListViewPredescessor.setVisibility(View.GONE);
        mTextViewPredecessorNotFound.setVisibility(View.GONE);
        mListViewFiles.setVisibility(View.GONE);
        mTextViewFileNotFound.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.GONE);
        mTextViewNoteNotFound.setVisibility(View.GONE);
        mListViewHistory.setVisibility(View.GONE);
        mTextViewHistoryNotFound.setVisibility(View.GONE);
        mTextViewDescription.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_list_arrow),
                null);
        mTextViewPredescessor.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_list_arrow),
                null);
        mTextViewFile.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_list_arrow), null);
        mTextViewNote.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_list_arrow), null);
        mTextViewHistory.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_list_arrow), null);
        mTextViewDescription.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
        mTextViewPredescessor.setBackgroundColor(getResources().getColor(R.color.white));
        mTextViewFile.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
        mTextViewNote.setBackgroundColor(getResources().getColor(R.color.white));
        mTextViewHistory.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
        mTextViewDescription.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewPredescessor.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewFile.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewNote.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewHistory.setTextColor(getResources().getColor(R.color.bg_bottom_tab_bar));

    }

    /**
     * Method call will set Data from previous screen.
     */
    private void setData() {

        int drawableResourceId = this.getResources().getIdentifier("icon_snagging_task_" + mDataParser.getStatus(), "drawable",
                mActivity.getPackageName());
        mImageViewLogo.setImageResource(drawableResourceId);

        mTextViewTaskName.setText(mDataParser.getName());

        if (mDataParser.getData() != null && mDataParser.getData().size() > 0) {
            mNotesParsersList = new ArrayList<NotesParser>();
            mNotesParsersList.addAll(mDataParser.getData());
            //mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList, false);
            //mListViewNotes.setAdapter(mNotesAdapter);
            //Helper.getListViewSize(mListViewNotes);
            LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
            mLoadNotesAdapter.loadHistoryData(mListViewNotes);

        }

        try {
            if (mDataParser.getActivityLocation() != null && mDataParser.getActivityLocation().length() > 0) {
                mTextViewPlace.setText(mDataParser.getActivityLocation());
            } else {
                mTextViewPlace.setVisibility(View.GONE);
            }
        } catch (Exception e) {
//			e.printStackTrace();
        }

        try {
            Calendar mCalendarDuteDate = Calendar.getInstance();
            mCalendarDuteDate.setTimeInMillis(Long.parseLong(mDataParser.getEndDate()));
            mTextViewDate.setText(getString(R.string.lbl_markeed_complete) + " "
                    + mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendarDuteDate, "dd MMM yyyy"));
        } catch (Exception e) {
//			e.printStackTrace();
        }

        try {
            if (mDataParser.getDescription() != null && mDataParser.getDescription().length() > 0) {
                mTextViewDescriptionValue.setText(mDataParser.getDescription());
            } else {
                mTextViewDescriptionValue.setText(getString(R.string.validation_discription_not_foune));
            }
        } catch (Exception e) {
//			e.printStackTrace();
        }

    }

    /**
     * Method call will get image from gallary or camera.
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
                            mActivity.getCommonMethod().copyImageFile(
                                    mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);
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
                mActivity.getCommonMethod().copyImageFile(
                        mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);

                mBackProcessGetDetails = new BackProcessGetDetails();
                mBackProcessGetDetails.execute(mMethodSendFile);

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
    public class BackProcessGetDetails extends AsyncTask<String, Void, String> {
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

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().getPredescessorsAPI(
                        mActivity.getMyApplication().getUserToken(), mStringGuID, mDataParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().setNotesAPI(
                        mActivity.getMyApplication().getUserToken(), mStringGuID, mActivity.getMyApplication().getUserID(), mStringMessage,
                        mDataParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

                mStringSendFileMessage = (String) mActivity.getCommonMethod().sendFileAPI(mActivity.getMyApplication().getUserToken(),
                        mStringFileName, mStringFilePath, mStringGuID, mStringSendFileMessage);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetHistory)) {

                mHistoryParser = (HistoryParser) mActivity.getCommonMethod().getHistoryAPI(mActivity.getMyApplication().getUserToken(),
                        mStringGuID, mHistoryParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetFileList)) {

                mFilesParser = (FilesParser) mActivity.getCommonMethod().getFileListAPI(mActivity.getMyApplication().getUserToken(),
                        mStringGuID, mFilesParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetFileDownload)) {

                responseData = (String) mActivity.getCommonMethod().getActivityFileDownloadAPI(mActivity.getMyApplication().getUserToken(),
                        mStringGuID, mStringFileNameForDownload);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodInspectAccept)) {

                mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().setInspectAcceptAPI(
                        mActivity.getMyApplication().getUserToken(), mStringGuID, mDataParser);
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

                // mActivity.getCommonMethod().showDialog("",
                // getString(R.string.validation_failed), true);
                mActivity.getCommonMethod().sessionOut();

            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethodGetPredescessors)) {

                        if (!mDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParser != null) {

                                mPredescessorsParsers = new ArrayList<PredescessorsParser>();
                                for (int i = 0; i < mDataParser.getConstrainingActivityRelations().size(); i++) {

                                    mPredescessorsParsers.add(mDataParser.getConstrainingActivityRelations().get(i).getStart());
                                }

                                mPredescessorsAdapter = new PredescessorsAdapter(mActivity, mPredescessorsParsers);
                                mListViewPredescessor.setAdapter(mPredescessorsAdapter);

                                mActivity.getCommonMethod().writeToFile("");
                                setData();

                                mNotesParsersList = new ArrayList<NotesParser>();
                                mNotesParsersList.addAll(mDataParser.getData());
                                //mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList, false);
                                //mListViewNotes.setAdapter(mNotesAdapter);
                                //mActivity.getCommonMethod().HideKeyboard(mEditTextNotes);
                                //Helper.getListViewSize(mListViewNotes);
                                LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
                                mLoadNotesAdapter.loadHistoryData(mListViewNotes);

                            } else {
//							mActivity.getCommonMethod().showDialog(getString(R.string.app_name),
//									getString(R.string.alt_predescessors_data_not_founds), false);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParser.getMessage());
                        }
                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

                        mEditTextNotes.setText("");
                        hideButtons();

                        mNotesParsersList = new ArrayList<NotesParser>();
                        mNotesParsersList.addAll(mDataParser.getData());

                        //mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList, false);
                        //mListViewNotes.setAdapter(mNotesAdapter);
                        //mActivity.getCommonMethod().HideKeyboard(mEditTextNotes);
                        //Helper.getListViewSize(mListViewNotes);
                        LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
                        mLoadNotesAdapter.loadHistoryData(mListViewNotes);

                        mActivity.getCommonMethod().HideKeyboard(mEditTextNotes);

                        setData();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetHistory)) {

                        if (!mHistoryParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mHistoryParser.getData().size() > 0) {

                                mHistoryDataParsers = new ArrayList<HistoryDataParser>();
                                mHistoryDataParsers.addAll(mHistoryParser.getData());
                                LoadHistoryData mLoadHistoryData = new LoadHistoryData(mActivity, mHistoryDataParsers);
                                mLoadHistoryData.loadHistoryData(mListViewHistory);
                                mActivity.getCommonMethod().setScrollDown(mScrollView);

                            } else {

                                mListViewHistory.setVisibility(View.GONE);
                                mTextViewHistoryNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mHistoryParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

                        hideButtons();
                        Toast.makeText(mActivity, mStringSendFileMessage, Toast.LENGTH_SHORT).show();

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetFileList)) {

                        if (!mFilesParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mFilesParser.getData().size() > 0) {
                                mArrayListFiles = new ArrayList<String>();
                                for (int i = 0; i < mFilesParser.getData().size(); i++) {
                                    mArrayListFiles.add(mFilesParser.getData().get(i).toString());
                                }
                                mFileAdapter = new FileAdapter(mActivity, mArrayListFiles);
                                mListViewFiles.setAdapter(mFileAdapter);
                                Helper.getListViewSize(mListViewFiles);
                                mListViewFiles.setSelection(mArrayListFiles.size());
                                mActivity.getCommonMethod().setScrollDown(mScrollView);

                            } else {

                                mListViewFiles.setVisibility(View.GONE);
                                mTextViewFileNotFound.setVisibility(View.VISIBLE);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mFilesParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetFileDownload)) {
                        if (responseData.length() > 0)
                            openFileUsingIntent(responseData);

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodInspectAccept)) {

                        if (!mDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParser != null && mDataParser.getGuid().length() > 0) {
                                mDialog.cancel();
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

    /**
     * Method will open file
     */
    private void openFileUsingIntent(String path) {
        File mFile = new File(path);
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(mActivity.getCommonMethod().fileExt(path).substring(1));
        newIntent.setDataAndType(Uri.fromFile(mFile), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }
}
