package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Insert.Album_Insert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Owner on 2016/11/17.
 */

public class Album_Related extends AsyncTask<Void, Void, Boolean> {
    private int keyWordCondition;
    private String date;
    private int dateCondition;
    private CountDownLatch _latch;

    public Album_Related(int keyWordCondition, String date, int dateCondition, CountDownLatch _latch) {
        this.keyWordCondition = keyWordCondition;
        this.date = date;
        this.dateCondition = dateCondition;
        this._latch = _latch;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // プログレス表示処理
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        new Album_Insert().Insert_Picture_Info(keyWordCondition, date, dateCondition);
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        _latch.countDown();
        super.onPostExecute(result);

    }
}
