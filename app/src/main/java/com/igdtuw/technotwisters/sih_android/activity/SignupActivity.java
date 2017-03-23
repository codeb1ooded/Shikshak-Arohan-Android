package com.igdtuw.technotwisters.sih_android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _username;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;

    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    
    Call<AccountDetails> signUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _username = (EditText) findViewById(R.id.input_username);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _signupButton=(Button)findViewById(R.id.btn_signup) ;
        _loginLink=(TextView)findViewById(R.id.link_login);

        sharedPreferences = getSharedPreferences(SharedPreferencesStrings.SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String username = _username.getText().toString();
        String password = _passwordText.getText().toString();
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();

        signUp = ApiClient.getInterface().signupUser(username, password, name, email);
        signUp.enqueue(new Callback<AccountDetails>() {
            @Override
            public void onResponse(Call<AccountDetails> call, Response<AccountDetails> response) {
                if (response.isSuccessful()) {
                    onSignupSuccess(response.body());
                    askUserToAddSchool();
                    finish();
                    Intent i = new Intent();
                    i.setClass(SignupActivity.this, DashboardActivity.class);
                    startActivity(i);
                } else {
                    Log.i(TAG, "Failed: "+response.errorBody());
                    onSignupFailed();
                }
            }

            @Override
            public void onFailure(Call<AccountDetails> call, Throwable t) {
                Log.i(TAG, "Failed: "+t);
                onSignupFailed();
            }
        });
    }

    private void askUserToAddSchool() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("School Add");
        builder.setMessage("CDo you want to add school now?");
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_confirm_logout, null);
        builder.setView(v);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setClass(SignupActivity.this, AddSchoolActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }


    public void onSignupSuccess(AccountDetails accountDetails) {
        _signupButton.setEnabled(true);
        editor.putString(SharedPreferencesStrings.SP_USER_ACCESS_TOKEN, accountDetails.access_token);
        editor.commit();
        editor.putString(SharedPreferencesStrings.SP_USER_NAME, accountDetails.name);
        editor.commit();
        editor.putString(SharedPreferencesStrings.SP_USER_USERNAME, accountDetails.username);
        editor.commit();
        editor.putBoolean(SharedPreferencesStrings.SP_USER_TOKEN_GRANTED, true);
        editor.commit();
        Log.i("AccessToken: ", accountDetails.name);
        progressDialog.dismiss();
    }

    public void onSignupFailed() {
       // progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String username = _username.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (username.isEmpty() || username.length() < 3) {
            _username.setError("at least 3 characters");
            valid = false;
        } else {
            _username.setError(null);
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


