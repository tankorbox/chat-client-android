package com.example.tankorbox.chatlibrary.socket.message;

import com.google.gson.annotations.SerializedName;

public class Authorization {

    public Authorization(String token) {
        this.token = token;
    }

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
