package com.web.curation.util;

/**
 * com.web.curation.util
 * DistanceUtil.java
 * @date    2021-02-9 오후 7:41
 * @author  이주희
 *
 * @변경이력
 **/

public class DistanceUtil {

    public static double calcDistance(double srcLat, double srcLng, double destLat, double destLng) {
        double theta = srcLng - destLng;
        double dist = Math.sin(deg2rad(srcLat)) * Math.sin(deg2rad(destLat))
                + Math.cos(deg2rad(srcLat)) * Math.cos(deg2rad(destLat)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;

        return dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
