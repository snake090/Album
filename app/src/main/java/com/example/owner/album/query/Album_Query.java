package com.example.owner.album.query;

import android.util.Log;

import com.example.owner.album.model.Album;
import com.example.owner.album.model.Album_Name_Related_Words;
import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.model.Related_Words;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/10/11.
 */

public class Album_Query {

    public RealmResults<Album> Id_Query() {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Album> results = r.where(Album.class).findAll();
        r.close();
        return results;
    }

    public RealmList<Picture_Info> Relation_Album(Album album, int keyWordCondition, String dateTime, int dateCondition) {

        RealmList<Picture_Info> picture_infos = new RealmList<>();
        java.util.Date date = null;
        if ("".equals(dateTime)) {
            System.out.print("");
        } else {
            dateTime = dateTime.replaceAll("/", ":");
            dateTime = dateTime + " 00:00:00";

            DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);

            try {
                date = format.parse(dateTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Realm r = Realm.getDefaultInstance();
        RealmList<Keyword> keywordRealmList = album.getKeywords();
        //キーワードが一つ
        if (keywordRealmList.size() == 1) {
            //日付無
            if ("".equals(dateTime)) {
                RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                        .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                        .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).findAll();
                if (RealmResults.size() != 0) {
                    RealmList<Picture_Info> results = new RealmList<>();
                    results.addAll(RealmResults.subList(0, RealmResults.size()));
                    addList(picture_infos, results);
                }
                for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                    RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                            .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                            .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE).findAll();
                    if (RealmResults1.size() != 0) {
                        RealmList<Picture_Info> results = new RealmList<>();
                        results.addAll(RealmResults1.subList(0, RealmResults1.size()));
                        addList(picture_infos, results);
                    }
                }
                //日付あり
            } else {
                //before
                if (dateCondition == 0) {
                    RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                            .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                            .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                            .lessThan("date", date).findAll();

                    if (RealmResults.size() != 0) {
                        RealmList<Picture_Info> results = new RealmList<>();
                        results.addAll(RealmResults.subList(0, RealmResults.size()));
                        addList(picture_infos, results);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                                .lessThan("date", date).findAll();
                        if (RealmResults1.size() != 0) {
                            RealmList<Picture_Info> results = new RealmList<>();
                            results.addAll(RealmResults1.subList(0, RealmResults1.size()));
                            addList(picture_infos, results);
                        }
                    }
                    //その日
                } else if (dateCondition == 1) {
                    RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                            .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                            .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                            .equalTo("date", date).findAll();

                    if (RealmResults.size() != 0) {
                        RealmList<Picture_Info> results = new RealmList<>();
                        results.addAll(RealmResults.subList(0, RealmResults.size()));
                        addList(picture_infos, results);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                                .equalTo("date", date).findAll();
                        if (RealmResults1.size() != 0) {
                            RealmList<Picture_Info> results = new RealmList<>();
                            results.addAll(RealmResults1.subList(0, RealmResults1.size()));
                            addList(picture_infos, results);
                        }

                    }
                }else if(dateCondition==2){
                    RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                            .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                            .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                            .greaterThan("date", date).findAll();

                    if (RealmResults.size() != 0) {
                        RealmList<Picture_Info> results = new RealmList<>();
                        results.addAll(RealmResults.subList(0, RealmResults.size()));
                        addList(picture_infos, results);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                                .greaterThan("date", date).findAll();
                        if (RealmResults1.size() != 0) {
                            RealmList<Picture_Info> results = new RealmList<>();
                            results.addAll(RealmResults1.subList(0, RealmResults1.size()));
                            addList(picture_infos, results);
                        }

                    }
                }
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //キーワード2つ
        }else if(keywordRealmList.size()==2){
            //AND検索
            if(keyWordCondition==1){
                //日付無
                if ("".equals(dateTime)) {
                    //キーワード
                    RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                            .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                            .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).findAll();
                    if (RealmResults.size() != 0) {
                        //キーワードとキーワード
                        RealmResults<Picture_Info> realmResults=RealmResults.where().equalTo("classification_info_engs.name", keywordRealmList.get(1).getKeyword_Eng(), Case.INSENSITIVE).or()
                                .contains("address", keywordRealmList.get(1).getKeyword_Jap(), Case.INSENSITIVE).or()
                                .equalTo("landmark_eng", keywordRealmList.get(1).getKeyword_Eng(), Case.INSENSITIVE).findAll();
                        if(realmResults.size()!=0){
                            RealmList<Picture_Info> results = new RealmList<>();
                            results.addAll(realmResults.subList(0, realmResults.size()));
                            addList(picture_infos, results);
                        }
                        //キーワードと関連語
                        for(Related_Words related_words:keywordRealmList.get(1).getRelated_wordses()){
                            RealmResults<Picture_Info> RealmResults1 = RealmResults.where().equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                    .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                    .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE).findAll();
                            if (RealmResults1.size() != 0) {
                                RealmList<Picture_Info> results = new RealmList<>();
                                results.addAll(RealmResults1.subList(0, RealmResults1.size()));
                                addList(picture_infos, results);
                            }
                        }
                    }
                    //関連語
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = RealmResults.where().equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE).findAll();
                        //関連語とキーワード
                        if (RealmResults1.size() != 0) {
                            RealmResults<Picture_Info> realmResults=RealmResults1.where().equalTo("classification_info_engs.name", keywordRealmList.get(1).getKeyword_Eng(), Case.INSENSITIVE).or()
                                    .contains("address", keywordRealmList.get(1).getKeyword_Jap(), Case.INSENSITIVE).or()
                                    .equalTo("landmark_eng", keywordRealmList.get(1).getKeyword_Eng(), Case.INSENSITIVE).findAll();
                            if(realmResults.size()!=0){
                                RealmList<Picture_Info> results = new RealmList<>();
                                results.addAll(realmResults.subList(0, realmResults.size()));
                                addList(picture_infos, results);
                            }
                            //関連語と関連語
                            for(Related_Words related_words1:keywordRealmList.get(1).getRelated_wordses()){
                                RealmResults<Picture_Info> RealmResults2 = RealmResults.where().equalTo("classification_info_engs.name", related_words1.getRelated_words(), Case.INSENSITIVE).or()
                                        .contains("address", related_words1.getRelated_words(), Case.INSENSITIVE).or()
                                        .equalTo("landmark_eng", related_words1.getRelated_words(), Case.INSENSITIVE).findAll();
                                if (RealmResults2.size() != 0) {
                                    RealmList<Picture_Info> results = new RealmList<>();
                                    results.addAll(RealmResults2.subList(0, RealmResults2.size()));
                                    addList(picture_infos, results);
                                }
                            }
                        }
                    }
                    //日付あり
                }else {

                }
            }
        }
        System.out.print("");


        return picture_infos;
    }


    private void addList(RealmList<Picture_Info> picture_infos, RealmList<Picture_Info> results) {
        for (Picture_Info pictureInfo : results) {
            picture_infos.add(pictureInfo);
        }
    }


}
