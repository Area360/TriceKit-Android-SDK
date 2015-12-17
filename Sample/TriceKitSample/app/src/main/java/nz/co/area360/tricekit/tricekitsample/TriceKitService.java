package nz.co.area360.tricekit.tricekitsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;

import nz.co.tricekit.zone.TriceKitManager;

/**
 * Created by Marc Giovannoni on 2/03/15.
 */
public class TriceKitService extends Service {

    private static final String TAG = TriceKitService.class.getSimpleName();

    public TriceKitManager mManager;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        try {
            mManager = new TriceKitManager(this);

            /**
             * Support for offline mode
             */
            mManager.offlineMode(true);

            /**
             * Will preserve the zones state so when the app get killed and the service restarts,
             * TriceKitManager will restore the state of each zone the user were.
             */
            mManager.preserveZones(true);
            mManager.start();
        } catch (Exception e) {
            Log.e(TAG, Arrays.toString(e.getStackTrace()));
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        Log.d(TAG, "onTaskRemoved");

        if (mManager != null)
            mManager.stop();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        if (mManager != null)
            mManager.stop();
    }
}
