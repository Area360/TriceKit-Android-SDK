// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nz.co.area360.tricekit.demo.R;
import nz.co.tricekit.maps.TriceKitMapActivity;
import nz.co.tricekit.maps.TriceKitMaps;
import nz.co.tricekit.maps.building.TriceKitBuilding;
import nz.co.tricekit.maps.building.TriceKitBuildingBrief;
import nz.co.tricekit.maps.building.TriceKitBuildingProvider;
import nz.co.tricekit.maps.internal.framework.widgets.TKCircularImageView;

/**
 * Created by Marc Giovannoni on 4/12/14.
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private ArrayList<TriceKitBuildingBrief> mBuildings;
    private View.OnClickListener mOnClickListener;
    private int mMode;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TKCircularImageView mBuildingThumbnail;
        public TextView mName;
        public TextView mStreet;
        public TextView mCity;
        public TextView mCountry;

        public ViewHolder(View v) {
            super(v);
            mBuildingThumbnail = (TKCircularImageView) v.findViewById(R.id.building_thumbnail);
            mName = (TextView) v.findViewById(R.id.building_name);
            mStreet = (TextView) v.findViewById(R.id.building_street);
            mCity = (TextView) v.findViewById(R.id.building_city);
            mCountry = (TextView) v.findViewById(R.id.building_country);
        }
    }

    public BuildingAdapter(Context context, RecyclerView recyclerView, ArrayList<TriceKitBuildingBrief> items, int mode) {
        mMode = mode;
        mBuildings = items;
        mContext = context;
        mRecyclerView = recyclerView;
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                TriceKitBuildingProvider buildingProvider = TriceKitMaps.getBuildingProvider();

                buildingProvider.loadBuildingWithUid("Your building ID", new TriceKitBuildingProvider.BuildingRequestDelegate() {
                    @Override
                    public void onSuccess(@NonNull TriceKitBuilding triceKitBuilding) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });

                final int position = mRecyclerView.getChildLayoutPosition(view);
                final ProgressDialog progress = ProgressDialog.show(mContext, "", "Loading building...", true);

                TriceKitBuildingBrief buildingBrief = buildingProvider.getAllBuildingBriefs().get(position);
                buildingProvider.loadBuildingFromBrief(buildingBrief, new TriceKitBuildingProvider.BuildingRequestDelegate() {
                    @Override
                    public void onSuccess(@NonNull TriceKitBuilding triceBuilding) {
                        progress.dismiss();

                        Intent intent;

                        switch(mMode) {
                            case BuildingActivity.MODE_BASIC_MAP:
                                intent = new Intent(mContext, BasicMapActivity.class);
                                break;
                            default:
                                intent = new Intent(mContext, MapActivity.class);
                                break;
                        }

                        intent.putExtra(TriceKitMapActivity.BUILDING, triceBuilding);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_building, parent, false);

        ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(mOnClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        TriceKitBuildingBrief building = mBuildings.get(position);

        viewHolder.mCity.setText(building.getAddress().getCity());
        viewHolder.mStreet.setText(building.getAddress().getStreet());
        viewHolder.mCountry.setText(building.getAddress().getCountry());
        viewHolder.mName.setText(building.getName());

        Glide.with(mContext).load(building.getThumbnail()).into(viewHolder.mBuildingThumbnail);
    }

    @Override
    public int getItemCount() {
        return mBuildings.size();
    }
}
