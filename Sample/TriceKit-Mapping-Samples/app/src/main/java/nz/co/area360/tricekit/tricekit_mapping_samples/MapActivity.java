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
            // User tapped the popup view at the bottom of the screen for the given point of interest.
        }

        @Override
        public void pointOfInterestMapMarkerSelected(@NonNull TriceKitPointOfInterest pointOfInterest) {
            // User tapped on the given point of interest map marker.
        }

        @Override
        public void mapTapped(int x, int y) {
            // User tapped on the map itself, at the given coordinate.
        }
    };
}
