package com.example.owner.album;

import io.realm.RealmObject;

/**
 * Created by Owner on 2016/10/09.
 */

public class Book extends RealmObject {

    private static final String TAG = Book.class.getSimpleName();

    private String title;
    private String author;
    private int pages;
}
