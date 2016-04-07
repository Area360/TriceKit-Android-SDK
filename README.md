This is the TriceKit iOS SDK that delivers delightful experiences in conjunction
with the [TriceKit management system](http://tricekit.com).

# Table of Contents
- [Installation](#installation)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [ZTA Usage](#usage)
    - [System requirements](#requirements)
    - [Starting TriceKit](#starting)
    - [Adding actions to triggers](#add-action)
        - [Adding a local action](#local-action)
        - [Handling server actions](#server-action)
    - [Setting and fetching user data](#user-data)
- [Maps Usage](#maps-usage)
    - [initialization](#maps-initialization)
    - [Displaying a TriceKit map](#maps-display)
    - [Configuration](#maps-configuration)
- [Release Build](#release-build)

## <a name="installation"></a>Installation

Using TriceKit in your Android Studio project required both tricekit-zta-android-sdk.aar or/and tricekit-maps-android-sdk.aar
and tricekit-shared-android-sdk.aar. Copy these libraries in your libs folder.
You will then need to modify you app build.gradle:

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

    ...

    // Shared dependencies
    compile 'com.google.code.gson:gson:2.4'
    compile 'com.squareup.okhttp:okhttp:2.5.0'
    compile 'com.squareup.okhttp:okhttp-urlconnection:2.5.0'
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.dagger:dagger:2.0.1'
    compile (name:'tricekit-shared-android-sdk', ext:'aar')

    // TriceKit ZTA
    compile 'com.google.android.gms:play-services-location:8.4.0'
    compile (name:'tricekit-zta-android-sdk', ext:'aar')

    // TriceKit Maps
    compile 'com.github.bumptech.glide:okhttp-integration:1.3.1@aar'
    compile 'com.github.bumptech.glide:glide:3.6.1'
    compile 'se.emilsjolander:stickylistheaders:2.7.0'
    compile (name:'tricekit-maps-android-sdk', ext:'aar')
}

```

## <a name="configuration"></a>Configuration

In your `AndroidManifest.xml`

```
<application ... >

  ...

  <meta-data android:name="tricekit_api_key" android:value="YOUR_API_KEY" />
  <meta-data android:name="tricekit_username" android:value="YOUR_USER_NAME" />

</application>
```

# <a name="usage"></a>ZTA Usage

## <a name="requirements"></a>System requirements

TriceKit needs Location permissions to run on Marshmallow and above, but also needs Bluetooth to be enable for Beacon scan.
`SystemRequirementsHelper` provides tools to help you identify what features need to be enabled or turned on. (Location permission for Android Marshmallow and above, Location Services or Bluetooth)
`checkRequirements` will tells you what is not currently enabled. If you wish to personalize the experience,
you can then prompt the user using your own dialogs.
`defaultCheckRequirements` provides default Android dialog to ask user to turn on any requirements.

```java
if (SystemRequirementsHelper.checkRequirements(this).isEmpty()) {
    mTriceKitManager.start(false);
} else {
    SystemRequirementsHelper.defaultCheckRequirements(this);
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
```

## <a name="starting"></a>Starting TriceKit


You need first need to initialize TriceKit ZTA, we recommend using the Application class.

```java
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TriceKitZTA.init(this);
    }
}
```

In order to use this Application class you will need to modify your `AndroidManifest.xml`

```
 <application
        android:name=".MainApp"
        ...
 </application>
```

Now you are ready to start TriceKitManager:

```java
TriceKitManager triceManager = new TriceKitManager(this);
triceManager.start(boolean restoreTriceKitState);
```
Note that you
This is an asynchronous call, you will need to  monitor TriceKit state by using `TriceKitEventListener`:

```java
tricekitManager.registerTriceKitEventListener(new TriceKitManager.TriceKitEventListener() {
  @Override
  public void onEvent(@NonNull TriceKitEvent event) {
    switch (event) {
        case STATUS:
        // see TriceKitStatus for all possible status.
    }
  }
});
```

Note that usually all TriceKit call need to happen during `STARTING` or `STARTED` state.

To stop TriceKit you simply need to call `stop` method:

```java
triceManager.stop();
```

## <a name="add-action"></a>Adding actions to triggers

There are two ways of handling actions in TriceKit.

### <a name="local-action"></a>Adding a local action

This is best done by detecting when triggers are added in response to data
obtained from the server. To do this, you will need to register `OnRemoteUpdateListener`:

```java
tricekitManager.registerOnRemoteUpdateListener(new TriceKitManager.OnRemoteUpdateListener() {
    @Override
    public void onTriggerAdded(@NonNull TriceKitZone zone, @NonNull TriceKitTrigger trigger) {
        // add you action here.
        trigger.attachAction(new MyAction());
    }
});
```

In order to attach action to particular trigger, you can use the name or the UUID of the trigger.
Important: Note that if you are using v1 trigger id, you will need to convert is to v2 UUID by using our `V1Helper` utility class:

```java
tricekitManager.registerOnRemoteUpdateListener(new TriceKitManager.OnRemoteUpdateListener() {
    @Override
    public void onTriggerAdded(@NonNull TriceKitZone zone, @NonNull TriceKitTrigger trigger) {
        UUID v1Zone = V1Helper.toZoneUUID(1748);
        UUID v1Trigger = V1Helper.toTriggerUUID(1264);

        if (V1Helper.isV1Zone(zone.getUUID()) && V1Helper.isV1Trigger(trigger.getUUID())) {
            if (v1Zone.equals(zone.getUUID()) && v1Trigger.equals(trigger.getUUID()))
                trigger.attachAction(new MyAction());
        }
    }
});
```

### <a name="server-action"></a>Handling server actions

If you have set up a notification action on our TriceKit CMS, you will be able
to handle that server action by registering for notifications when triggers of a
particular type fire:

```java
tricekitManager.registerTriceKitEventListener(new TriceKitManager.TriceKitEventListener() {
    @Override
    public void onEvent(@NonNull TriceKitEvent status) {
        switch (status) {
            case STATUS:
                if (mTriceManager.getStatus() == TriceKitStatus.STARTING) {
                    mTriceManager.registerActionTypeCallback(TriceKitActionType.LOCAL_NOTIFICATION_TYPE, new TriceKitActionTypeCallback() {
                        @Override
                        public void onActionTypeTriggered(@NonNull String type, @NonNull JSONObject metadata) {
                         // Handle your notification action the way you want here.
                        }
                    });
                }
            default:
                break;
        }
    }
});
```

Example of the Notification Action metadata:

```json
{
    "message": "my notification"
}
```

You will soon be able to create your own action type on our CMS.

## <a name="user-data"></a>Setting and fetching user data

Details of the user (such as a frequent flyer ID) can be set by calling `setUserData` on the `STARTED` event:

```java
case STATUS:
  if (mTriceManager.getStatus() == TriceKitStatus.STARTED) {
      try {
          mTriceManager.setUserData(new JSONObject("{\"ff_id\": \"136HDF89\"}"));
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }
  break;
```

When this method is called, the user data is uploaded to the server.  The response
from the server (containing information uploaded via the user data REST API) is
made available by listening the `USERDATA` event:

```java
case USERDATA:
    JSONObject userData = mTriceManager.getUserData();
  break;
```

# <a name="maps-usage"></a>Maps Usage

## <a name="maps-initialization"></a>Initialization

```java
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TriceKitMaps.init(this);
    }
}
```

## <a name="maps-display"></a>Displaying a TriceKit map

To display a TriceKit map within your Android application, you will need to have a configured map within the TriceKit CMS.

### Overview

A TriceKit map can be displayed by launching an activity which extends the `TriceKitMapActivity` class, passing in the building data from the TriceKit CMS via an Intent extra.

### Create your map activity

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
### Correctly launching the map activity

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
        TriceKitMaps.getBuildingProvider().loadBuildingWithUid("Your building ID", new TriceKitBuildingProvider.BuildingRequestDelegate() {
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

## <a name="maps-configuration"></a>Configuration

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

### Callback delegate

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


## <a name="release-build"></a>Release Build

For release build you will need to include some proguard rules.

```
-keepattributes Signature
-keep class nz.co.tricekit.** { *; }

#----------------------------------------------------
# Google gms
#----------------------------------------------------
-keep class com.google.android.gms.**

#----------------------------------------------------
# OkIO (used for okhttp)
#----------------------------------------------------
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

#----------------------------------------------------
# OkHttp
#----------------------------------------------------
-dontwarn com.squareup.okhttp.internal.huc.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

#----------------------------------------------------
# Glide image loading library
#----------------------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
}
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
```
