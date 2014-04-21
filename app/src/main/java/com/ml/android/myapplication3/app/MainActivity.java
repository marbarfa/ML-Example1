package com.ml.android.myapplication3.app;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.ml.android.myapplication3.common.HttpUtils;


public class MainActivity extends Activity {

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



}
