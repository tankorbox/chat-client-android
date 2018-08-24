package com.example.tankorbox.chatlibrary.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.tankorbox.chatlibrary.R;
import com.example.tankorbox.chatlibrary.adapters.GroupAdapter;
import com.example.tankorbox.chatlibrary.models.Group;
import com.example.tankorbox.chatlibrary.services.GroupService;
import com.example.tankorbox.chatlibrary.services.ServiceGenerator;
import com.example.tankorbox.chatlibrary.services.responses.GroupResponse;
import com.example.tankorbox.chatlibrary.services.responses.LogInResponse;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.util.ConnectConsumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements GroupAdapter.AdapterListener {
    private Disposable mFetchGroupDisposable;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private LogInResponse logInResponse;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawerMenu();
        setTelegramTheme();

        logInResponse = (LogInResponse) getIntent().getExtras().getSerializable("user");

        ServiceGenerator.shared().createService(GroupService.class).getGroups()
                .doOnSubscribe(new ConnectConsumer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GroupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mFetchGroupDisposable = d;
                        mCompositeDisposable.add(mFetchGroupDisposable);
                    }

                    @Override
                    public void onNext(GroupResponse groupResponse) {
                        setUpRecyclerView(groupResponse.getGroups());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setUpRecyclerView(List<Group> groups) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GroupAdapter(groups, this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    // INITIALIZATIONS
    private void initDrawerMenu() {
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
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
                mDrawerLayout,
                MainActivity.this.mToolbar,
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

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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
        MainActivity.this.mToolbar.setTitle("My ChatApp");
        MainActivity.this.getWindow().setBackgroundDrawableResource(android.R.color.white);
        this.mToolbar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryTelegram));
        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryTelegram));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    @Override
    public void onClick(Group group) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", logInResponse);
        bundle.putSerializable("group", group);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
