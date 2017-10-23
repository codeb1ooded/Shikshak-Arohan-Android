package com.igdtuw.technotwisters.sih_android.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.OtherFiles.SharedPreferencesUtils;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.ActivityTitles;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;
import com.igdtuw.technotwisters.sih_android.model.CheckSchool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ActivityTitles, View.OnClickListener {

    private EditText _usernameText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    ProgressDialog progressDialog;

    Call<AccountDetails> authenticateUser;
    Call<CheckSchool> isSchoolAdded;
    SharedPreferencesUtils spUtils;
    String username, accessToken;

    private static final String TAG = TITLE_LOGIN_ACTIVITY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews(){
        _usernameText = (EditText)findViewById(R.id.input_username);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login) ;
        _signupLink = (TextView)findViewById(R.id.link_signup);

        spUtils = new SharedPreferencesUtils(LoginActivity.this);

        _loginButton.setOnClickListener(this);
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
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
                Log.i("AccessToken: ", "not granted " + t);
                onLoginFailed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void onLoginSuccess(AccountDetails accountDetails) {
        username = accountDetails.username;
        accessToken = accountDetails.access_token;
        spUtils.loginUser(accountDetails.name, accountDetails.username, accountDetails.access_token);
        Log.i("AccessToken: ", accountDetails.access_token);
        checkIsSchoolAdded();

        finish();
        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    private void checkIsSchoolAdded() {
        isSchoolAdded = ApiClient.getInterface().isSchoolAdded(username, accessToken);
        isSchoolAdded.enqueue(new Callback<CheckSchool>() {
            @Override
            public void onResponse(Call<CheckSchool> call, Response<CheckSchool> response) {
                CheckSchool school = response.body();
                if (response.isSuccessful()) {
                    if(school.getMessage().equals("school present")){
                        spUtils.addSchool(school.getSchoolName(), school.getSchoolUsername(), (float) school.getLatitude(), (float) school.getLongitude());
                    }
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                    finish();
                    Intent i = new Intent();
                    i.setClass(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<CheckSchool> call, Throwable t) {
                Log.i("AccessToken: ", "not granted " + t);
                onLoginFailed();
            }
        });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        _loginButton.setEnabled(true);
        finish();
        Intent i = new Intent();
        i.setClass(LoginActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _usernameText.setError("Enter a valid email address");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}




