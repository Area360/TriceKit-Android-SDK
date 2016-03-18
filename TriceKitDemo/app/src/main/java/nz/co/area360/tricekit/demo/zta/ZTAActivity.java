// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo.zta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import nz.co.area360.tricekit.demo.R;
import nz.co.tricekit.zta.TriceKitManager;
import nz.co.tricekit.zta.system.Requirement;
import nz.co.tricekit.zta.system.SystemRequirementsHelper;

public class ZTAActivity extends AppCompatActivity {

    TriceKitManager mTriceKitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zta);

        mTriceKitManager = new TriceKitManager(this);

        if (SystemRequirementsHelper.checkRequirements(this).isEmpty()) {
            mTriceKitManager.start(false);
        } else {
            SystemRequirementsHelper.defaultCheckRequirements(this);
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
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SystemRequirementsHelper.TRICEKIT_REQUIREMENTS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "All requirements granted!", Toast.LENGTH_SHORT).show();

                    mTriceKitManager.start(false);
                }
                else {
                    List<Requirement> requirements = SystemRequirementsHelper.checkRequirements(this);

                    if (requirements.size() == 1 && requirements.contains(Requirement.BLUETOOTH_DISABLED)) {
                        mTriceKitManager.start(false);

                        Toast.makeText(this, "Bluetooth has been denied, location is granted, let's start!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(this, "Failed to meet requirements", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
