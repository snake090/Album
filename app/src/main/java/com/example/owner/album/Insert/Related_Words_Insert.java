package com.example.owner.album.Insert;

import android.util.Log;

import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Related_Words;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/11/09.
 */

public class Related_Words_Insert {
    public void Insert_Keyword_Jap(ArrayList<String> relatedWords,String keyword){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        RealmList<Related_Words> relatedWordsRealmList=new RealmList<>();

        Related_Words related_words;
        for(String word:relatedWords){
            related_words=r.createObject(Related_Words.class);
            related_words.setRelated_words(word);
            relatedWordsRealmList.add(related_words);
        }


        Keyword keyword1=r.where(Keyword.class).equalTo("Keyword_Jap",keyword).findFirst();
        RealmList<Related_Words> related_wordses=keyword1.getRelated_wordses();
        for(Related_Words related_words1:related_wordses){
            relatedWordsRealmList.add(related_words1);
        }
        keyword1.setRelated_wordses(relatedWordsRealmList);

        r.commitTransaction();
        r.close();
        Log.d("dbFinish","japkeyword");

    }

    public void Insert_Keyword_Eng(ArrayList<String> relatedWords,String keyword){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        RealmList<Related_Words> relatedWordsRealmList=new RealmList<>();
        Related_Words related_words;
        for(String word:relatedWords){
            related_words=r.createObject(Related_Words.class);
            related_words.setRelated_words(word);
            relatedWordsRealmList.add(related_words);
        }

        Keyword keyword1=r.where(Keyword.class).equalTo("Keyword_Eng",keyword).findFirst();
        keyword1.setRelated_wordses(relatedWordsRealmList);

        r.commitTransaction();
        r.close();

        Log.d("dbFinish","engkeyword");

    }
}
