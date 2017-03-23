package com.igdtuw.technotwisters.sih_android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;
import com.igdtuw.technotwisters.sih_android.model.Result;
import com.igdtuw.technotwisters.sih_android.model.SchoolDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by megha on 22/03/17.
 */

public class AddSchoolActivity extends AppCompatActivity implements SharedPreferencesStrings{
    
    EditText schoolUsernameEditText;
    Button addButton;
    
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    
    Call<Result> addSchoolCall;
    Call<SchoolDetails> getSchoolDetailsCall;

    String username, accessToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);
        
        schoolUsernameEditText = (EditText) findViewById(R.id.edit_text_add_school);
        addButton = (Button) findViewById(R.id.button_add_school);

        sharedPreferences = getSharedPreferences(SharedPreferencesStrings.SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(AddSchoolActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        username = sharedPreferences.getString(SP_USER_USERNAME, null);
        accessToken = sharedPreferences.getString(SP_USER_ACCESS_TOKEN, null);
        
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String schoolUsername = schoolUsernameEditText.getText().toString();
                if(schoolUsername != null && !schoolUsername.equals("")){

                    progressDialog.show();

                    addSchoolCall = ApiClient.getInterface().addSchoolToUser(username, accessToken, schoolUsername);
                    addSchoolCall.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.isSuccessful()) {
                                editor.putBoolean(SharedPreferencesStrings.SP_SCHOOL_ADDED, true);
                                editor.commit();
                                editor.putString(SharedPreferencesStrings.SP_SCHOOL_USERNAME, schoolUsername);
                                editor.commit();
                                getSchoolLatLong(schoolUsername);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddSchoolActivity.this, "Error: "+response.errorBody(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSchoolActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }

                    });
                }
                else {
                    Toast.makeText(AddSchoolActivity.this, "Empty text", Toast.LENGTH_SHORT).show();
                }
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
                    finish();
                    Intent i = new Intent();
                    i.setClass(AddSchoolActivity.this, DashboardActivity.class);
                    startActivity(i);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddSchoolActivity.this, "Error: "+schoolDetails.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SchoolDetails> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddSchoolActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }

        });

    }

}
