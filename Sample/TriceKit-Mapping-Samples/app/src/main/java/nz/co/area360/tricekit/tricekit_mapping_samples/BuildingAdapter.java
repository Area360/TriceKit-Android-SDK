package nz.co.area360.tricekit.tricekit_mapping_samples;

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

import nz.co.tricekit.TriceKit;
import nz.co.tricekit.building.TriceKitBuilding;
import nz.co.tricekit.building.TriceKitBuildingBrief;
import nz.co.tricekit.building.TriceKitBuildingProvider;
import nz.co.tricekit.internal.framework.widgets.TKCircularImageView;
import nz.co.tricekit.location.TriceKitMapActivity;

/**
 * BuildingAdapter
 *
 * Display all buildings.
 * If a building is selected, will download all floors, point of interest information and then start the Map Activity.
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private ArrayList<TriceKitBuildingBrief> mBuildings;
    private View.OnClickListener mOnClickListener;

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

    public BuildingAdapter(Context context, RecyclerView recyclerView, ArrayList<TriceKitBuildingBrief> items) {
        mBuildings = items;
        mContext = context;
        mRecyclerView = recyclerView;

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                TriceKitBuildingProvider buildingProvider = TriceKit.getTriceBuildingProvider();
                final int position = mRecyclerView.getChildLayoutPosition(view);
                final ProgressDialog progress = ProgressDialog.show(mContext, "", "Loading building...", true);

                TriceKitBuildingBrief buildingBrief = buildingProvider.getAllBuildingBriefs().get(position);
                buildingProvider.loadBuildingFromBrief(buildingBrief, new TriceKitBuildingProvider.BuildingRequestDelegate() {
                    @Override
                    public void onSuccess(@NonNull TriceKitBuilding triceBuilding) {
                        progress.dismiss();

                        /**
                         * Use MapActivity.class sample in order to have the TriceKitMapActivity bundle.
                         */
                        Intent intent = new Intent(mContext, MapActivity.class);

                        /**
                         * Provide the building to load to the Map Activity
                         */
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
