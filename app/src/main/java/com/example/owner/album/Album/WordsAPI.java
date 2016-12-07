package com.example.owner.album.Album;

import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Picture_Insert;
import com.example.owner.album.Insert.Related_Words_Insert;
import com.example.owner.album.Translate.Translate_landmark_EngToJap;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Owner on 2016/11/08.
 */

public class WordsAPI extends AsyncTask<Void, Void, ArrayList<String>> {
    private String words;
    private StringBuilder sb = new StringBuilder();
    private JSONArray jsonArray;
    private ArrayList<String> message;
    private  CountDownLatch _latch;

    public WordsAPI(String word ,CountDownLatch _latch)  {
        super();
        words = word;
        this._latch=_latch;
    }


    @Override
    protected ArrayList<String> doInBackground(Void... value) {
        message = new ArrayList<String>();

        try {
            HttpURLConnection con = null;
            URL url = new URL("https://wordsapiv1.p.mashape.com/words/" + words);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            con.setRequestProperty("X-Mashape-Key", "QAlwM65b7AmshghYIFdHFYTNqAUcp1NBW6ijsnFxFXOcapvApV");
            con.setRequestProperty("Accept", "application/json");
            con.connect();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject rootObject = new JSONObject(sb.toString());
                JSONArray rootArray = rootObject.getJSONArray("results");
                ArrayList<JSONObject> jsonObject = new ArrayList<>();
                for (int i = 0; i < rootArray.length(); i++) {
                    jsonObject.add(rootArray.getJSONObject(i));
                }

                for (int i = 0; i < jsonObject.size(); i++) {
                    getWord(jsonObject.get(i), "synonyms");
                    getWord(jsonObject.get(i), "hasCategories");
                    getWord(jsonObject.get(i), "typeOf");
                    getWord(jsonObject.get(i), "hasTypes");
                    getWord(jsonObject.get(i), "memberOf");
                    getWord(jsonObject.get(i), "hasParts");
                    getWord(jsonObject.get(i), "hasInstances");
                    getWord(jsonObject.get(i), "hasMembers");
                    getWord(jsonObject.get(i), "hasSubstances");
                    getWord(jsonObject.get(i), "hatchback");
                    getWord(jsonObject.get(i), "instanceOf");
                    getWord(jsonObject.get(i), "partOf");
                    getWord(jsonObject.get(i), "pertainsTo");
                    getWord(jsonObject.get(i), "regionOf");
                    getWord(jsonObject.get(i), "similarTo");
                    getWord(jsonObject.get(i), "usageOf");
                }


            }

            System.out.print("");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("json", e.getMessage());
        }

        return message;

    }

    private void getWord(JSONObject jsonObject, String word) {
        try {
            jsonArray = jsonObject.getJSONArray(word);
            for (int j = 0; j < jsonArray.length(); j++) {
                message.add(jsonArray.get(j).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onPostExecute(ArrayList<String> result) {

        Related_Words_Insert related_words_insert = new Related_Words_Insert();
        related_words_insert.Insert_Keyword_Eng(result, words);
        _latch.countDown();
    }
}