package nz.co.area360.tricekit.tricekitsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import nz.co.tricekit.zone.TriceAction;
import nz.co.tricekit.zone.TriceKitManager;
import nz.co.tricekit.zone.TriceTrigger;
import nz.co.tricekit.zone.TriceZone;

public class MainActivity extends AppCompatActivity {

    TriceKitManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            /**
            * Create a new instance of TriceKitManager
            */
            mManager = new TriceKitManager(this);

            /**
             * Start TriceKitManager. This will fetch remote and monitor all fetched zones.
             */
            mManager.start();

            mManager.attachAction(new MyAction(), new TriceAction.TriceActionFilter() {
                @Override
                public boolean onTriceActionFiltering(TriceZone triceZone, TriceTrigger triceTrigger) {
                    /**
                     * You will check here if the name or uid of the zone & trigger correspond to the ones you want to add your action.
                     */
                    if (("MyZone").equals(triceZone.getName()) && ("MyTrigger").equals(triceTrigger.getName()))
                        return true;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.stop();
    }
}
