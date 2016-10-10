package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Classification_Info_Eng extends RealmObject {
    RealmList<Related_Words> related_wordses;
    @Required
    private String name;

    public void setRelated_wordses(RealmList<Related_Words> related_wordses) {
        this.related_wordses = related_wordses;
    }

    public void setName(String name) {
        this.name = name;
    }
}
