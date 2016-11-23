package com.sample.tapdaq.mediationsample;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMRewardAdListener;

import java.util.Locale;

/**
 * Created by dominicroberts on 10/11/2016.
 */

public class AdListener implements TMRewardAdListener {
    @Override
    public void onLimitReached() {
        Log.i("MEDIATION-SAMPLE", "onLimitReached");
    }

    @Override
    public void onRejected() {
        Log.i("MEDIATION-SAMPLE", "onRejected");
    }

    @Override
    public void onFailed(TMAdError tmAdError) {
        Log.i("MEDIATION-SAMPLE", "onFailed " + tmAdError.getErrorMessage());
    }

    @Override
    public void onUserDeclined() {
        Log.i("MEDIATION-SAMPLE", "onUserDeclined");
    }

    @Override
    public void onVerified(String s, String s1, Double aDouble) {
        Log.i("MEDIATION-SAMPLE", String.format(Locale.ENGLISH, "onVerified %s %s %.2f", s, s1, aDouble));
    }

    @Override
    public void onComplete() {
        Log.i("MEDIATION-SAMPLE", "onComplete");
    }

    @Override
    public void onEngagement() {
        Log.i("MEDIATION-SAMPLE", "onEngagement");
    }

    @Override
    public void onAdClosed() {
        Log.i("MEDIATION-SAMPLE", "onAdClosed");
    }

    @Override
    public void onAdFailedToLoad(TMAdError tmAdError) {
        Log.i("MEDIATION-SAMPLE", "onAdFailedToLoad " + tmAdError.getErrorMessage());
    }

    @Override
    public void onAdClick() {
        Log.i("MEDIATION-SAMPLE", "onAdClick");
    }

    @Override
    public void onAdOpened() {
        Log.i("MEDIATION-SAMPLE", "onAdOpened");
    }

    @Override
    public void onAdLoaded() {
        Log.i("MEDIATION-SAMPLE", "onAdLoaded");
    }
}
