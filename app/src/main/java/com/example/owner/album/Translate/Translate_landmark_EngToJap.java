package com.example.owner.album.Translate;

import android.os.AsyncTask;

import com.example.owner.album.Insert.Classification_Info_Jap_Insert;
import com.example.owner.album.Insert.Picture_Insert;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

/**
 * Created by Owner on 2016/10/24.
 */

public class Translate_landmark_EngToJap extends AsyncTask<Void, Void, String> {

    private String words;

    public Translate_landmark_EngToJap(String word) {
        super();
        words = word;
    }


    @Override
    protected String doInBackground(Void... value) {
        String result=null ;
        try {

            Translate.setClientId("A41410507");
            Translate.setClientSecret("RwmuBBxT3M1y7VpHAw0bJrp6zbN9vyemfhrCUtu64qk=");

                result=Translate.execute(words, Language.ENGLISH, Language.JAPANESE);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Picture_Insert picture_insert=new Picture_Insert();
        picture_insert.Insert_Landmark_Jap(result);

    }
}
