package com.example.submission.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.submission.R;
import com.example.submission.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    @SerializedName("items")
    @Expose
    private List<User> listUsers;

    private OnItemClickCallback onItemClickCallback;

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(listUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView txtUsername;
        CircleImageView avatarUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txt_username);
            avatarUser = itemView.findViewById(R.id.img_avatar);
        }

        void bind (final User user) {
            txtUsername.setText(user.getLogin());

            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .apply(new RequestOptions().override(60, 60))
                    .into(avatarUser);

            itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listUsers.get(getAdapterPosition())));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }
}

