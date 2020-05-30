package com.example.submission.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    @SerializedName("followers")
    @Expose
    private Integer followers;

    @SerializedName("following")
    @Expose
    private Integer following;

    private Integer id;

    protected User(Parcel in) {
        login = in.readString();
        avatarUrl = in.readString();
    }

    public User(String login, String avatarUrl, Integer followers, Integer following) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.followers = followers;
        this.following = following;
    }

    public User(Integer id, String login, String name, String avatarUrl){
        this.id = id;
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(avatarUrl);
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() { return name; }

    public Integer getFollowers() {
        return followers;
    }

    public Integer getFollowing() {
        return following;
    }
}
