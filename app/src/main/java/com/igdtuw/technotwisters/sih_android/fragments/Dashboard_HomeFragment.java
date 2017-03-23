package com.igdtuw.technotwisters.sih_android.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.activity.AddSchoolActivity;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;

public class Dashboard_HomeFragment extends Fragment implements SharedPreferencesStrings{

    View view;
    TextView displaySchoolTextView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String username, accessToken, schoolName;
    boolean schoolDetailsAdded;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment_home, container, false);

        displaySchoolTextView = (TextView) view.findViewById(R.id.display_school_text_view);

        displaySchoolTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displaySchoolTextView.equals("Add your school first!!!")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("You aren't allowed this action!");
                    builder.setMessage("Click ok to add school first");
                    View dialogView = inflater.inflate(R.layout.dialog_confirm_logout, null);
                    builder.setView(dialogView);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), AddSchoolActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                }
            }
        });

        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        username = sharedPreferences.getString(SP_USER_USERNAME, null);
        accessToken = sharedPreferences.getString(SP_USER_ACCESS_TOKEN, null);
        schoolDetailsAdded = sharedPreferences.getBoolean(SP_SCHOOL_DETAILS_DONE, false);
        schoolName = sharedPreferences.getString(SP_SCHOOL_NAME, null);

        if(schoolDetailsAdded)
            displaySchoolTextView.setText("School: "+schoolName);
        else
            displaySchoolTextView.setText("Add your school first!!!");

        return view;
    }

}
