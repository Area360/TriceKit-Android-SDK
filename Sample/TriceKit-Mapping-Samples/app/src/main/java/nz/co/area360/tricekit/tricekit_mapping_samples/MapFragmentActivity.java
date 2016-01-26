package nz.co.area360.tricekit.tricekit_mapping_samples;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import nz.co.tricekit.building.TriceKitBuilding;
import nz.co.tricekit.building.TriceKitFloor;
import nz.co.tricekit.location.TriceIconFactory;
import nz.co.tricekit.location.TriceIndoorMap;
import nz.co.tricekit.location.TriceKitMapActivity;
import nz.co.tricekit.location.TriceKitMapFragment;
import nz.co.tricekit.location.TriceKitMarker;
import nz.co.tricekit.location.TriceKitMarkerOptions;

/**
 * MapActivity.
 * Use TriceKitMapFragment which is the naked component.
 */
public class MapFragmentActivity extends AppCompatActivity {

    private static final String MAP_FRAGMENT_TAG = "tricekit_map";
    private TriceKitMapFragment mMapFragment;
    private TriceIndoorMap mMap;
    private TriceKitFloor mCurrentFloor;
    private TriceKitBuilding mBuilding;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_frament);

        if (savedInstanceState == null) {
            mMapFragment = TriceKitMapFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.map_host_container, mMapFragment, MAP_FRAGMENT_TAG);
            ft.commit();
        } else {
            mMapFragment = (TriceKitMapFragment) getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        }


        mBuilding = getIntent().getExtras().getParcelable(TriceKitMapActivity.BUILDING);

        mMapFragment.setMapListener(new TriceKitMapFragment.MapListener() {
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

        options.setIcon(TriceIconFactory.fromResource(R.mipmap.marker));
        options.setWidth(60);
        options.setHeight(60);
        options.setType(TriceKitMarkerOptions.eMarkerType.POI);
        options.setPosition(-(mCurrentFloor.getWidth() / 2) + x, (mCurrentFloor.getHeight() / 2) - y);

        TriceKitMarker triceMarker = mMap.addMarker(mCurrentFloor.getLevel(), options);
    }

    private TriceIndoorMap.OnMarkerClickListener mMarkerLister = new TriceIndoorMap.OnMarkerClickListener() {
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
