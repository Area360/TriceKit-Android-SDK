package nz.co.area360.tricekit.tricekit_mapping_samples;

import android.support.annotation.NonNull;
import android.widget.Toast;

import nz.co.tricekit.building.TriceKitPointOfInterest;
import nz.co.tricekit.location.TriceKitMapActivity;
import nz.co.tricekit.location.TriceKitMapConfig;
import nz.co.tricekit.location.TriceKitMapDelegate;

/**
 * MapActivity.
 * Use TriceKitMapActivity which is a bundle that automatically loads Floors, Point of interest, Way-Finding, contains a search function.
 */
public class MapActivity extends TriceKitMapActivity {

    private Toast mToast;

    @NonNull
    @Override
    protected TriceKitMapConfig getMapConfig() {

        /**
         * Initialize TriceKitMap configuration.
         * Enable Wayfinding
         * Provide TriceKitMapDelegate
         */
        return TriceKitMapConfig
                .create()
                .wayFindingEnabled(true)
                .mapDelegate(mMapDelegate);
    }

    private TriceKitMapDelegate mMapDelegate = new TriceKitMapDelegate() {
        @Override
        public void pointOfInterestPopupSelected(@NonNull TriceKitPointOfInterest pointOfInterest, @NonNull String uid) {
            showToast("Point of interest selected: " + pointOfInterest.getName());
        }

        @Override
        public void pointOfInterestMapMarkerSelected(@NonNull TriceKitPointOfInterest pointOfInterest) {
            showToast("Point of interest map marker selected: " + pointOfInterest.getName());
        }

        @Override
        public void mapTapped(int x, int y) {
            showToast("Map tapped: " + x + ", " + y);
        }
    };

    private void showToast(String message) {
        cancelToast();
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void cancelToast() {
        if(mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
