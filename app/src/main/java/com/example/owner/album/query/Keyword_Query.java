package com.example.owner.album.query;

import com.example.owner.album.model.Keyword;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/11/09.
 */

public class Keyword_Query {

    public RealmResults<Keyword>Double_Check(String name){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Keyword> realmResults=r.where(Keyword.class).equalTo("Keyword_Jap",name).findAll();

        return realmResults;
    }

}
