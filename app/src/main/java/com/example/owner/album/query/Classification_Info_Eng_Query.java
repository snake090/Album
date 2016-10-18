package com.example.owner.album.query;

import com.example.owner.album.model.*;
import com.example.owner.album.model.Classification_Info_Eng;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/18.
 */

public class Classification_Info_Eng_Query {
    public RealmResults<Classification_Info_Eng>  Double_Check(){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Classification_Info_Eng> realmResults=r.where(Classification_Info_Eng.class).findAll();

        return  realmResults;

    }
    public RealmResults<Classification_Info_Eng>  Double_Check(String name){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Classification_Info_Eng> realmResults=r.where(Classification_Info_Eng.class).equalTo("name",name).findAll();

        return  realmResults;

    }




}
