package com.demo.friendlyscore.connect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.friendlyscore.base.Environments;
import com.friendlyscore.ui.obp.FriendlyScoreView;


public class FriendlyScoreConnect extends AppCompatActivity {

    public String TAG = FriendlyScoreConnect.class.getSimpleName();
    AppCompatButton initiateFriendlyScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlyscore_connect_layout);
        initiateFriendlyScore = (AppCompatButton)findViewById(R.id.initiate_friendlyscore_connect);
        initiateFriendlyScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFriendlyScore();
            }
        });
    }
    /**
     In order to initialize FriendlyScore for your user you must have the `userReference` for that user.
     The `userReference` uniquely identifies the user in your systems.
     This `userReference` can then be used to access information from the FriendlyScore [api](https://friendlyscore.com/developers/api).
     */
    public String userReference = "your_user_reference";

    /**
     In order to listen when the user returns from the FriendlyScoreView in your `onActivityResult`, you must provide the `requestcode` that you will be using.
     */
    public final int REQUEST_CODE_FRIENDLY_SCORE = 11;

    public void startFriendlyScore() {
        FriendlyScoreView.Companion.startFriendlyScoreView(this, getString(R.string.fs_client_id), userReference, REQUEST_CODE_FRIENDLY_SCORE, Environments.SANDBOX);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_FRIENDLY_SCORE){
            if(data!=null){

                //Present if there was error in creating an access token for the supplied userReference.
                if(data.hasExtra("userReferenceAuthError")){
                    Log.e(TAG, "userReferenceAuthError");
                    //Do Something
                }

                //Present if there was service denied.
                if( data.hasExtra("serviceDenied")){
                    if(data.hasExtra("serviceDeniedMessage")){
                        String serviceDeniedMessage = data.getStringExtra("serviceDeniedMessage");
                        if(serviceDeniedMessage!=null)
                            Log.e(TAG, serviceDeniedMessage);

                    }
                }
                //Present if the configuration on the server is incomplete.
                if(data!=null && data.hasExtra("incompleteConfiguration")){
                    if(data.hasExtra("incompleteConfigurationMessage")){
                        String errorDescription = data.getStringExtra("incompleteConfigurationMessage");
                        if(errorDescription!=null)
                            Log.e(TAG, errorDescription);
                    }
                }
                //Present if there was error in obtaining configuration from server
                if(data.hasExtra("serverError")){
                    Log.e(TAG, "serverError");
                    //Try again later
                }
                //Present if the user closed the flow
                if(data.hasExtra("userClosedView")){
                    //The user closed the process
                    Log.e(TAG, "userClosedView");
                }
            }

        }
    }
}
