package com.example.silence.proximitybeacon;
import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;


/**
 * 內容： 垃圾,不用看,也是gui
 */
class SplashScreenActivity extends Activity {

    // String for LogCat documentation
    private final static String TAG = "SplashScreen-SplashScreenActivity";
    private ProgressBar spinbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spinbar = (ProgressBar) findViewById(R.id.progressBar1);
        spinbar.setVisibility(View.GONE);
        new SplashScreenAsyncTask(this, spinbar).execute();
    }
}
