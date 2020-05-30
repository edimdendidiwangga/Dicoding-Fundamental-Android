package com.example.submission.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.submission.R;
import com.example.submission.adapter.FavouriteAdapter;
import com.example.submission.helpers.MappingHelper;
import com.example.submission.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.submission.db.DatabaseContract.UserColumns.CONTENT_URI;
import static com.example.submission.ui.UserDetailActivity.EXTRA_USER;

public class Favourite extends AppCompatActivity implements LoadUserCallback {
    private ProgressBar progressBar;
    private RecyclerView rvFavourites;
    private FavouriteAdapter favAdapter;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        getSupportActionBar().setTitle("Favorite User");

        progressBar = findViewById(R.id.favourite_progressBar);
        rvFavourites = findViewById(R.id.rv_favourite);
        rvFavourites.setLayoutManager(new LinearLayoutManager(this));
        rvFavourites.setHasFixedSize(true);
        favAdapter = new FavouriteAdapter();
        rvFavourites.setAdapter(favAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver observer = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, observer);

        if (savedInstanceState == null) {
            new LoadUserAsync(this, this).execute();
        } else {
            ArrayList<User> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favAdapter.setListUser(list);
                favAdapter.setOnItemClickCallback(data -> {
                    Intent toDetailUser = new Intent(Favourite.this, UserDetailActivity.class);
                    toDetailUser.putExtra(EXTRA_USER, data);
                    Favourite.this.startActivity(toDetailUser);
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favAdapter.getListUser());
    }

    @Override
    public void preExecute() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    @Override
    public void postExecute(ArrayList<User> userLocal) {
        progressBar.setVisibility(View.INVISIBLE);
        if (userLocal.size() > 0) {
            favAdapter.setListUser(userLocal);
            favAdapter.setOnItemClickCallback(data -> {
                Intent goToDetailUser = new Intent(Favourite.this, UserDetailActivity.class);
                goToDetailUser.putExtra(EXTRA_USER, data);
                Favourite.this.startActivity(goToDetailUser);
            });
        } else {
            favAdapter.setListUser(new ArrayList<>());
            Toast.makeText(this, "List Favorite Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private static class LoadUserAsync extends AsyncTask<Void, Void, ArrayList<User>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        LoadUserAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            assert cursor != null;
            return MappingHelper.mapCursorToArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> userLocals) {
            super.onPostExecute(userLocals);
            weakCallback.get().postExecute(userLocals);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUserAsync(context, (LoadUserCallback) context).execute();
        }
    }


}

interface LoadUserCallback {
    void preExecute();
    void postExecute(ArrayList<User> userLocal);
}
