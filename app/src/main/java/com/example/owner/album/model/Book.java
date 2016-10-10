package com.example.owner.album.model;

import io.realm.RealmObject;

/**
 * Created by Owner on 2016/10/09.
 */

public class Book extends RealmObject {

    private static final String TAG = Book.class.getSimpleName();

    private String title;
    private String author;
    private int pages;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
