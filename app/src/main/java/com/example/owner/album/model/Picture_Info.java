package com.example.owner.album.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Picture_Info extends RealmObject {

    RealmList<Tag> tags;
    @PrimaryKey
    private int id;
    @Required
    private String path;
    private Date date;
    private String longitude;
    private String latitude;

    public void setTags(RealmList<Tag> tags) {
        this.tags = tags;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPath() {
        return path;
    }


}
