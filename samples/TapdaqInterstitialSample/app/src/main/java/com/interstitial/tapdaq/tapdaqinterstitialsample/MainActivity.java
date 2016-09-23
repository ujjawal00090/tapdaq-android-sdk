package com.interstitial.tapdaq.tapdaqinterstitialsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;

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

        //Tapdaq Config (Optional)
        TapdaqConfig config = new TapdaqConfig(this);
        config.withTestAdvertsEnabled(false);
        config.withCreativeTypesSupport(types);
        config.withFrequencyCapping(0,0);
        config.withMaxNumberOfCachedAdverts(2);

        //Initialise Tapdaq SDK
        Tapdaq.tapdaq().initialize(this, "<APP_ID>", "<CLIENT_KEY>", new TCallbacks(this), config); //Callbacks && Config are optional
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
