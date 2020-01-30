# Android SDK

## Overview

### Introduction

Here you can find instructions on how to integrate and use FriendlyScore Connect for Android.

To get started quickly with FriendlyScore Connect for Android, clone the [GitHub repository](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example) and run the example. You need to [sign-up](https://friendlyscore.com/getting-started) for the free API keys through our Developer Console.

### Requirements

- Install or update Android Studio version 3.2 or greater
- Android 5.0 and greater
- [FriendlyScore API keys](https://friendlyscore.com/company/keys)


## Quickstart Demo App

Clone and run the demo project from our [GitHub repository](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example).

## Installation

Please follow the instructions below to install FriendlyScore Connect for Android, provide the necessary configuration and understand the flow.

**Add the following values to your project-level build.gradle file**

In your project-level Gradle file (you can find an example in the demo [build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/build.gradle)), add rules to include the Android Gradle plugin. The version should be equal to or greater than `3.2.1`.

```groovy
buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:3.2.1'
    }
}
```
Add  Jitpack to the list of repositories

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' } // Include to import FriendlyScore Connect dependencies
  }
}
```
**Add FriendlyScore Connect for Android configuration to your gradle.properties file**

Go to the [Redirects](https://friendlyscore.com/company/redirects) section of the FriendlyScore developer console and provide your `App Package Id` and `App Redirect Scheme`.

You will also need your [Client Id](https://friendlyscore.com/company/keys) for the specific environment (SANDBOX, DEVELOPMENT, PRODUCTION).

In the project-level properties file (you can find an example in the demo [gradle.properties](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/gradle.properties)), please add the following configuration values:

```bash
# Client Id value is specified in the keys section of the developer console.
# Use the Client Id for the correct ENVIRONMENT.

CLIENT_ID=client_id

# App Redirect Scheme value is in the Redirects section of the developer console.
# You must specify the value the SDK will use for android:scheme to redirect back to your app. https://developer.android.com/training/app-links/deep-linking

APP_REDIRECT_SCHEME=app_redirect_scheme
```

**Add FriendlyScore Connect for Android to your app-level build.gradle file**

In your module or app-level Gradle file (you can find an example in the demo, [app/build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/build.gradle)), please add the FriendlyScore Connect for Android listed below to your list of dependencies:

```groovy
dependencies {
   api 'com.github.friendlyscore.fs-android-sdk:friendlyscore-connect:latest.release'
}
```

Now we must read the configuration to create the string resources that will be used by the FriendlyScore Connect for Android.

```groovy
android {
  compileOptions {
  sourceCompatibility 1.8
  targetCompatibility 1.8
  }

  defaultConfig {
    resValue "string", "fs_client_id", (project.findProperty("CLIENT_ID") ?: "NO_CLIENT_ID")
    resValue "string", "fs_app_redirect_scheme", (project.findProperty("APP_REDIRECT_SCHEME") ?: "NO_APP_REDIRECT_SCHEME_PROVIDED")
  }
}
```

## Initialize SDK

The code described below is from the file in the demo [FriendlyScoreConnect.java](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/src/main/java/com/demo/friendlyscore/connect/FriendlyScoreConnect.java).


You can select the environment you want to use:

| Environment  |   Description   |
| :----       | :--             |
| Environments.SANDBOX     | Use this environment to test your integration with Unlimited API Calls |
| Environments.DEVELOPMENT | Use this your environment to test your integration with live but limited Production API Calls |
| Environments.PRODUCTION  | Production API environment |

```java
public class FriendlyScoreConnect extends AppCompatActivity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friendlyscore_connect_layout);
    //Call this function on the click of a button.
    startFriendlyScore();
}
/**
  In order to initialise FriendlyScore for your user, you must have their `userReference`. The `userReference` is an alphanumeric string that uniquely identifies the user in your systems.
  `userReference` can then be used to access information from the FriendlyScore API (https://friendlyscore.com/developers/api-reference).
*/
public String userReference = "your_user_reference";

/**
  In order to check that the user has returned from the FriendlyScoreView in your `onActivityResult`, you must provide the `requestcode` that you will be using
*/
public final int REQUEST_CODE_FRIENDLY_SCORE = 11;
public void startFriendlyScore() {
    FriendlyScoreView.Companion.startFriendlyScoreView(this, getString(R.string.fs_client_id), userReference, REQUEST_CODE_FRIENDLY_SCORE, Environments.SANDBOX);
}
```
## Handle Response

The code described below is from the file in the demo [FriendlyScoreConnect.java](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/src/main/java/com/demo/friendlyscore/connect/FriendlyScoreConnect.java).

  The `onActivityResult` is called when the FriendlyScore Connect for Android is closed. The `data` object returned `onActivityResult` can contain both `errors` & `states`.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if(requestCode == REQUEST_CODE_FRIENDLY_SCORE){
        //Present if there was error in creating an access token for the supplied userReference.
        if(data!=null && data.hasExtra("userReferenceAuthError")){
            //Do Something
        }
        //Present if there was service denied.
        if(data!=null && data.hasExtra("serviceDenied")){
            if(data.hasExtra("serviceDeniedMessage")){
                String errorDescription = data.getStringExtra("serviceDeniedMessage");
            }
        }
        //Present if the configuration on the server is incomplete.
        if(data!=null && data.hasExtra("incompleteConfiguration")){
            if(data.hasExtra("incompleteConfigurationMessage")){
                String errorDescription = data.getStringExtra("incompleteConfigurationMessage");
            }
        }
        //Present if there was error in obtaining configuration from server
        if(data!=null && data.hasExtra("serverError")){
            //Try again later
        }
        // //Present if the user closed the flow
        if(data!=null && data.hasExtra("userClosedView")){
            //The user closed the process
        }
    }
}
```

### Error Definition
| Error                     | Definitions  |
| -------------             | -------------|
| userReferenceAuthError   | Present if there was an authentication error for the supplied `userReference`.
| serviceDenied             | Present if service was denied. Please check the description for more information.
| incompleteConfiguration   | Present if the configuration on the server is incomplete. Please check the description for more information.
| serverError               | Present if there was a critical error on the server.

### Response State Definition
| State                    | Definitions  |
| -------------             | -------------|
| userClosedView            | Present if the user closed the FriendlyScore flow.

## Next Steps

### Access to Production Environment

You can continue to integrate FriendlyScore Connect in your app in our sandbox and development environments. Once you have completed testing, you can request access to the production environment in the developer console or speak directly to your account manager.

### Support

Find commonly asked questions and answers in our [F.A.Q](https://friendlyscore.com/developers/faq). You can also contact us via email at [developers@friendlyscore.com](mailto:developers@friendlyscore.com) or speak directly with us on LiveChat.

You can find all the code for FriendlyScore Connect for Web component, iOS and Android on our [GitHub](https://github.com/FriendlyScore).
