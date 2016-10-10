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
    RealmList<Classification_Info_Eng> classification_info_engs;
    RealmList<Classification_Info_Jap> classification_info_japs;
    @PrimaryKey
    private int related_words_id;
    @Required
    @Index
    private String related_words;

    public void setClassification_info_engs(RealmList<Classification_Info_Eng> classification_info_engs) {
        this.classification_info_engs = classification_info_engs;
    }

    public void setClassification_info_japs(RealmList<Classification_Info_Jap> classification_info_japs) {
        this.classification_info_japs = classification_info_japs;
    }

    public void setRelated_words_id(int related_words_id) {
        this.related_words_id = related_words_id;
    }

    public void setRelated_words(String related_words) {
        this.related_words = related_words;
    }
}
