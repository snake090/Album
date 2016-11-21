package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Album_Insert;
import com.example.owner.album.model.Keyword;
import com.example.owner.album.model.Related_Words;
import com.example.owner.album.query.Keyword_Query;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Owner on 2016/11/17.
 */

public class ControlTask extends AsyncTask<Void, Void, Boolean> {
    private CountDownLatch _latch;
    private ArrayList<String> keywords = new ArrayList<>();
    private String albumName;
    private int keyWordCondition;
    private String date;
    private int dateCondition;


    public ControlTask(ArrayList<String> keywords, String albumName, int keyWordCondition, String date, int dateCondition) {
        this.keywords = keywords;
        this.albumName = albumName;
        this.keyWordCondition = keyWordCondition;
        this.date = date;
        this.dateCondition = dateCondition;
        this._latch = new CountDownLatch(keywords.size() * 2);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // プログレス表示処理
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            Album_Insert album_insert = new Album_Insert();
            album_insert.Insert_DB(albumName,keywords,_latch);
            Keyword_Query keyword_query = new Keyword_Query();

            for (String keyword : keywords) {
                RealmResults<Keyword> realmResults = keyword_query.Double_Check(keyword);
                if (realmResults.size() != 0) {
                    RealmList<Related_Words> realmList = realmResults.get(0).getRelated_wordses();
                    if (realmList.size() == 0) {
                        new Word_association(keyword,_latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                } else {
                    new Word_association(keyword,_latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    ;
                }
            }

            Log.d("db","wait");
            _latch.await(5, TimeUnit.SECONDS);
            Log.d("db","related");
            _latch=new CountDownLatch(1);
            new Album_Related(keyWordCondition, date, dateCondition,_latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            _latch.await(5, TimeUnit.SECONDS);
            Log.d("db","finish");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        // プログレス終了処理
    }
}
