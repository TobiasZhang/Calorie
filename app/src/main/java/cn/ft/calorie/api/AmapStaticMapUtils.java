package cn.ft.calorie.api;

import com.amap.api.maps.model.LatLng;

import java.util.List;

/**
 * Created by TT on 2017/1/30.
 */
public class AmapStaticMapUtils {
    public static final String AMAP_WEB_SERVICE_KEY = "817c7afc89c02201e3fd99db24807b6f";
    public static final String AMAP_STATIC_MAP_URL = "http://restapi.amap.com/v3/staticmap";

    public static String getStaticMapUrl(LatLng center, int zoom, List<LatLng> locationList){
        StringBuilder sb = new StringBuilder();
        sb.append(AMAP_STATIC_MAP_URL);
        sb.append("?");
        sb.append("key=");
        sb.append(AMAP_WEB_SERVICE_KEY);
        sb.append("&");
        sb.append("location=");
        sb.append(center.longitude);
        sb.append(",");
        sb.append(center.latitude);
        sb.append("&");
        sb.append("zoom=");
        sb.append(zoom);
        sb.append("&");
        sb.append("size=");
        sb.append("400*400");
        sb.append("&");
        //markers
        sb.append("markers=");
        sb.append("mid,0x66CCFF,起:");
        LatLng start = locationList.get(0);
        LatLng end = locationList.get(locationList.size()-1);
        sb.append(start.longitude);
        sb.append(",");
        sb.append(start.latitude);
        sb.append("|");
        sb.append("mid,0xFF6699,终:");
        sb.append(end.longitude);
        sb.append(",");
        sb.append(end.latitude);
        //paths
        sb.append("&");
        sb.append("paths=");
        sb.append("5,0x0000FF,1,,:");
        StringBuilder locationListSb = new StringBuilder();
        for(LatLng latLng : locationList){
            locationListSb.append(latLng.longitude);
            locationListSb.append(",");
            locationListSb.append(latLng.latitude);
            locationListSb.append(";");
        }
        sb.append(locationListSb.subSequence(0,locationListSb.length()-1));

        return sb.toString();
    }
}
