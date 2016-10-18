package com.example.owner.album.Insert;


import android.graphics.Picture;
import android.net.ParseException;
import android.util.Log;

import com.example.owner.album.model.Album;
import com.example.owner.album.model.Classification_Info_Eng;
import com.example.owner.album.model.Classification_Info_Jap;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.model.Related_Words;
import com.example.owner.album.model.Tag;
import com.example.owner.album.query.Classification_Info_Eng_Query;
import com.example.owner.album.query.Picture_Query;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
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
        if (realmList.size() == 0l) {
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

    public void Insert_DB(ArrayList<String> classification_info_eng) throws ParseException {
        Realm r = Realm.getDefaultInstance();

        r.beginTransaction();

        //分類情報
        Classification_Info_Eng classification_info_eng1[] = new Classification_Info_Eng[classification_info_eng.size()];
        for (int i = 0; i < classification_info_eng1.length; i++) {
            classification_info_eng1[i] = r.createObject(Classification_Info_Eng.class);
            classification_info_eng1[i].setName(classification_info_eng.get(i));
        }

/*
        //分類情報
        Classification_Info_Jap classification_info_jap1 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap1.setName("パンダ");

        Classification_Info_Eng classification_info_eng1 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng1.setName("panda");

        //関連語
        String words[] = {"procyonid", "ailurus"};
        Related_Words related_words1[] = new Related_Words[words.length];
        for (int i = 0; i < related_words1.length; i++) {
            related_words1[i] = r.createObject(Related_Words.class);
            related_words1[i].setRelated_words(words[i]);
        }
        RealmList<Related_Words> related_wordses_list1 = new RealmList<>();
        for (int i = 0; i < related_words1.length; i++) {
            related_wordses_list1.add(related_words1[i]);
        }

        //分類情報と関連語紐づけ
        classification_info_jap1.setRelated_wordses(related_wordses_list1);
        classification_info_eng1.setRelated_wordses(related_wordses_list1);

        //タグと分類情報紐づけ
        Tag tag1 = r.createObject(Tag.class);
        tag1.setClassification_info_japs(classification_info_jap1);
        tag1.setClassification_info_engs(classification_info_eng1);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        //分類情報
        Classification_Info_Jap classification_info_jap2 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap2.setName("哺乳類");

        Classification_Info_Eng classification_info_eng2 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng2.setName("mammal");

        //関連語
        Related_Words related_words2 = r.createObject(Related_Words.class);
        related_words2.setRelated_words("craniate");
        RealmList<Related_Words> related_wordses_list2 = new RealmList<>();
        related_wordses_list2.add(related_words2);

        //分類情報と関連語紐づけ
        classification_info_jap2.setRelated_wordses(related_wordses_list2);
        classification_info_eng2.setRelated_wordses(related_wordses_list2);

        //タグと分類情報紐づけ
        Tag tag2 = r.createObject(Tag.class);
        tag2.setClassification_info_japs(classification_info_jap2);
        tag2.setClassification_info_engs(classification_info_eng2);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //分類情報
        Classification_Info_Jap classification_info_jap3 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap3.setName("脊椎動物");

        Classification_Info_Eng classification_info_eng3 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng3.setName("vertebrate");

        //関連語
        Related_Words related_words3 = r.createObject(Related_Words.class);
        related_words3.setRelated_words("amphibian");
        RealmList<Related_Words> related_wordses_list3 = new RealmList<>();
        related_wordses_list3.add(related_words3);

        //分類情報と関連語紐づけ
        classification_info_jap3.setRelated_wordses(related_wordses_list3);
        classification_info_eng3.setRelated_wordses(related_wordses_list3);

        //タグと分類情報紐づけ
        Tag tag3 = r.createObject(Tag.class);
        tag3.setClassification_info_japs(classification_info_jap3);
        tag3.setClassification_info_engs(classification_info_eng3);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //タグリスト
        RealmList<Tag> tagslist1 = new RealmList<>();
        tagslist1.add(tag1);
        tagslist1.add(tag2);
        tagslist1.add(tag3);

        //写真情報
        Picture_Info pictureInfo1 = r.createObject(Picture_Info.class, 1);
        pictureInfo1.setTags(tagslist1);
        pictureInfo1.setPath("/mnt/sdcard/DCIM/Camera/sample1.jpg");
        //pictureInfo1.setDate(Date.valueOf("2016-01-01"));
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        try {
            java.util.Date date = format.parse("2016/10/2 12:00:00");
            pictureInfo1.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pictureInfo1.setLongitude("35/1,50/1,3615/100");
        pictureInfo1.setLatitude("139/1,23/1,595/100");

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //分類情報
        Classification_Info_Jap classification_info_jap4 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap4.setName("虎");
        Classification_Info_Eng classification_info_eng4 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng4.setName("tiger");

        //関連語
        Related_Words related_words4 = r.createObject(Related_Words.class);
        related_words4.setRelated_words("cat");
        RealmList<Related_Words> related_wordses_list4 = new RealmList<>();
        related_wordses_list4.add(related_words4);

        //分類情報と関連語紐づけ
        classification_info_jap4.setRelated_wordses(related_wordses_list4);
        classification_info_eng4.setRelated_wordses(related_wordses_list4);

        //タグと分類情報紐づけ
        Tag tag4 = r.createObject(Tag.class);
        tag4.setClassification_info_japs(classification_info_jap4);
        tag4.setClassification_info_engs(classification_info_eng4);

        //タグリスト
        RealmList<Tag> tagslist2 = new RealmList<>();
        tagslist2.add(tag4);
        tagslist2.add(tag2);
        tagslist2.add(tag3);

        //写真情報
        Picture_Info pictureInfo2 = r.createObject(Picture_Info.class, 2);
        pictureInfo2.setTags(tagslist2);
        pictureInfo2.setPath("/mnt/sdcard/DCIM/Camera/sample2.jpg");
        try {
            java.util.Date date = format.parse("2016/10/3 13:00:00");
            pictureInfo2.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //pictureInfo2.setDate(Date.valueOf("2016:10:03"));
        pictureInfo2.setLongitude("35/1,50/1,3715/100");
        pictureInfo2.setLatitude("139/1,23/1,695/100");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //分類情報
        Classification_Info_Jap classification_info_jap5 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap5.setName("史跡");
        Classification_Info_Eng classification_info_eng5 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng5.setName("historic site");

        //関連語
        Related_Words related_words5 = r.createObject(Related_Words.class);
        related_words5.setRelated_words("Natural monument");
        RealmList<Related_Words> related_wordses_list5 = new RealmList<>();
        related_wordses_list5.add(related_words5);

        //分類情報と関連語紐づけ
        classification_info_jap5.setRelated_wordses(related_wordses_list5);
        classification_info_eng5.setRelated_wordses(related_wordses_list5);

        //タグと分類情報紐づけ
        Tag tag5 = r.createObject(Tag.class);
        tag5.setClassification_info_japs(classification_info_jap5);
        tag5.setClassification_info_engs(classification_info_eng5);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //分類情報
        Classification_Info_Jap classification_info_jap6 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap6.setName("寺");
        Classification_Info_Eng classification_info_eng6 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng6.setName("temple");

        //関連語
        Related_Words related_words6 = r.createObject(Related_Words.class);
        related_words6.setRelated_words("building");
        RealmList<Related_Words> related_wordses_list6 = new RealmList<>();
        related_wordses_list6.add(related_words6);

        //分類情報と関連語紐づけ
        classification_info_jap6.setRelated_wordses(related_wordses_list6);
        classification_info_eng6.setRelated_wordses(related_wordses_list6);

        //タグと分類情報紐づけ
        Tag tag6 = r.createObject(Tag.class);
        tag6.setClassification_info_japs(classification_info_jap6);
        tag6.setClassification_info_engs(classification_info_eng6);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //分類情報
        Classification_Info_Jap classification_info_jap7 = r.createObject(Classification_Info_Jap.class);
        classification_info_jap7.setName("観光");
        Classification_Info_Eng classification_info_eng7 = r.createObject(Classification_Info_Eng.class);
        classification_info_eng7.setName("tourism");

        //関連語
        Related_Words related_words7 = r.createObject(Related_Words.class);
        related_words7.setRelated_words("tourist");
        RealmList<Related_Words> related_wordses_list7 = new RealmList<>();
        related_wordses_list7.add(related_words7);

        //分類情報と関連語紐づけ
        classification_info_jap7.setRelated_wordses(related_wordses_list7);
        classification_info_eng7.setRelated_wordses(related_wordses_list7);

        //タグと分類情報紐づけ
        Tag tag7 = r.createObject(Tag.class);
        tag7.setClassification_info_japs(classification_info_jap7);
        tag7.setClassification_info_engs(classification_info_eng7);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //タグリスト
        RealmList<Tag> tagslist3 = new RealmList<>();
        tagslist3.add(tag5);
        tagslist3.add(tag6);
        tagslist3.add(tag7);

        //写真情報
        Picture_Info pictureInfo3 = r.createObject(Picture_Info.class, 3);
        pictureInfo3.setTags(tagslist3);
        pictureInfo3.setPath("/mnt/sdcard/DCIM/Camera/sample3.jpg");
        try {
            java.util.Date date = format.parse("2016/10/4 14:00:00");
            pictureInfo3.setDate(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //pictureInfo3.setDate(Date.valueOf("2016:10:04"));
        pictureInfo3.setLongitude("35/1,50/1,3815/100");
        pictureInfo3.setLatitude("139/1,23/1,795/100");

*/
        r.commitTransaction();
        r.close();

    }
}
