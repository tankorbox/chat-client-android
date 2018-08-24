package com.chatmodule.model.layout;


public interface ChatPanelEventListener {

    public void onAttachClicked();

    public void onMicClicked();

    public void onSendClicked();

    public void onTyping(String msg);

}
