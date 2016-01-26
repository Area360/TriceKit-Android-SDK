package nz.co.area360.tricekit.tricekit_mapping_samples;

import android.app.Application;

import nz.co.tricekit.TriceKit;
import nz.co.tricekit.TriceKitConfig;

/**
 * Application class which initialize TriceKit
 */
public class MainApp extends Application {

    public void onCreate() {
        super.onCreate();

        TriceKitConfig config = new TriceKitConfig();
        TriceKit.init(this, config);
    }
}
