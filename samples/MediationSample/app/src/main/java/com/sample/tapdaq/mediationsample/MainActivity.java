package com.sample.tapdaq.mediationsample;

import android.os.Bundle;
import android.view.View;

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.ads.*;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.*;


/*
*
* Mediation Sample for Tapdaq Integration
*
* This app demonstrates how to initialise and show banners, static/video/rewarded interstitials
* using Tapdaq Mediation service, you should have set up an account with Tapdaq and retreived
* your AppID and Client Key in order to use this app.
*
 */

public class MainActivity extends TMLifecycleActivity {

    TMBannerAdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TLog.setLoggingLevel(TLogLevel.DEBUG);

        //Get Show Buttons from layout
        findViewById(R.id.sample_show_debug_btn).setOnClickListener(new ClickShowDebug());
        findViewById(R.id.sample_show_banner_btn).setOnClickListener(new ClickShowBanner());
        findViewById(R.id.sample_show_interstitial_btn).setOnClickListener(new ClickShowInterstitial());
        findViewById(R.id.sample_show_video_btn).setOnClickListener(new ClickShowVideo());
        findViewById(R.id.sample_show_rewarded_btn).setOnClickListener(new ClickShowRewardedVideo());

        //Get Banner UI from layout
        mBannerAd = (TMBannerAdView)findViewById(R.id.sample_banner_ad);

        //Register Adapters
        Tapdaq.getInstance().registerAdapter(this, new TMAdMobAdapter(this)); //Ad Mob
        Tapdaq.getInstance().registerAdapter(this, new TMFacebookAdapter(this)); //Facebook Audience Network

        //Set Placements & Ad Types
        List<TapdaqPlacement> enabledPlacements = new ArrayList<>();
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE), TapdaqPlacement.TDPTagDefault));
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.BANNER, CreativeType.VIDEO_INTERSTITIAL, CreativeType.REWARDED_VIDEO_INTERSTITIAL), TapdaqPlacement.TDPTagDefault));

        //Configuration
        TapdaqConfig config = new TapdaqConfig(this);
        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        //Initialise Cardview app
        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", new AdAvailabilityListener(), config);
    }

    private class ClickShowDebug implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /* Start Test Activity
            * Params Context (Current Activity), AdSize, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().startTestActivity(MainActivity.this);
        }
    }

    private class ClickShowBanner implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new ad
            * Params Context (Current Activity), AdSize, AdListener (optional, can be set to null)
            */
            mBannerAd.load(MainActivity.this, TMBannerAdSizes.STANDARD, new AdListener());
        }
    }

    private class ClickShowInterstitial implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Static interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().showInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickShowVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Video interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().showVideoInterstital(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickShowRewardedVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Rewarded Video interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().showRewardedVideoInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }
}
