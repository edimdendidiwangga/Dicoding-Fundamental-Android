package com.example.submission.api;

import com.example.submission.model.Result;
import com.example.submission.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/search/users")
    Call<Result> getUsers(@Query("q") String username);

    @GET("/users/{username}")
    Call<User> getDetailUser(@Path("username") String username);

    @GET("/users/{username}/followers")
    Call<List<User>> getFollowers(@Path("username") String username);

    @GET("/users/{username}/following")
    Call<List<User>> getFollowing(@Path("username") String username);

}
