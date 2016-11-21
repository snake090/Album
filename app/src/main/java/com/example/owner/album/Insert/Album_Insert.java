package com.example.owner.album.Insert;

import android.os.AsyncTask;
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
import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by Owner on 2016/10/10.
 */

public class Album_Insert {

    public void Insert_DB(String name,ArrayList<String>keyword,CountDownLatch _latch){
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
        //WordsAPI
        new Translate_Keyword_JapToEng(keyword,_latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
        Log.d("related_word",album.getKeywords().get(0).getRelated_wordses().get(0).getRelated_words());
        Log.d("dbFinish","picture");


    }


}
