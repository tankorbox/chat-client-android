package com.example.tankorbox.chatlibrary.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("email")
    private String email;
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("expire_in")
    private int expire_in;

    public User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.username = userBuilder.username;
        this.password = userBuilder.password;
        this.displayName = userBuilder.displayName;
        this.email = userBuilder.email;
        this.access_token = userBuilder.access_token;
        this.expire_in = userBuilder.expire_in;
    }

    public static class UserBuilder {
        private String id;
        private String username;
        private String password;
        private String displayName;
        private String email;
        private String access_token;
        private int expire_in;

        public UserBuilder() {

        }

        public User build() {
            return new User(this);
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public void setExpire_in(int expire_in) {
            this.expire_in = expire_in;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(int expire_in) {
        this.expire_in = expire_in;
    }
}
