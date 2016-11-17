package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Album_Insert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Owner on 2016/11/17.
 */

public class Album_Related  extends AsyncTask<Void, Void, Boolean> {
    private int keyWordCondition;
    private String date;
    private int dateCondition;

    public Album_Related(int keyWordCondition, String date, int dateCondition) {
        this.keyWordCondition = keyWordCondition;
        this.date = date;
        this.dateCondition = dateCondition;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // プログレス表示処理
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("db","wait");
        try {
            Thread.sleep(10000);
        }catch (Exception e){

        }
        Log.d("db","related");
        new Album_Insert().Insert_Picture_Info(keyWordCondition,date,dateCondition);
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

    }
}
