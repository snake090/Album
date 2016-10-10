package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Related_Words extends RealmObject {

    @Required
    @Index
    private String related_words;



    public void setRelated_words(String related_words) {
        this.related_words = related_words;
    }
}
