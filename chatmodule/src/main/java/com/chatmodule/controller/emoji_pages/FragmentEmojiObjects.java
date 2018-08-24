package com.chatmodule.controller.emoji_pages;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.chatmodule.R;
import com.chatmodule.adapter.EmojiAdapter;
import com.chatmodule.controller.FragmentEmoji;
import com.chatmodule.model.Emoji;
import com.chatmodule.model.Objects;

public class FragmentEmojiObjects extends FragmentEmoji {

    public static final String TAG = "FragmentEmojiObjects";

    private View mRootView;
    private Emoji[] mData;
    private boolean mUseSystemDefault = false;

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String EMOJI_KEY = "emojic";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.frag_emoji_objects, container, false);
        return this.mRootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GridView gridView = view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();
        if (bundle == null) {
            mData = Objects.DATA;
            mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(EMOJI_KEY);
            mData = new Emoji[parcels.length];
            for (int i = 0; i < parcels.length; i++) {
                mData[i] = (Emoji) parcels[i];
            }
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData, mUseSystemDefault));
        gridView.setOnItemClickListener(this);
    }
}
