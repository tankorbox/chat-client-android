package com.example.tankorbox.chatlibrary.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("type")
    private String type;
    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
