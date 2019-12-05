package com.amaze.filemanager.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amaze.filemanager.activities.MainActivity;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.squareup.haha.perflib.Main;

public class LowStorageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent intent1 = new Intent("com.amaze.filemanager.debug.LOW_STORAGE");
        context.sendBroadcast(intent1);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
