package nz.co.area360.tricekit.maps.samples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import nz.co.tricekit.maps.TriceKitMaps;
import nz.co.tricekit.maps.building.TriceKitBuildingBrief;
import nz.co.tricekit.maps.building.TriceKitBuildingProvider;

/**
 * Sample Activity.
 * Use TriceKitBuildingProvider in order to fetch all buildings that belong the current API KEY.
 * Show all available building using RecyclerView.
 * See BuildingAdapter for implementation.
 *
 * TriceKit provides two different components:
 *  - TriceKitMapActivity which is a bundle that automatically loads Floors, Point of interest, Wayfinding, contains a search function.
 *  - TriceKitBasicMapFragment which is only the Map component, gives more control to the developer.
 *
 *  @see BuildingAdapter in order to use either TriceKitMapActivity or TriceKitBasicMapFragment.
 */
public class MainActivity extends AppCompatActivity {

    private BuildingAdapter mAdapter;
    private ArrayList<TriceKitBuildingBrief> mBuildings = new ArrayList<>();
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Setup Recycler view
         */
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BuildingAdapter(this, recyclerView, mBuildings);
        recyclerView.setAdapter(mAdapter);

        /**
         * Fetch buildings brief. Only contains Name, Description, Address, Icon.
         * The entire building data will be downloaded in the BuildingAdapter class.
         */
        TriceKitBuildingProvider buildingProvider = TriceKitMaps.getBuildingProvider();
        buildingProvider.loadBuildingsBriefs(new TriceKitBuildingProvider.BuildingBriefsRequestDelegate() {
            @Override
            public void onSuccess(@NonNull List<TriceKitBuildingBrief> triceBuildingBriefs) {
                mBuildings.addAll(triceBuildingBriefs);
                mProgress.setVisibility(View.GONE);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
