package com.example.owner.album.query;

import com.example.owner.album.model.Picture_Info;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/11.
 */

public class Picture_Query {

    public ArrayList<String> Query(String name) {

        Realm r = Realm.getDefaultInstance();

        RealmResults<Picture_Info> results = r.where(Picture_Info.class).contains("classification_info_japs.name", name)
                .or()
                .contains("classification_info_engs.name", name)
                .or()
                .contains("landmark_eng", name)
                .or()
                .contains("landmark_jap", name).findAll();


        ArrayList<String> path = new ArrayList<>();
        for (Picture_Info pictureInfo : results) {
            path.add(pictureInfo.getPath());
        }
        r.close();
        return path;
    }

    public ArrayList<String> Query() {

        Realm r = Realm.getDefaultInstance();

        RealmResults<Picture_Info> results = r.where(Picture_Info.class).findAll();
        ArrayList<String> path = new ArrayList<>();
        for (Picture_Info pictureInfo : results) {
            path.add(pictureInfo.getPath());
        }
        r.close();
        return path;
    }

    public RealmResults<Picture_Info> Id_Query() {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> results = r.where(Picture_Info.class).findAll();
        r.close();
        return results;
    }

    public int Get_ID(String bitmap_path){

        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> results = r.where(Picture_Info.class).equalTo("path",bitmap_path).findAll();
        int id=results.get(0).getId();
        r.close();
        return id;
    }
    public ArrayList<String> Get_Picture_Info(int id){
        Realm r = Realm.getDefaultInstance();
        ArrayList<String> youso=new ArrayList<>();
        RealmResults<Picture_Info> results = r.where(Picture_Info.class).equalTo("id",id).findAll();
        youso.add(results.get(0).getLatitude());
        youso.add(results.get(0).getLongitude());
        youso.add(results.get(0).getLandmark_jap());
        youso.add(results.get(0).getPath());

        r.close();
        return youso;

    }


}
