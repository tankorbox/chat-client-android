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
import com.chatmodule.model.People;
import com.chatmodule.util.Constants;

public class FragmentEmojiPeople extends FragmentEmoji {

    public static final String TAG = "FragmentEmojiPeople";

    private View mRootView;
    private Emoji[] mData;
    private boolean mUseSystemDefault = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.frag_emoji_people, container, false);
        return this.mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GridView gridView = view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();

        if (bundle == null) {
            this.mData = People.DATA;
            this.mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(Constants.EMOJI_KEY);
            this.mData = new Emoji[parcels.length];

            for (int i = 0; i < parcels.length; i++) {
                this.mData[i] = (Emoji) parcels[i];
            }

            this.mUseSystemDefault = bundle.getBoolean(Constants.USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), this.mData, this.mUseSystemDefault));
        gridView.setOnItemClickListener(this);
    }
}
