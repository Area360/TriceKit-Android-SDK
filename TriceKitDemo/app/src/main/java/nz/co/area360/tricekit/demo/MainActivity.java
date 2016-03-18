// Copyright © 2016 Area360.
// All rights reserved.
//
// This file is released under the New BSD License (see NewBSDLicense.txt).

package nz.co.area360.tricekit.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import nz.co.area360.tricekit.demo.maps.BuildingActivity;
import nz.co.area360.tricekit.demo.zta.ZTAActivity;
import nz.co.tricekit.zta.TriceKitManager;

public class MainActivity extends AppCompatActivity {

    TriceKitManager mTriceKitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ztaButton = (Button) findViewById(R.id.zta_button);
        ztaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ZTAActivity.class));
            }
        });

        Button mapButton = (Button) findViewById(R.id.tk_map_activity_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuildingActivity.class);
                intent.putExtra(BuildingActivity.EXTRA_MODE, BuildingActivity.MODE_FEATURE_MAP);
                startActivity(intent);
            }
        });

        Button basicMapButton = (Button) findViewById(R.id.tk_basic_map_fragment_button);
        basicMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BuildingActivity.class);
                intent.putExtra(BuildingActivity.EXTRA_MODE, BuildingActivity.MODE_BASIC_MAP);
                startActivity(intent);
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
