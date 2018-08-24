package com.example.tankorbox.chatlibrary.services;


import com.example.tankorbox.chatlibrary.services.responses.LogInResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/api/auth/login")
    Observable<LogInResponse> login(@Body RequestBody requestBody);

}
