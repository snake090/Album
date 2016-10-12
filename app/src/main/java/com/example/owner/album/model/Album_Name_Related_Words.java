package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Album_Name_Related_Words extends RealmObject {

    @Required
    @Index
    private String album_nmae_related_words;

    public void setAlbum_nmae_related_words(String album_nmae_related_words) {
        this.album_nmae_related_words = album_nmae_related_words;
    }

    public String getAlbum_nmae_related_words() {
        return album_nmae_related_words;
    }
}
