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
import java.util.List;
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

    public ArrayList<String>Get_AlbumName(){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Album> alba = r.where(Album.class).findAll();
        ArrayList<String> member=new ArrayList<>();
        for(Album album:alba){
            member.add(album.getAlbum_name());
        }
        r.close();
        return member;
    }
    public ArrayList<String>Get_Path(int id){
        Realm r = Realm.getDefaultInstance();
        RealmResults<Album> alba = r.where(Album.class).equalTo("album_id",id).findAll();
        ArrayList<String> path=new ArrayList<>();
        for(Picture_Info pictureInfo:alba.get(0).getPicture_infos()){
            path.add(pictureInfo.getPath());
        }
        return path;
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
                RealmResults<Picture_Info> RealmResults = getRealmResultKeyWord(album);
                if (RealmResults.size() != 0) {
                    addList(RealmResults, picture_infos);
                }
                for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                    RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWord(related_words);
                    if (RealmResults1.size() != 0) {
                        addList(RealmResults1, picture_infos);
                    }
                }
                //日付あり
            } else {
                //before
                if (dateCondition == 0) {
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateBefore(album, date);
                    if (RealmResults.size() != 0) {
                        addList(RealmResults, picture_infos);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordDateBefore(related_words, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                    }
                    //その日
                } else if (dateCondition == 1) {
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateThatDay(album, date);
                    if (RealmResults.size() != 0) {
                        addList(RealmResults, picture_infos);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordThatDay(related_words, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }

                    }
                    //after
                } else if (dateCondition == 2) {
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateAfter(album, date);
                    if (RealmResults.size() != 0) {
                        addList(RealmResults, picture_infos);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordAfter(related_words, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                    }
                }
            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //キーワード2つ
        } else if (keywordRealmList.size() == 2) {
            //AND検索
            if (keyWordCondition == 0) {
                //日付無
                if ("".equals(dateTime)) {
                    //キーワード
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeyWord(album);
                    if (RealmResults.size() != 0) {
                        //キーワードとキーワード
                        RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                        if (realmResults.size() != 0) {
                            addList(realmResults, picture_infos);
                        }
                        //キーワードと関連語
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                            if (RealmResults1.size() != 0) {
                                addList(RealmResults1, picture_infos);
                            }
                        }
                    }
                    //関連語
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWord(related_words);
                        //関連語とキーワード
                        if (RealmResults1.size() != 0) {
                            RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                addList(realmResults, picture_infos);
                            }
                            //関連語と関連語
                            for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                if (RealmResults2.size() != 0) {
                                    addList(RealmResults2, picture_infos);
                                }
                            }
                        }
                    }
                    //日付あり
                } else {
                    //before
                    if (dateCondition == 0) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateBefore(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                addList(realmResults, picture_infos);
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    addList(RealmResults1, picture_infos);
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordDateBefore(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    addList(realmResults, picture_infos);
                                }
                                //関連語と関連語
                                for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                    if (RealmResults2.size() != 0) {
                                        addList(RealmResults2, picture_infos);
                                    }
                                }
                            }
                        }
                    }//その日
                    else if (dateCondition == 1) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateThatDay(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                addList(realmResults, picture_infos);
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    addList(RealmResults1, picture_infos);
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordDateBefore(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    addList(realmResults, picture_infos);
                                }
                                //関連語と関連語
                                for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                    if (RealmResults2.size() != 0) {
                                        addList(RealmResults2, picture_infos);
                                    }
                                }
                            }
                        }
                        //after
                    } else if (dateCondition == 2) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateAfter(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                addList(realmResults, picture_infos);
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    addList(RealmResults1, picture_infos);
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordDateBefore(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    addList(realmResults, picture_infos);
                                }
                                //関連語と関連語
                                for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                    if (RealmResults2.size() != 0) {
                                        addList(RealmResults2, picture_infos);
                                    }
                                }
                            }
                        }
                    }
                }
                //OR検索
            } else if(keyWordCondition==1) {
                //日付無
                if ("".equals(dateTime)) {
                    //キーワード
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeywordOR(keywordRealmList, 0);
                    if (RealmResults.size() != 0) {
                        addList(RealmResults, picture_infos);
                    }
                    RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordOR(keywordRealmList, 1);
                    if (RealmResults1.size() != 0) {
                        addList(RealmResults1, picture_infos);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWord(related_words);
                        if (RealmResults2.size() != 0) {
                            addList(RealmResults2, picture_infos);
                        }
                    }
                    for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWord(related_words);
                        if (RealmResults3.size() != 0) {
                            addList(RealmResults3, picture_infos);
                        }
                    }
                    OverlapRemove(picture_infos);
                } else {
                    //before
                    if (dateCondition == 0) {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateBefore(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateBefore(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordDateBefore(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordDateBefore(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                        //thatday
                    } else if (dateCondition == 1) {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateThatday(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateThatday(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordThatDay(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordThatDay(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                        //after
                    } else {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateAfter(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateAfter(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordAfter(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordAfter(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //キーワード3つ
        } else {
            //AND検索
            if (keyWordCondition == 0) {
                //日付無
                if ("".equals(dateTime)) {
                    //キーワード
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeyWord(album);
                    if (RealmResults.size() != 0) {
                        //キーワードとキーワード
                        RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                        if (realmResults.size() != 0) {
                            //キーワードとキーワードとキーワード
                            RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(realmResults, keywordRealmList, 2);
                            if (realmResults1.size() != 0) {
                                addList(realmResults1, picture_infos);
                            }
                            //キーワードとキーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(realmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    addList(RealmResults1, picture_infos);
                                }
                            }
                        }
                        //キーワードと関連語
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                            if (RealmResults1.size() != 0) {
                                //キーワードと関連語とキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(RealmResults1, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //キーワードと関連語と関連語
                                for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                    if (RealmResults2.size() != 0) {
                                        addList(RealmResults2, picture_infos);
                                    }
                                }
                            }
                        }
                    }
                    //関連語
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWord(related_words);
                        //関連語とキーワード
                        if (RealmResults1.size() != 0) {
                            RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                //関連語とキーワードとキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(realmResults, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //関連語とキーワードと関連語
                                for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                    if (RealmResults2.size() != 0) {
                                        addList(RealmResults2, picture_infos);
                                    }
                                }
                            }
                        }
                        //関連語と関連語
                        for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                            if (RealmResults2.size() != 0) {
                                //関連語と関連語とキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(RealmResults2, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //関連語と関連語と関連語
                                for (Related_Words related_words2 : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults3 = getRealmResultKeywordANDRelatedword(RealmResults2, related_words2);
                                    if (RealmResults3.size() != 0) {
                                        addList(RealmResults3, picture_infos);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //before
                    if (dateCondition == 0) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateBefore(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                //キーワードとキーワードとキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(realmResults, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //キーワードとキーワードと関連語
                                for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(realmResults, related_words);
                                    if (RealmResults1.size() != 0) {
                                        addList(RealmResults1, picture_infos);
                                    }
                                }
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    //キーワードと関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(RealmResults1, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //キーワードと関連語と関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordDateBefore(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    //関連語とキーワードとキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(realmResults, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語とキーワードと関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                            //関連語と関連語
                            for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                if (RealmResults2.size() != 0) {
                                    //関連語と関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(RealmResults2, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語と関連語と関連語
                                    for (Related_Words related_words2 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults3 = getRealmResultKeywordANDRelatedword(RealmResults2, related_words2);
                                        if (RealmResults3.size() != 0) {
                                            addList(RealmResults3, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                        //thatday
                    } else if (dateCondition == 1) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateThatDay(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                //キーワードとキーワードとキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(realmResults, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //キーワードとキーワードと関連語
                                for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(realmResults, related_words);
                                    if (RealmResults1.size() != 0) {
                                        addList(RealmResults1, picture_infos);
                                    }
                                }
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    //キーワードと関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(RealmResults1, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //キーワードと関連語と関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordThatDay(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    //関連語とキーワードとキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(realmResults, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語とキーワードと関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                            //関連語と関連語
                            for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                if (RealmResults2.size() != 0) {
                                    //関連語と関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(RealmResults2, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語と関連語と関連語
                                    for (Related_Words related_words2 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults3 = getRealmResultKeywordANDRelatedword(RealmResults2, related_words2);
                                        if (RealmResults3.size() != 0) {
                                            addList(RealmResults3, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                        //after
                    } else if (dateCondition == 2) {
                        //キーワード
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordDateAfter(album, date);
                        if (RealmResults.size() != 0) {
                            //キーワードとキーワード
                            RealmResults<Picture_Info> realmResults = getRealmResultKeywordANDKeyword(RealmResults, keywordRealmList, 1);
                            if (realmResults.size() != 0) {
                                //キーワードとキーワードとキーワード
                                RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(realmResults, keywordRealmList, 2);
                                if (realmResults1.size() != 0) {
                                    addList(realmResults1, picture_infos);
                                }
                                //キーワードとキーワードと関連語
                                for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                                    RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(realmResults, related_words);
                                    if (RealmResults1.size() != 0) {
                                        addList(RealmResults1, picture_infos);
                                    }
                                }
                            }
                            //キーワードと関連語
                            for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordANDRelatedword(RealmResults, related_words);
                                if (RealmResults1.size() != 0) {
                                    //キーワードと関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultKeywordANDKeyword(RealmResults1, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //キーワードと関連語と関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                        //関連語
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults1 = getRealmResultRelatedWordAfter(related_words, date);
                            //関連語とキーワード
                            if (RealmResults1.size() != 0) {
                                RealmResults<Picture_Info> realmResults = getRealmResultRelatedwordANDKeyword(RealmResults1, keywordRealmList, 1);
                                if (realmResults.size() != 0) {
                                    //関連語とキーワードとキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(realmResults, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語とキーワードと関連語
                                    for (Related_Words related_words1 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults2 = getRealmResultKeywordANDRelatedword(realmResults, related_words1);
                                        if (RealmResults2.size() != 0) {
                                            addList(RealmResults2, picture_infos);
                                        }
                                    }
                                }
                            }
                            //関連語と関連語
                            for (Related_Words related_words1 : keywordRealmList.get(1).getRelated_wordses()) {
                                RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedwordANDRelatedWord(RealmResults1, related_words1);
                                if (RealmResults2.size() != 0) {
                                    //関連語と関連語とキーワード
                                    RealmResults<Picture_Info> realmResults1 = getRealmResultRelatedwordANDKeyword(RealmResults2, keywordRealmList, 2);
                                    if (realmResults1.size() != 0) {
                                        addList(realmResults1, picture_infos);
                                    }
                                    //関連語と関連語と関連語
                                    for (Related_Words related_words2 : keywordRealmList.get(2).getRelated_wordses()) {
                                        RealmResults<Picture_Info> RealmResults3 = getRealmResultKeywordANDRelatedword(RealmResults2, related_words2);
                                        if (RealmResults3.size() != 0) {
                                            addList(RealmResults3, picture_infos);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (keyWordCondition == 1) {
                //OR検索
                //日付無
                if ("".equals(dateTime)) {
                    //キーワード
                    RealmResults<Picture_Info> RealmResults = getRealmResultKeywordOR(keywordRealmList, 0);
                    if (RealmResults.size() != 0) {
                        addList(RealmResults, picture_infos);
                    }
                    RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordOR(keywordRealmList, 1);
                    if (RealmResults1.size() != 0) {
                        addList(RealmResults1, picture_infos);
                    }
                    RealmResults<Picture_Info> RealmResults5 = getRealmResultKeywordOR(keywordRealmList, 2);
                    if (RealmResults5.size() != 0) {
                        addList(RealmResults5, picture_infos);
                    }
                    for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWord(related_words);
                        if (RealmResults2.size() != 0) {
                            addList(RealmResults2, picture_infos);
                        }
                    }
                    for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWord(related_words);
                        if (RealmResults3.size() != 0) {
                            addList(RealmResults3, picture_infos);
                        }
                    }
                    for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                        RealmResults<Picture_Info> RealmResults4 = getRealmResultRelatedWord(related_words);
                        if (RealmResults4.size() != 0) {
                            addList(RealmResults4, picture_infos);
                        }
                    }
                    OverlapRemove(picture_infos);
                } else {
                    //before
                    if (dateCondition == 0) {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateBefore(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateBefore(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults5 = getRealmResultKeywordORDateBefore(keywordRealmList, 2, date);
                        if (RealmResults5.size() != 0) {
                            addList(RealmResults5, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordDateBefore(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordDateBefore(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults4 = getRealmResultRelatedWordDateBefore(related_words, date);
                            if (RealmResults4.size() != 0) {
                                addList(RealmResults4, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                        //thatday
                    } else if (dateCondition == 1) {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateThatday(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateThatday(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults5 = getRealmResultKeywordORDateThatday(keywordRealmList, 2, date);
                        if (RealmResults5.size() != 0) {
                            addList(RealmResults5, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordThatDay(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordThatDay(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults4 = getRealmResultRelatedWordThatDay(related_words, date);
                            if (RealmResults4.size() != 0) {
                                addList(RealmResults4, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                        //after
                    } else {
                        RealmResults<Picture_Info> RealmResults = getRealmResultKeywordORDateAfter(keywordRealmList, 0, date);
                        if (RealmResults.size() != 0) {
                            addList(RealmResults, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults1 = getRealmResultKeywordORDateAfter(keywordRealmList, 1, date);
                        if (RealmResults1.size() != 0) {
                            addList(RealmResults1, picture_infos);
                        }
                        RealmResults<Picture_Info> RealmResults5 = getRealmResultKeywordORDateAfter(keywordRealmList, 2, date);
                        if (RealmResults5.size() != 0) {
                            addList(RealmResults5, picture_infos);
                        }
                        for (Related_Words related_words : keywordRealmList.get(0).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults2 = getRealmResultRelatedWordAfter(related_words, date);
                            if (RealmResults2.size() != 0) {
                                addList(RealmResults2, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(1).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults3 = getRealmResultRelatedWordAfter(related_words, date);
                            if (RealmResults3.size() != 0) {
                                addList(RealmResults3, picture_infos);
                            }
                        }
                        for (Related_Words related_words : keywordRealmList.get(2).getRelated_wordses()) {
                            RealmResults<Picture_Info> RealmResults4 = getRealmResultRelatedWordAfter(related_words, date);
                            if (RealmResults4.size() != 0) {
                                addList(RealmResults4, picture_infos);
                            }
                        }
                        OverlapRemove(picture_infos);
                    }
                }
            }
            System.out.print("");
        }
            return picture_infos;
        }


    private void OverlapRemove(RealmList<Picture_Info> picture_infos) {
        for (int i = 0; i < picture_infos.size(); i++) {
            for (int j = i + 1; j < picture_infos.size(); j++) {
                if (picture_infos.get(i).getId() == picture_infos.get(j).getId()) {
                    picture_infos.remove(j);
                    j--;

                }
            }
        }
    }

    private void addList
            (RealmResults<Picture_Info> RealmResults, RealmList<Picture_Info> picture_infos) {
        RealmList<Picture_Info> results = new RealmList<>();
        results.addAll(RealmResults.subList(0, RealmResults.size()));
        addList(picture_infos, results);
    }

    private void addList
            (RealmList<Picture_Info> picture_infos, RealmList<Picture_Info> result) {

        for (Picture_Info pictureInfo : result) {
            picture_infos.add(pictureInfo);
        }
    }

    private RealmResults<Picture_Info> getRealmResultKeyWord(Album album) {
        Realm r = Realm.getDefaultInstance();
        RealmList<Keyword> keywordRealmList = album.getKeywords();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).findAll();
        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedWord(Related_Words
                                                                         related_words) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE).findAll();
        return RealmResults1;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordDateBefore(Album
                                                                               album, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmList<Keyword> keywordRealmList = album.getKeywords();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                .lessThan("date", date).findAll();

        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedWordDateBefore
            (Related_Words related_words, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                .lessThan("date", date).findAll();
        return RealmResults1;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordDateThatDay(Album
                                                                                album, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmList<Keyword> keywordRealmList = album.getKeywords();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                .equalTo("date", date).findAll();
        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedWordThatDay(Related_Words
                                                                                related_words, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                .equalTo("date", date).findAll();
        return RealmResults1;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordDateAfter(Album album, Date
            date) {
        Realm r = Realm.getDefaultInstance();
        RealmList<Keyword> keywordRealmList = album.getKeywords();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(0).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(0).getKeyword_Eng(), Case.INSENSITIVE)
                .greaterThan("date", date).findAll();
        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedWordAfter(Related_Words
                                                                              related_words, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults1 = r.where(Picture_Info.class).equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE)
                .greaterThan("date", date).findAll();
        return RealmResults1;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordANDKeyword
            (RealmResults<Picture_Info> RealmResults, RealmList<Keyword> keywordRealmList,
             int index) {
        RealmResults<Picture_Info> realmResults = RealmResults.where().equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).findAll();
        return realmResults;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordANDRelatedword
            (RealmResults<Picture_Info> RealmResults, Related_Words related_words) {
        RealmResults<Picture_Info> RealmResults1 = RealmResults.where().equalTo("classification_info_engs.name", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words.getRelated_words(), Case.INSENSITIVE).findAll();
        return RealmResults1;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedwordANDKeyword
            (RealmResults<Picture_Info> RealmResults1, RealmList<Keyword> keywordRealmList,
             int index) {
        RealmResults<Picture_Info> realmResults = RealmResults1.where().equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).findAll();
        return realmResults;
    }

    private RealmResults<Picture_Info> getRealmResultRelatedwordANDRelatedWord
            (RealmResults<Picture_Info> RealmResults, Related_Words related_words1) {

        RealmResults<Picture_Info> RealmResults2 = RealmResults.where().equalTo("classification_info_engs.name", related_words1.getRelated_words(), Case.INSENSITIVE).or()
                .contains("address", related_words1.getRelated_words(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", related_words1.getRelated_words(), Case.INSENSITIVE).findAll();
        return RealmResults2;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordOR
            (RealmList<Keyword> keywordRealmList, int index) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> realmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).findAll();
        return realmResults;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordORDateBefore
            (RealmList<Keyword> keywordRealmList, int index, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE)
                .lessThan("date", date).findAll();

        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordORDateThatday
            (RealmList<Keyword> keywordRealmList, int index, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE)
                .equalTo("date", date).findAll();

        return RealmResults;
    }

    private RealmResults<Picture_Info> getRealmResultKeywordORDateAfter
            (RealmList<Keyword> keywordRealmList, int index, Date date) {
        Realm r = Realm.getDefaultInstance();
        RealmResults<Picture_Info> RealmResults = r.where(Picture_Info.class).equalTo("classification_info_engs.name", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE).or()
                .contains("address", keywordRealmList.get(index).getKeyword_Jap(), Case.INSENSITIVE).or()
                .equalTo("landmark_eng", keywordRealmList.get(index).getKeyword_Eng(), Case.INSENSITIVE)
                .greaterThan("date", date).findAll();

        return RealmResults;
    }

}
