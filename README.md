  # Overview
  <p align="center">
    <img src="device_2019_2_13_150355_framed.png" width="200" height="400" />
  </p>
  
  ## Products

  ### Products overview

 You need to [login](https://friendlyscore.com/login) to Customer panel and choose the right product configuration (Connect, Insights, Forecast) to have access to the right User Journey and API endpoints. Please see products and details below.

| product | Feature | Description |
| :------ | :------ | :---------- |
| Connect | Accounts | Obtain basic account information such as account type, currency and status | 
|         | Transactions | Classify transactional data into identifiable segments, such as income, bills and lifestyle. | 
|         | Balance | Obtain financial data from personal current accounts such as balance and overdraft limits | 
| Insights| Spending Patterns | Comprehensive analytics of spending behaviour over time within transactional data | 
|         | Employment | Infer employment details through salary and bonus details, payment histories and payer information | 
|         | Income | Classify a wide range of income streams, from regular salary to irregular deposits | 
|         | Assets | Identify assets within transactional data, from big ticket material items such as cars to investments such as stocks and shares | 
|         | Liabilities | Identify financial obligations, such as rent, debt repayments and maintenance | 
| Forecast| Credit Score | Customised modern credit score based on financial behaviour | 
|         | Customer Segmentation | Intelligent segmentation tools help better inform customer management, marketing and conduct risk strategies| 

  ## Introduction

  This guide provides the instructions to integrate FriendlyScore Android SDK [**Connect**](https://friendlyscore.com/login).

  You will want to [Sign-up](https://friendlyscore.com/login) and obtain your `client_id` to get started.

  ## Requirements

  - Install or update Android Studio to version 3.2 or greater
  - FriendlyScore `client_id`
  - We support Android 5.0 and above

  ## QuickStart
  The easiest way to get started is to clone the repository https://github.com/FriendlyScore/Open-Banking-Connect. Please follow the instructions below to provide the necessary configuration and to understand the flow.

  # **Getting Set up**

  #### Add the following values to your Project Level build.gradle file
  In your project-level Gradle file (build.gradle), add rules to include the Android Gradle plugin. The version should be equal to or greater than `3.2.1`

    buildscript {
        ...
        dependencies {
            classpath 'com.android.tools.build:gradle:3.2.1'
        }
    }  
  ### **FriendlyScore Configuration**
   
   #### **Add FriendlyScore Android Framework configuration to your app**
   In the project-level gradle.properties file please add the following configuration values

    #This value must be specified
    FS_CLIENT_ID=client_id
    #You must specify the value the SDK will use for android:scheme to redirect back to your app. https://developer.android.com/training/app-links/deep-linking
    FS_OBP_SCHEME=Please Provide this value.


  #### **Add the following values to your App Level build.gradle file(In the demo app/build.gradle)**
  Now we must read the configuration to create the string resources that will be used by the FriendlyScore Android SDK.

    android {
      ...
      compileOptions {
      sourceCompatibility 1.8
      targetCompatibility 1.8
      }
 
      defaultConfig {
        //Must Provide these Values
        resValue "string", "fs_client_id", (project.findProperty("FS_CLIENT_ID") ?: "NO_CLIENT_ID")
        resValue "string", "fs_app_redirect_scheme", (project.findProperty("FS_OBP_SCHEME") ?: "NO_APP_REDIRECT_SCHEME_PROVIDED")
      }
    }

  #### **Add FriendlyScore Android Framework to your app**
  In your module or app-level gradle file(In the demo `app/build.gradle`) please add the FriendlyScore Android SDK library listed below to your list of dependencies

    dependencies {
       ...
       implementation 'com.github.friendlyscore.fs-android-sdk:friendlyscore-connect:0.7'
    }

  # **Integrating with FriendlyScore**
  
  #### **Start FriendlyScore**
  In order to start FriendlyScore for your user you must have the `userIdentifier` for that user. The `userIdentifier` uniquely identifies the user in your systems. This `userIdentifier` can then be used to access information from the FriendlyScore [api](https://developers.friendlyscore.com). 

    public String userIdentifier = "your_user_identifier";

  In order to listen when the user returns from the FriendlyScoreView in your `onActivityResult`, you must provide the `requestcode` that you will be using

    public final int REQUEST_CODE_FRIENDLY_SCORE = 11;

  You can select which environment you want to use the FriendlyScore SDK

  | Environment  |   Description   |
| :----       | :--             |
| sandbox     | Use this environment to test your integration with Unlimited API Calls |
| development | Use this your environment to test your integration with live but limited Production API Calls |
| production  | Production API environment |

These environments are listed in the SDK as below

    Environments.SANDBOX
    Environments.DEVELOPMENT
    Environments.PRODUCTION

  Define the environment variable

    public Environments environment = Environments.SANDBOX;

  Initiate FriendlyScoreView using the above values

    public void startFriendlyScore() {
                FriendlyScoreView.Companion.startFriendlyScoreView(this, getString(R.string.fs_client_id), userIdentifier, REQUEST_CODE_FRIENDLY_SCORE, environment);
    }

## **Handle Response from FriendlyScore**
  #### If you need to know when the user returns from the FriendlyScore Flow add this to your Activity or Fragment
  The `onActivityResult` is called when the SDK is closed. The `data` object returned `onActivityResult` contains various states that are available depending on the product configuration on the server. The `data` object can contain both `errors` & `events`


        
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
| Error                    | Definitions  | 
| -------------             | -------------|
| userIdentifierAuthError   | Present if there was an authentication error for the supplied `userIdentifier`. 
| serviceDenied             | Present if service was denied. Please check the description for more information.
| incompleteConfiguration             | Present if the configuration on the server is incomplete. Please check the description for more information.
| serverError               | Present if there was a critical error on the server.      

## Response State Definition
| State                    | Definitions  | 
| -------------             | -------------|
| userClosedView            | Present if the user Closed the FriendlyScore Flow.      