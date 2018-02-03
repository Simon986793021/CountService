package com.wind.countservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IcounterCallBack {
    private TextView textView;
    private Button startButton;
    private Button stopButton;
    private IcounterService icounterService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_show);
        startButton = findViewById(R.id.bt_start);
        stopButton = findViewById(R.id.bt_stop);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);


        Intent intent = new Intent(MainActivity.this, CounterService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(startButton)) {
            if (icounterService != null) {
                icounterService.startCounter(0, this);
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        } else if (view.equals(stopButton)) {
            if (icounterService != null) {
                icounterService.stopCounter();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            icounterService = ((CounterService.CounterBiner) iBinder).getService();

            Log.i("SIMON", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            icounterService = null;
            Log.i("SIMON", "disconnected");
        }
    };

    @Override
    public void count(int val) {
        String text = String.valueOf(val);
        textView.setText(text);
    }
}
