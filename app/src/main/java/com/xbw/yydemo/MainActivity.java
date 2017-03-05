package com.xbw.yydemo;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import edu.cmu.pocketsphinx.demo.RecognitionListener;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.xbw.yydemo.util.PocketSphinxUtil;

public class MainActivity extends AppCompatActivity {

    private TextView tx1;
    private Button btn1;
    private Context mContext;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private Vibrator vibrator;
    //private YyService yyService;
    private YyService serviceBinder;
    // Handles the connection between the service and activity
    private ServiceConnection mConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnection = new ServiceConnection(){
            public void onServiceConnected(ComponentName className, IBinder service) {
                // Called when the connection is made.
                serviceBinder = ((YyService.MyBinder)service).getService();
                Log.d("daodi显示吗","chaoi1233 ");
                serviceBinder.setYyResultListener(new YyService.YyResultListener() {
                    @Override
                    public void YyR(String s) {
                        //Log.d("daodi显示吗","chaoi "+s);
                        Toast.makeText(MainActivity.this,"哈哈哈"+s,Toast.LENGTH_LONG).show();
                    }
                });
            }
            public void onServiceDisconnected(ComponentName className) {
                // Received when the service unexpectedly disconnects.
                serviceBinder = null;
            }
        };
        Intent bindIntent = new Intent(this, YyService.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
        //Intent startIntent = new Intent(this, YyService.class);
        startService(bindIntent);
        initView();
        //registerReceiver();
    }
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.xbw.yydemo.YyService");
        MainActivity.this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                Log.d("", "count_data=" + bundle
                        .getInt("hh") + "");
                Toast.makeText(MainActivity.this,bundle
                        .getInt("hh"),Toast.LENGTH_LONG).show();
            }
        }, intentFilter);
    }
    private void initView(){
        tx1 = (TextView) findViewById(R.id.textView);
        btn1 = (Button) findViewById(R.id.button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
    private void wakeVibrator(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};
        vibrator.vibrate(pattern,-1);
    }
    private void wakeAndUnlock()
    {
        //获取电源管理器对象
        pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
    }
}
