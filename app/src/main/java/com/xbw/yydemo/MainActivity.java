package com.xbw.yydemo;


import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import edu.cmu.pocketsphinx.demo.RecognitionListener;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.Camera;
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
import android.hardware.Camera.Parameters;

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
    // 打开闪光灯
    boolean islight=false;
    Camera camera = Camera.open();
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
                        if (!islight&&s.equals("开灯")) {
                            Parameters mParameters = camera.getParameters();
                            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(mParameters);
                            camera.startPreview();
                            islight = true;
                            Toast.makeText(MainActivity.this,"哈哈哈"+s,Toast.LENGTH_LONG).show();
                        } else if(islight&&s.equals("关灯")){
                            Parameters mParameters = camera.getParameters();
                            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(mParameters);
                            camera.stopPreview();
                            islight = false;
                            Toast.makeText(MainActivity.this,"哈哈哈"+s,Toast.LENGTH_LONG).show();
                        }
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
        startService(bindIntent);
        //finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
    //震动
    private void wakeVibrator(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};
        vibrator.vibrate(pattern,-1);
    }
    //唤醒屏幕
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
