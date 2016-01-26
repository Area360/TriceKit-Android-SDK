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
            // Trigger when a user clicked on the point of interest's sliding panel
        }

        @Override
        public void pointOfInterestMapMarkerSelected(@NonNull TriceKitPointOfInterest pointOfInterest) {
            // Trigger when a user clicked on the point of interest
        }

        @Override
        public void mapTapped(int x, int y) {
            // Trigger when the map has been clicked
        }
    };
}
