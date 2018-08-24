package com.example.tankorbox.chatlibrary.models;

import com.google.gson.annotations.SerializedName;

public class UserGroup {
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("memberName")
    private String memberName;
    @SerializedName("groupName")
    private String groupName;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
