package com.igdtuw.technotwisters.sih_android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.igdtuw.technotwisters.sih_android.constants.URLs;

import java.lang.reflect.Modifier;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by megha on 10/03/17.
 */

public class ApiClient implements URLs {
    private static ApiInterface mService;

    public static ApiInterface getInterface() {
        if (mService == null) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            mService = retrofit.create(ApiInterface.class);
        }
        return mService;
    }
}
