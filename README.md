  ## Overview
  <p align="center">
    <img src="https://dpcnl9aeuvod8.cloudfront.net/docs_assets/device_2019_2_13_150355_framed.png" width="200" height="400" />
  </p>

  ## Introduction

  Here you find instructions on how to integrate and use FriendlyScore Connect for Android.

To get started quickly with FriendlyScore Connect for Android clone the [GitHub repository](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example) and run the example. You need to [Sign-up](https://friendlyscore.com/getting-started) for the free API keys through our Developer Console.

  ## Requirements

  - Install or update Android Studio to version 3.2 or greater
  - You will need your [FriendlyScore API keys](https://friendlyscore.com/company/keys)
  - We support Android 5.0 and above

  ## **Quickstart**

The easiest way to get started is to clone the repository [GitHub repository](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example).

  ## **Getting Set up**

Please follow the instructions below to install FriendlyScore Connect for Android, provide the necessary configuration and to understand the flow.

  #### Add the following values to your Project Level build.gradle file
  In your project-level Gradle file (In the demo [build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/build.gradle)), add rules to include the Android Gradle plugin. The version should be equal to or greater than `3.2.1`

    buildscript {
        dependencies {
            classpath 'com.android.tools.build:gradle:3.2.1'
        }
    }

  #### Add the following values to your Project Level build.gradle file
  In your project-level Gradle file (In the demo, [build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/build.gradle)), add the Jitpack maven repository

    allprojects {
      repositories {
        maven { url 'https://jitpack.io' } // Include to import FriendlyScore Connect dependencies
      }
	}

  #### **Add FriendlyScore Connect for Android to your app**
  In your module or app-level gradle file(In the demo, [app/build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/build.gradle)) please add the FriendlyScore Connect for Android listed below to your list of dependencies

    dependencies {
       api 'com.github.friendlyscore.fs-android-sdk:friendlyscore-connect:0.9'
    }

   #### **Add FriendlyScore Connect for Android configuration to your app**
   Go to the [Redirects](https://friendlyscore.com/company/redirects) section of the FriendlyScore developer console and provide your `App Package Id` and `App Redirect Scheme`.

   You will also need your [Client Id](https://friendlyscore.com/company/keys) for the specific environment (SANDBOX, DEVELOPMENT, PRODUCTION).

   In the project-level properties file (In the demo, [gradle.properties](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/gradle.properties))file please add the following configuration values.

    # Client Id value is specified in the keys section of the developer console.
    # Use the Client Id for the correct ENVIRONMENT.

    CLIENT_ID=client_id

    # App Redirect Scheme value is in the Redirects section of the developer console.
    #You must specify the value the SDK will use for android:scheme to redirect back to your app. https://developer.android.com/training/app-links/deep-linking

    APP_REDIRECT_SCHEME=app_redirect_scheme

  #### **Add the following values to your App Level build.gradle file(In the demo, [app/build.gradle](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/build.gradle))**
  Now we must read the configuration to create the string resources that will be used by the FriendlyScore Connect for Android.

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


  #### **Initialize FriendlyScore Connect**
  The code described below is from the file in the demo [FriendlyScoreConnect.java](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/src/main/java/com/demo/friendlyscore/connect/FriendlyScoreConnect.java)


  You can select which environment you want to use the FriendlyScore SDK

  | Environment  |   Description   |
| :----       | :--             |
| Environments.SANDBOX     | Use this environment to test your integration with Unlimited API Calls |
| Environments.DEVELOPMENT | Use this your environment to test your integration with live but limited Production API Calls |
| Environments.PRODUCTION  | Production API environment |

    public class FriendlyScoreConnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obp_start);
        startFriendlyScore();
    }
    /**
      In order to initialize FriendlyScore for your user you must have the `userIdentifier` for that user. The `userIdentifier` uniquely identifies the user in your systems.
      This `userIdentifier` can then be used to access information from the FriendlyScore [api](https://friendlyscore.com/developers/api).
    */
    public String userIdentifier = "your_user_identifier";

    /**
      In order to listen when the user returns from the FriendlyScoreView in your `onActivityResult`, you must provide the `requestcode` that you will be using
    */
    public final int REQUEST_CODE_FRIENDLY_SCORE = 11;
    public void startFriendlyScore() {
        FriendlyScoreView.Companion.startFriendlyScoreView(this, getString(R.string.fs_client_id), userIdentifier, REQUEST_CODE_FRIENDLY_SCORE, Environments.SANDBOX);
    }

## **Handle Response from FriendlyScore**
The code described below is from the file in the demo [FriendlyScoreConnect.java](https://github.com/FriendlyScore/FriendlyScore-Connect-Android-Example/blob/master/app/src/main/java/com/demo/friendlyscore/connect/FriendlyScoreConnect.java)

  The `onActivityResult` is called when the FriendlyScore Connect for Android is closed. The `data` object returned `onActivityResult` can contain both `errors` & `states`.



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if(requestCode == REQUEST_CODE_FRIENDLY_SCORE){
            //Present if there was error in creating an access token for the supplied userIdentifier.
            if(data!=null && data.hasExtra("userIdentifierAuthError")){
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

## Error Definition
| Error                     | Definitions  |
| -------------             | -------------|
| userIdentifierAuthError   | Present if there was an authentication error for the supplied `userIdentifier`.
| serviceDenied             | Present if service was denied. Please check the description for more information.
| incompleteConfiguration   | Present if the configuration on the server is incomplete. Please check the description for more information.
| serverError               | Present if there was a critical error on the server.

## Response State Definition
| State                    | Definitions  |
| -------------             | -------------|
| userClosedView            | Present if the user Closed the FriendlyScore Flow.

## Next Steps

## Access to Production Environment

You can continue to integrate FriendlyScore Connect in your app in our Sandbox and Development environments. Once you have completed testing, you can request access to the Production environment in the developer console or speak directly to your account manager.

## Support

Find common questions and answers in our [F.A.Q](https://friendlyscore.com/developers/faq). You can always send us email to [developers@friendlyscore.com](mailto:developers@friendlyscore.com) or speak with us on LiveChat.

You can find all the code for FriendlyScore Connect for Web component, iOS and Android on our [GitHub](https://github.com/FriendlyScore).
