package com.example.owner.album.Insert;

import com.example.owner.album.model.Classification_Info_Jap;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Classification_Info_Jap_Query;
import com.example.owner.album.query.Picture_Query;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Owner on 2016/10/19.
 */

public class Classification_Info_Jap_Insert {
    public void Insert_Classification_Info(ArrayList<String> classification_Info) {
        Realm r = Realm.getDefaultInstance();

        r.beginTransaction();
        //分類情報
        Classification_Info_Jap_Query classification_info_jap_query = new Classification_Info_Jap_Query();
        RealmList<Classification_Info_Jap> classification_info_japs = new RealmList<>();
        ArrayList<String> name = new ArrayList<>();

        for (int i = 0; i < classification_Info.size(); i++) {
            RealmResults<Classification_Info_Jap> realmResults = classification_info_jap_query.Double_Check(classification_Info.get(i));
            if (realmResults.size() == 0) {
                name.add(classification_Info.get(i));
            } else {
                classification_info_japs.add(realmResults.get(0));
            }
        }
        Classification_Info_Jap classification_info_jap1[];
        if (name.size() != 0) {
            classification_info_jap1 = new Classification_Info_Jap[name.size()];
            for (int i = 0; i < classification_info_jap1.length; i++) {
                classification_info_jap1[i] = r.createObject(Classification_Info_Jap.class);
                classification_info_jap1[i].setName(name.get(i));
            }
            for (Classification_Info_Jap classificationInfoJap : classification_info_jap1) {
                classification_info_japs.add(classificationInfoJap);
            }
        }

        Picture_Query picture_query = new Picture_Query();
        RealmResults<Picture_Info> picture_infos = picture_query.Id_Query();
        picture_infos = picture_infos.sort("id", Sort.DESCENDING);
        int id = picture_infos.get(0).getId();

        Picture_Info pictureInfo = r.where(Picture_Info.class).equalTo("id", id).findFirst();
        pictureInfo.setClassification_info_japs(classification_info_japs);

        r.commitTransaction();
        r.close();

    }
}
