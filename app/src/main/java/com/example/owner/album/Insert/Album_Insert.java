package com.example.owner.album.Insert;

import android.util.Log;

import com.example.owner.album.Translate.Translate_Keyword_JapToEng;
import com.example.owner.album.model.Album;
import com.example.owner.album.model.Album_Name_Related_Words;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Album_Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Owner on 2016/10/10.
 */

public class Album_Insert {
    public void Insert_DB() {
        /*
        Realm r = Realm.getDefaultInstance();

        r.beginTransaction();

        Album album1 = r.createObject(Album.class, 1);
        album1.setAlbum_name("動物");
        album1.setGetAlbum_name_eng("animal");

        Album album2 = r.createObject(Album.class, 2);
        album2.setAlbum_name("建造物");
        album2.setGetAlbum_name_eng("building");

        String words1[]={"insect","plant","birds","mammal","ecology"};
        Album_Name_Related_Words album_name_related_words1[]=new Album_Name_Related_Words[words1.length];
        for(int i=0;i<words1.length;i++){
            album_name_related_words1[i]=r.createObject(Album_Name_Related_Words.class);;
            album_name_related_words1[i].setAlbum_nmae_related_words(words1[i]);
        }
        RealmList<Album_Name_Related_Words> album_name_related_wordsesList1 = new RealmList<>();
        for(int i=0;i<album_name_related_words1.length;i++){
            album_name_related_wordsesList1.add(album_name_related_words1[i]);
        }
        album1.setAlbum_name_related_wordses(album_name_related_wordsesList1);
        //アルバムと写真関連付け
        Album_Query album_query=new Album_Query();
        album_query.Picture_Query(album1,1);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String words2[]={"historic site","landscape","monument"};
        Album_Name_Related_Words album_name_related_words2[]=new Album_Name_Related_Words[words2.length];
        for(int i=0;i<words2.length;i++){
            album_name_related_words2[i]=r.createObject(Album_Name_Related_Words.class);
            album_name_related_words2[i].setAlbum_nmae_related_words(words2[i]);
        }
        RealmList<Album_Name_Related_Words> album_name_related_wordsesList2 = new RealmList<>();
        for(int i=0;i<album_name_related_words2.length;i++){
            album_name_related_wordsesList2.add(album_name_related_words2[i]);
        }
        album2.setAlbum_name_related_wordses(album_name_related_wordsesList2);

        //アルバムと写真関連付け
        Album_Query album_query1=new Album_Query();
        album_query1.Picture_Query(album2,2);


        r.commitTransaction();
        r.close();*/
    }

    public void Insert_DB(String name,ArrayList<String>keyword){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();
        Album_Query album_query=new Album_Query();
        RealmResults<Album> realmResults=album_query.Id_Query();

        if(realmResults.size()==0){
            Album album=r.createObject(Album.class,1);
            album.setAlbum_name(name);

        }else{
            realmResults=realmResults.sort("album_id", Sort.DESCENDING);
            int id=realmResults.get(0).getAlbum_id();
            Album album=r.createObject(Album.class,id+1);
            album.setAlbum_name(name);

        }
        r.commitTransaction();
        r.close();
        Translate_Keyword_JapToEng translate_keyword_japToEng=new Translate_Keyword_JapToEng(keyword);
        translate_keyword_japToEng.execute();
    }

    public void Insert_Picture_Info(int keyWordCondition,String date,int dateCondition){

        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();
        Album_Query query=new Album_Query();


        Album_Query album_query=new Album_Query();
        RealmResults<Album> alba=album_query.Id_Query();
        alba=alba.sort("album_id", Sort.DESCENDING);
        int id=alba.get(0).getAlbum_id();
        Album album=r.where(Album.class).equalTo("album_id",id).findFirst();
        RealmList<Picture_Info> picture_infos=query.Relation_Album(album,keyWordCondition,date,dateCondition);
        album.setPicture_infos(picture_infos);

        r.commitTransaction();
        r.close();

        Log.d("dbFinish","picture");

    }


}
