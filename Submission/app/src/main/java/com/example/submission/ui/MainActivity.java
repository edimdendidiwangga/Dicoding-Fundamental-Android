package com.example.submission.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.submission.R;
import com.example.submission.adapter.UserAdapter;
import com.example.submission.model.User;
import com.example.submission.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editSearch;
    private ProgressBar progressBar;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private UserViewModel userViewModel;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        progressBar = findViewById(R.id.progressBar);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonSearch.setOnClickListener(this);

        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);

        userViewModel.getListUsers().observe(this, list -> {
            adapter = new UserAdapter();
            adapter.setListUsers(list);
            recyclerView.setAdapter(adapter);
            showLoading(false);

            adapter.setOnItemClickCallback((User data) -> {
                Intent toDetailUser = new Intent(MainActivity.this, UserDetailActivity.class);
                toDetailUser.putExtra(UserDetailActivity.EXTRA_USER, data);
                startActivity(toDetailUser);
            });
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSearch) {
            showLoading(true);
            String valueEditSearch = editSearch.getText().toString();
            if (valueEditSearch.isEmpty()) {
                editSearch.setError("Username Required");
                editSearch.setFocusable(true);
                showLoading(false);
            } else {
                String username = valueEditSearch;
                userViewModel.setListUsers(username, getApplicationContext());
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent toSetting = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(toSetting);
        }

        if (item.getItemId() == R.id.favorite) {
            Intent favoriteIntent = new Intent(MainActivity.this, Favourite.class);
            startActivity(favoriteIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoading(Boolean isOpen) {
        if (isOpen) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}

