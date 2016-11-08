package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Picture_Insert;
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
    String accessToken = "QAlwM65b7AmshghYIFdHFYTNqAUcp1NBW6ijsnFxFXOcapvApV";
    String word = "lovely";
    String detail = "definitions";
    final StringBuilder result = new StringBuilder();

    public WordsAPI(String word) {
        super();
        words = word;
    }


    @Override
    protected ArrayList<String> doInBackground(Void... value) {
        ArrayList<String> message = new ArrayList<String>();

        try {
            HttpURLConnection con = null;
            URL url = new URL("https://wordsapiv1.p.mashape.com/words/bump/also");
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            con.setRequestProperty("X-Mashape-Key", "QAlwM65b7AmshghYIFdHFYTNqAUcp1NBW6ijsnFxFXOcapvApV");
            con.setRequestProperty("Accept", "application/json");
            con.connect();
// HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                final InputStream in = con.getInputStream();
                final String encoding = con.getContentEncoding();
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                // 1行ずつテキストを読み込む
                while((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();

                BufferedInputStream inputStream = new BufferedInputStream(con.getInputStream());
                ByteArrayOutputStream responseArray = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];

                int length;
                while((length = inputStream.read(buff)) != -1) {
                    if(length > 0) {
                        responseArray.write(buff, 0, length);
                    }
                }
                StringBuilder viewStrBuilder = new StringBuilder();
                JSONObject jsonObj = new JSONObject(new String(responseArray.toByteArray()));
                JSONArray result = jsonObj.getJSONArray("results");
                for(int i = 0; i < result.length(); i++) {
                    JSONObject word = result.getJSONObject(i);
                    message.add(word.getString("also"));
                }
                System.out.print("");
            }
            System.out.print("");
        }catch (Exception e){
            e.getStackTrace();
        }



        return message;

    }

    protected void onPostExecute(ArrayList<String>result) {

    }
}
