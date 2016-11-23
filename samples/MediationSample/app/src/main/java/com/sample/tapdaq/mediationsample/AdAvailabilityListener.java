package com.sample.tapdaq.mediationsample;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdType;
import com.tapdaq.sdk.listeners.TMAdAvailabilityListener;

/**
 * Created by dominicroberts on 10/11/2016.
 */

public class AdAvailabilityListener implements TMAdAvailabilityListener {
    @Override
    public void onAvailable(int adType) {
        Log.i("MEDIATION-SAMPLE", "onAvailable " + TMAdType.getString(adType));
    }
}
