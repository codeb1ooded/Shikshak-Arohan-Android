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

}
