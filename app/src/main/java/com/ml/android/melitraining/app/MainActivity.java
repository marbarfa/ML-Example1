package com.ml.android.melitraining.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import com.ml.android.melitraining.common.HttpUtils;
import com.ml.android.melitraining.services.MeliBookmarksService;


public class MainActivity extends Activity {

    private MeliBookmarksService s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
                if (HttpUtils.isConnected(connMgr)){
                    String searchStr = ((TextView)findViewById(R.id.input)).getText().toString();
                    android.content.Intent i = new android.content.Intent(MainActivity.this, SearchResultActivity.class);
                    i.putExtra("search_string", searchStr);
                    startActivity(i);
                }else{
                    Toast t = Toast.makeText(MainActivity.this,"You are not connected to the internet!", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, MeliBookmarksService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
    }



    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            MeliBookmarksService.MyBinder b = (MeliBookmarksService.MyBinder) binder;
            s = b.getService();
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT)
                    .show();
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };
}
