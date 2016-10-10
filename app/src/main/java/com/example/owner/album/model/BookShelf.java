package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Owner on 2016/10/09.
 */

public class BookShelf extends RealmObject{
    private static final String TAG = BookShelf.class.getSimpleName();

    private RealmList<Book> books;

    public RealmList<Book> getBooks() {
        return books;
    }

    public void setBooks(RealmList<Book> books) {
        this.books = books;
    }
}
