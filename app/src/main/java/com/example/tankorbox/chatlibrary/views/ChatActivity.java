package com.example.tankorbox.chatlibrary.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chatmodule.controller.ChatPanel;
import com.chatmodule.model.layout.ChatPanelEventListener;
import com.chatmodule.model.layout.EmojiCompatActivity;
import com.example.tankorbox.chatlibrary.BuildConfig;
import com.example.tankorbox.chatlibrary.R;
import com.example.tankorbox.chatlibrary.adapters.MessageAdapter;
import com.example.tankorbox.chatlibrary.helpers.TimestampUtil;
import com.example.tankorbox.chatlibrary.models.Group;
import com.example.tankorbox.chatlibrary.models.Message;
import com.example.tankorbox.chatlibrary.models.MessageType;
import com.example.tankorbox.chatlibrary.services.MessageService;
import com.example.tankorbox.chatlibrary.services.ServiceGenerator;
import com.example.tankorbox.chatlibrary.services.responses.LogInResponse;
import com.example.tankorbox.chatlibrary.services.responses.MessageGetResponse;
import com.example.tankorbox.chatlibrary.socket.Events;
import com.example.tankorbox.chatlibrary.socket.SocketHandler;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;

public class ChatActivity extends EmojiCompatActivity implements ChatPanelEventListener {

    public static final String TAG = "ChatActivity";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    private ChatPanel mBottomPanel;
    private RecyclerView mMessages;
    private MessageAdapter mAdapter;
    private Group group;
    private LogInResponse logInResponse;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        group = (Group) getIntent().getExtras().getSerializable("group");
        logInResponse = (LogInResponse) getIntent().getExtras().getSerializable("user");

        initToolbar();
        initDrawerMenu();
        setTelegramTheme();
        initMessageList();
        mBottomPanel = new ChatPanel(this, this);

        try {
            mSocket = IO.socket(BuildConfig.CHAT);
            mSocket.io().on(Manager.EVENT_TRANSPORT, args -> {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, args1 -> {
                    @SuppressWarnings("unchecked")
                    Map<String, List<String>> headers = (Map<String, List<String>>) args1[0];
                    // modify request headers
                    headers.put("authorization", Collections.singletonList("Bearer " + logInResponse.getUser().getAccess_token()));
                });
            });
            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.i("tag1", "connect");
            }).on(Events.MESSAGE_RECEIVE, args -> {
                if (args.length > 0 && args[0] != null) {
                    Message income = new Gson().fromJson(args[0].toString(), Message.class);
                    echoMessage(income);
                }
            }).on(Events.MESSAGE_UPDATE, args -> {
                Log.i("tag1", "message update");
            }).on(Events.TYPING, args -> {
                if (args.length > 0 && args[0] != null) {
                    Message income = new Gson().fromJson(args[0].toString(), Message.class);
                    updateTypingStatus(income);
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {
                Log.i("tag1", "disconnect");
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void updateTypingStatus(Message income) {
        if (income.getGroupId().equals(group.getId())) {
            Observable.fromArray(income)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(message -> {
                        if (message.isTyping()) {
                            this.getSupportActionBar().setTitle(income.getDisplayName() + " is typing...");
                        }
                        else {
                            this.getSupportActionBar().setTitle("Telegram");
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SocketHandler.sendTyping(mSocket, logInResponse, group.getId(), "");
        mSocket.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mSocket != null && !mSocket.connected()) {
            mSocket.connect();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toogle:

                break;
            case android.R.id.home:
                if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    this.mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // INITIALIZATIONS
    private void initDrawerMenu() {
        this.mDrawerLayout = findViewById(R.id.drawer_layout);
        this.findViewById(R.id.github_thumbnail);

        CircularImageView thumbnail = this.findViewById(R.id.github_thumbnail);

        Picasso.with(this)
                .load(R.drawable.github)
                .resize(60, 60)
                .centerCrop()
                .into(thumbnail);

        thumbnail.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/instachat/emoji-library"));
            startActivity(browserIntent);
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                this.mDrawerLayout,
                ChatActivity.this.mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };

        this.mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initMessageList() {
        this.mMessages = this.findViewById(R.id.messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(Boolean.TRUE);
        this.mMessages.setLayoutManager(linearLayoutManager);
        this.mAdapter = new MessageAdapter(new ArrayList<Message>());
        this.mMessages.setAdapter(mAdapter);
        loadMessages();
    }

    private void loadMessages() {
        ServiceGenerator.shared().createService(MessageService.class)
                .getMessages(group.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageGetResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageGetResponse messageGetResponse) {
                        Log.i(ChatActivity.class.getName(), String.valueOf(messageGetResponse.getMessages().size()));
                        mAdapter.addMessages(messageGetResponse.getMessages());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initToolbar() {
        this.mToolbar = this.findViewById(R.id.toolbar);
        this.mToolbar.setTitleTextColor(0xFFFFFFFF);
        this.mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        this.getWindow().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.telegram_bkg));
        this.setSupportActionBar(this.mToolbar);
        this.getSupportActionBar().setTitle("My ChatApp");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setTelegramTheme() {
        ChatActivity.this.mToolbar.setTitle("My ChatApp");
        ChatActivity.this.getWindow().setBackgroundDrawable(ChatActivity.this.getResources().getDrawable(R.drawable.telegram_bkg));
        this.mToolbar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryTelegram));
        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryTelegram));
        }
    }

    // CHAT PANEL INTERFACE
    @Override
    public void onAttachClicked() {
        Toast.makeText(ChatActivity.this, "Attach was clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMicClicked() {
        Log.i(TAG, "Mic was clicked");
        Toast.makeText(ChatActivity.this, "Mic was clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendClicked() {
        Log.i(TAG, "message: " + this.mBottomPanel.getText());
        Message message = new Message();
        message.setType(MessageType.OUTGOING);
        message.setTimestamp(TimestampUtil.getCurrentTimestamp());
        message.setContent(this.mBottomPanel.getText());
        message.setGroupId(group.getId());
        SocketHandler.postMessage(mSocket, logInResponse, this.mBottomPanel.getText(), group.getId());
        this.mBottomPanel.setText("");
        this.updateMessageList(message);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onTyping(String msg) {
        SocketHandler.sendTyping(mSocket ,logInResponse, group.getId(), msg);
    }

    @SuppressLint("StaticFieldLeak")
    private void echoMessage(final Message income) {
        new AsyncTask<Void, Void, Message>() {
            @Override
            protected void onPostExecute(Message message) {
                ChatActivity.this.updateMessageList(message);
            }

            @Override
            protected Message doInBackground(Void... params) {
                try {
                    Thread.sleep(300);
                    Message outgoing = new Message();
                    outgoing.setType(MessageType.INCOME);
                    outgoing.setTimestamp(TimestampUtil.getCurrentTimestamp());
                    outgoing.setContent(income.getData());
                    outgoing.setGroupId(income.getGroupId());
                    outgoing.setUserId(income.getUserId());
                    return outgoing;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void updateMessageList(Message message) {
        if (message.getGroupId().equals(group.getId())) {
            this.mAdapter.getDataset().add(message);
            this.mAdapter.notifyDataSetChanged();
            this.mMessages.scrollToPosition(this.mAdapter.getItemCount() - 1);
        }
    }
}
