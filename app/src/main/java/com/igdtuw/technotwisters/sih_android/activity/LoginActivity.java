package com.igdtuw.technotwisters.sih_android.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.ActivityTitles;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ActivityTitles{

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    ProgressDialog progressDialog;

    Call<AccountDetails> authenticateUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String TAG = TITLE_LOGIN_ACTIVITY;
        private static final int REQUEST_SIGNUP = 0;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            _emailText = (EditText)findViewById(R.id.input_email);
            _passwordText=(EditText)findViewById(R.id.input_password);
            _loginButton=(Button)findViewById(R.id.btn_login) ;
            _signupLink=(TextView)findViewById(R.id.link_signup);

            _loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: after adding url for login remove below three lines and put instead: login();
                    Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    // login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                }
            });

            sharedPreferences = getSharedPreferences(SharedPreferencesStrings.SP_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        public void login() {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }

            _loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            authenticateUser = ApiClient.getInterface().getAuthenticalToken(email, password);
            authenticateUser.enqueue(new Callback<AccountDetails>() {
                @Override
                public void onResponse(Call<AccountDetails> call, Response<AccountDetails> response) {
                    if (response.isSuccessful()) {
                        AccountDetails accountDetails = response.body();
                        editor.putString(SharedPreferencesStrings.SP_USER_ACCESS_TOKEN, accountDetails.access_token);
                        editor.commit();
                        editor.putString(SharedPreferencesStrings.SP_USER_NAME, accountDetails.name);
                        editor.commit();
                        editor.putString(SharedPreferencesStrings.SP_USER_EMAIL, accountDetails.email);
                        editor.commit();
                        editor.putBoolean(SharedPreferencesStrings.SP_USER_TOKEN_GRANTED, true);
                        editor.commit();
                        Log.i("AccessToken: ", accountDetails.name);
                        onLoginSuccess();
                    } else {
                        Log.i("AccessToken: ", "not granted " + response.errorBody());
                        onLoginFailed();
                    }
                }

                @Override
                public void onFailure(Call<AccountDetails> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.i("AccessToken: ", "not granted " + t);
                    onLoginFailed();
                }
            });
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {

                    // TODO: Implement successful signup logic here
                    // By default we just finish the Activity and log them in automatically
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // disable going back to the MainActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            progressDialog.dismiss();
            _loginButton.setEnabled(true);
            editor.putBoolean(SharedPreferencesStrings.SP_USER_TOKEN_GRANTED, false);
            editor.commit();
            finish();
            Intent i = new Intent();
            i.setClass(LoginActivity.this, DashboardActivity.class);
            startActivity(i);
        }

        public void onLoginFailed() {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("enter a valid email address");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _passwordText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }
    }




