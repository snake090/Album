package com.example.owner.album.Album;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Owner on 2016/11/09.
 */

public class Word_association extends AsyncTask<Void, Void, ArrayList<String>> {
    StringBuilder sb = new StringBuilder();
    JSONArray jsonArray;
    @Override
    protected ArrayList<String> doInBackground(Void... value) {
        ArrayList<String>message = new ArrayList<String>();
        try {
            HttpURLConnection con = null;
            URL url = new URL("http://kizasi.jp/kizapi.py?span=24&kw_expr=暖冬&type=coll");
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));


                StringBuilder result = new StringBuilder();

                String line;

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

            }
            System.out.print("");
        }catch (Exception e){
            e.getStackTrace();
        }
        return message;
    }
}
