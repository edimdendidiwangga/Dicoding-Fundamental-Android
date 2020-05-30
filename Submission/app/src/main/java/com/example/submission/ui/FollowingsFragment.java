package com.example.submission.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.submission.R;
import com.example.submission.adapter.UserAdapter;
import com.example.submission.model.User;
import com.example.submission.viewmodel.FollowingViewModel;

import java.util.List;


public class FollowingsFragment extends Fragment {

    private RecyclerView rvFollowing;
    private ProgressBar progressBar;
    private UserAdapter userAdapter;
    private FollowingViewModel followingViewModel;

    public static final String EXTRA_FOLLOWING = "extra_following";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_followings, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarFollowing);
        rvFollowing = view.findViewById(R.id.rvFollowing);
        rvFollowing.setLayoutManager(new LinearLayoutManager(getContext()));

        String username = getArguments().getString(EXTRA_FOLLOWING);

        showLoading(false);
        followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);

        followingViewModel.setListFollowing(username, getContext());

        followingViewModel.getListFollowing().observe(getViewLifecycleOwner(), list -> {
            userAdapter = new UserAdapter();
            userAdapter.notifyDataSetChanged();
            userAdapter.setListUsers(list);

            rvFollowing.setAdapter(userAdapter);
            showLoading(false);
            userAdapter.setOnItemClickCallback((User data) -> {
                showLoading(true);
                Intent goToDetailUser = new Intent(getContext(), UserDetailActivity.class);
                goToDetailUser.putExtra(UserDetailActivity.EXTRA_USER, data);
                startActivity(goToDetailUser);
            });
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading(false);
    }
}
