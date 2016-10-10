package com.example.owner.album.Insert;


import android.net.ParseException;

import com.example.owner.album.model.Classification_Info_Eng;
import com.example.owner.album.model.Classification_Info_Jap;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.model.Related_Words;
import com.example.owner.album.model.Tag;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Owner on 2016/10/11.
 */

public class Picture_Insert {
    public void Insert_DB() throws ParseException {
        Realm r = Realm.getDefaultInstance();

        r.beginTransaction();

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

        //タグリスト
        RealmList<Tag> tagslist1 = new RealmList<>();
        tagslist1.add(tag1);
        tagslist1.add(tag2);
        tagslist1.add(tag3);




        Picture_Info pictureInfo1 = r.createObject(Picture_Info.class, 1);
        pictureInfo1.setTags(tagslist1);
        pictureInfo1.setPath("/mnt/sdcard/DCIM/Camera/sample1.jpg");

        //pictureInfo1.setDate(Date.valueOf("2016-01-01"));
        DateFormat format=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.US);
        try {
            java.util.Date date = format.parse("2016/10/2 12:00:00");
            pictureInfo1.setDate(date);
        }catch (Exception e){
            e.printStackTrace();
        }

        pictureInfo1.setLongitude("35/1,50/1,3615/100");
        pictureInfo1.setLatitude("139/1,23/1,595/100");

/*
        Picture_Info pictureInfo2 = r.createObject(Picture_Info.class, 2);
        pictureInfo2.setPath("/mnt/sdcard/DCIM/Camera/sample2.jpg");
        pictureInfo2.setDate(Date.valueOf("2016:10:03"));
        pictureInfo2.setLongitude("35/1,50/1,3715/100");
        pictureInfo2.setLatitude("139/1,23/1,695/100");

        Picture_Info pictureInfo3 = r.createObject(Picture_Info.class, 3);
        pictureInfo3.setPath("/mnt/sdcard/DCIM/Camera/sample3.jpg");
        pictureInfo3.setDate(Date.valueOf("2016:10:04"));
        pictureInfo3.setLongitude("35/1,50/1,3815/100");
        pictureInfo3.setLatitude("139/1,23/1,795/100");
*/
        r.commitTransaction();
        r.close();

    }
}
