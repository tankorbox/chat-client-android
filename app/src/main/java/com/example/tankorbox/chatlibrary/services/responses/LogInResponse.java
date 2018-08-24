package com.example.tankorbox.chatlibrary.services.responses;

import com.example.tankorbox.chatlibrary.models.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LogInResponse implements Serializable {
    @SerializedName("data")
    private User user;

    public LogInResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
