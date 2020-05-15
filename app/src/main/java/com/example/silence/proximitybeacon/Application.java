package com.example.silence.proximitybeacon;

import android.content.Context;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by silence on 8/20/16.
 *
 * 內容： beacon manager的實體化與beacon-uuid的設置
 *
 */
public class Application {

    private static final String TAG = "Application";

    private static UUID ESTIMOTE_PROXIMITY_UUID =
            UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");

    private BeaconManager beaconManager;

    private Region region; // 用來找出欲定位的beacon

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    private static final Map<String, List<String>> PLACES_BY_BEACONS;
    //沒用到
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("22504:48827", new ArrayList<String>() {{
            add("Heavenly Sandwiches");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Green & Green Salads");
            // "Green & Green Salads" is the next closest
            add("Mini Panini");
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("648:12", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }
    //
    //
    Application(Context context){
        beaconManager = new BeaconManager(context);
        region = new Region("ranged region", ESTIMOTE_PROXIMITY_UUID, null, null);
        // 亦即找出此uuid,主鍵,次鍵＝null的beacon
        /** uuid可能會一樣正常可以找多個
         若主鍵次鍵都有設定最多定位一個 */

        /**
         *
         With only UUID:
         it consists of all beacons with a given UUID.
         For example: a region defined with default Estimote UUID would consist of all Estimote Beacons with unchanged UUID.

         With UUID and Major:
         it consists of all beacons using a specific combination of UUID and Major.
         For example: all Estimote Beacons with default UUID and Major set to 13579.

         With UUID, Major and Minor:
         it consists of only a single beacon (Estimote Cloud prevents having two beacons with the same IDs).
         For example, one with default Estimote UUID, Major set to 13579 and Minor set to 2468.
         *
         */
    }

    BeaconManager getBeaconManager(){
        return beaconManager;
    }
    Region getRegion(){
        return  region;
    }
    UUID getUUID(){
        return ESTIMOTE_PROXIMITY_UUID;
    }

}
