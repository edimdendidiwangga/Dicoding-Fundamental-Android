package com.example.submission.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.submission.R;
import com.example.submission.adapter.TabsAdapter;
import com.example.submission.api.Api;
import com.example.submission.api.ApiInterface;
import com.example.submission.model.User;
import com.example.submission.viewmodel.UserViewModel;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.submission.db.DatabaseContract.UserColumns.CONTENT_URI;
import static com.example.submission.db.DatabaseContract.UserColumns.LOGIN;
import static com.example.submission.db.DatabaseContract.UserColumns.NAME;
import static com.example.submission.db.DatabaseContract.UserColumns.AVATAR_URL;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_USER = "extra_user";
    CircleImageView tvAvatar;
    TextView tvName;
    TextView tvSumFollower;
    TextView tvSumFollowing;
    ImageButton btnFavourite;

    ContentValues userValue = new ContentValues();

    public String login = "";
    public Integer id;
    Boolean isFavorite = false;
    Boolean hasAddFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        tvAvatar = findViewById(R.id.tv_avatar);
        tvName = findViewById(R.id.tv_name);
        tvSumFollower = findViewById(R.id.tv_followers);
        tvSumFollowing = findViewById(R.id.tv_following);
        btnFavourite = findViewById(R.id.btn_favourite);
        btnFavourite.setOnClickListener(this);

        User user = getIntent().getParcelableExtra(EXTRA_USER);
        login = user.getLogin();
        getDetailUser(user.getLogin());

        Glide.with(getApplicationContext())
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(150,150))
                .into(tvAvatar);

        UserViewModel userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.setUserLocal(getContentResolver());
        userViewModel.getUsersLocal().observe(this, users -> {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getLogin().equals(login))
                {
                    isFavorite = true;
                    id = users.get(i).getId();
                }
            }
            handleFavorite();
        });

        TabsAdapter sectionsPageAdapter = new TabsAdapter(this, getSupportFragmentManager(), user);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPageAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void getDetailUser(String username) {
        ApiInterface Actions;
        retrofit2.Call<User> Hit;
        try {
            Actions = Api.getApi().create(ApiInterface.class);
            Hit = Actions.getDetailUser(username);
            Hit.enqueue(new Callback<User>() {
                @Override
                public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                    User mUser = response.body();
                    tvName.setText(mUser.getName());
                    tvSumFollower.setText(mUser.getFollowers().toString());
                    tvSumFollowing.setText(mUser.getFollowing().toString());
                    getSupportActionBar().setTitle(mUser.getName());

                    userValue.put(LOGIN, mUser.getLogin());
                    userValue.put(NAME, mUser.getName());
                    userValue.put(AVATAR_URL, mUser.getAvatarUrl());
                }

                @Override
                public void onFailure(retrofit2.Call<User> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favourite && !hasAddFavorite) {
            hasAddFavorite = true;
            if (isFavorite) {
                isFavorite = false;
                Uri uriWithId = Uri.parse(CONTENT_URI + "/" + id);
                getContentResolver().delete(uriWithId, null, null );
                btnFavourite.setImageResource(R.drawable.ic_favorite_white);
                Toast.makeText(UserDetailActivity.this, "Success Remove from Favorite", Toast.LENGTH_SHORT).show();
            } else {
                isFavorite = true;
                getContentResolver().insert(CONTENT_URI, userValue);
                btnFavourite.setImageResource(R.drawable.ic_favorite_red);
                Toast.makeText(UserDetailActivity.this, "Success Add to Favorite", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void handleFavorite(){
        if (isFavorite) {
            btnFavourite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            btnFavourite.setImageResource(R.drawable.ic_favorite_white);
        }
    }
}

