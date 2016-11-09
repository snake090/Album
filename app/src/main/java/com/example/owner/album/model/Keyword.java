package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Owner on 2016/11/09.
 */

public class Keyword extends RealmObject {

    RealmList<Album_Name_Related_Words> album_name_related_wordses;
    private String Keyword;

    public void setAlbum_name_related_wordses(RealmList<Album_Name_Related_Words> album_name_related_wordses) {
        this.album_name_related_wordses = album_name_related_wordses;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }
}
