package com.example.owner.album.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Picture_Info extends RealmObject {

    @PrimaryKey
    private int id;
    private  int tag_id;
    @Required
    private String path;
    private Date date;
    private String longitude;
    private String latitude;

    public void setId(int id) {
        this.id = id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
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
}
