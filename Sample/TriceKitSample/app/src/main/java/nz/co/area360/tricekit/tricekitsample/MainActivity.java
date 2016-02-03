package nz.co.area360.tricekit.tricekitsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.co.tricekit.zone.TriceKitManager;

public class MainActivity extends AppCompatActivity {
    TriceKitManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            /**
             * Create a new instance of TriceKitManager
             */
            mManager = new TriceKitManager(this);

            /**
             * Start TriceKitManager. This will fetch remote and monitor all fetched zones.
             */
            mManager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mManager != null)
            mManager.stop();
    }
}
