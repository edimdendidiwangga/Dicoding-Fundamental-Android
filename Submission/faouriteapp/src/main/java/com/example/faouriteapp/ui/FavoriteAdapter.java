package com.example.faouriteapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.faouriteapp.R;
import com.example.faouriteapp.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private ArrayList<User> users = new ArrayList<>();

    FavoriteAdapter() {
    }

    public ArrayList<User> getListUser() {
        return users;
    }

    public void setListUser(ArrayList<User> listUser) {
        this.users.clear();
        this.users.addAll(listUser);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        final TextView tvName;
        final CircleImageView imgAvatar;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_username);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
        }

        void bind (final User user) {
            tvName.setText(user.getLogin());
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .apply(new RequestOptions().override(60, 60))
                    .into(imgAvatar);
        }
    }
}
