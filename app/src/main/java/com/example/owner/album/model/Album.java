package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Album extends RealmObject{
    RealmList<Picture_Info> picture_infos;
    @PrimaryKey
    private int album_id;
    private int picture_id;
    @Required
    private String album_name;
    @Required
    private String getAlbum_name_eng;

    public void setPicture_infos(RealmList<Picture_Info> picture_infos) {
        this.picture_infos = picture_infos;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public void setPicture_id(int picture_id) {
        this.picture_id = picture_id;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public void setGetAlbum_name_eng(String getAlbum_name_eng) {
        this.getAlbum_name_eng = getAlbum_name_eng;
    }
}
