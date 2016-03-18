// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo;

import android.app.Application;

import nz.co.tricekit.maps.TriceKitMaps;
import nz.co.tricekit.zta.TriceKitZTA;

/**
 * Created by Marc on 3/16/2016.
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TriceKitZTA.init(this);
        TriceKitMaps.init(this);
    }
}
