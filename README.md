<p align="center"><img src="https://cloud.githubusercontent.com/assets/6678136/8150781/4a39f442-134b-11e5-8414-d2f4a583f5f7.png" alt="TriceKit logo" /></p>

This is the TriceKit Android SDK that delivers delightful experiences in conjunction with the [TriceKit management system](http://beta.tricekit.com).

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

In your build.gradle

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
    compile 'com.android.support:appcompat-v7:21.0.3'
    compile 'com.google.android.gms:play-services:6.5.87'
    compile 'com.android.support:cardview-v7:22.1.0'
    compile 'com.android.support:support-v4:22.1.0'
    compile (name:'tricekit-android-sdk-release', ext:'aar')
}

```

## Zones, Triggers, Actions

### TriceKitManager

###### Starting Monitoring

```
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

###### Stoping Monitoring

```
/**
 * Stopping TriceKitManager
 */
mManager.stop();
```

IMPORTANT: Please note that start and stop must be called in the same life cycle.

### Example Tasks

###### Adding a custom action to a zone & trigger created in CMS:

This is assuming you have set up both a zone and trigger in the CMS.

```
 mManager.attachAction(new MyAction(), new TriceAction.TriceActionFilter() {
      @Override
      public boolean onTriceActionFiltering(TriceZone triceZone, TriceTrigger triceTrigger) {
          /**
           * You will check here if the name or id of the zone & trigger correspond to the ones you want to add your action.
           */
            if (("MyZone").equals(triceZone.getName()) && ("MyTrigger").equals(triceTrigger.getName()))
                return true;
            return false;
      }
  });
```
