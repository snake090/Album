package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Album extends RealmObject{

    RealmList<Album_Name_Related_Words> album_name_related_wordses;
    RealmList<Picture_Info> picture_infos;
    @PrimaryKey
    private int album_id;
    @Required
    private String album_name;
    @Required
    private String Album_name_eng;


    public void setAlbum_name_related_wordses(RealmList<Album_Name_Related_Words> album_name_related_wordses) {
        this.album_name_related_wordses = album_name_related_wordses;
    }

    public void setPicture_infos(RealmList<Picture_Info> picture_infos) {
        this.picture_infos = picture_infos;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }


    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public void setGetAlbum_name_eng(String getAlbum_name_eng) {
        this.Album_name_eng = getAlbum_name_eng;
    }

    public RealmList<Album_Name_Related_Words> getAlbum_name_related_wordses() {
        return album_name_related_wordses;
    }

    public String getGetAlbum_name_eng() {
        return Album_name_eng;
    }

    public RealmList<Picture_Info> getPicture_infos() {
        return picture_infos;
    }
}
