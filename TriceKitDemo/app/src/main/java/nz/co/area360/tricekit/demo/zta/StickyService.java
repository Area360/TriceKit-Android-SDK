// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.zta;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import nz.co.tricekit.zta.TriceKitManager;

/**
 * Created by Marc on 3/16/2016.
 */
public class StickyService extends Service {

    TriceKitManager mTriceManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Update TriceKit cache when you bind to the service.
        if (mTriceManager != null) {
            mTriceManager.updateCache();
        }
        // Implement you binder via Binder, Messenger or AIDL.
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // Stop TriceKitManager when nobody is bound to the service anymore.
        if (mTriceManager != null)
            mTriceManager.stop();
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        mTriceManager = new TriceKitManager(this);
        mTriceManager.start(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // onDestroy is not guaranteed to be called, but if so, stop TriceKitManager.
        if (mTriceManager != null) {
            mTriceManager.stop();
            mTriceManager = null;
        }
    }
}
