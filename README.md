<p align="center"><img src="https://cloud.githubusercontent.com/assets/6678136/8150781/4a39f442-134b-11e5-8414-d2f4a583f5f7.png" alt="TriceKit logo" /></p>

This is the TriceKit Android SDK that delivers delightful experiences in conjunction with the [TriceKit management system](http://beta.tricekit.com).

## Table of Contents
 - [Installation](#installation)
 - [Initialization](#initialization)
 - [Zone, Trigger & Action](#zones-triggers-actions)
    - [TriceKitManager](#tricekitmanager)
    - [Example Tasks](#example-tasks)
    - [Beacon Zone](#creating-a-new-beacon-zone-trigger-and-action-programmatically)
    - [Geolocation Zone](#creating-a-new-geo-radius-zone-programmatically)
    - [Background Service](#background-service)
 - [Mapping](#mapping)
    - [Create your map activity](#1-create-your-map-activity)
    - [Correctly launching the map activity](#2-correctly-launching-the-map-activity)
    - [Configure the map](#3-configure-the-map)
    - [Callback delegate](#4-callback-delegate)
 - [Release build](#release-build)

## Installation

Copy tricekit-android-sdk-release.aar into your app lib folder.

In you AndroidManifest.xml

```
<application ... >

  ...

  <meta-data android:name="trice_api_key" android:value="YOUR_API_KEY" />
  <meta-data android:name="trice_username" android:value="YOUR_USER_NAME" />
  
</application>
```

In your app module build.gradle

```
android {
  ...
  
  repositories {
      flatDir {
          dirs 'libs'
      }
  }
}

dependencies {
    apt 'com.google.dagger:dagger-compiler:2.0.1'
    provided 'javax.annotation:javax.annotation-api:1.2'
    compile 'com.google.dagger:dagger:2.0.1'
    
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:21.0.3'
    compile 'com.google.android.gms:play-services:6.5.87'
    compile 'com.android.support:cardview-v7:22.1.0'
    compile 'com.android.support:support-v4:22.1.0'
    
    compile 'se.emilsjolander:stickylistheaders:2.7.0'
    compile 'com.google.code.gson:gson:2.4'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.5.0'
    compile 'com.squareup.okio:okio:1.5.0'
    compile 'com.github.bumptech.glide:okhttp-integration:1.3.1@aar'
    compile 'com.github.bumptech.glide:glide:3.6.1'

    compile (name:'tricekit-android-sdk-release', ext:'aar')
}

```

In your project build.gradle

```
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.5.1'
```

### Initialization

You need to initialize TriceKit, we recommend using the Application class.

```java
public class MainApp extends Application {

    public void onCreate() {
        super.onCreate();

        TriceKitConfig config = new TriceKitConfig();
        TriceKit.init(this, config);
    }
}
```

In order to use this custom Application class you will need to modify your AndroidManifest.xml

```
 <application
        android:name=".MainApp"
        ...
 </application>
```

## Zones, Triggers, Actions

### TriceKitManager

###### Starting Monitoring

```java
try {
    /**
     * Create a new instance of TriceKitManager
     */
    TriceKitManager mManager = new TriceKitManager(this);

    /**
     * Starting TriceKitManager. This will fetch remote and monitor all fetched zones.
     */
    mManager.start();
} catch (Exception e) {
    Log.e(TAG, e.getMessage());
}
```

###### Stopping Monitoring

```java
/**
 * Stopping TriceKitManager
 */
mManager.stop();
```

IMPORTANT: Please note that start and stop must be called in the same life cycle.

### Example Tasks

###### Adding a custom action to a zone & trigger created in CMS:

This is assuming you have set up both a zone and trigger in the CMS.

```java
 mManager.attachAction(new MyAction(), new TriceAction.TriceActionFilter() {
      @Override
      public boolean onTriceActionFiltering(TriceZone triceZone, TriceTrigger triceTrigger) {
          /**
           * You will check here if the name or uid of the zone & trigger correspond to the ones you want to add your action on.
           */
            if (("MyZone").equals(triceZone.getName()) && ("MyTrigger").equals(triceTrigger.getName()))
                return true;
            return false;
      }
  });
```

### Creating a new beacon zone, trigger and action programmatically:

```java
TriceBeaconProximityZone zone = (TriceBeaconProximityZone) TriceFactory.createZone(TriceZone.eZoneType.BEACON_PROXIMITY);

zone.setRegion(new TriceBeaconProximityRegion("Beacon UUID Here", MAJOR, MINOR));
zone.setProximity(TriceBeaconProximityZone.Proximity.NEAR));
zone.setName("MyZone");

TriceTrigger trigger = TriceFactory.createTriceTrigger(TriceTrigger.eTriggerType.DWELL);

trigger.setName("My trigger");
trigger.setFrequency(5000);
trigger.setDwellTime(2000);
trigger.setLimit(1);
trigger.attachAction(new TriceNotificationAction(mContext, "My notification message", myIntent);

zone.attachTrigger(trigger);

mManager.addBeaconProximityZone(zone);
```

### Creating a new geo radius zone programmatically:

Creating triggers, actions and attaching are ommitted. See creating beacon zone for this sample.

```java
TriceGeoLocationZone zone = (TriceGeoLocationZone) TriceFactory.createZone(TriceZone.eZoneType.GEOLOCATION_RADIUS);

zone.setLatitude(LATITUDE);
zone.setLongitude(LONGITUDE);
zone.setRadius(15);
```

###Background Service

In order to run TriceKit in the background, you need to implement your own background service. An example is provided with the samples.

## Mapping

### Displaying a TriceKit map

To display a TriceKit map within your Android application, you will need to have a configured map within the TriceKit CMS.

### Overview

A TriceKit map can be displayed by launching an activity which extends the `TriceKitMapActivity` class, passing in the building data from the TriceKit CMS via an Intent extra.

### 1. Create your map activity

Create your own Android activity which extends the `TriceKitMapActivity` class. Don't forget to register it in your manifest. Note that you must implement the `getMapConfig()` method.

```java
public class MyMapActivity extends TriceKitMapActivity {
	@Override
	@NonNull
	protected TriceKitMapConfig getMapConfig() {
		return TriceKitMapConfig.create();
	}
}
```
### 2. Correctly launching the map activity

To successfully launch your map activity you need to start an Android intent and be sure to include a TriceKit building data extra in the intent.

Here is a basic example of starting the TriceKit map activity `MyMapActivity` after loading the required building data from TriceKit.

You have multiple way of retrieve the building you need, if you know the building UID you can directly fetch it.

```java
public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Start a request through the TriceKit Building Provider to fetch the data
		// for a specific building. Which loading method to call will depend on what
		// information you have about your TriceKit building. This example assumes
		// you know the building ID ahead of time.
		TriceKit.getTriceBuildingProvider().loadBuildingWithUid("Your building ID", new TriceKitBuildingProvider.BuildingRequestDelegate() {
			@Override
			public void onSuccess(@NonNull TriceKitBuilding triceKitBuilding) {
				startMyMapActivity(triceKitBuilding);
			}

			@Override
			public void onFailure() {
				// Networking error ...
			}
		});
	}
	
	private void startMyMapActivity(@NonNull TriceKitBuilding triceKitBuilding) {
		Intent intent = new Intent(this, MyMapActivity.class);
		intent.putExtra(TriceKitMapActivity.BUILDING, triceKitBuilding);
		startActivity(intent);
	}	
}
```

If you don't know the building UID then you will need to fetch all BuildingBrief (contains only description, name, icon and address - doestn't include points of interest or wayfinding data) and then you will be able to fetch to fetch the entire dataset for the building you want.

```java
mBuildingProvider.loadBuildingsBriefs(new TriceKitBuildingProvider.BuildingBriefsRequestDelegate() {
            @Override
            public void onSuccess(@NonNull List<TriceKitBuildingBrief> triceBuildingBriefs) {
             // Look for the building you want to use and then call loadBuildingFromBrief in order to fetch all data for that building.
            }

            @Override
            public void onFailure() {

            }
        });
 ...
 
 TriceKitBuildingBrief buildingBrief = buildingProvider.getAllBuildingBriefs().get(position);
 buildingProvider.loadBuildingFromBrief(buildingBrief, new TriceKitBuildingProvider.BuildingRequestDelegate() {
     @Override
     public void onSuccess(@NonNull TriceKitBuilding triceBuilding) {
         /**
          * Use MapActivity.class sample in order to have the TriceKitMapActivity bundle.
          */
         Intent intent = new Intent(mContext, MapActivity.class);
         
         /**
          * Provide the building to load to the Map Activity
          */
         intent.putExtra(TriceKitMapActivity.BUILDING, triceBuilding);

         startActivity(intent);
     }

     @Override
     public void onFailure() {
     }
 });
```

### 3. Configure the map

Note that the `getMapConfig()` method returns an instance of the `TriceKitMapConfig` class. This is your opportunity to configure how you want the map to behave and what features should be activated.

The `TriceKitMapConfig` class has a number of configuration settings which can be applied by chaining from the `TriceKitMapConfig.create()` method.

Setting | Default | Description
--------|-------------|--------
levelSelectionEnabled(boolean)|true|Whether to display the dropdown navigation list of levels for the building, allowing the user to navigate between levels.
pointOfInterestSearchEnabled(boolean)|true|Whether to display the text entry point of interest search field, allowing the user to enter text to find points of interest in the building.
pointOfInterestPopupEnabled(boolean)|true|Whether to display the default popup view at the bottom of the screen when a point of interest is selected on the map.
zoomPromptEnabled(boolean)|true|Whether to initially display the hint bubble to the user about how to zoom the map.
wayFindingEnabled(boolean)|false|Whether enable the wayfinding capability. If this setting is enabled, the user can tap a wayfinding button in the point of interest popup view to begin the wayfinding interaction. Note: You must have wayfinding data correctly configured in TriceKit to use this feature successfully.
mapDelegate(TriceKitMapDelegate)|none|Set the map delegate to an instance of a TriceKitMapDelegate to receive callback notifications related to user interaction in the map.
INACTIVE: geolocationEnabled(boolean)|false|This setting is not currently implemented. Do not enable.

Example scenario: We want to enable wayfinding and disable the zoom prompt. Our `getMapConfig()` method would look like this:

```java
@Override
@NonNull
protected TriceKitMapConfig getMapConfig() {
	return TriceKitMapConfig.create().wayFindingEnabled(true).zoomPromptEnabled(false);
}
```

### 4. Callback delegate

The TriceKitMapActivity can notify you when common user interactions take place such as the selection of a point of interest, or tapping somewhere on the map.

If you would like to receive these notifications, you need to set a callback delegate in the `TriceKitMapConfig` instance returned by the `getMapConfig()` method.

A common use case for setting a delegate is to be notified that the user has tapped the point of interest popup view - there is no default action take by TriceKit so it is up to you as a developer to decide what action to take.

```java
public class MyMapActivity extends TriceKitMapActivity {
	@Override
	@NonNull
	protected TriceKitMapConfig getMapConfig() {
		return TriceKitMapConfig.create().mapDelegate(mMapDelegate);
	}
	
	// Implementation of the TriceKitMapDelegate interface.
	private TriceKitMapDelegate mMapDelegate = new TriceKitMapDelegate() {
		@Override
		public void pointOfInterestPopupSelected(@NonNull TriceKitPointOfInterest pointOfInterest, @NonNull String storyUid) {
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
```

### Release Build

For release build you will need to include proguard rules.

```
#-----------------------------------------------------------------------------------------------------------
# Glide image loading library
#-----------------------------------------------------------------------------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
}-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule

#-----------------------------------------------------------------------------------------------------------
# OkIO (used for okhttp)
#-----------------------------------------------------------------------------------------------------------
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#-----------------------------------------------------------------------------------------------------------
# OkHttp
#-----------------------------------------------------------------------------------------------------------
-dontwarn com.squareup.okhttp.internal.huc.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
```
