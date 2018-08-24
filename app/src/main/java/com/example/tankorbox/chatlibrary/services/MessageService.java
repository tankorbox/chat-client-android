package com.example.tankorbox.chatlibrary.services;


import com.example.tankorbox.chatlibrary.services.responses.MessageGetResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MessageService {

    @GET("/messages")
    Observable<MessageGetResponse> getMessages();
}
