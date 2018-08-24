package com.chatmodule.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chatmodule.R;
import com.chatmodule.model.Emoji;
import com.chatmodule.model.OnEmojiClickListener;
import com.chatmodule.util.TimestampUtil;

public class FragmentEmoji extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "FragmentEmoji";

    private RecentListener mRecentListener;
    private OnEmojiClickListener mOnEmojiconClickedListener;
    private View mRootView;
    protected static RecentEmojisManager mRecentEmojisManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.frag_emoji_people, container, false);
        RecentEmojisManager.initContext(mRootView.getContext());
        mRecentEmojisManager = RecentEmojisManager.getInstance();
        return this.mRootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.mOnEmojiconClickedListener != null) {
            final Emoji clickedEmoji = (Emoji) parent.getItemAtPosition(position);
            this.mOnEmojiconClickedListener.onEmojiClicked(clickedEmoji);
            clickedEmoji.setTimestamp(TimestampUtil.getCurrentTimestamp());

            mRecentEmojisManager.addRecentEmoji(clickedEmoji);
            if(mRecentListener != null){
                mRecentListener.notifyEmojiAdded();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void addEmojiconClickListener(OnEmojiClickListener listener) {
        this.mOnEmojiconClickedListener = listener;
    }

    public void subscribeRecentListener(RecentListener listener) {
        this.mRecentListener = listener;
    }

    public interface RecentListener {
        void notifyEmojiAdded();
    }
}
