package com.example.tankorbox.chatlibrary.socket.message;

import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("userId")
    private String userId;

    @SerializedName("groupId")
    private String groupId;

    @SerializedName("message")
    private String data;

    @SerializedName("isTyping")
    private boolean isTyping;

    public Body(String userId, String groupId, String data) {
        this.userId = userId;
        this.groupId = groupId;
        this.data = data;
    }

    public Body(String userId, String groupId, String data, boolean isTyping) {
        this.userId = userId;
        this.groupId = groupId;
        this.data = data;
        this.isTyping = isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
