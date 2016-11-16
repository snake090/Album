package com.example.owner.album.Translate;

import android.os.AsyncTask;

import com.example.owner.album.Album.WordsAPI;
import com.example.owner.album.Insert.Related_Words_Insert;
import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Related_Words;
import com.example.owner.album.query.Keyword_Query;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/11/09.
 */

public class  Translate_Words_JapToEng extends AsyncTask<Void, Void, ArrayList<String>> {
    private ArrayList<String> words;
    private String keyword;

    public Translate_Words_JapToEng(ArrayList<String> words, String keyword) {
        this.words = words;
        this.keyword = keyword;
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
        Related_Words_Insert related_words_insert = new Related_Words_Insert();
        related_words_insert.Insert_Keyword_Jap(result, keyword);

    }
}
