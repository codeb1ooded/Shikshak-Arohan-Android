package com.igdtuw.technotwisters.sih_android.api;

import com.igdtuw.technotwisters.sih_android.constants.URLs;
import com.igdtuw.technotwisters.sih_android.model.AccountDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by megha on 10/03/17.
 */

public interface ApiInterface extends URLs {

     @GET(LOGIN_URL)
     Call<AccountDetails> getAuthenticalToken(@Query(PARAM_EMAIL) String email, @Query(PARAM_PASSWORD) String password);

}
