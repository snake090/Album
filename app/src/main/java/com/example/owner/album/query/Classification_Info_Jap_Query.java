package com.example.owner.album.query;

import com.example.owner.album.model.Classification_Info_Jap;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/19.
 */

public class Classification_Info_Jap_Query {
    public RealmResults<Classification_Info_Jap> Double_Check(String name){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Classification_Info_Jap> realmResults=r.where(Classification_Info_Jap.class).equalTo("name",name).findAll();

        return  realmResults;

    }
}
