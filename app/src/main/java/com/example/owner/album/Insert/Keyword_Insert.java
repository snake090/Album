package com.example.owner.album.Insert;

import com.example.owner.album.model.Album;
import com.example.owner.album.model.Keyword;
import com.example.owner.album.query.Album_Query;
import com.example.owner.album.query.Keyword_Query;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Owner on 2016/11/09.
 */

public class Keyword_Insert {
    public void Insert_Keyword(ArrayList<String> keywordJap,ArrayList<String> keywordEng){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        Keyword_Query keyword_query=new Keyword_Query();
        RealmList<Keyword> keywordRealmList=new RealmList<>();
        ArrayList<String> nameJap=new ArrayList<>();
        ArrayList<String> nameEng=new ArrayList<>();

        for(int i=0;i<keywordJap.size();i++){
            RealmResults<Keyword> keywordRealmResults=keyword_query.Double_Check(keywordJap.get(i));
            if(keywordRealmResults.size()==0){
                nameJap.add(keywordJap.get(i));
                nameEng.add(keywordEng.get(i));
            }else{
                keywordRealmList.add(keywordRealmResults.get(0));
            }
        }
        Keyword keyword1[];
        if(nameJap.size()!=0){
            keyword1=new Keyword[nameJap.size()];
            for(int i=0;i<keyword1.length;i++){
                keyword1[i]=r.createObject(Keyword.class);
                keyword1[i].setKeyword_Jap(nameJap.get(i));
                keyword1[i].setKeyword_Eng(nameEng.get(i));
            }
            for(Keyword keyword2:keyword1){
                keywordRealmList.add(keyword2);
            }
        }
        Album_Query album_query=new Album_Query();
        RealmResults<Album> alba=album_query.Id_Query();
        alba=alba.sort("album_id", Sort.DESCENDING);
        int id=alba.get(0).getAlbum_id();

        Album album=r.where(Album.class).equalTo("album_id",id).findFirst();
        album.setKeywords(keywordRealmList);

        r.commitTransaction();
        r.close();

    }
}
