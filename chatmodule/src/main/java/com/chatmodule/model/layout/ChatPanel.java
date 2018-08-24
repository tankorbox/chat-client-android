package com.chatmodule.model.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.chatmodule.R;

public class ChatPanel extends LinearLayout {

    private static final String TAG = "EmojiKeyboardLayout";

    // CONSTRUCTORS
    public ChatPanel(Context context) {
        super(context);
        init(context);
    }

    public ChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    // INITIALIZATIONS
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.rsc_telegram_panel, this, true);
        }
    }

}
