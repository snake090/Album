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

    RealmList<Classification_Info_Eng> classification_info_engs;
    RealmList<Classification_Info_Jap> classification_info_japs;
    @PrimaryKey
    private int id;
    @Required
    private String path;
    private Date date;
    private String longitude;
    private String latitude;

    public void setClassification_info_engs(RealmList<Classification_Info_Eng> classification_info_engs) {
        this.classification_info_engs = classification_info_engs;
    }

    public void setClassification_info_japs(RealmList<Classification_Info_Jap> classification_info_japs) {
        this.classification_info_japs = classification_info_japs;
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

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }


}
