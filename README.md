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
    compile fileTree(dir: 'libs', include: ['*.jar'])

    /**
     * TriceKit dependencies
     */
    compile 'com.android.support:appcompat-v7:22.2.0'
    compile 'com.google.android.gms:play-services-location:8.4.0'
    compile 'com.android.support:support-v4:22.2.0'
    compile 'com.google.code.gson:gson:2.4'
    compile 'com.google.dagger:dagger:2.0.1'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.5.0'

    compile (name:'tricekit-android-sdk-release', ext:'aar') {
        transitive=true
    }
}

```

### Initialization

You need to initialize TriceKit, we recommend using the Application class.

```java
public class MainApp extends Application {

    public void onCreate() {
        super.onCreate();

        TriceKit.init(this);
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


### Release Build

For release build you will need to include some proguard rules.

```
#-----------------------------------------------------------------------------------------------------------
# OkHttp
#-----------------------------------------------------------------------------------------------------------
-dontwarn com.squareup.okhttp.internal.huc.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
```
