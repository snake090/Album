package com.example.owner.album.query;

import com.example.owner.album.model.Picture_Info;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/11.
 */

public class Picture_Query {

    public RealmResults<Picture_Info> Query(){

        Realm r = Realm.getDefaultInstance();

        RealmResults<Picture_Info>results=r.where(Picture_Info.class).equalTo("tags.classification_info_japs.name","panda")
                                                                        .or()
                                                                        .equalTo("tags.classification_info_engs.name","panda").findAll();
        r.close();
        return results;
    }
    public RealmResults<Picture_Info> Id_Query(){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info>results=r.where(Picture_Info.class).findAll();

        r.close();
        return results;
    }

}
