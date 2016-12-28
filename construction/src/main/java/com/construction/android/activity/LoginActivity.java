package com.construction.android.activity;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.construction.android.MyApplication;
import com.construction.android.R;
import com.construction.android.parser.AuthenticateParser;
import com.construction.android.utils.CommonMethod;

public class LoginActivity extends MyFragmentActivity implements OnClickListener {

    public MyApplication mApplication;

    private TextView mTextViewLogin;
    private TextView mTextViewForgotPassword;
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;

    private String mStringUserName = "";
    private String mStringPassword = "";

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;

    private SharedPreferences mSharedPreferencesServer;
    private Editor mEditorServer;

    private Dialog mDialog;

    private String mCurrentMethod = "";
    private String mMethodLogin = "Login";
    private String mMethodForgotPassword = "ForgotPassword";

    private BackProcessLogin mBackProcessLogin;
    private ProgressDialog mProgressDialog;

    private MyFragmentActivity mActivity;
    private CommonMethod mCommonMethod;
    private AuthenticateParser mAuthenticateParser;


    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getSharedPreferences(getString(R.string.sp_file_name), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mSharedPreferencesServer = getSharedPreferences(getString(R.string.sp_server_name_file), Context.MODE_PRIVATE);
        mEditorServer = mSharedPreferencesServer.edit();

        mActivity = this;
        mApplication = (MyApplication) getApplication();
        mAuthenticateParser = new AuthenticateParser();


        System.out.println("server name.." + mSharedPreferencesServer.getString(getString(R.string.sp_server_name), ""));




        if (mSharedPreferencesServer.getString(getString(R.string.sp_server_name), "").length() > 0) {
            mCommonMethod = new CommonMethod(mActivity, mSharedPreferencesServer.getString(getString(R.string.sp_server_name), ""));
            if (mSharedPreferences.getBoolean(getString(R.string.sp_is_login), false)) {
                Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
                LoginActivity.this.finish();
            }
        } else {
            final String[] items = getResources().getStringArray(R.array.server_array);
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setSingleChoiceItems(items, 0, null)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            // Do something useful withe the position of the selected radio button
                            mEditorServer.putString(getString(R.string.sp_server_name), items[selectedPosition]);
                            mEditorServer.commit();
                            mCommonMethod = new CommonMethod(mActivity, items[selectedPosition]);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        getWidgetRefrence();
        registerOnClick();

        if(mApplication.isLogin()){
            mStringUserName = mApplication.getUserName();
            mStringPassword = mApplication.getPassword();
            mBackProcessLogin = new BackProcessLogin();
            mBackProcessLogin.execute(mMethodLogin);

        }

    }

    /**
     * Method will call get widget id from xml.
     */
    public void getWidgetRefrence() {

        mTextViewLogin = (TextView) findViewById(R.id.activity_login_button_login);
        mTextViewForgotPassword = (TextView) findViewById(R.id.activity_login_textview_forgot_password);
        mEditTextUserName = (EditText) findViewById(R.id.activity_login_edittex_email);
        mEditTextPassword = (EditText) findViewById(R.id.activity_login_edittex_password);

        // mEditTextUserName.setText("ronald@example.com");
        // mEditTextUserName.setText("thorstein@example.com");
        // mEditTextPassword.setText("password");

    }

    /**
     * Method will call register onClick() Events.
     */
    public void registerOnClick() {

        mTextViewLogin.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);

    }

    /**
     * Method will call fire onClick() Events.
     */
    @Override
    public void onClick(View v) {

        if (v == mTextViewLogin) {

            callLogin();

        } else if (v == mTextViewForgotPassword) {

            showForgotPasswordDialog();
        }

    }

    /**
     * Method will validate login field
     */
    public void callLogin() {

        mStringUserName = mEditTextUserName.getText().toString().trim();
        mStringPassword = mEditTextPassword.getText().toString().trim();

        if (mCommonMethod.emailValidator(mEditTextUserName.getText().toString(), mActivity, mEditTextUserName)
                && mCommonMethod.validateBlankField(mEditTextPassword, mActivity, getString(R.string.validation_enter_password))) {
            mCommonMethod.HideKeyboard(mEditTextPassword);

            mBackProcessLogin = new BackProcessLogin();
            mBackProcessLogin.execute(mMethodLogin);
        } else {
            mCommonMethod.showKeyboard(mEditTextUserName);
        }

    }

    /**
     * AsyncTask for calling webservice in background.
     *
     * @author npatel
     */
    public class BackProcessLogin extends AsyncTask<String, Void, String> {
        String responseData = "";

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(mActivity, "", getString(R.string.dialog_loading), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            mCurrentMethod = params[0];
            if (mCurrentMethod.equalsIgnoreCase(mMethodLogin)) {
                mAuthenticateParser = (AuthenticateParser) mActivity.getCommonMethod()
                        .loginAPI(mStringUserName, mStringPassword, mAuthenticateParser);
            } else if (mCurrentMethod.equalsIgnoreCase(mMethodForgotPassword)) {

                // mStatusData = (StatusData)
                // mActivity.getCommonMethod().forgotPasswordAPI(mStringEmail,
                // mStatusData);

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
                mActivity.getCommonMethod().showDialog("", getString(R.string.alt_msg_invalid_username_password), false);
            } else {
                try {
                    if (mCurrentMethod.equalsIgnoreCase(mMethodLogin)) {

                        if (!mAuthenticateParser.getMessage().equalsIgnoreCase("Invalid credentials")) {
                            if (mAuthenticateParser != null) {

                                mEditor.putString(getString(R.string.sp_user_id), mAuthenticateParser.getUserId());
                                mEditor.putString(getString(R.string.sp_user_name), mAuthenticateParser.getUsername());
                                mEditor.putString(getString(R.string.sp_user_status), mAuthenticateParser.getEnabled());
                                mEditor.putString(getString(R.string.sp_user_token), mActivity.getCommonMethod().setToken());
                                mEditor.putString(getString(R.string.sp_user_email), mAuthenticateParser.getUsername());
                                mEditor.putString(getString(R.string.sp_password), mStringPassword);
                                mEditor.putBoolean(getString(R.string.sp_is_login), true);
                                mEditor.commit();

                                Intent mIntent = new Intent(mActivity, MainActivity.class);
                                startActivity(mIntent);
                                mActivity.finish();
                            }
                        } else {
                            mActivity.getCommonMethod().showDialog(getString(R.string.app_name),mAuthenticateParser.getMessage(), false);
                        }
                    } else if (mCurrentMethod.equalsIgnoreCase(mMethodForgotPassword)) {
                        // if (mStatusData.getStatus().equalsIgnoreCase("true"))
                        // {
                        // mDialog.cancel();
                        // mActivity.getCommonMethod().showDialog("",
                        // mStatusData.getMessage(), false);
                        // } else {
                        // mActivity.getCommonMethod().showDialog("",
                        // mStatusData.getMessage(), false);
                        // }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(result);
        }
    }

    /**
     * Method call will showing forgot password dialog.
     */
    private void showForgotPasswordDialog() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_forgot_password);
        Window window = mDialog.getWindow();
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        final EditText mEditText = (EditText) mDialog.findViewById(R.id.dialog_username_editText_usrename);
        final TextView mButton = (TextView) mDialog.findViewById(R.id.dialog_username_textview_send);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mEditText.getText().toString();

                if (mActivity.getCommonMethod().emailValidator(mEditText.getText().toString(), mActivity, mEditText)) {
                    mActivity.getCommonMethod().HideKeyboard(mEditTextPassword);
                    mBackProcessLogin = new BackProcessLogin();
                    mBackProcessLogin.execute(mMethodForgotPassword);
                }
            }
        });

        mDialog.show();
    }

    @Override
    public com.construction.android.utils.CommonMethod getCommonMethod() {
        return mCommonMethod;
    }

    @Override
    public void replaceFragment(Fragment mFragment, boolean addBackStack) {

    }

    @Override
    public void setTitle(int title, int image) {

    }

    @Override
    public void setTitle(int title) {

    }

    @Override
    public MyApplication getMyApplication() {
        return mApplication;
    }

    @Override
    public void setTabSelectionDisable() {

    }

}
