package com.example.submission.helpers;

import android.database.Cursor;

import com.example.submission.db.DatabaseContract;
import com.example.submission.model.User;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<User> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<User> usersList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            String login = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN));
            String name = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME));
            String avatar_url = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));
            usersList.add(new User(id, login, name, avatar_url));
        }

        return usersList;
    }
}
