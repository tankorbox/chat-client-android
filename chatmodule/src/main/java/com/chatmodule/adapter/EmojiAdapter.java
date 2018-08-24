package com.chatmodule.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.chatmodule.R;
import com.chatmodule.model.Emoji;
import com.chatmodule.model.layout.EmojiTextView;

import java.util.List;

public class EmojiAdapter extends ArrayAdapter<Emoji> {

    private boolean mUseSystemDefault = Boolean.FALSE;

    public EmojiAdapter(Context context, Emoji[] data) {
        super(context, R.layout.rsc_emoji_item, data);
    }

    public EmojiAdapter(Context context, List<Emoji> data) {
        super(context, R.layout.rsc_emoji_item, data);
    }

    public EmojiAdapter(Context context, List<Emoji> data, boolean useSystemDefault) {
        super(context, R.layout.rsc_emoji_item, data);
        this.mUseSystemDefault = useSystemDefault;
    }

    public EmojiAdapter(Context context, Emoji[] data, boolean useSystemDefault) {
        super(context, R.layout.rsc_emoji_item, data);
        this.mUseSystemDefault = useSystemDefault;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = View.inflate(getContext(), R.layout.rsc_emoji_item, null);
            view.setTag(new ViewHolder(view, this.mUseSystemDefault));
        }

        if (null != getItem(position)) {
            Emoji emoji = this.getItem(position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.icon.setText(emoji.getEmoji());
        }

        return view;
    }

    static class ViewHolder {
        EmojiTextView icon;

        public ViewHolder(View view, Boolean useSystemDefault) {
            this.icon = view.findViewById(R.id.emoji_icon);
            this.icon.setUseSystemDefault(useSystemDefault);
        }
    }
}
