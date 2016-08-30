package com.interstitial.tapdaq.tapdaqinterstitialsample;

import android.content.Context;
import android.util.Log;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqCallbacks;

/**
 * Created by dominicroberts on 30/08/2016.
 *
 * TCallbacks extends TapdaqCallbacks
 * Class provides updates on Tapdaq ad services
 * This implementation detects when an ad is available
 * and displays it immediately, but only once.
 * Use this type of implemenation for a bootup interstitial
 *
 * Note: There are more methods to override
 */

public class TCallbacks extends TapdaqCallbacks{
    private final Context mContext;
    private boolean hasDisplayedInterstitial = false;

    public TCallbacks(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void hasPortraitInterstitialAvailable() {
        super.hasPortraitInterstitialAvailable();

        Log.d("TAPDAQ", "hasAdsAvailable");
        //Display Interstitial as soon as its available. But only once
        if (!hasDisplayedInterstitial) {
            Log.d("TAPDAQ", "Display Bootup Interstitial");
            Tapdaq.tapdaq().displayInterstitial(mContext);
        }
    }

    @Override
    public void didDisplayInterstitial() {
        super.didDisplayInterstitial();
        hasDisplayedInterstitial = true;
    }

    @Override
    public void didFailToDisplayInterstitial() {
        super.didFailToDisplayInterstitial();
        Log.d("TAPDAQ", "didFailToDisplayInterstitial");
    }
}
