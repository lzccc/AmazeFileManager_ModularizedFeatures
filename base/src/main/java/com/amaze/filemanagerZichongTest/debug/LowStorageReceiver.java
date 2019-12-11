package com.amaze.filemanagerZichongTest.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class LowStorageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent intent1 = new Intent("com.amaze.filemanagerZichongTest.debug.LOW_STORAGE");
        context.sendBroadcast(intent1);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
