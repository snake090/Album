package com.example.owner.album.Album;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Album_Insert;
import com.example.owner.album.Main.MainActivity;
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

public class ControlTask extends AsyncTask<Void, Integer, Boolean> {
    private CountDownLatch _latch;
    private ArrayList<String> keywords = new ArrayList<>();
    private String albumName;
    private int keyWordCondition;
    private String date;
    private String date1;
    private int dateCondition;
    private Activity mActivity;
    private ProgressDialog dialog;


    public ControlTask(ArrayList<String> keywords, String albumName, int keyWordCondition, String date, String date1, int dateCondition, Activity activity) {
        this.keywords = keywords;
        this.albumName = albumName;
        this.keyWordCondition = keyWordCondition;
        this.date = date;
        this.date1 = date1;
        this.dateCondition = dateCondition;
        this._latch = new CountDownLatch(keywords.size() * 2);
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mActivity);
        dialog.setTitle("Please wait");
        dialog.setMessage("Creating album...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            Album_Insert album_insert = new Album_Insert();
            album_insert.Insert_DB(albumName, keywords, _latch);
            Keyword_Query keyword_query = new Keyword_Query();

            for (String keyword : keywords) {
                RealmResults<Keyword> realmResults = keyword_query.Double_Check(keyword);
                if (realmResults.size() != 0) {
                    RealmList<Related_Words> realmList = realmResults.get(0).getRelated_wordses();
                    if (realmList.size() == 0) {
                        new Word_association(keyword, _latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                } else {
                    new Word_association(keyword, _latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            }
            publishProgress(50);

            Log.d("db", "wait");
            _latch.await(5, TimeUnit.SECONDS);
            Log.d("db", "related");
            _latch = new CountDownLatch(1);
            new Album_Related(keyWordCondition, date, date1, dateCondition, _latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            _latch.await(5, TimeUnit.SECONDS);
            Log.d("db", "finish");
            publishProgress(100);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d("db", "seni");
        mActivity.startActivity(new Intent(mActivity, AlbumList_Activity.class));
    }

}
