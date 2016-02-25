package nz.co.area360.tricekit.maps.samples;

import android.app.Application;

import nz.co.tricekit.maps.TriceKitMaps;

/**
 * Application class which initialize TriceKit
 */
public class MainApp extends Application {

    public void onCreate() {
        super.onCreate();

        TriceKitMaps.Config config = TriceKitMaps.Config.newInstance();

        TriceKitMaps.init(this, config);
    }
}
