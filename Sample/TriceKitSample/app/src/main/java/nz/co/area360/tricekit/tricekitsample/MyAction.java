package nz.co.area360.tricekit.tricekitsample;

import android.util.Log;

import nz.co.tricekit.zone.TriceAction;

/**
 * Created by Marc on 9/25/2015.
 */
public class MyAction extends TriceAction {

    private static final String TAG = MyAction.class.getSimpleName();

    @Override
    protected void onActionTriggered() {
        Log.d(TAG, "Action triggered");
    }
}
