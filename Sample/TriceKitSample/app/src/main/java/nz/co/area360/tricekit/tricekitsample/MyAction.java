package nz.co.area360.tricekit.tricekitsample;

import android.util.Log;

import nz.co.tricekit.zone.TriceAction;

public class MyAction extends TriceAction {

    private static final String TAG = MyAction.class.getSimpleName();

    @Override
    protected void onActionTriggered() {
        Log.d(TAG, "Action triggered");
    }
}
