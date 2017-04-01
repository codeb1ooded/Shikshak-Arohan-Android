package com.igdtuw.technotwisters.sih_android.model;

/**
 * Created by megha on 23/03/17.
 */

public class CheckSchool {

    String status;
    String message;
    String school_username;
    String school_name;
    double latitude;
    double longitude;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getSchoolUsername() {
        return school_username;
    }

    public String getSchoolName() {
        return school_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
