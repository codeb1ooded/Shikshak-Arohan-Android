package com.igdtuw.technotwisters.sih_android.api;

import com.igdtuw.technotwisters.sih_android.constants.URLs;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;
import com.igdtuw.technotwisters.sih_android.model.CheckSchool;
import com.igdtuw.technotwisters.sih_android.model.Result;
import com.igdtuw.technotwisters.sih_android.model.SchoolDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by megha on 10/03/17.
 */

public interface ApiInterface  {

    @GET(URLs.LOGIN_URL)
    Call<AccountDetails> getAuthenticationToken(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_PASSWORD) String password);

    @GET(URLs.LOGOUT_URL)
    Call<Result> logoutUser(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken);

    @GET(URLs.SIGN_UP_URL)
    Call<AccountDetails> signupUser(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_PASSWORD) String password,
                                    @Query(URLs.PARAM_NAME) String name, @Query(URLs.PARAM_EMAIL) String email);

    @GET(URLs.MARK_ATTENDANCE_URL)
    Call<Result> markAttendance(@Query(URLs.PARAM_TEACHER_USERNAME) String teacherUsername, @Query(URLs.PARAM_DATE) String date,
                                @Query(URLs.PARAM_LATITUDE) float latitude1, @Query(URLs.PARAM_LONGITUDE) float longitude1,
                                @Query(URLs.PARAM_LATITUDE) float latitude2, @Query(URLs.PARAM_LONGITUDE) float longitude2,
                                @Query(URLs.PARAM_LATITUDE) float latitude3, @Query(URLs.PARAM_LONGITUDE) float longitude3,
                                @Query(URLs.PARAM_LATITUDE) float latitude4, @Query(URLs.PARAM_LONGITUDE) float longitude4,
                                @Query(URLs.PARAM_ACCURACY) float accuracy, @Query(URLs.PARAM_PRESENCE) int presence);

    @GET(URLs.SIGN_UP_URL)
    Call<Result> updateUserDetails(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken,
                                           @Query(URLs.PARAM_NAME) String name, @Query(URLs.PARAM_AGE) int age, @Query(URLs.PARAM_CONTACT_NUMBER) long contactNum,
                                           @Query(URLs.PARAM_EMAIL) String email, @Query(URLs.PARAM_EXPERTISE) String expertise,
                                           @Query(URLs.PARAM_ADDRESS) String address, @Query(URLs.PARAM_CITY) String city,
                                           @Query(URLs.PARAM_STATE) String state, @Query(URLs.PARAM_PREFERRED_LOCATION) String preferredLoaction,
                                           @Query(URLs.PARAM_QUALIFICATION) String qualification, @Query(URLs.PARAM_TEACHING_EXPERIENCE) int teachingExperience);

    @GET(URLs.ADD_SCHOOL_URL)
    Call<Result> addSchoolToUser(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken,
                                    @Query(URLs.PARAM_SCHOOL_USERNAME) String schoolUsername);

    @GET(URLs.GET_LAT_LONG)
    Call<SchoolDetails> getLatLong(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken,
                                   @Query(URLs.PARAM_SCHOOL_USERNAME) String schoolUsername);

    @GET(URLs.IS_SCHOOL_ADDED_URL)
    Call<CheckSchool> isSchoolAdded(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken);

}
