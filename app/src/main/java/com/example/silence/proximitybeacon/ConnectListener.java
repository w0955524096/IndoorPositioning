package com.example.silence.proximitybeacon;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by silence on 8/20/16.
 *
 * 內容： 觸發連結按鈕,這裡暫時搜索,連結,監控寫在一起,
 *      所以較長,與eddystone的官方寫法差不多一樣
 *
 *      輸出是以log檔輸出,如果按連結log檔會一直跑beacon[]
 *      找到就可以了
 *
 *
 */
class ConnectListener implements View.OnClickListener {

    private static Toast toast;
    private MainActivity activity;
    private BeaconManager beaconManager;
    private Region region;
    private UUID uuid;
    private boolean lock ;
    private ArrayList list;
    private String TAG = "ConnectListener";

    public ConnectListener(MainActivity activity,Application application) {
        this.activity = activity;
        this.beaconManager = application.getBeaconManager();
        this.region = application.getRegion();
        this.uuid = application.getUUID();
    }
    public ConnectListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonStartConnect:

                //確認權限
                lock = SystemRequirementsChecker.checkWithDefaultDialogs(activity);

                if(lock)
                {
                    //建立連線
                    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                        @Override
                        public void onServiceReady() {
                            makeTextAndShow(activity, "start to find", 2);

                            try {
                                beaconManager.startRanging(region);
                                beaconManager.startMonitoring(region);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception:" + e);
                            }
                        }
                    });
                    //搜索設備
                    beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                        //這裡會持續搜尋
                        @Override
                        public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                            TAG = " onBeaconsDiscovered";
                            Log.i(TAG, "searching...");
                            Log.i(TAG, "Ranged beacons: " + beacons);
/*
                            HashMap<String,String> item = new HashMap();
                            item.put( "uuid",beacons.get(0).getProximityUUID().toString());
                            item.put( "primary", beacons.get(1).getProximityUUID().toString());
                            list.add( item );
*/
                            if (beacons != null && beacons.size() > 0) {
                                if (beacons.get(0).getProximityUUID().equals(uuid)) {
                                    Log.i(TAG, "same beacon");
                                    return;
                                }
                                uuid = beacons.get(0).getProximityUUID();
                                Log.i(TAG, "ESTIMOTE_PROXIMITY_UUID:" + uuid);

                            }
                        }
                    });
                    //監控設備
                    beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

                        @Override
                        public void onEnteredRegion(Region region, List<Beacon> list) {
                            TAG = "onEnteredRegion";
                            Log.i(TAG, "you cross the boundary of the region passed to");

                            if (list != null && list.size() > 0) {

                                if (list.get(0).getProximityUUID().equals(uuid)) {
                                    Log.i(TAG, "same beacon");
                                    return;
                                }
                                uuid = list.get(0).getProximityUUID();
                                Log.i(TAG, "ESTIMOTE_PROXIMITY_UUID:" + uuid);
                            }
                        }

                        @Override
                        public void onExitedRegion(Region region) {
                            TAG = "onExitedRegion";
                            Log.w(TAG, "you cross the boundary");
                            activity.showNotification("go away", "the beacon is away from here");
                        }
                    });

                }
                else
                    Log.i(TAG,"已經正在尋找設備或是尚未開啟藍芽");
                break;

            case R.id.buttonStopConnect:
                Log.i(TAG,"stop");
                beaconManager.stopMonitoring(region);
                beaconManager.stopRanging(region);
                break;
        }
    }

    private static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (toast == null) {
            //如果還沒有用過makeText方法，才使用
            toast = android.widget.Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }
}
