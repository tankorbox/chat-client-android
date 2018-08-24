package com.example.tankorbox.chatlibrary.services.responses;

import com.example.tankorbox.chatlibrary.models.Message;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageGetResponse {
    @SerializedName("data")
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
