package com.example.tankorbox.chatlibrary;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        MainActivity.this.getWindow().setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.telegram_bkg));
        this.mToolbar.setBackgroundColor(this.getResources().getColor(R.color.colorPrimaryTelegram));
        if (Build.VERSION.SDK_INT >= 21) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryTelegram));
        }
    }
}
