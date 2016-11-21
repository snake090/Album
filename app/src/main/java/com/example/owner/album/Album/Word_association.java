package com.example.owner.album.Album;

import android.os.AsyncTask;
import android.util.Log;

import com.example.owner.album.Translate.Translate_Words_JapToEng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Owner on 2016/11/09.
 */

public class Word_association extends AsyncTask<Void, Void, ArrayList<String>> {

    private String word;
    private CountDownLatch _latch;


    public Word_association(String word, CountDownLatch _latch) {
        this.word = word;
        this._latch = _latch;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... value) {
        ArrayList<String> message = new ArrayList<String>();
        try {
            HttpURLConnection con = null;
            URL url = new URL("http://kizasi.jp/kizapi.py?span=24&kw_expr=" + word + "&type=coll");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);
            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {

                String xml = InputStreamToString(con.getInputStream());
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(xml));
                int eventType = xpp.getEventType();

                while ((eventType = xpp.next()) != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && "title".equals(xpp.getName())) {
                        message.add(xpp.nextText());
                    }
                }

            }
            message.remove(0);
            message.remove(0);
            System.out.print("");
        } catch (Exception e) {
            e.getStackTrace();
        }
        return message;
    }

    protected void onPostExecute(ArrayList<String> result) {

        ArrayList<String> keyword = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            keyword.add(result.get(i));
        }
        new Translate_Words_JapToEng(keyword,word,_latch).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
