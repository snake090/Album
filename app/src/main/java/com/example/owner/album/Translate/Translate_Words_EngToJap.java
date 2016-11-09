package com.example.owner.album.Translate;

import android.os.AsyncTask;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

/**
 * Created by Owner on 2016/11/09.
 */

public class Translate_Words_EngToJap extends AsyncTask<Void, Void, ArrayList<String>> {
    private ArrayList<String> words;

    public Translate_Words_EngToJap(ArrayList<String> words) {
        this.words = words;
    }
    @Override
    protected ArrayList<String> doInBackground(Void... value) {

        ArrayList<String> result = new ArrayList<>();
        try {

            Translate.setClientId("A41410507");
            Translate.setClientSecret("RwmuBBxT3M1y7VpHAw0bJrp6zbN9vyemfhrCUtu64qk=");
            for (String translateword : words)
                result.add(Translate.execute(translateword, Language.ENGLISH, Language.JAPANESE));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {


    }
}
