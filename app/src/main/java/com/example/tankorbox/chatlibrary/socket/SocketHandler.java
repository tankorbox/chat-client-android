package com.example.tankorbox.chatlibrary.socket;

import android.util.Log;

import com.example.tankorbox.chatlibrary.services.responses.LogInResponse;
import com.example.tankorbox.chatlibrary.socket.message.Authorization;
import com.example.tankorbox.chatlibrary.socket.message.Body;
import com.example.tankorbox.chatlibrary.socket.message.Payload;
import com.google.gson.Gson;

import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;

public class SocketHandler {

    public static void postMessage(Socket socket, LogInResponse logInResponse, String message, String groupId) {
        Authorization authorization = new Authorization("Bearer " + logInResponse.getUser().getAccess_token());
        Body body = new Body(logInResponse.getUser().getId(), groupId, message);
        Payload payload = new Payload("/api/messages/create", authorization, body);

        socket.emit(Method.POST, new Gson().toJson(payload, Payload.class), (Ack) args -> {
            if (args.length >= 1) {
                if (args[0] != null) {
                    String errorMessage = args[0].toString();
                    Log.i("tag1", errorMessage);
                } else {
                    JSONObject jsonObject = (JSONObject) args[1];
                    if (jsonObject != null) {
                        Log.i("tag1", jsonObject.toString());
                    }
                }
            } else {
                Log.i("tag1", "Oops! Internet connection error. Please try again.");
            }
        });
    }

    public static void putMessage(Socket socket) {

    }

    public static void deleteMessage(Socket socket) {

    }

    public static void sendTyping(Socket socket, LogInResponse logInResponse, String groupId, String msg) {
        Authorization authorization = new Authorization("Bearer " + logInResponse.getUser().getAccess_token());
        Body body = new Body(logInResponse.getUser().getId(), groupId, "", !msg.equals(""));
        Payload payload = new Payload("/api/messages/typing", authorization, body);
        socket.emit(Method.POST, new Gson().toJson(payload, Payload.class), (Ack) (args) -> {
            if (args.length >= 1) {
                if (args[0] != null) {
                    String errorMessage = args[0].toString();
                    Log.i("tag1", errorMessage);
                } else {

                }
            } else {
                Log.i("tag1", "Oops! Internet connection error. Please try again.");
            }
        });
    }

}
