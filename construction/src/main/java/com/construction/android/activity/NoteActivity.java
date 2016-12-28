package com.construction.android.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.construction.android.MyApplication;
import com.construction.android.R;
import com.construction.android.adapter.NotesAdapter;
import com.construction.android.parser.NotesParser;
import com.construction.android.parser.ProjectDetailsDataParser;
import com.construction.android.utils.CommonMethod;

@SuppressLint("NewApi")
public class NoteActivity extends MyFragmentActivity implements OnClickListener {

	public MyFragmentActivity mActivity;
	public CommonMethod mCommonMethod;
	public MyApplication mApplication;
	private ProgressDialog mProgressDialog;

	private ArrayList<NotesParser> mNotesParsersList;
	private ProjectDetailsDataParser mDataParser;
	private NotesAdapter mNotesAdapter;

	private ListView mListView;
	private ImageView mImageViewBack;
	private ImageView mImageViewPickImage;
	private ImageView mImageViewSend;
	private EditText mEditTextMessage;

	private Intent mIntentPicture = null;
	private Bitmap mBitmap = null;
	protected static final int CAMERA_REQUEST = 0;
	protected static final int GALLERY_PICTURE = 1;
	private Dialog mDialog;
	private String mStringFilePath = "";
	private String mStringFileName = "";
	private long mIntTimeStamp = 0;

	private String mStringGuID = "";
	private String mCurrentMethod = "";
	private String mMethodSetNoteMessage = "setNoteMessage";
	private String mMethodGetNotes = "getNotes";
	private String mMethodSendFile = "sendFile";
	private String mStringNotes = "";
	private String mStringSendFileMessage = "";

	private BackProcessSendNotes mBackProcessSendNotes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);

		mActivity = this;
		
		mApplication = (MyApplication) getApplication();
		mCommonMethod = new CommonMethod(mActivity, getSharedPreferences(getString(R.string.sp_server_name_file), Context.MODE_PRIVATE).getString(getString(R.string.sp_server_name), ""));
		mDataParser = new ProjectDetailsDataParser();

		getWiegetReference();
		registerOnClick();

		mStringGuID = getIntent().getStringExtra(getString(R.string.bunble_project_guid));

		mBackProcessSendNotes = new BackProcessSendNotes();
		mBackProcessSendNotes.execute(mMethodGetNotes);

	}

	/**
	 * Method call will get IDs from xml file.
	 * 
	 */
	public void getWiegetReference() {
		mListView = (ListView) findViewById(R.id.activity_note_listview);
		mImageViewBack = (ImageView) findViewById(R.id.activity_imageview_back);
		mImageViewPickImage = (ImageView) findViewById(R.id.view_add_note_imageview_camera);
		mImageViewSend = (ImageView) findViewById(R.id.view_add_note_imageview_send);
		mEditTextMessage = (EditText) findViewById(R.id.view_add_note_edittex_text);
	}

	/**
	 * Method call will Register OnClick() Events for widgets.
	 * 
	 */
	public void registerOnClick() {
		mImageViewBack.setOnClickListener(this);
		mImageViewPickImage.setOnClickListener(this);
		mImageViewSend.setOnClickListener(this);

	}

	/**
	 * Method call OnClick Event fire.
	 * 
	 */
	@Override
	public void onClick(View v) {

		if (v == mImageViewBack) {

			NoteActivity.this.finish();

		} else if (v == mImageViewPickImage) {

			selectPhotoDialog();

		} else if (v == mImageViewSend) {

			mStringNotes = mEditTextMessage.getText().toString().trim();
			if (mStringNotes.length() > 0) {

				mBackProcessSendNotes = new BackProcessSendNotes();
				mBackProcessSendNotes.execute(mMethodSetNoteMessage);

			}
		}
	}

	/**
	 * Method call will get image from gallary or camera.
	 */
	private void selectPhotoDialog() {

		mIntTimeStamp = (long) System.currentTimeMillis();

		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle(getString(R.string.alt_msg_send_picture));
		myAlertDialog.setMessage(getString(R.string.alt_msg_send_picture_msessage));

		myAlertDialog.setPositiveButton(getString(R.string.lbl_gallery), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				mIntentPicture = new Intent(Intent.ACTION_GET_CONTENT, null);
				mIntentPicture.setType("image/*");
				mIntentPicture.putExtra("return-data", true);
				startActivityForResult(mIntentPicture, GALLERY_PICTURE);
			}
		});

		myAlertDialog.setNegativeButton(getString(R.string.lbl_camera), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				File mFile = new File(mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png");
				mIntentPicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				mIntentPicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
				startActivityForResult(mIntentPicture, CAMERA_REQUEST);

			}
		});
		myAlertDialog.show();
	}

	/**
	 * On Activity Result 
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GALLERY_PICTURE) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					try {

						mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
						mBitmap = mActivity.getCommonMethod().resizeBitmap(mBitmap);

						if (mBitmap != null) {
							mActivity.getCommonMethod().getApplicationDirectory();
							copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);
							mStringFilePath = mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png";
							mStringFileName =  mIntTimeStamp + ".png";
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
					Toast.makeText(this, getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == CAMERA_REQUEST) {
			if (resultCode == RESULT_OK) {
				File imgFile = new File(mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png");
				mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				mBitmap = mActivity.getCommonMethod().resizeBitmap(mBitmap);
				mStringFilePath = mActivity.getCommonMethod().getAppPath() + "/" + mIntTimeStamp + ".png";
				mStringFileName =  mIntTimeStamp + ".png";
				showImageDisplayDialog();

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getString(R.string.lbl_cancelled), Toast.LENGTH_SHORT).show();
			}
		} 
	}

	/**
	 * Method will write file
	 * 
	 * @param mStringImagePath
	 * @param mBitmap
	 */
	public void copyImageFile(String mStringImagePath, Bitmap mBitmap) {
		OutputStream fOut = null;
		File file = new File(mStringImagePath);
		try {
			fOut = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
				copyImageFile(mActivity.getCommonMethod().getApplicationDirectory() + "/" + mIntTimeStamp + ".png", mBitmap);
				// mImageViewProfilePic.setImageBitmap(bitmap);

				NotesParser mParser = new NotesParser();
				Calendar mCalendar = Calendar.getInstance();
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				mParser.setMessage(mStringNotes);
				mParser.setTimestamp(mActivity.getCommonMethod().getDateFormatFromCalendar(mCalendar, "dd MMM yyyy"));
				mParser.setCheckSender("sender");
				mParser.setSenderImagePath(mStringFilePath);
				mNotesParsersList.add(mParser);
				mNotesAdapter.notifyDataSetChanged();
				mListView.setSelection(mNotesParsersList.size());

				mBackProcessSendNotes = new BackProcessSendNotes();
				mBackProcessSendNotes.execute(mMethodSendFile);
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
	 * Method will set title
	 * 
	 * @param resource
	 */
	@Override
	public void setTitle(int resource) {
	}

	/**
	 * Method will set title
	 * 
	 * @param resource
	 */
	@Override
	public void setTitle(int resource, int image) {
	}

	@Override
	public CommonMethod getCommonMethod() {

		return mCommonMethod;
	}

	@Override
	public void replaceFragment(Fragment mFragment, boolean addBackStack) {
		FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
		mFragmentTransaction.replace(R.id.frame_container, mFragment);
		if (addBackStack)
			mFragmentTransaction.addToBackStack(null);
		mFragmentTransaction.commit();

	}

	@Override
	public MyApplication getMyApplication() {
		return mApplication;
	}

	@Override
	public void setTabSelectionDisable() {

	}

	/**
	 * AsyncTask for calling webservice in background.
	 * 
	 * @author ebaraiya
	 * 
	 */
	public class BackProcessSendNotes extends AsyncTask<String, Void, String> {
		String responseData = "";

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			mCurrentMethod = params[0];

			if (mCurrentMethod.equalsIgnoreCase(mMethodGetNotes)) {

				mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().getPredescessorsAPI(mActivity.getMyApplication().getUserToken(),
						mStringGuID, mDataParser);

			} else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

				mDataParser = (ProjectDetailsDataParser) mActivity.getCommonMethod().setNotesAPI(mActivity.getMyApplication().getUserToken(),
						mStringGuID, mActivity.getMyApplication().getUserID(), mStringNotes, mDataParser);

			} else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

				mStringSendFileMessage = (String) mActivity.getCommonMethod().sendFileAPI(mActivity.getMyApplication().getUserToken(),
						mStringFileName,mStringFilePath, mStringGuID, mStringSendFileMessage);
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

				mActivity.getCommonMethod().showDialog("", getString(R.string.validation_failed), false);

			} else {
				try {
					if (mCurrentMethod.equalsIgnoreCase(mMethodGetNotes)) {
						if (mDataParser.getData().size() > 0) {

							mNotesParsersList = new ArrayList<NotesParser>();
							mNotesParsersList.addAll(mDataParser.getData());

							mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList,true);
							mListView.setAdapter(mNotesAdapter);
							mListView.setSelection(mNotesParsersList.size());

						} else {
							mActivity.getCommonMethod().showDialog(getString(R.string.app_name), getString(R.string.alt_notes_data_not_founds), false);
						}
					} else if (mCurrentMethod.equalsIgnoreCase(mMethodSetNoteMessage)) {

						mNotesParsersList = new ArrayList<NotesParser>();
						mNotesParsersList.addAll(mDataParser.getData());

						mNotesAdapter = new NotesAdapter(mActivity, mNotesParsersList,true);
						mListView.setAdapter(mNotesAdapter);
						mListView.setSelection(mNotesParsersList.size());

						mActivity.getCommonMethod().HideKeyboard(mEditTextMessage);
						mEditTextMessage.setText("");
						mNotesAdapter.notifyDataSetChanged();

					} else if (mCurrentMethod.equalsIgnoreCase(mMethodSendFile)) {

						Toast.makeText(mActivity, mStringSendFileMessage, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.onPostExecute(result);
		}
	}

}
