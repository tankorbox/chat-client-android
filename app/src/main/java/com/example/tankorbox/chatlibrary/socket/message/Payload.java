package com.example.tankorbox.chatlibrary.socket.message;

import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("url")
    private String url;
    @SerializedName("authorization")
    private String authorization;
    @SerializedName("data")
    private Body body;

    public Payload(String url, String authorization, Body body) {
        this.url = url;
        this.authorization = authorization;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
