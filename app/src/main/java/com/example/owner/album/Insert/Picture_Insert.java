package com.example.owner.album.Insert;


import android.net.ParseException;

import com.example.owner.album.model.Classification_Info_Eng;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Picture_Query;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Owner on 2016/10/11.
 */

public class Picture_Insert {
    public static void Insert_Picture(String path, String dateTime, String latitude, String longitude) throws ParseException {
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        Picture_Query picture_query = new Picture_Query();
        Picture_Info pictureInfo1 = null;
        RealmResults<Picture_Info> realmList = picture_query.Id_Query();
        if (realmList.size() == 0) {
            pictureInfo1 = r.createObject(Picture_Info.class, 1);
            pictureInfo1.setPath(path);

            DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
            try {
                java.util.Date date = format.parse(dateTime);
                pictureInfo1.setDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pictureInfo1.setLongitude(longitude);
            pictureInfo1.setLatitude(latitude);
        } else {
            realmList = realmList.sort("id", Sort.DESCENDING);
            int id = realmList.get(0).getId();
            pictureInfo1 = r.createObject(Picture_Info.class, id + 1);
            pictureInfo1.setPath(path);
            DateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
            try {
                java.util.Date date = format.parse(dateTime);
                pictureInfo1.setDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pictureInfo1.setLongitude(longitude);
            pictureInfo1.setLatitude(latitude);
        }
        r.commitTransaction();
        r.close();
    }

    public void Insert_Landmark_Eng(String landmark){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        Picture_Query picture_query = new Picture_Query();
        RealmResults<Picture_Info> picture_infos = picture_query.Id_Query();
        picture_infos = picture_infos.sort("id", Sort.DESCENDING);
        int id = picture_infos.get(0).getId();
        Picture_Info pictureInfo = r.where(Picture_Info.class).equalTo("id", id).findFirst();
        pictureInfo.setLandmark_eng(landmark);

        r.commitTransaction();
        r.close();
    }
    public void Insert_Landmark_Jap(String landmark){
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();

        Picture_Query picture_query = new Picture_Query();
        RealmResults<Picture_Info> picture_infos = picture_query.Id_Query();
        picture_infos = picture_infos.sort("id", Sort.DESCENDING);
        int id = picture_infos.get(0).getId();
        Picture_Info pictureInfo = r.where(Picture_Info.class).equalTo("id", id).findFirst();
        pictureInfo.setLandmark_jap(landmark);

        r.commitTransaction();
        r.close();
    }


}
