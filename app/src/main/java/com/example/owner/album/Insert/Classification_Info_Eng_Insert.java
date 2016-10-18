package com.example.owner.album.Insert;

import com.example.owner.album.model.Classification_Info_Eng;
import com.example.owner.album.model.Picture_Info;
import com.example.owner.album.query.Classification_Info_Eng_Query;
import com.example.owner.album.query.Picture_Query;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Owner on 2016/10/19.
 */

public class Classification_Info_Eng_Insert {
    public void Insert_Classification_Info(ArrayList<String> classification_Info) {
        Realm r = Realm.getDefaultInstance();

        r.beginTransaction();
        //分類情報
        Classification_Info_Eng_Query classification_info_eng_query = new Classification_Info_Eng_Query();
        RealmList<Classification_Info_Eng> classification_info_engs = new RealmList<>();
        ArrayList<String>name =new ArrayList<>();

        for(int i=0;i<classification_Info.size();i++){
            RealmResults<Classification_Info_Eng> realmResults=classification_info_eng_query.Double_Check(classification_Info.get(i));
            if(realmResults.size()==0){
                name.add(classification_Info.get(i));
            }else{
                classification_info_engs.add(realmResults.get(0));
            }
        }
        Classification_Info_Eng classification_info_eng1[];
        if(name.size()!=0) {
            classification_info_eng1 = new Classification_Info_Eng[name.size()];
            for (int i = 0; i < classification_info_eng1.length; i++) {
                classification_info_eng1[i] = r.createObject(Classification_Info_Eng.class);
                classification_info_eng1[i].setName(name.get(i));
            }
            for (Classification_Info_Eng classificationInfoEng : classification_info_eng1) {
                classification_info_engs.add(classificationInfoEng);
            }
        }

        Picture_Query picture_query = new Picture_Query();
        RealmResults<Picture_Info> picture_infos = picture_query.Id_Query();
        picture_infos = picture_infos.sort("id", Sort.DESCENDING);
        int id = picture_infos.get(0).getId();

        Picture_Info pictureInfo = r.where(Picture_Info.class).equalTo("id", id).findFirst();
        pictureInfo.setClassification_info_engs(classification_info_engs);

        r.commitTransaction();
        r.close();

    }
}
