package com.xbw.yydemo.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import edu.cmu.pocketsphinx.demo.RecognitionListener;

import edu.cmu.pocketsphinx.demo.RecognizerTask;

/**
 * Created by xubowen on 2017/3/4.
 */
public class PocketSphinxUtil {
    public static final int DELAY_MILLIS = 120 * 1000;//120秒

    //PocketSphinxUtil(){
    static {
        System.loadLibrary("pocketsphinx_jni");
    }

    private boolean isStop;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("重启");
            rec.stop();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isStop)
                    {
                        return;
                    }
                    rec.start();
                    mHandler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
                }
            }).start();

        }
    };

    private static PocketSphinxUtil instance = null;
    private RecognizerTask rec;
    private Thread rec_thread;

    public static PocketSphinxUtil get(Context context) {
        if (instance == null) {
            instance = new PocketSphinxUtil(context);
        }
        return instance;
    }

    public PocketSphinxUtil setListener(RecognitionListener listener) {
        rec.setRecognitionListener(listener);
        return this;
    }

    public PocketSphinxUtil start() {
        isStop=false;
        rec.start();
        mHandler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
        return this;
    }

    public PocketSphinxUtil stop() {
        isStop=true;
        rec.stop();
        mHandler.removeMessages(1);
        return this;
    }

    private PocketSphinxUtil(Context context) {
        rec = new RecognizerTask(context);
        rec_thread = new Thread(rec);
        rec_thread.start();
    }

}
