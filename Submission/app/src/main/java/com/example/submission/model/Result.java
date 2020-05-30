package com.example.submission.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("items")
    @Expose
    private List<User> resultMember;

    public Result(List<User> resultMember) {
        this.resultMember = resultMember;
    }

    public List<User> getResultMember() {
        return resultMember;
    }
}
