package com.example.tankorbox.chatlibrary.services;


import com.example.tankorbox.chatlibrary.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.tankorbox.chatlibrary.config.Config.TOKEN;


public class ServiceGenerator {


    private static ServiceGenerator generator;

    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(new AuthenticationInterceptor(TOKEN))
            .build();

    private Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.HOME_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    private ServiceGenerator() {

    }

    public synchronized static ServiceGenerator shared() {
        if (generator == null) {
            generator = new ServiceGenerator();
        }
        return generator;
    }

    public <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }


    public static class AuthenticationInterceptor implements Interceptor {

        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization", "Bearer " + authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}