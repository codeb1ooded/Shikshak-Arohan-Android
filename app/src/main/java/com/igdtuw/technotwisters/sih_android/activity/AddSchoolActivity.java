package com.igdtuw.technotwisters.sih_android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.OtherFiles.SharedPreferencesUtils;
import com.igdtuw.technotwisters.sih_android.api.ApiClient;
import com.igdtuw.technotwisters.sih_android.model.CheckSchool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by megha on 22/03/17.
 */

public class AddSchoolActivity extends AppCompatActivity{
    
    EditText schoolUsernameEditText;
    Button addButton;
    
    SharedPreferencesUtils spUtils;
    ProgressDialog progressDialog;
    
    Call<CheckSchool> addSchoolCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);
        
        schoolUsernameEditText = (EditText) findViewById(R.id.edit_text_add_school);
        addButton = (Button) findViewById(R.id.button_add_school);
        spUtils = new SharedPreferencesUtils(AddSchoolActivity.this);

        progressDialog = new ProgressDialog(AddSchoolActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String schoolUsername = schoolUsernameEditText.getText().toString();
                if(schoolUsername != null && !schoolUsername.equals("")){

                    progressDialog.show();

                    addSchoolCall = ApiClient.getInterface().addSchoolToUser(spUtils.getUsername(), spUtils.getAccessToken(), schoolUsername);
                    addSchoolCall.enqueue(new Callback<CheckSchool>() {
                        @Override
                        public void onResponse(Call<CheckSchool> call, Response<CheckSchool> response) {
                            if (response.isSuccessful()) {
                                CheckSchool school = response.body();
                                spUtils.addSchool(school.getSchoolName(), schoolUsername, (float) school.getLatitude(), (float) school.getLongitude());
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddSchoolActivity.this, "Error: "+response.errorBody(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckSchool> call, Throwable t) {
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

}
