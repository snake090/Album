package com.example.owner.album.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Classification_Info_Eng extends RealmObject {
    private Tag tag;
    @PrimaryKey
    private  int classification_id;
    @Required
    private String name;

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setClassification_id(int classification_id) {
        this.classification_id = classification_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
