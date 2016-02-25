package nz.co.area360.tricekit.maps.samples;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import nz.co.tricekit.maps.TriceKitBasicMapFragment;
import nz.co.tricekit.maps.TriceKitIcon;
import nz.co.tricekit.maps.TriceKitMap;
import nz.co.tricekit.maps.TriceKitMapActivity;
import nz.co.tricekit.maps.TriceKitMarker;
import nz.co.tricekit.maps.TriceKitMarkerOptions;
import nz.co.tricekit.maps.building.TriceKitBuilding;
import nz.co.tricekit.maps.building.TriceKitFloor;

/**
 * MapActivity.
 * Use TriceKitBasicMapFragment which is the naked component.
 */
public class MapFragmentActivity extends AppCompatActivity {

    private static final String MAP_FRAGMENT_TAG = "tricekit_map";
    private TriceKitBasicMapFragment mMapFragment;
    private TriceKitMap mMap;
    private TriceKitFloor mCurrentFloor;
    private TriceKitBuilding mBuilding;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_frament);

        if (savedInstanceState == null) {
            mMapFragment = TriceKitBasicMapFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.map_host_container, mMapFragment, MAP_FRAGMENT_TAG);
            ft.commit();
        } else {
            mMapFragment = (TriceKitBasicMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        }


        mBuilding = getIntent().getExtras().getParcelable(TriceKitMapActivity.BUILDING);

        mMapFragment.setMapListener(new TriceKitBasicMapFragment.MapListener() {
            @Override
            public void onMapReady() {
                if (mMap == null) {
                    mMap = mMapFragment.getMap();

                    mCurrentFloor = mBuilding.getFloors().get(0);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);

                    /**
                     * Load the building into the map component
                     */
                    mMap.loadBuilding(mBuilding);

                    /**
                     * Set Current floor
                     */
                    mMap.setCurrentFloor(mCurrentFloor.getLevel());

                    /**
                     * Manually add a Point of Interest to the map
                     */
                    addMarker();

                    /**
                     * Set listener when a marker is clicked
                     */
                    mMap.setOnMarkerClickListener(mMarkerLister);
                }
            }
        });
    }

    /**
     * Manually add a marker to the TriceKitMapFragment.
     */
    private void addMarker() {
        int x = 1024;
        int y = 1024;
        TriceKitMarkerOptions options = new TriceKitMarkerOptions();

        options.setIcon(TriceKitIcon.fromResource(R.mipmap.marker));
        options.setWidth(60);
        options.setHeight(60);
        options.setType(TriceKitMarkerOptions.eMarkerType.POI);
        options.setPosition(-(mCurrentFloor.getWidth() / 2) + x, (mCurrentFloor.getHeight() / 2) - y);

        TriceKitMarker triceMarker = mMap.addMarker(mCurrentFloor.getLevel(), options);
    }

    private TriceKitMap.OnMarkerClickListener mMarkerLister = new TriceKitMap.OnMarkerClickListener() {
        @Override
        public void onMarkerClick(TriceKitMarker triceKitMarker) {
            showToast("Marker clicked");
        }
    };

    private void showToast(String message) {
        cancelToast();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
