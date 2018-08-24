package com.chatmodule.controller;

import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.chatmodule.R;
import com.chatmodule.model.layout.ChatPanelEventListener;
import com.chatmodule.model.layout.EmojiCompatActivity;
import com.chatmodule.model.layout.EmojiEditText;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatPanel {

    private static final String TAG = "ChatPanel";

    private EmojiCompatActivity mActivity;
    private Toolbar mBottomPanel;
    private EmojiEditText mInput;
    private EmojiKeyboard mEmojiKeyboard;
    private ChatPanelEventListener mListener;
    private LinearLayout mCurtain;
    private Boolean mToogleIcon = Boolean.TRUE;

    private Boolean isEmojiKeyboardVisible = Boolean.FALSE;

    // CONSTRUCTOR
    public ChatPanel(EmojiCompatActivity activity, ChatPanelEventListener listener) {
        this.mActivity = activity;
        this.initBottomPanel();
        this.setInputConfig();
        this.setOnBackPressed();
        this.mEmojiKeyboard = new EmojiKeyboard(this.mActivity, this.mInput);
        this.mListener = listener;
    }

    // INITIALIZATION
    private void initBottomPanel() {
        this.mBottomPanel = this.mActivity.findViewById(R.id.panel);
        this.mBottomPanel.setNavigationIcon(R.drawable.input_emoji);
        this.mBottomPanel.setTitleTextColor(0xFFFFFFFF);
        this.mBottomPanel.inflateMenu(R.menu.telegram_menu);

        this.mBottomPanel.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChatPanel.this.isEmojiKeyboardVisible) {
                    ChatPanel.this.closeCurtain();
                    if (ChatPanel.this.mInput.isSoftKeyboardVisible()) {
                        ChatPanel.this.mBottomPanel.setNavigationIcon(R.drawable.ic_keyboard_grey600_24dp);
                        ChatPanel.this.mInput.hideSoftKeyboard();
                    } else {
                        ChatPanel.this.mBottomPanel.setNavigationIcon(R.drawable.input_emoji);
                        ChatPanel.this.mInput.showSoftKeyboard();
                    }
                } else {
                    ChatPanel.this.mBottomPanel.setNavigationIcon(R.drawable.ic_keyboard_grey600_24dp);
                    ChatPanel.this.closeCurtain();
                    ChatPanel.this.showEmojiKeyboard(0);
                }
            }
        });

        this.mBottomPanel.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (ChatPanel.this.mListener != null) {
                    if (item.getItemId() == R.id.action_attach) {
                        ChatPanel.this.mListener.onAttachClicked();
                    } else if (item.getItemId() == R.id.action_mic) {
                        if (ChatPanel.this.mInput.getText().toString().equals("")) {
                            ChatPanel.this.mListener.onMicClicked();
                        } else {
                            ChatPanel.this.mListener.onSendClicked();
                        }
                    }
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        });

        this.mCurtain = this.mActivity.findViewById(R.id.curtain);
    }

    private void setInputConfig() {
        this.mInput = this.mBottomPanel.findViewById(R.id.input);
        mInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.mInput.addOnSoftKeyboardListener(new EmojiEditText.OnSoftKeyboardListener() {
            @Override
            public void onSoftKeyboardDisplay() {
                if (!ChatPanel.this.isEmojiKeyboardVisible) {
                    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            Handler mainHandler = new Handler(ChatPanel.this.mActivity.getMainLooper());
                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    ChatPanel.this.openCurtain();
                                    ChatPanel.this.showEmojiKeyboard(0);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }
                    }, 150, TimeUnit.MILLISECONDS);
                }
            }

            @Override
            public void onSoftKeyboardHidden() {
                if (ChatPanel.this.isEmojiKeyboardVisible) {
                    ChatPanel.this.closeCurtain();
                    ChatPanel.this.hideEmojiKeyboard(200);
                }
            }
        });

        this.mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ChatPanel.this.mListener.onTyping(s.toString());
                final MenuItem micButton = ChatPanel.this.mBottomPanel.getMenu().findItem(R.id.action_mic);
                if (!ChatPanel.this.mInput.getText().toString().equals("") && ChatPanel.this.mToogleIcon) {
                    ChatPanel.this.mToogleIcon = Boolean.FALSE;
                    ChatPanel.this.mBottomPanel.findViewById(R.id.action_attach).animate().scaleX(0).scaleY(0).setDuration(150).start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ChatPanel.this.mBottomPanel.findViewById(R.id.action_mic).animate().scaleX(0).scaleY(0).setDuration(75).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                micButton.setIcon(R.drawable.ic_send_telegram);
                                ChatPanel.this.mBottomPanel.findViewById(R.id.action_mic).animate().scaleX(1).scaleY(1).setDuration(75).start();
                            }
                        }).start();
                    }
                } else if (ChatPanel.this.mInput.getText().toString().equals("")) {
                    ChatPanel.this.mToogleIcon = Boolean.TRUE;
                    ChatPanel.this.mBottomPanel.findViewById(R.id.action_attach).animate().scaleX(1).scaleY(1).setDuration(150).start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ChatPanel.this.mBottomPanel.findViewById(R.id.action_mic).animate().scaleX(0).scaleY(0).setDuration(75).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                micButton.setIcon(R.drawable.ic_microphone_grey600_24dp);
                                ChatPanel.this.mBottomPanel.findViewById(R.id.action_mic).animate().scaleX(1).scaleY(1).setDuration(75).start();
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setOnBackPressed() {
        this.mActivity.setOnBackPressed(new EmojiCompatActivity.OnBackPressedListener() {
            @Override
            public Boolean onBackPressed() {
                if (ChatPanel.this.isEmojiKeyboardVisible) {
                    ChatPanel.this.isEmojiKeyboardVisible = Boolean.FALSE;
                    ChatPanel.this.hideEmojiKeyboard(0);
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        });
    }

    private void showEmojiKeyboard(int delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ChatPanel.this.isEmojiKeyboardVisible = Boolean.TRUE;
        ChatPanel.this.mEmojiKeyboard.getEmojiKeyboardLayout().setVisibility(LinearLayout.VISIBLE);
    }

    private void hideEmojiKeyboard(int delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ChatPanel.this.mBottomPanel.setNavigationIcon(R.drawable.input_emoji);
        ChatPanel.this.isEmojiKeyboardVisible = Boolean.FALSE;
        ChatPanel.this.mEmojiKeyboard.getEmojiKeyboardLayout().setVisibility(LinearLayout.GONE);
    }

    private void openCurtain() {
        this.mCurtain.setVisibility(LinearLayout.VISIBLE);
    }

    private void closeCurtain() {
        this.mCurtain.setVisibility(LinearLayout.INVISIBLE);
    }

    //GETTER AND SETTERS
    public void setListener(ChatPanelEventListener mListener) {
        this.mListener = mListener;
    }

    public String getText() {
        return this.mInput.getText().toString();
    }

    public void setText(String text) {
        this.mInput.setText(text);
    }
}
