package com.example.submission.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.submission.api.Api;
import com.example.submission.api.ApiInterface;
import com.example.submission.helpers.MappingHelper;
import com.example.submission.model.Result;
import com.example.submission.model.User;
import com.example.submission.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

import static com.example.submission.db.DatabaseContract.UserColumns.CONTENT_URI;

public class UserViewModel extends ViewModel {
    private MutableLiveData<List<User>> listUsers = new MutableLiveData<>();

    public MutableLiveData<List<User>> getListUsers() {
        return listUsers;
    }

    public void setListUsers(String username, final Context context) {
        ApiInterface Actions;
        retrofit2.Call<Result> Hit;

        try {
            Actions = Api.getApi().create(ApiInterface.class);
            Hit = Actions.getUsers(username);
            Hit.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(retrofit2.Call<Result> call, Response<Result> res) {
                    List<User> users = res.body().getResultMember();
                    listUsers.postValue(users);
                    if (users.isEmpty()) {
                        Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<Result> call, Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MutableLiveData<List<User>> usersDataLocal = new MutableLiveData<>();

    public MutableLiveData<List<User>> getUsersLocal() {
        return usersDataLocal;
    }

    public void setUserLocal(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            ArrayList<User> userLocal = MappingHelper.mapCursorToArrayList(cursor);
            cursor.close();
            usersDataLocal.postValue(userLocal);
        }
    }
}

