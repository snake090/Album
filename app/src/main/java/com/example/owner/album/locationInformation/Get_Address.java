package com.example.owner.album.locationInformation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Owner on 2016/11/14.
 */

public class Get_Address {
    public String getAddress(Context context, String longitude, String latitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        StringBuilder result = new StringBuilder();

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
        } catch (IOException e) {
            return "";
        }

        for (Address address : addresses) {
            int idx = address.getMaxAddressLineIndex();
            // 1番目のレコードは国名のため省略
            for (int i = 0; i <= idx; i++) {
                result.append(address.getAddressLine(i));
            }
        }

        return result.toString();
    }
}
