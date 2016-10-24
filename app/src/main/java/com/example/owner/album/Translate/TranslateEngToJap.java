package com.example.owner.album.Translate;

import android.os.AsyncTask;

import com.example.owner.album.Insert.Classification_Info_Jap_Insert;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

/**
 * Created by Owner on 2016/10/19.
 */

public class TranslateEngToJap extends AsyncTask<Void, Void, ArrayList<String>> {

    private ArrayList<String> words;

    public TranslateEngToJap(ArrayList<String> word) {
        super();
        words = word;
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
        Classification_Info_Jap_Insert classification_info_jap_insert=new Classification_Info_Jap_Insert();
        classification_info_jap_insert.Insert_Classification_Info(result);

    }
}
