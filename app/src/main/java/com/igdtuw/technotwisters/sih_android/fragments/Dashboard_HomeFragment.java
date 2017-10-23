package com.igdtuw.technotwisters.sih_android.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.OtherFiles.SharedPreferencesUtils;
import com.igdtuw.technotwisters.sih_android.activity.AddSchoolActivity;

public class Dashboard_HomeFragment extends Fragment{

    View view;
    TextView displaySchoolTextView;
    SharedPreferencesUtils spUtils;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment_home, container, false);

        displaySchoolTextView = (TextView) view.findViewById(R.id.display_school_text_view);
        spUtils = new SharedPreferencesUtils(getContext());

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

        if(spUtils.isSchoolAdded())
            displaySchoolTextView.setText("School: " + spUtils.getSchoolName());
        else
            displaySchoolTextView.setText("Add your school first!!!");

        return view;
    }

}
