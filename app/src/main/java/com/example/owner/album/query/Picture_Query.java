package com.example.owner.album.query;

import com.example.owner.album.model.Picture_Info;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/11.
 */

public class Picture_Query {

    public ArrayList<String> Query(String name){

        Realm r = Realm.getDefaultInstance();

        RealmResults<Picture_Info>results=r.where(Picture_Info.class).contains("classification_info_japs.name",name)
                                                                        .or()
                                                                        .contains("classification_info_engs.name",name)
                                                                        .or()
                                                                        .contains("landmark_eng",name)
                                                                        .or()
                                                                        .contains("landmark_jap",name).findAll();


        ArrayList<String>path=new ArrayList<>();
        for(Picture_Info pictureInfo:results){
            path.add(pictureInfo.getPath());
        }
        r.close();
        return path;
    }
    public ArrayList<String> Query(){

        Realm r = Realm.getDefaultInstance();

        RealmResults<Picture_Info>results=r.where(Picture_Info.class).findAll();
        ArrayList<String>path=new ArrayList<>();
        for(Picture_Info pictureInfo:results){
            path.add(pictureInfo.getPath());
        }
        r.close();
        return path;
    }

    public RealmResults<Picture_Info> Id_Query(){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info>results=r.where(Picture_Info.class).findAll();
        r.close();
        return results;
    }


}
