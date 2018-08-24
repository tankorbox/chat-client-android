package com.example.tankorbox.chatlibrary.services;


import com.example.tankorbox.chatlibrary.services.responses.GroupResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface GroupService {

    @GET("/api/groups")
    Observable<GroupResponse> getGroups();

}
