package com.example.owner.album.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Owner on 2016/11/09.
 */

public class Keyword extends RealmObject {

    private RealmList<Related_Words> related_wordses;
    private String Keyword_Jap;
    private String Keyword_Eng;


    public void setRelated_wordses(RealmList<Related_Words> related_wordses) {
        this.related_wordses = related_wordses;
    }

    public void setKeyword_Jap(String keyword_Jap) {
        Keyword_Jap = keyword_Jap;
    }

    public RealmList<Related_Words> getRelated_wordses() {
        return related_wordses;
    }

    public void setKeyword_Eng(String keyword_Eng) {
        Keyword_Eng = keyword_Eng;
    }

    public String getKeyword_Eng() {
        return Keyword_Eng;
    }

    public String getKeyword_Jap() {
        return Keyword_Jap;
    }
}
