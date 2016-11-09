package com.example.owner.album.Insert;

import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Related_Words;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Owner on 2016/11/09.
 */

public class Related_Words_Insert {
    public void Insert_Keyword(ArrayList<String> relatedWords,String keyword){
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
        keyword1.setRelated_wordses(relatedWordsRealmList);

        r.commitTransaction();
        r.close();

    }
}
