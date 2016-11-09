package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Picture_Insert;
import com.example.owner.album.Insert.Related_Words_Insert;
import com.example.owner.album.Translate.Translate_landmark_EngToJap;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


import org.json.JSONArray;
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

/**
 * Created by Owner on 2016/11/08.
 */

public class WordsAPI extends AsyncTask<Void, Void, ArrayList<String>> {
    private String words;
    StringBuilder sb = new StringBuilder();
    JSONArray jsonArray;
    ArrayList<String> message;
    public WordsAPI(String word) {
        super();
        words = word;
    }


    @Override
    protected ArrayList<String> doInBackground(Void... value) {
        message = new ArrayList<String>();

        try {
            HttpURLConnection con = null;
            URL url = new URL("https://wordsapiv1.p.mashape.com/words/"+words+"/hasCategories");
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            con.setRequestProperty("X-Mashape-Key", "QAlwM65b7AmshghYIFdHFYTNqAUcp1NBW6ijsnFxFXOcapvApV");
            con.setRequestProperty("Accept", "application/json");
            con.connect();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));


                StringBuilder result = new StringBuilder();

                String line;

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                // 取得した文字列からjsonobjectを作成
                JSONObject jsonObject = new JSONObject(sb.toString());
                jsonArray = jsonObject.getJSONArray("hasCategories");
                for(int i=0;i<jsonArray.length();i++){
                    message.add(jsonArray.get(i).toString());
                }
            }

            System.out.print("");
        }catch (Exception e){
            e.getStackTrace();
        }

        return message;

    }

    protected void onPostExecute(ArrayList<String>result) {

        Related_Words_Insert related_words_insert=new Related_Words_Insert();
        related_words_insert.Insert_Keyword_Eng(result,words);
    }
}