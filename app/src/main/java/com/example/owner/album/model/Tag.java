package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2016/10/10.
 */

public class Tag extends RealmObject {

    Classification_Info_Eng classification_info_engs;
    Classification_Info_Jap classification_info_japs;

    public void setClassification_info_engs(Classification_Info_Eng classification_info_engs) {
        this.classification_info_engs = classification_info_engs;
    }

    public void setClassification_info_japs(Classification_Info_Jap classification_info_japs) {
        this.classification_info_japs = classification_info_japs;
    }

}
