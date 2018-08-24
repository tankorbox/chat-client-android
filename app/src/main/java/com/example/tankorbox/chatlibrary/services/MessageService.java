package com.example.tankorbox.chatlibrary.services;


import com.example.tankorbox.chatlibrary.services.responses.MessageGetResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MessageService {

    @GET("/api/messages/{groupId}")
    Observable<MessageGetResponse> getMessages(@Path("groupId") String groupId);
}
