package com.demo.friendlyscore.connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.friendlyscore.base.Environments;
import com.friendlyscore.ui.obp.FriendlyScoreView;


public class FriendlyScoreConnect extends AppCompatActivity {

    public String userIdentifier = "your_user_identifier";
    AppCompatButton startScoring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obp_start);
        startScoring = findViewById(R.id.start_friendly_score);
        startScoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFriendlyScore();
            }
        });
    }

    public final int REQUEST_CODE_FRIENDLY_SCORE = 11;
    public void startFriendlyScore() {
        FriendlyScoreView.Companion.startFriendlyScoreView(this, getString(R.string.fs_client_id), userIdentifier, REQUEST_CODE_FRIENDLY_SCORE, Environments.PRODUCTION);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_FRIENDLY_SCORE){
            if(data!=null){

                //Present if there was error in creating an access token for the supplied userIdentifier.
                if(data.hasExtra("userIdentifierAuthError")){
                    //Do Something
                }

                //Present if there was service denied.
                if( data.hasExtra("serviceDenied")){
                    if(data.hasExtra("serviceDeniedMessage")){
                        String serviceDeniedMessage = data.getStringExtra("serviceDeniedMessage");
                    }
                }

                //Present if there was error in obtaining configuration from server
                if(data.hasExtra("serverError")){
                    //Try again later
                }

                //Present if the user completed the entire flow for Forecast
                if( data.hasExtra("userCompletedFlow")){
                    //Success
                }

                //Present if the user closed the flow
                if(data.hasExtra("userClosedView")){
                    //The user closed the process
                }
            }

        }
    }
}
