package com.example.silence.proximitybeacon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.Connection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * main
 *
 * 內容： 主程式,不重要,垃圾,做gui用
 *
 */
public class MainActivity extends AppCompatActivity {


    private Button Start, Stop;
    private ListView listView;
    private SimpleAdapter adapter;
    private Application application;
    private static String TAG = "eddyStoneBeaconActivity";
    private ArrayList<HashMap<String,String>> list;

    private static final String[] uuid = new String[] {"Beacon_UUID"};

    private static final String[] primary = new String[] {"Primary:......Minor:......."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList();
        application = new Application(this.getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        Start = (Button) findViewById(R.id.buttonStartConnect);
        Stop = (Button) findViewById(R.id.buttonStopConnect);

        Start.setOnClickListener(new ConnectListener(this,application));
        Stop.setOnClickListener(new ConnectListener(this,application));

        //把資料加入ArrayList中
        for(int i=0; i<uuid.length; i++){
            HashMap<String,String> item = new HashMap();
            item.put( "uuid", uuid[i]);
            item.put( "primary",primary[i] );
            list.add( item );
        }
        adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[] { "primary","uuid" },
                new int[] { android.R.id.text1, android.R.id.text2 } );

        listView.setAdapter( adapter );
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"第"+(position+1)+"项", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //自動做權限保障
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }
    @Override
    protected void onPause() {
        //   beaconManager.stopRanging(region);
        //   beaconManager.stopMonitoring(region);
        super.onPause();
    }


    public void showNotification(String title, String message) {

        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}