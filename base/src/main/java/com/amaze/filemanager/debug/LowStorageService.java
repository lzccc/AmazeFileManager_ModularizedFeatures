package com.amaze.filemanager.debug;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LowStorageService extends Service {
    SplitInstallManager manager;
    //private UpdateHandler updateHandler; // 更新 Widget 的 Handler
    private static final int ALARM_DURATION  = 1 * 60 * 1000; // service 自启间隔

    public LowStorageService() {

    }

    boolean checkSDCard() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSizeLong();
            long blockCount = sf.getBlockCountLong();
            long availCount = sf.getAvailableBlocksLong();
            Log.d(Float.toString((float) (availCount * blockSize) / (float) (blockSize * blockCount)), "selfAdaptive1");
            return (float) (availCount * blockSize) / (float) (blockSize * blockCount) > 0.05;
        }
        return true;
    }

    boolean checkSystem() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSizeLong();
        long blockCount = sf.getBlockCountLong();
        long availCount = sf.getAvailableBlocksLong();
        Log.d(Float.toString((float) (availCount * blockSize) / (float) (blockSize * blockCount)), "selfAdaptive2");
        return (float)(availCount*blockSize)/(float)(blockSize*blockCount) > 0.05;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        //Log.d("SelfAdaptive", "onCreate");
        manager = SplitInstallManagerFactory.create(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //Intent intent1 = new Intent("com.amaze.filemanager.debug.LOW_STORAGE");
        //this.sendBroadcast(intent1);
        Log.d("SelfAdaptive", "checkStorage");
        checkSystemStorage();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getBaseContext(), LowStorageService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DURATION, pendingIntent);

        return START_STICKY;
    }

    void checkSystemStorage(){
        Log.d("SelfAdaptive", "checkSystemStorage");
        if(checkSDCard()==false || checkSystem()==false){
            Log.d("SelfAdaptive", "UninstallAll2");
            List<String> installed = new ArrayList<String>();
            installed.addAll(manager.getInstalledModules());
            manager.deferredUninstall(installed);
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
