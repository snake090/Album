package com.example.owner.album.Translate;

import android.os.AsyncTask;

import com.example.owner.album.Album.WordsAPI;
import com.example.owner.album.Insert.Keyword_Insert;
import com.example.owner.album.Insert.Related_Words_Insert;
import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Related_Words;
import com.example.owner.album.query.Keyword_Query;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/11/09.
 */

public class Translate_Keyword_JapToEng extends AsyncTask<Void, Void, ArrayList<String>> {
    private ArrayList<String> words;

    public Translate_Keyword_JapToEng(ArrayList<String> words) {
        this.words = words;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... value) {

        ArrayList<String> result = new ArrayList<>();
        try {

            Translate.setClientId("A41410507");
            Translate.setClientSecret("RwmuBBxT3M1y7VpHAw0bJrp6zbN9vyemfhrCUtu64qk=");
            for (String translateword : words)
                result.add(Translate.execute(translateword, Language.JAPANESE, Language.ENGLISH));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {

        Keyword_Insert keyword_insert=new Keyword_Insert();
        keyword_insert.Insert_Keyword(words,result);

        Keyword_Query keyword_query=new Keyword_Query();
        for(String keyword:result){
            RealmResults<Keyword> realmResults=keyword_query.Double_Check_Eng(keyword);
            if(realmResults.size()!=0) {
                RealmList<Related_Words> realmList = realmResults.get(0).getRelated_wordses();
                if (realmList.size() == 0) {
                    WordsAPI wordsapi=new WordsAPI(keyword);
                    wordsapi.execute();
                }
            }else{
                new WordsAPI(keyword).execute();
            }
        }

    }
}
