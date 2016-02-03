package nz.co.area360.tricekit.tricekitsample;

import android.app.Application;

import nz.co.tricekit.TriceKit;

/**
 * Created by Marc on 6/10/2015.
 */
public class MainApp extends Application {
    public void onCreate() {
        super.onCreate();

        TriceKit.init(this);
    }
}
