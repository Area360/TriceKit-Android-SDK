// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import nz.co.area360.tricekit.demo.R;
import nz.co.tricekit.maps.TriceKitMaps;
import nz.co.tricekit.maps.building.TriceKitBuildingBrief;
import nz.co.tricekit.maps.building.TriceKitBuildingProvider;

/**
 * Created by Marc Giovannoni on 24/03/15.
 */
public class BuildingActivity extends AppCompatActivity {
    public static final String EXTRA_MODE = "Mode";

    public static final int MODE_FEATURE_MAP = 0;
    public static final int MODE_BASIC_MAP = 1;

    private RecyclerView mRecycler;
    private BuildingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TriceKitBuildingBrief> mBuildings = new ArrayList<>();
    private ProgressBar mProgress;

    private int mMode;
    private TriceKitBuildingProvider mBuildingProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        mMode = getIntent().getIntExtra(EXTRA_MODE, MODE_FEATURE_MAP);

        mBuildingProvider = TriceKitMaps.getBuildingProvider();

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new BuildingAdapter(this, mRecycler, mBuildings, mMode);
        mRecycler.setAdapter(mAdapter);

        mBuildingProvider.loadBuildingsBriefs(new TriceKitBuildingProvider.BuildingBriefsRequestDelegate() {
            @Override
            public void onSuccess(@NonNull List<TriceKitBuildingBrief> triceBuildingBriefs) {
                mBuildings.addAll(triceBuildingBriefs);
                mProgress.setVisibility(View.GONE);
                BuildingActivity.this.runOnUiThread(new Runnable() {
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
}
