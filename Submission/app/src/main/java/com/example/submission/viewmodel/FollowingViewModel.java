package com.example.submission.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission.api.Api;
import com.example.submission.api.ApiInterface;
import com.example.submission.model.User;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class FollowingViewModel extends ViewModel {

    private MutableLiveData<List<User>> listFollowing = new MutableLiveData<>();
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MutableLiveData<List<User>> getListFollowing() {
        return listFollowing;
    }

    public void setListFollowing(String username, Context context) {
        ApiInterface Service;
        retrofit2.Call<List<User>> Call;

        try {
            Service = Api.getApi().create(ApiInterface.class);
            Call = Service.getFollowing(username);
            Call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(retrofit2.Call<List<User>> call, Response<List<User>> response) {

                    Log.d("Response", "" + " " + response.body());
                    List<User> listUser;
                    listUser = response.body();
                    listFollowing.postValue(listUser);
                }

                @Override
                public void onFailure(retrofit2.Call<List<User>> call, Throwable t) {
                    Log.d("Message", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
