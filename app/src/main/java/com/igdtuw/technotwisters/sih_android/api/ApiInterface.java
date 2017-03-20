package com.igdtuw.technotwisters.sih_android.api;

import com.igdtuw.technotwisters.sih_android.constants.URLs;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;
import com.igdtuw.technotwisters.sih_android.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by megha on 10/03/17.
 */

public interface ApiInterface  {

    @GET(URLs.LOGIN_URL)
    Call<AccountDetails> getAuthenticalToken(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_PASSWORD) String password);

    @GET(URLs.LOGOUT_URL)
    Call<Result> logoutUser(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_ACCESS_TOKEN) String accessToken);

    @GET(URLs.SIGN_UP_URL)
    Call<AccountDetails> signupUser(@Query(URLs.PARAM_USERNAME) String username, @Query(URLs.PARAM_PASSWORD) String password,
                                    @Query(URLs.PARAM_NAME) String name, @Query(URLs.PARAM_EMAIL) String email);

    @GET(URLs.MARK_ATTENDANCE_URL)
    Call<Result> markAttendance(@Query(URLs.PARAM_TEACHER_USERNAME) String teacherUsername, @Query(URLs.PARAM_DATE) String date,
                                @Query(URLs.PARAM_LATITUDE) float latitude, @Query(URLs.PARAM_LONGITUDE) float longitude,
                                @Query(URLs.PARAM_ACCURACY) float accuracy, @Query(URLs.PARAM_PRESENCE) int presence);

}
