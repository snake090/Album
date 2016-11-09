package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Album extends RealmObject{

    RealmList<Keyword> keywords;
    RealmList<Picture_Info> picture_infos;
    @PrimaryKey
    private int album_id;
    @Required
    private String album_name;




    public void setKeywords(RealmList<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void setPicture_infos(RealmList<Picture_Info> picture_infos) {
        this.picture_infos = picture_infos;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }


    public RealmList<Picture_Info> getPicture_infos() {
        return picture_infos;
    }
}
