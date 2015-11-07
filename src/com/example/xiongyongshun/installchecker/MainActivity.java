package com.example.xiongyongshun.installchecker;

import android.os.Bundle;
import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xys.certificatereader.KeystoreReader;
import com.xys.installchecker.InstallChecker;


public class MainActivity extends Activity implements View.OnClickListener{
    Button btn_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long time1 = System.currentTimeMillis();
        Log.d("xys", "time: " + time1);
        try {
            InstallChecker.initKeystoreInfo(KeystoreReader.getKeystoreInfo("/sdcard/publickey.cert"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long time2 = System.currentTimeMillis();
        Log.d("xys", "time: " + time2);
        Log.d("xys", "Init elapsed: " + (time2 - time1));

        btn_check = (Button)findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_check:
            long time1 = System.currentTimeMillis();
            Log.d("xys", "time: " + time1);
            Log.d("xys", "Can install: " + InstallChecker.check("/sdcard/wandoujia_signed.apk"));
            long time2 = System.currentTimeMillis();
            Log.d("xys", "time: " + time2);
            Log.d("xys", "Elapsed: " + (time2 - time1));
            break;
        }
    }
}
