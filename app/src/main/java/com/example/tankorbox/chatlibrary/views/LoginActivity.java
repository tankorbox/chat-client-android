package com.example.tankorbox.chatlibrary.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.chatmodule.util.SoftKeyboardUtil;
import com.example.tankorbox.chatlibrary.R;
import com.example.tankorbox.chatlibrary.services.ServiceGenerator;
import com.example.tankorbox.chatlibrary.services.UserService;
import com.example.tankorbox.chatlibrary.services.requests.LogInRequest;
import com.example.tankorbox.chatlibrary.services.responses.LogInResponse;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {
    private EditText mEdtUsername, mEdtPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEdtUsername = findViewById(R.id.edtUsername);
        mEdtPassword = findViewById(R.id.edtPassword);
        mEdtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                SoftKeyboardUtil.dismissSoftKeyboard(LoginActivity.this, mEdtPassword);
            }
        });
        mEdtUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                SoftKeyboardUtil.dismissSoftKeyboard(LoginActivity.this, mEdtUsername);
            }
        });

        (findViewById(R.id.btnLogin)).setOnClickListener(view -> {
            login(mEdtUsername.getText().toString(), mEdtPassword.getText().toString());
        });
    }

    private void login(String username, String password) {
        final LogInRequest logInRequest = new LogInRequest(username, password);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(logInRequest));
        ServiceGenerator.shared().createService(UserService.class).login(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LogInResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        SharedPreferences.Editor editor = getSharedPreferences("my_data", MODE_PRIVATE).edit();
                        editor.putString("token", logInResponse.getUser().getAccess_token());
                        editor.putInt("expire", logInResponse.getUser().getExpire_in());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", logInResponse);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
