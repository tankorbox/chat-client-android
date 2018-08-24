package com.example.tankorbox.chatlibrary.services.responses;

import com.example.tankorbox.chatlibrary.models.Group;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {
    @SerializedName("data")
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
