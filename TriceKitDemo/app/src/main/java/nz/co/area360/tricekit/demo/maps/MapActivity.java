// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.maps;

import android.support.annotation.NonNull;

import nz.co.tricekit.maps.TriceKitMapActivity;
import nz.co.tricekit.maps.TriceKitMapConfig;

/**
 * Created by Marcel Braghetto on 17/02/16.
 */
public class MapActivity extends TriceKitMapActivity {
    @NonNull
    @Override
    protected TriceKitMapConfig getMapConfig() {
        return TriceKitMapConfig.create().wayFindingEnabled(true);
    }
}
