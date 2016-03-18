// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.maps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nz.co.area360.tricekit.demo.R;
import nz.co.tricekit.maps.TriceKitBasicMapFragment;
import nz.co.tricekit.maps.TriceKitCameraUpdateFactory;
import nz.co.tricekit.maps.TriceKitIcon;
import nz.co.tricekit.maps.TriceKitMap;
import nz.co.tricekit.maps.TriceKitMapActivity;
import nz.co.tricekit.maps.TriceKitMarkerOptions;
import nz.co.tricekit.maps.building.TriceKitBuilding;

/**
 * Created by Marcel Braghetto on 18/02/16.
 */
public class BasicMapActivity extends AppCompatActivity {
    private TriceKitBasicMapFragment mFragment;
    private TriceKitMap mMap;
    private TriceKitBuilding mBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic_map_activity);

        mBuilding = getIntent().getParcelableExtra(TriceKitMapActivity.BUILDING);

        if (mBuilding == null) {
            throw new UnsupportedOperationException("You must pass a parcelled TriceKitBuilding instance for the TriceKitMapActivity.BUILDING extra through the intent bundle when launching a TriceKitMapActivity.");
        }
        if (mBuilding.getFloors().isEmpty()) {
            throw new UnsupportedOperationException("You must pass a parcelled TriceKitBuilding TriceKitMapActivity.BUILDING extra that has at least one floor through the intent bundle when launching a TriceKitMapActivity.");
        }

        if(savedInstanceState == null) {
            mFragment = TriceKitBasicMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_container, mFragment, "MapFragment").commit();
        } else {
            mFragment = (TriceKitBasicMapFragment) getSupportFragmentManager().findFragmentByTag("MapFragment");
        }

        mFragment.setMapListener(new TriceKitBasicMapFragment.MapListener() {
            @Override
            public void onMapReady() {
                if(mMap == null) {
                    initMap();
                }
            }
        });
    }

    private void initMap() {
        mMap = mFragment.getMap();
        mMap.loadBuilding(mBuilding);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setFloorSelectorEnabled(false);
        mMap.setCurrentFloor(mBuilding.getFloors().get(0).getLevel());

        mMap.setOnMapClickListener(new TriceKitMap.OnMapClickListener() {
            @Override
            public void onMapClick(int x, int y) {
                mMap.moveCamera(TriceKitCameraUpdateFactory.zoomTo(1f));
            }
        });

        TriceKitMarkerOptions options = new TriceKitMarkerOptions();
        TriceKitIcon icon = TriceKitIcon.fromResource(R.mipmap.ic_launcher);
        options.setIcon(icon);
        options.setWidth(50);
        options.setHeight(50);
        options.setType(TriceKitMarkerOptions.eMarkerType.POI);
        options.setPosition(0, 0);

        mMap.addMarker(mMap.getCurrentFloor().getLevel(), options);
    }
}
