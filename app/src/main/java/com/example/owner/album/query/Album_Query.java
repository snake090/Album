package com.example.owner.album.query;

import android.util.Log;

import com.example.owner.album.model.Album;
import com.example.owner.album.model.Album_Name_Related_Words;
import com.example.owner.album.model.Picture_Info;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/11.
 */

public class Album_Query {

    public void Picture_Query(Album album,int id) {
        /*
        Realm r = Realm.getDefaultInstance();
        RealmList<Picture_Info>picture_infos=new RealmList<>();
        RealmList<Picture_Info>picture_infos1=new RealmList<>();
        RealmResults<Album> results = r.where(Album.class).equalTo("album_id", id).findAll();
        RealmResults<Picture_Info> results1 = r.where(Picture_Info.class).equalTo("tags.classification_info_engs.name", results.get(0).getGetAlbum_name_eng())
                .or()
                .equalTo("tags.classification_info_engs.related_wordses.related_words", results.get(0).getGetAlbum_name_eng()).findAll();
        if(results1.size()!=0){
            picture_infos.addAll(results1.subList(0,results1.size()));
            picture_infos1.addAll(picture_infos.subList(0,picture_infos.size()));
        }

        for (Album_Name_Related_Words album_name_related_words : results.get(0).getAlbum_name_related_wordses()) {
            RealmResults<Picture_Info>results2 = r.where(Picture_Info.class).equalTo("tags.classification_info_engs.name", album_name_related_words.getAlbum_nmae_related_words())
                    .or()
                    .equalTo("tags.classification_info_engs.related_wordses.related_words", album_name_related_words.getAlbum_nmae_related_words())
                    .findAll();
            Log.d("for",Integer.toString(results2.size()) );
            if(results2.size()!=0){
                picture_infos.addAll(results2.subList(0,results2.size()));
                picture_infos1.addAll(picture_infos.subList(0,picture_infos.size()));
            }
        }
        album.setPicture_infos(picture_infos1);*/
    }
    public ArrayList<String> Path_Query(int id){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Album> results = r.where(Album.class).equalTo("album_id", id).findAll();
        Set<String> hashSet=new HashSet<>();
        for(Picture_Info pictureInfo: results.get(0).getPicture_infos()){
            hashSet.add(pictureInfo.getPath());
        }
        ArrayList<String>path=new ArrayList<>();

        for(Iterator<String> iterator = hashSet.iterator(); iterator.hasNext(); ) {
            path.add(iterator.next());
        }

        for(int i=0;i<path.size();i++){
            Log.d("for",path.get(i));
        }

        return path;
    }

    public RealmResults<Album> Id_Query(){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Album>results=r.where(Album.class).findAll();
        r.close();
        return results;
    }

    public RealmList<Picture_Info> Relation_Album(RealmResults<Album> realmResults, int keyWordCondition, Date date,int DateCondition){

        RealmList<Picture_Info>picture_infos=new RealmList<>();

        return picture_infos;
    }


}
