package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Tag extends RealmObject {

    private RealmList<Picture_Info> picture_infos;
    @PrimaryKey
    private int tag_id;
    private int classification_id;

    public void setPicture_infos(RealmList<Picture_Info> picture_infos) {
        this.picture_infos = picture_infos;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public void setClassification_id(int classification_id) {
        this.classification_id = classification_id;
    }
}
