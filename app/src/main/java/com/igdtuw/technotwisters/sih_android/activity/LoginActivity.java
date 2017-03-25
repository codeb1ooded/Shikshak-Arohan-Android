package com.igdtuw.technotwisters.sih_android.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.ActivityTitles;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;
import com.igdtuw.technotwisters.sih_android.model.CheckSchool;
import com.igdtuw.technotwisters.sih_android.model.SchoolDetails;

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

    private EditText _usernameText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    ProgressDialog progressDialog;

    Call<AccountDetails> authenticateUser;
    Call<CheckSchool> isSchoolAdded;
    Call<SchoolDetails> getSchoolDetailsCall;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String username, accessToken;

    private static final String TAG = TITLE_LOGIN_ACTIVITY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _usernameText = (EditText)findViewById(R.id.input_username);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _loginButton=(Button)findViewById(R.id.btn_login) ;
        _signupLink=(TextView)findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = getSharedPreferences(SharedPreferencesStrings.SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void login() {
        Log.d(TAG, "Login");

            /*if (!validate()) {
                onLoginFailed();
                return;
            }*/

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        authenticateUser = ApiClient.getInterface().getAuthenticationToken(username, password);
        authenticateUser.enqueue(new Callback<AccountDetails>() {
            @Override
            public void onResponse(Call<AccountDetails> call, Response<AccountDetails> response) {
                if (response.isSuccessful()) {
                    onLoginSuccess(response.body());
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
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
        Intent i = new Intent();
        i.setClass(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void onLoginSuccess(AccountDetails accountDetails) {
        username = accountDetails.username;
        accessToken = accountDetails.access_token;
        editor.putString(SharedPreferencesStrings.SP_USER_ACCESS_TOKEN, accountDetails.access_token);
        editor.commit();
        editor.putString(SharedPreferencesStrings.SP_USER_NAME, accountDetails.name);
        editor.commit();
        editor.putString(SharedPreferencesStrings.SP_USER_USERNAME, accountDetails.username);
        editor.commit();
        editor.putBoolean(SharedPreferencesStrings.SP_USER_TOKEN_GRANTED, true);
        editor.commit();
        Log.i("AccessToken: ", accountDetails.name);
        checkIsSchoolAdded();
        progressDialog.dismiss();
        _loginButton.setEnabled(true);
        finish();
        Intent i = new Intent();
        i.setClass(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    private void checkIsSchoolAdded() {
        isSchoolAdded = ApiClient.getInterface().isSchoolAdded(username, accessToken);
        isSchoolAdded.enqueue(new Callback<CheckSchool>() {
            @Override
            public void onResponse(Call<CheckSchool> call, Response<CheckSchool> response) {
                CheckSchool school = response.body();
                if (response.isSuccessful()) {
                    if(school.getMessage().equals("school already added")){
                        String schoolUsername = school.getSchoolUsername();
                        editor.putBoolean(SharedPreferencesStrings.SP_SCHOOL_ADDED, true);
                        editor.commit();
                        editor.putString(SharedPreferencesStrings.SP_SCHOOL_USERNAME, schoolUsername);
                        editor.commit();
                        getSchoolLatLong(schoolUsername);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckSchool> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("AccessToken: ", "not granted " + t);
                onLoginFailed();
            }
        });
    }

    private void getSchoolLatLong(final String schoolUsername) {

        getSchoolDetailsCall = ApiClient.getInterface().getLatLong(username, accessToken, schoolUsername);
        getSchoolDetailsCall.enqueue(new Callback<SchoolDetails>() {
            @Override
            public void onResponse(Call<SchoolDetails> call, Response<SchoolDetails> response) {
                SchoolDetails schoolDetails = response.body();
                if (response.isSuccessful()) {
                    editor.putBoolean(SharedPreferencesStrings.SP_SCHOOL_DETAILS_DONE, true);
                    editor.commit();
                    editor.putFloat(SharedPreferencesStrings.SP_SCHOOL_LATITUDE, (float) schoolDetails.getLatitude());
                    editor.commit();
                    editor.putFloat(SharedPreferencesStrings.SP_SCHOOL_LONGITUDE, (float) schoolDetails.getLongitude());
                    editor.commit();
                    editor.putString(SharedPreferencesStrings.SP_SCHOOL_NAME, schoolDetails.getSchoolName());
                    editor.commit();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: "+response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SchoolDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _usernameText.setError("enter a valid email address");
            valid = false;
        } else {
            _usernameText.setError(null);
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




