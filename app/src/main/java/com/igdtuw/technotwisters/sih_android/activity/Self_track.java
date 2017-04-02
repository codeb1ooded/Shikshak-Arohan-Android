package com.igdtuw.technotwisters.sih_android.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.igdtuw.technotwisters.sih_android.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Admin on 02-04-2017.
 */

public class Self_track extends AppCompatActivity {
    EditText fromDateEditText, toDateEditText;
    DatePickerDialog fromDatePickerDialog, toDatePickerDialog;
    Button createDemographics;

    Calendar calendar;

    int currentYear, currentMonth, currentDay;
    int fromYear, fromMonth, fromDay;
    int toYear, toMonth, toDay;

    Context context;


    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_fragment_track_attendance);

        context = this;
        // initiate the date picker and a button
        fromDateEditText = (EditText) findViewById(R.id.from_date_picker);
        toDateEditText = (EditText) findViewById(R.id.to_date_picker);
        createDemographics = (Button) findViewById(R.id.create_demographics);

        calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        currentYear = calendar.get(Calendar.YEAR); // current year
        currentMonth = calendar.get(Calendar.MONTH); // current month
        currentDay = calendar.get(Calendar.DAY_OF_MONTH); // current day

        fromYear = fromMonth = fromDay = 0;
        toYear = toMonth = toDay = 0;

        // Create date pickers for click on edit text of from and to
        setEditTextListeners();

        createDemographics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(isValidToAndFrom()){
                    /*
                    TODO:
                        1. make an api call to get data
                        2. count no. of days in between
                        3. create scale based on no. of days
                        4. present data in graph
                     */
                   Toast.makeText(getApplicationContext(), "Count: "+countDaysBetween(), Toast.LENGTH_SHORT).show();
                     Intent i = new Intent();
                     i.setClass(Self_track.this, GraphActivity.class);
                     startActivity(i);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid filters", Toast.LENGTH_SHORT).show();
                }

            }
        });

       // return view;

    }

    // TODO: not working properly
    // Returns number of days between from and to date
    private int countDaysBetween(){

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.DAY_OF_MONTH, fromYear);
        calendar.set(Calendar.MONTH, fromMonth);
        calendar.set(Calendar.YEAR, fromDay);
        Date fromDateCount = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, toYear);
        calendar.set(Calendar.MONTH, toMonth);
        calendar.set(Calendar.YEAR, toDay);
        Date toDateCount = calendar.getTime();

        long diff = toDateCount.getTime() - fromDateCount.getTime();
        int count = (int) diff / 1000 / 60 / 60 / 24;
        return count;
    }

    // check if dates from and to are valid or not and also earlier than current date
    private boolean isValidToAndFrom() {
        if (fromDay == 0 || fromMonth == 0 || fromYear == 0)
            return false;
        if (toDay == 0 || toMonth == 0 || toYear == 0)
            return false;
        if (fromYear > currentYear || toYear > currentYear || fromYear > toYear)
            return false;
        if ((fromYear == currentYear && fromMonth > currentMonth) ||
                (toYear == currentYear && toMonth > currentMonth) ||
                (fromYear == toYear && fromMonth > toMonth))
            return false;
        if ((fromYear == currentYear && fromMonth == currentMonth && fromDay > currentDay) ||
                (toYear == currentYear && toMonth == currentMonth && toDay > currentDay) ||
                (fromYear == toYear && fromMonth == toMonth && fromDay > toDay))
            return false;

        return true;
    }

    private void setEditTextListeners() {
        fromDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // date picker dialog
                fromDatePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fromDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                fromYear = year;
                                fromMonth = monthOfYear;
                                fromDay = dayOfMonth;
                            }
                        }, currentYear, currentMonth, currentDay);
                fromDatePickerDialog.show();
            }
        });

        toDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // date picker dialog
                toDatePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                toDateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                toYear = year;
                                toMonth = monthOfYear;
                                toDay = dayOfMonth;
                            }
                        }, currentYear, currentMonth, currentDay);
                toDatePickerDialog.show();
            }
        });
    }

}
