package com.construction.android.fragments;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.construction.android.parser.ProjectListDataParser;
import com.construction.android.parser.ProjectsParser;
import com.construction.android.utils.CustomDatePickerFragment;
import com.construction.android.utils.InterfaceDatePicker;
import com.construction.android.utils.StaticData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskCardFragment extends Fragment implements OnClickListener {

    private MyFragmentActivity mActivity;
    private ProgressDialog mProgressDialog;

    private TextView mTextViewTaskName;
    private TextView mTextViewPlace;
    private TextView mTextViewDate;
    private TextView mTextViewStart;
    private TextView mTextViewAlert;
    private TextView mTextViewStop;
    private TextView mTextViewComplete;
    private TextView mTextViewDescription;
    private TextView mTextViewPredescessor;
    private TextView mTextViewFile;
    private TextView mTextViewNote;
    private TextView mTextViewHistory;
    private TextView mTextViewDescriptionValue;
    private TextView mTextViewPredescessorNoData;
    private TextView mTextViewFileNoData;
    private TextView mTextViewNoteNoData;
    private TextView mTextViewHistoryNoData;

    private TextView mTextViewPredecessorNotFound;
    private TextView mTextViewFileNotFound;
    private TextView mTextViewNoteNotFound;
    private TextView mTextViewHistoryNotFound;

    private boolean isPredecessors = false;
    private boolean isDescription = false;
    private boolean isFile = false;
    private boolean isNotes = false;
    private boolean isHistory = false;

    private ImageView mImageViewLogo;
    private ImageView mImageViewSendNote;
    private ImageView mImageViewPickImage;
    private TextView mTextViewMore;

    private EditText mEditTextNotes;

    private RelativeLayout mRelativeLayout;

    private ListView mListViewPredescessor;
    private ListView mListViewFiles;
    private LinearLayout mListViewHistory;
    private LinearLayout mListViewNotes;

    private ScrollView mScrollView;

    private PredescessorsAdapter mPredescessorsAdapter;
    private FileAdapter mFileAdapter;
    private HistoryAdapter mHistoryAdapter;
    private NotesAdapter mNotesAdapter;

    private ProjectDetailsDataParser mDataParser;
    private ProjectDetailsDataParser mDataParserActions;
    private ArrayList<PredescessorsParser> mPredescessorsParsers;
    private ArrayList<HistoryDataParser> mHistoryDataParsers;
    private HistoryParser mHistoryParser;

    private FilesParser mFilesParser;
    private ArrayList<String> mArrayListFiles;
    private ArrayList<NotesParser> mNotesParsersList;
    private String mStringGuID = "";
    private boolean isTaskProgressScreen = false;

    private String mStringMessage = "";
    private String mStringStatus = "";
    private String mCurrentMethod = "";
    private String mMethodGetPredescessors = "getPredescessors";
    private String mMethodSetNoteMessage = "setNoteMessage";
    private String mMethodSetActionStart = "setActionStart";
    private String mMethodSetActionWarn = "setActionWarn";
    private String mMethodSetActionStop = "setActionStop";
    private String mMethodGetApproveActors = "getApproveActors";
    private String mMethodSetActionComplete = "setActionComplete";
    private String mMethodGetHistory = "getHistory";
    private String mMethodSendFile = "sendFile";
    private String mMethodGetFileList = "getFiles";
    private String mMethodGetFileDownload = "getFileDownload";

    private ArrayList<String> mListResons;
    private String mStringReason = "";
    private String mStringComments = "";
    private String mStringApproveGUID = "";
    private String mStringStatusChangeDate = "";
    private String mStringSendFileMessage = "";
    private BackProcessGetDetails mBackProcessGetDetails;
    private String mStringFileNameForDownload = "";

    private String mStringStartDate = "";
    private String mStringEndDate = "";
    private boolean isMaterial = false;
    private boolean isEquipment = false;
    private boolean isWork = false;
    private boolean isSpace = false;
    private boolean isDesing = false;
    private boolean isPredecessor = false;
    private boolean isExternal = false;
    private boolean isPredecessorCompleted = true;

    private Intent mIntentPictureAction = null;
    private Bitmap mBitmap = null;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Dialog mDialog;
    private String mStringFilePath = "";
    private String mStringFileName = "";
    private long mIntTimeStamp = 0;

    private ActoreListAdapter mActoreListAdapter;
    private ProjectsParser mProjectsParser;
    private List<ProjectListDataParser> mPackageListDataParsers;
    private int mIntCurrentPosition = -1;
    private ListView mListViewActores;

    public TaskCardFragment() {

    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task_card, container, false);

        mActivity = (MyFragmentActivity) getActivity();
        mActivity.setTitle(R.string.lbl_task_card);

        getWidgetRefrence(rootView);
        registerEvents();

        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mStringGuID = mBundle.getString(getString(R.string.bunble_project_guid));
            isTaskProgressScreen = mBundle.getBoolean(getString(R.string.bunble_is_task_in_progress_screen));
        }

        mIntTimeStamp = (long) System.currentTimeMillis();

        mDataParser = new ProjectDetailsDataParser();
        mDataParserActions = new ProjectDetailsDataParser();
        mHistoryParser = new HistoryParser();
        mFilesParser = new FilesParser();
        mProjectsParser = new ProjectsParser();

        mBackProcessGetDetails = new BackProcessGetDetails();
        mBackProcessGetDetails.execute(mMethodGetPredescessors);

        return rootView;
    }

    /**
     * Method call will get IDs from xml file.
     *
     * @param v
     */
    public void getWidgetRefrence(View v) {

        mImageViewLogo = (ImageView) v.findViewById(R.id.fragment_task_card_imagview_logo);

        mTextViewTaskName = (TextView) v.findViewById(R.id.fragment_task_card_textview_task_name);
        mActivity.getCommonMethod().setTitleFont(mTextViewTaskName);
        mTextViewPlace = (TextView) v.findViewById(R.id.fragment_task_card_textview_task_place);
        mTextViewDate = (TextView) v.findViewById(R.id.fragment_task_card_textview_task_date);
        mTextViewStart = (TextView) v.findViewById(R.id.fragment_task_card_textview_start);
        mTextViewAlert = (TextView) v.findViewById(R.id.fragment_task_card_textview_alert);
        mTextViewStop = (TextView) v.findViewById(R.id.fragment_task_card_textview_stop);
        mTextViewComplete = (TextView) v.findViewById(R.id.fragment_task_card_textview_complete);
        mTextViewDescription = (TextView) v.findViewById(R.id.fragment_task_card_textview_description);
        mTextViewPredescessor = (TextView) v.findViewById(R.id.fragment_task_card_textview_predescessor);
        mTextViewFile = (TextView) v.findViewById(R.id.fragment_task_card_textview_files);
        mTextViewNote = (TextView) v.findViewById(R.id.fragment_task_card_textview_notes);
        mTextViewHistory = (TextView) v.findViewById(R.id.fragment_task_card_textview_history);

        mTextViewPredescessorNoData = (TextView) v.findViewById(R.id.fragment_task_card_textview_predescessor_no_data);
        mTextViewFileNoData = (TextView) v.findViewById(R.id.fragment_task_card_textview_files_no_data);
        mTextViewNoteNoData = (TextView) v.findViewById(R.id.fragment_task_card_textview_notes_no_data);
        mTextViewHistoryNoData = (TextView) v.findViewById(R.id.fragment_task_card_textview_history_no_data);

        mTextViewMore = (TextView) v.findViewById(R.id.fragment_task_details_imageview_more);
        mRelativeLayout = (RelativeLayout) v.findViewById(R.id.fragment_task_details_relative);

        mImageViewSendNote = (ImageView) v.findViewById(R.id.view_add_note_imageview_send);
        mImageViewPickImage = (ImageView) v.findViewById(R.id.view_add_note_imageview_camera);
        mEditTextNotes = (EditText) v.findViewById(R.id.view_add_note_edittex_text);

        mTextViewDescriptionValue = (TextView) rootView.findViewById(R.id.fragment_task_card_textview_description_value);
        mListViewPredescessor = (ListView) rootView.findViewById(R.id.fragment_task_card_listview_predescessor);
        mListViewFiles = (ListView) rootView.findViewById(R.id.fragment_task_card_listview_files);
        mListViewHistory = (LinearLayout) rootView.findViewById(R.id.fragment_task_card_linear_history);
        mListViewNotes = (LinearLayout) rootView.findViewById(R.id.fragment_task_card_linear_notes);

        mScrollView = (ScrollView) rootView.findViewById(R.id.fragment_task_card_scrollview);

        mTextViewPredecessorNotFound = (TextView) rootView.findViewById(R.id.fragment_task_card_textview_predescessor_no_data);
        mTextViewFileNotFound = (TextView) rootView.findViewById(R.id.fragment_task_card_textview_files_no_data);
        mTextViewNoteNotFound = (TextView) rootView.findViewById(R.id.fragment_task_card_textview_notes_no_data);
        mTextViewHistoryNotFound = (TextView) rootView.findViewById(R.id.fragment_task_card_textview_history_no_data);

    }

    /**
     * Method call will register onClick() events.
     */
    public void registerEvents() {
        mTextViewStart.setOnClickListener(this);
        mTextViewAlert.setOnClickListener(this);
        mTextViewStop.setOnClickListener(this);
        mTextViewComplete.setOnClickListener(this);
        mTextViewDescription.setOnClickListener(this);
        mTextViewPredescessor.setOnClickListener(this);
        mTextViewFile.setOnClickListener(this);
        mTextViewNote.setOnClickListener(this);
        mTextViewMore.setOnClickListener(this);
        mTextViewHistory.setOnClickListener(this);

        mImageViewSendNote.setOnClickListener(this);
        mImageViewPickImage.setOnClickListener(this);

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

            }
        });
    }

    /**
     * Method call will fire onClick Events.
     */
    @Override
    public void onClick(View v) {
        if (v == mTextViewDescription) {

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
            isPredecessors = !isPredecessors;
            if (isPredecessors) {
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
                if (mDataParser.getData() != null && mDataParser.getData().size() > 0) {
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

        } else if (v == mTextViewStart) {

//			if (isPredecessorCompleted) {
            setActionDialog(true, mMethodSetActionStart);
//			} else {
//				mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name),
//						getString(R.string.alt_predescessor_not_complete));
//			}

        } else if (v == mTextViewAlert) {

//			if (isPredecessorCompleted) {
            setActionDialog(false, mMethodSetActionWarn);
//			} else {
//				mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name),
//						getString(R.string.alt_predescessor_not_complete));
//			}

        } else if (v == mTextViewStop) {

//			if (isPredecessorCompleted) {
            setActionDialog(false, mMethodSetActionStop);
//			} else {
//				mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name),
//						getString(R.string.alt_predescessor_not_complete));
//			}

        } else if (v == mTextViewComplete) {

            //if (isPredecessorCompleted) {
            if (mDataParser.getStatus().equalsIgnoreCase("6")) {

                mActivity.getCommonMethod()
                        .showAlert(mActivity, getString(R.string.app_name), getString(R.string.alt_start_task_first));

            } else {
                getActoreDialog();
            }
//			} else {
//				mActivity.getCommonMethod().showAlert(mActivity, getString(R.string.app_name),
//						getString(R.string.alt_predescessor_not_complete));
//			}

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
     * Method call will set Data from previous screen.
     */
    private void setData() {

        if (isTaskProgressScreen) {
            int drawableResourceId = this.getResources().getIdentifier("icon_task_in_progress_" + mDataParser.getStatus(), "drawable",
                    mActivity.getPackageName());
            mImageViewLogo.setImageResource(drawableResourceId);
            mImageViewLogo.setBackgroundResource(R.drawable.icon_tasks_in_progres);

        } else {

            int drawableResourceId = this.getResources().getIdentifier("icon_reassignment_task_" + mDataParser.getStatus(), "drawable",
                    mActivity.getPackageName());
            mImageViewLogo.setImageResource(drawableResourceId);
            mImageViewLogo.setBackgroundResource(R.drawable.icon_reassignments);
        }

        mTextViewTaskName.setText(mDataParser.getName());

        if (mDataParser.getData() != null && mDataParser.getData().size() > 0) {
            mNotesParsersList = new ArrayList<NotesParser>();
            mNotesParsersList.addAll(mDataParser.getData());
            //mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList, false);

            LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
            mLoadNotesAdapter.loadHistoryData(mListViewNotes);
            //mListViewNotes.setAdapter(mNotesAdapter);
            //Helper.getListViewSize(mListViewNotes);

        }

        try {
            if (mDataParser.getData() != null && mDataParser.getActivityLocation().length() > 0) {
                mTextViewPlace.setText(mDataParser.getActivityLocation());
            } else {
                mTextViewPlace.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        try {
            Calendar mCalendarDuteDate = Calendar.getInstance();
            mCalendarDuteDate.setTimeInMillis(Long.parseLong(mDataParser.getStartDate()));
            mStringStartDate = mDataParser.getStartDate();
            mStringEndDate = mDataParser.getEndDate();
            mTextViewDate.setText(getString(R.string.lbl_due) + ": " + mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendarDuteDate, "dd MMM yyyy"));
        } catch (Exception e1) {
            // e1.printStackTrace();
        }

        try {
            if (mDataParser.getDescription() != null && mDataParser.getDescription().length() > 0) {
                mTextViewDescriptionValue.setText(mDataParser.getDescription());
            } else {
                mTextViewDescriptionValue.setText(getString(R.string.validation_discription_not_foune));
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

        // Set Status Button
        if (mDataParser.getStatus().equalsIgnoreCase("4")) {

            setTabSelection();
            mTextViewStart.setEnabled(false);
            mTextViewStart.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));

        } else if (mDataParser.getStatus().equalsIgnoreCase("5")) {

            setTabSelection();
            mTextViewAlert.setEnabled(false);
            mTextViewAlert.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));

        } else if (mDataParser.getStatus().equalsIgnoreCase("6")) {

            setTabSelection();
            mTextViewStop.setEnabled(false);
            mTextViewStop.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));

        } else if (mDataParser.getStatus().equalsIgnoreCase("7")) {

            setTabSelection();
            mTextViewStart.setEnabled(false);
            mTextViewAlert.setEnabled(false);
            mTextViewStop.setEnabled(false);
            mTextViewComplete.setEnabled(false);
            mTextViewComplete.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));

        }
    }

    /**
     * Method call will check ListView visibility.
     *
     * @return
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
     * Method call will set Tab selection.
     */
    private void setTabSelection() {
        mTextViewStart.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewAlert.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewStop.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewComplete.setBackgroundColor(getResources().getColor(R.color.bg_bottom_tab_bar));
        mTextViewStart.setEnabled(true);
        mTextViewAlert.setEnabled(true);
        mTextViewStop.setEnabled(true);
        mTextViewComplete.setEnabled(true);
    }

    /**
     * Method call will set Tab De-selection.
     */
    private void setDisableAllTab() {
        mTextViewStart.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));
        mTextViewAlert.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));
        mTextViewStop.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));
        mTextViewComplete.setBackgroundColor(getResources().getColor(R.color.task_card_button_selector_bg));
        mTextViewStart.setEnabled(false);
        mTextViewAlert.setEnabled(false);
        mTextViewStop.setEnabled(false);
        mTextViewComplete.setEnabled(false);
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

    /**
     * Method will compare selected date with activity date
     *
     * @param currentDate
     * @param activityLongDate
     * @return boolean
     */
    public boolean checkSelectedDateWithActivityDate(String currentDate, String activityLongDate) {
        String mDateLong = mActivity.getCommonMethod().getDateInFormateReverse(currentDate, StaticData.DATE_FORMAT_2,
                StaticData.DATE_FORMAT_4);
        long selectedDateValue = Long.parseLong(mDateLong);
        Calendar mCalendar = Calendar.getInstance();
        try {
            long activityLongTime = Long.parseLong(activityLongDate);
            mCalendar.setTimeInMillis(activityLongTime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        long activityDateValue = Long.parseLong(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, StaticData.DATE_FORMAT_4));
        if (selectedDateValue > activityDateValue)
            return true;
        return false;
    }

    /**
     * Method call will show dialog for send comment whenclick Alert or Stop.
     */
    private void setActionDialog(final boolean needDatePicker, final String mStringMethod) {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_set_action_task);
        Window window = mDialog.getWindow();
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final TextView mTextViewDate = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_date);
        final EditText mEditTextComments = (EditText) mDialog.findViewById(R.id.dialog_set_action_task_edittext_message);
        final TextView mTextViewMaterial = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_material);
        final TextView mTextViewEquipment = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_equipment);
        final TextView mTextViewWorkers = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_workers);
        final TextView mTextViewSpace = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_space);
        final TextView mTextViewDesing = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_design);
        final TextView mTextViewPredecessor = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_predecessor);
        final TextView mTextViewExternal = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_external);
        final TextView mTextViewSend = (TextView) mDialog.findViewById(R.id.dialog_set_action_task_textview_send);
        final LinearLayout mLinearLayoutComment = (LinearLayout) mDialog.findViewById(R.id.dialog_set_action_task_linear_message);

        isMaterial = false;
        isEquipment = false;
        isWork = false;
        isSpace = false;
        isDesing = false;
        isPredecessor = false;
        isExternal = false;
        mEditTextComments.setText("");

        mStringStatusChangeDate = String.valueOf(Calendar.getInstance().getTimeInMillis());
        mTextViewDate.setText(mActivity.getCommonMethod().getCurrentDate());

        if (needDatePicker)
            mTextViewDate.setVisibility(View.VISIBLE);
        else
            mTextViewDate.setVisibility(View.GONE);

        String dateValue = "";
        if (mStringMethod.equalsIgnoreCase(mMethodSetActionStart))
            dateValue = mStringStartDate;
        else
            dateValue = mStringEndDate;

        if (mStringMethod.equalsIgnoreCase(mMethodSetActionStart) || mStringMethod.equalsIgnoreCase(mMethodSetActionComplete)) {
            if (checkSelectedDateWithActivityDate(mTextViewDate.getText().toString(), dateValue)) {
                mLinearLayoutComment.setVisibility(View.VISIBLE);
            } else {
                mLinearLayoutComment.setVisibility(View.GONE);
            }

        } else {
            mLinearLayoutComment.setVisibility(View.VISIBLE);
        }

        mTextViewDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new CustomDatePickerFragment(new InterfaceDatePicker() {

                    @Override
                    public void onDateSelected(int year, int month, int day) {

                        final Calendar c = Calendar.getInstance();
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.DAY_OF_MONTH, day);

                        mStringStatusChangeDate = String.valueOf(c.getTimeInMillis());
                        String date = mActivity.getCommonMethod().getDateFormatFromCalendar(c, StaticData.DATE_FORMAT_2);
                        mTextViewDate.setText(date);
                        String dateValue = "";
                        if (mStringMethod.equalsIgnoreCase(mMethodSetActionStart))
                            dateValue = mStringStartDate;
                        else
                            dateValue = mStringEndDate;

                        if (checkSelectedDateWithActivityDate(date, dateValue)) {
                            mLinearLayoutComment.setVisibility(View.VISIBLE);
                        } else {
                            mLinearLayoutComment.setVisibility(View.GONE);
                            isMaterial = false;
                            isEquipment = false;
                            isWork = false;
                            isSpace = false;
                            isDesing = false;
                            mEditTextComments.setText("");
                        }
                    }
                }, mActivity);
            }
        });

        mTextViewMaterial.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isMaterial = !isMaterial;
                if (isMaterial) {
                    mTextViewMaterial.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewMaterial.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }

            }
        });

        mTextViewEquipment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                isEquipment = !isEquipment;
                if (isEquipment) {
                    mTextViewEquipment.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));

                } else {
                    mTextViewEquipment.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }

            }
        });

        mTextViewWorkers.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isWork = !isWork;
                if (isWork) {
                    mTextViewWorkers.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewWorkers.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));

                }
            }
        });

        mTextViewSpace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isSpace = !isSpace;
                if (isSpace) {
                    mTextViewSpace.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewSpace.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }

            }
        });

        mTextViewDesing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isDesing = !isDesing;
                if (isDesing) {
                    mTextViewDesing.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewDesing.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }
            }
        });

        mTextViewPredecessor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isPredecessor = !isPredecessor;
                if (isPredecessor) {
                    mTextViewPredecessor.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewPredecessor.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }
            }
        });

        mTextViewExternal.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isExternal = !isExternal;
                if (isExternal) {
                    mTextViewExternal.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_on));
                } else {
                    mTextViewExternal.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                            getResources().getDrawable(R.drawable.make_ready_tasks_screen_checkbox_off));
                }
            }
        });

        mTextViewSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mBackProcessGetDetails = new BackProcessGetDetails();

                mStringComments = mEditTextComments.getText().toString().trim();
                createReasonsString();

                if (mLinearLayoutComment.getVisibility() == View.VISIBLE) {
                    // if (mStringComments.length() > 0 && mListResons.size() >
                    // 0) {
                    if (mListResons.size() > 0) {

                        if (mStringMethod.equalsIgnoreCase(mMethodSetActionStart)) {

                            mBackProcessGetDetails.execute(mMethodSetActionStart);

                        } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionWarn)) {

                            mBackProcessGetDetails.execute(mMethodSetActionWarn);

                        } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionStop)) {

                            mBackProcessGetDetails.execute(mMethodSetActionStop);

                        } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionComplete)) {

                            mBackProcessGetDetails.execute(mMethodSetActionComplete);
                        }
                    } else if (mListResons.size() == 0) {
                        mActivity.getCommonMethod().showDialog("", getString(R.string.validation_select_reasons), false);
                    }

                    // else if (mStringComments.length() == 0) {
                    // mActivity.getCommonMethod().showDialog("",
                    // getString(R.string.validation_enter_comments), false);
                    //
                    // }
                } else {
                    // if (mStringComments.length() > 0) {

                    if (mStringMethod.equalsIgnoreCase(mMethodSetActionStart)) {

                        mBackProcessGetDetails.execute(mMethodSetActionStart);

                    } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionWarn)) {

                        mBackProcessGetDetails.execute(mMethodSetActionWarn);

                    } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionStop)) {

                        mBackProcessGetDetails.execute(mMethodSetActionStop);

                    } else if (mStringMethod.equalsIgnoreCase(mMethodSetActionComplete)) {

                        mBackProcessGetDetails.execute(mMethodSetActionComplete);
                    }
                    // }
                }
            }
        });

        mDialog.show();
    }

    /**
     * Method will create reasons string
     */
    public void createReasonsString() {
        mListResons = new ArrayList<String>();
        if (isSpace)
            mListResons.add("0");
        if (isMaterial)
            mListResons.add("1");
        if (isEquipment)
            mListResons.add("2");
        if (isWork)
            mListResons.add("3");
        if (isPredecessor)
            mListResons.add("4");
        if (isDesing)
            mListResons.add("5");
        if (isExternal)
            mListResons.add("6");

        mStringReason = "";
        for (int i = 0; i < mListResons.size(); i++) {
            if (mStringReason.length() == 0)
                mStringReason = mListResons.get(i);
            else
                mStringReason = mStringReason + "," + mListResons.get(i);
        }
    }

    /**
     * Method call will get image from gallary or camera.
     */
    private void selectPhotoDialog() {
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
     * Method call will show dialog for send comment whenclick Alert or Stop.
     */
    private void getActoreDialog() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_projects_list);
        Window window = mDialog.getWindow();
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        final TextView mTextViewTitle = (TextView) mDialog.findViewById(R.id.dialog_project_list_textview_title);
        mTextViewTitle.setText(getString(R.string.lbl_select_actore));
        mListViewActores = (ListView) mDialog.findViewById(R.id.dialog_project_list_projects);
        BackProcessGetDetails mBackProcessGetDetails = new BackProcessGetDetails();
        mBackProcessGetDetails.execute(mMethodGetApproveActors);

        mListViewActores.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                mIntCurrentPosition = position;
                mActoreListAdapter.notifyDataSetChanged();
                mStringApproveGUID = mPackageListDataParsers.get(position).getGuid();

                mDialog.cancel();

                setActionDialog(true, mMethodSetActionComplete);

            }
        });

        mDialog.show();
    }

    /**
     * Set Project list Adapter
     *
     * @author ebaraiya
     */
    public class ActoreListAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mPackageListDataParsers.size();
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.row_notifications, null);

                holder.mTextViewTitle = (TextView) convertView.findViewById(R.id.row_notifications_textview_title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextViewTitle.setText(mPackageListDataParsers.get(position).getName());
            holder.mTextViewTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if (position % 2 == 0) {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.ready_task_txt_bg));
            } else {
                holder.mTextViewTitle.setBackgroundColor(getResources().getColor(R.color.white));
            }
            return convertView;
        }

    }

    public class ViewHolder {
        TextView mTextViewTitle;
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

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionStart)) {

                mDataParserActions = (ProjectDetailsDataParser) mActivity.getCommonMethod().setActionStartAPI(mStringGuID, mStringComments,
                        mListResons, mActivity.getMyApplication().getUserToken(), mStringStatusChangeDate, mDataParserActions);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionWarn)) {

                mDataParserActions = (ProjectDetailsDataParser) mActivity.getCommonMethod().setActionWarnAPI(mStringGuID, mStringComments,
                        mListResons, mActivity.getMyApplication().getUserToken(), mDataParserActions);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionStop)) {

                mDataParserActions = (ProjectDetailsDataParser) mActivity.getCommonMethod().setActionStopAPI(mStringGuID, mStringComments,
                        mListResons, mActivity.getMyApplication().getUserToken(), mDataParserActions);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetApproveActors)) {

                mProjectsParser = (ProjectsParser) mActivity.getCommonMethod().getApproverGuidAPI(
                        mActivity.getMyApplication().getUserToken(), mActivity.getMyApplication().getProjecID(), mProjectsParser);

            } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionComplete)) {

                mDataParserActions = (ProjectDetailsDataParser) mActivity.getCommonMethod().setActionCompleteAPI(mStringGuID,
                        mStringApproveGUID, mStringComments, mListResons, mActivity.getMyApplication().getUserToken(),
                        mStringStatusChangeDate, mDataParserActions);

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
                                isPredecessorCompleted = true;
                                for (int i = 0; i < mDataParser.getConstrainingActivityRelations().size(); i++) {
                                    mPredescessorsParsers.add(mDataParser.getConstrainingActivityRelations().get(i).getStart());
                                    if (!mDataParser.getConstrainingActivityRelations().get(i).getStart().getStatus().equalsIgnoreCase("7")) {
                                        isPredecessorCompleted = false;
                                    }
                                }

                                mPredescessorsAdapter = new PredescessorsAdapter(mActivity, mPredescessorsParsers);
                                mListViewPredescessor.setAdapter(mPredescessorsAdapter);
                                Helper.getListViewSize(mListViewPredescessor);

                                mActivity.getCommonMethod().writeToFile("");
                                setData();

                                mNotesParsersList = new ArrayList<NotesParser>();
                                mNotesParsersList.addAll(mDataParser.getData());
                                LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
                                mLoadNotesAdapter.loadHistoryData(mListViewNotes);

                            } else {
                                mActivity.getCommonMethod().showDialog(getString(R.string.app_name),
                                        getString(R.string.alt_predescessors_data_not_founds), true);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParser.getMessage());
                        }
                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {


                        if (!mDataParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            mEditTextNotes.setText("");
                            hideButtons();
                            mNotesParsersList = new ArrayList<NotesParser>();
                            mNotesParsersList.addAll(mDataParser.getData());

                            setData();
                            LoadNotesAdapter mLoadNotesAdapter = new LoadNotesAdapter(mActivity, mNotesParsersList, false);
                            mLoadNotesAdapter.loadHistoryData(mListViewNotes);

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParser.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionStart)) {

                        if (!mDataParserActions.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParserActions != null && mDataParserActions.getGuid().length() > 0) {
                                mDataParser = mDataParserActions;
                                mDialog.cancel();
                                setData();

                            } else {
                                mActivity.getCommonMethod().showDialog("", mDataParserActions.getError(), false);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParserActions.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionWarn)) {
                        if (!mDataParserActions.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParserActions != null && mDataParserActions.getGuid().length() > 0) {
                                mDataParser = mDataParserActions;
                                mDialog.cancel();
                                setData();
                            } else {
                                mActivity.getCommonMethod().showDialog("", mDataParserActions.getError(), false);
                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParserActions.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionStop)) {

                        if (!mDataParserActions.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParserActions != null && mDataParserActions.getGuid().length() > 0) {
                                mDataParser = mDataParserActions;
                                mDialog.cancel();
                                setData();
                            } else {
                                mActivity.getCommonMethod().showDialog("", mDataParserActions.getError(), false);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParserActions.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetApproveActors)) {

                        if (!mProjectsParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mProjectsParser.getData().size() > 0) {

                                mPackageListDataParsers = new ArrayList<ProjectListDataParser>();
                                mPackageListDataParsers.addAll(mProjectsParser.getData());
                                mActoreListAdapter = new ActoreListAdapter();
                                mListViewActores.setAdapter(mActoreListAdapter);

                            }
                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mProjectsParser.getMessage());
                        }


                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodSetActionComplete)) {

                        if (!mDataParserActions.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {
                            if (mDataParserActions != null && mDataParserActions.getGuid().length() > 0) {
                                mDataParser = mDataParserActions;
                                setDisableAllTab();
                                mDialog.cancel();
                                setData();
                            } else {
                                mActivity.getCommonMethod().showDialog("", mDataParserActions.getError(), false);
                            }

                        } else {
                            mActivity.getCommonMethod().showSessionExpireAlert(mActivity, getString(R.string.app_name), mDataParserActions.getMessage());
                        }

                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodGetHistory)) {

                        if (!mHistoryParser.getMessage().equalsIgnoreCase(getString(R.string.alt_invalid_token))) {

                            if (mHistoryParser.getData().size() > 0) {

                                mHistoryDataParsers = new ArrayList<HistoryDataParser>();
                                mHistoryDataParsers.addAll(mHistoryParser.getData());

                                LoadHistoryData mLoadHistoryData = new LoadHistoryData(mActivity, mHistoryDataParsers);
                                mLoadHistoryData.loadHistoryData(mListViewHistory);
                                mActivity.getCommonMethod().setScrollDown(mScrollView);
                                mTextViewHistoryNoData.setVisibility(View.GONE);

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

                                mTextViewFileNoData.setVisibility(View.GONE);

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
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

}
