package com.chatmodule.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.chatmodule.model.Emoji;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecentEmojisManager {

    private static final String PREFERENCES_FILE = "EmojiProperties";
    private static final String RECENT_EMOJIS = "RECENTS";
    private static final String DELIMETER = "213488984564321";
    private static RecentEmojisManager ourInstance;
    static private SharedPreferences.Editor mWriter;
    static private SharedPreferences mReader;
    private static Context context;
    private volatile ArrayList<Emoji> recentEmojis;

    private RecentEmojisManager(Context context) {
        mWriter = context.getSharedPreferences(PREFERENCES_FILE, context.MODE_PRIVATE).edit();
        mReader = context.getSharedPreferences(PREFERENCES_FILE, context.MODE_PRIVATE);
        recentEmojis = getSavedRecentEmojis();
        if (recentEmojis == null) {
            recentEmojis = new ArrayList<>();
        }
    }

    public static void initContext(Context pContext) {
        if (context == null)
            context = pContext;
    }

    public static RecentEmojisManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new RecentEmojisManager(context);
        }
        return ourInstance;
    }

    public void saveRecentEmojis(List<Emoji> emojiStack) {
        Gson gson = new Gson();
        mWriter.putString(RECENT_EMOJIS, gson.toJson(emojiStack));
        mWriter.commit();
    }

    public ArrayList<Emoji> getSavedRecentEmojis() {
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy HH:MM:ss").create();
        Type type = new TypeToken<ArrayList<Emoji>>() {
        }.getType();
        return gson.fromJson(mReader.getString(RECENT_EMOJIS, ""), type);
    }

    public ArrayList<Emoji> getRecentEmojis() {
        return recentEmojis;
    }

    @SuppressLint("StaticFieldLeak")
    public void addRecentEmoji(Emoji emoji) {
        int emojiPosition = recentEmojis.indexOf(emoji);
        if (emojiPosition != -1) {
            recentEmojis.remove(emojiPosition);
        }
        recentEmojis.add(0, emoji);

        //save new recent stack async
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                saveRecentEmojis(recentEmojis);
                return true;
            }
        }.execute();

    }

}
