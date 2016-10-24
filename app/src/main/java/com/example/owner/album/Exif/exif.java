package com.example.owner.album.Exif;

/**
 * Created by Owner on 2016/10/18.
 */

public class Exif {
    public String ExifHourMinSecToDegrees(String exifhourminsec) {
        String hourminsec[] = exifhourminsec.split(",");
        String hour[] = hourminsec[0].split("/");
        String min[] = hourminsec[1].split("/");
        String sec[] = hourminsec[2].split("/");
        double dhour = (double)Integer.parseInt(hour[0]) / (double)Integer.parseInt(hour[1]);
        double dmin = (double)Integer.parseInt(min[0]) / (double)Integer.parseInt(min[1]);
        double dsec = (double)Integer.parseInt(sec[0]) / (double)Integer.parseInt(sec[1]);
        double degrees = dhour + dmin / 60.0 + dsec / 3600.0;
        return String.valueOf(degrees);
    }

}
