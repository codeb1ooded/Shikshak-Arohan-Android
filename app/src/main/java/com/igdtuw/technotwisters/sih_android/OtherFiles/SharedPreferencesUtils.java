package com.igdtuw.technotwisters.sih_android.OtherFiles;

import android.content.Context;

import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;

/**
 * Created by megha on 23/10/17.
 */

public class SharedPreferencesUtils implements SharedPreferencesStrings{

    Context mContext;
    android.content.SharedPreferences sharedPreferences;
    android.content.SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context){
        mContext = context;
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void loginUser(String name, String username, String access_token){
        editor.putString(USER_ACCESS_TOKEN, access_token);
        editor.putString(USER_NAME, name);
        editor.putString(USER_USERNAME, username);
        editor.putBoolean(USER_LOGGED_IN, true);
        editor.commit();
    }
    
    public void logoutUser(){
        editor.putBoolean(USER_LOGGED_IN, false);
        editor.putString(USER_ACCESS_TOKEN, "N/A");
        editor.putString(USER_USERNAME, "N/A");
        editor.putString(SP_NAME, "N/A");
        editor.putBoolean(SCHOOL_ADDED, false);
        editor.commit();
    }

    public void addSchool(String schoolName, String schoolUsername, float latitude, float longitude){
        editor.putBoolean(SCHOOL_ADDED, true);
        editor.putString(SCHOOL_USERNAME, schoolUsername);
        editor.putString(SCHOOL_NAME, schoolName);
        editor.putFloat(SCHOOL_LATITUDE, latitude);
        editor.putFloat(SCHOOL_LONGITUDE, longitude);
        editor.commit();
    }
    
    public void addProfileDetails(String name, int age, String email, long contact, String address, String city,
                                  String state, String expertise, int experience, String qualification, String pref_location){
        editor.putString(USER_NAME, name);
        editor.putInt(USER_AGE, age);
        editor.putString(USER_EMAIL, email);
        editor.putLong(USER_CONTACT_NUMBER, contact);
        editor.putString(USER_ADDRESS, address);
        editor.putString(USER_CITY, city);
        editor.putString(USER_STATE, state);
        editor.putString(USER_EXPERTISE, expertise);
        editor.putInt(USER_TEACHING_EXPERIENCE, experience);
        editor.putString(USER_QUALIFICATION, qualification);
        editor.putString(USER_PREFERRED_LOCATION, pref_location);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(USER_LOGGED_IN, false);
    }
    
    public String getUsername(){
        return sharedPreferences.getString(USER_USERNAME, null);
    }

    public String getAccessToken(){
        return sharedPreferences.getString(USER_ACCESS_TOKEN, null);
    }

    public String getSchoolName(){
        return sharedPreferences.getString(SCHOOL_NAME, null);
    }

    public boolean isSchoolAdded(){
        return sharedPreferences.getBoolean(SCHOOL_ADDED, false);
    }

}
