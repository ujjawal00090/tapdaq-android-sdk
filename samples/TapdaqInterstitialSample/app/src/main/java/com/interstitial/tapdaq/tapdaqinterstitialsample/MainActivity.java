package com.interstitial.tapdaq.tapdaqinterstitialsample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqCallbacks;

/*
* This is a basic implementation of the Tapdaq SDK
* It provides a portrait interstitial on bootup
* and on request via a UI button
* To use this, first you must add your
* AppID & Client Key to the initialisation code
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup UI Button Click Event
        ((Button) findViewById(R.id.main_displayad_btn)).setOnClickListener(new OnClickDisplayAd());

        //Create List of creative types to display
        CreativeType[] types = {CreativeType.INTERSTITIAL_PORTRAIT};

        //Initialise Tapdaq SDK
        Tapdaq.tapdaq().initializeWithConfiguration()
                .withTestAdvertsEnabled(false) //Optional
                .withCreativeTypesSupport(types) //Optional
                .withFrequencyCapping(0,0) //Optional
                .withMaxNumberOfCachedAdverts(2) //Optional
                .initialize("<AppID>", "<ClientKey>", this, new TCallbacks(this)); //Callbacks is optional
    }

    /*
    * Button Click Event handler
    * Will display an advert (if available)
     */
    private class OnClickDisplayAd implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            //Display interstitial
            Tapdaq.tapdaq().displayInterstitial(MainActivity.this);
        }
    }

}
