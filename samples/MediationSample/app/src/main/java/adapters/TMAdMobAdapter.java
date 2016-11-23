package adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.google.android.gms.ads.*;

import com.tapdaq.sdk.common.*;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.adnetworks.TMMediationNetworks;
import com.tapdaq.sdk.listeners.*;
import com.tapdaq.sdk.model.TMAdSize;
import com.tapdaq.sdk.storage.Storage;
import com.tapdaq.sdk.model.launch.TMNetworkCredentials;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by dominicroberts on 01/09/2016.
 */
public class TMAdMobAdapter implements TMAdapter {

    private final static String ERROR_CODE_INTERNAL_ERROR = "Something happened internally; for instance, an invalid response was received from the ad server.";//CODE 0
    private final static String ERROR_CODE_INVALID_REQUEST = "The ad request was invalid; for instance, the ad unit ID was incorrect.";                         //CODE 1
    private final static String ERROR_CODE_NETWORK_ERROR = "The ad request was unsuccessful due to network connectivity.";                                      //CODE 2
    private final static String ERROR_CODE_NO_FILL = "The ad request was successful, but no ad was returned due to lack of ad inventory.";                      //CODE 3
    private final static String ERROR_UNKNOWN = "UNKNOWN ERROR";

    private Activity mCurrentActivity;
    private AdapterListener mListener;

    private TMAdMobBannerSizes mBannerSizes = new TMAdMobBannerSizes();

    private List<InterstitialAd> mInterstitialAd = new ArrayList<>();
    private List<InterstitialAd> mVideoInterstitialAd = new ArrayList<>();

    private TMNetworkCredentials mKeys;

    public TMAdMobAdapter(Context context){
        super();
        clear(context);
    }

    private void clear(Context context) {
        Storage storage = new Storage(context);
        storage.remove("ADMOB_TEST_DEVICES");
        storage.remove("ADMOB_BANNER_ID");
        storage.remove("ADMOB_STATIC_ID");
        storage.remove("ADMOB_VIDEO_ID");
    }

    @Override
    public void initialise(Activity activity, TMNetworkCredentials keys) {
        if (activity != null)
            mCurrentActivity = activity;

        if (keys != null) {
            mKeys = keys;
        }

        if (mCurrentActivity != null && mKeys != null) {
            MobileAds.initialize(mCurrentActivity);
            mListener.onInitSuccess(mCurrentActivity, TMMediationNetworks.AD_MOB);

            Storage storage = new Storage(mCurrentActivity);
            storage.putString("ADMOB_BANNER_ID", mKeys.getBanner_id());
            storage.putString("ADMOB_STATIC_ID", mKeys.getInterstitial_id());
            storage.putString("ADMOB_VIDEO_ID", mKeys.getVideo_id());
        }
    }

    public TMAdMobAdapter setTestDevices(Context context, List<String> devices) {
        Storage storage = new Storage(context);
        String devicesStr = TextUtils.join(", ", devices);
        storage.putString("ADMOB_TEST_DEVICES",devicesStr);
        return this;
    }

    @Override
    public boolean isInitialised(Context context) {
        return mCurrentActivity != null && (getBannerId(context) != null || getStaticId(context) != null || getVideoId(context) != null) ;
    }

    @Override
    public boolean hasFailedRecently(Context context, int ad_type) {
        String failedKey = String.format(Locale.ENGLISH, "Failed_%s_%d", TMMediationNetworks.AD_MOB_NAME, ad_type);
        Storage storage = new Storage(context);
        if(storage.contains(failedKey)) {
            long failedTimeStamp = storage.getLong(failedKey);
            if (new Date().getTime() - failedTimeStamp < 60000)
                return true;
        }
        return false;
    }

    private String getBannerId(Context context) {
        if (mKeys != null && mKeys.getBanner_id() != null)
            return mKeys.getBanner_id();
        else
            return new Storage(context).getString("ADMOB_BANNER_ID");
    }

    private String getStaticId(Context context) {
        if (mKeys != null && mKeys.getInterstitial_id() != null)
            return mKeys.getInterstitial_id();
        else
            return new Storage(context).getString("ADMOB_STATIC_ID");
    }

    private String getVideoId(Context context) {
        if (mKeys != null && mKeys.getVideo_id() != null)
            return mKeys.getVideo_id();
        else
            return new Storage(context).getString("ADMOB_VIDEO_ID");
    }

    @Override
    public String getName() {
        return TMMediationNetworks.AD_MOB_NAME + "_Adapter";
    }

    @Override
    public int getID() {
        return TMMediationNetworks.AD_MOB;
    }

    @Override
    public void setAdapterListener(AdapterListener listener) {
        mListener = listener;
    }

    @Override
    public boolean isBannerAvailable(TMAdSize size) {
        return getBannerId(mCurrentActivity) != null && mBannerSizes.getSize(size) != null;
    }

    @Override
    public boolean canDisplayInterstitial(Context context) {
        return getStaticId(context) != null; //Has keys
    }

    @Override
    public boolean canDisplayVideo(Context context) {
        return getVideoId(context) != null; //Has keys
    }

    @Override
    public boolean canDisplayRewardedVideo(Context context) {
        return false;
    }

    @Override
    public ViewGroup loadAd(Context context, TMAdSize size, TMAdListener listener) {
        com.google.android.gms.ads.AdSize adSize = mBannerSizes.getSize(size);
        if(adSize != null) {
            AdView view = new AdView(context);
            view.setAdUnitId(getBannerId(context));
            view.setAdSize(adSize);
            view.setAdListener(new AdMobAdListener(listener));
            AdRequest.Builder builder = new AdRequest.Builder();

            Storage storage = new Storage(context);
            if (storage.contains("ADMOB_TEST_DEVICES")) {
                String[] devices = TextUtils.split(storage.getString("ADMOB_TEST_DEVICES"), ", ");
                for (String d : devices) {
                    builder.addTestDevice(d);
                }
            }

            view.loadAd(builder.build());
            return view;
        }
        return null;
    }

    @Override
    public void loadInterstitial(Activity activity, TMAdListener listener) {
        if (activity != null && getStaticId(activity) != null) {
            InterstitialAd ad = new InterstitialAd(activity);
            ad.setAdUnitId(getStaticId(activity));
            ad.setAdListener(new AdMobInterstitialAdListener(ad, listener, TMAdType.STATIC_INTERSTITIAL));

            AdRequest.Builder builder = new AdRequest.Builder();

            Storage storage = new Storage(activity);
            if (storage.contains("ADMOB_TEST_DEVICES")) {
                String[] devices = TextUtils.split(storage.getString("ADMOB_TEST_DEVICES"), ", ");

                for (String d : devices) {
                    builder.addTestDevice(d);
                }
            }

            ad.loadAd(builder.build());
            mInterstitialAd.add(ad);
        }
    }

    @Override
    public void loadVideo(Activity activity, TMAdListener listener) {
        if (activity != null && getVideoId(activity) != null) {
            InterstitialAd ad = new InterstitialAd(activity);
            ad.setAdUnitId(getVideoId(activity));
            ad.setAdListener(new AdMobInterstitialAdListener(ad, listener, TMAdType.VIDEO_INTERSTITIAL));
            mVideoInterstitialAd.add(ad);

            AdRequest.Builder builder = new AdRequest.Builder();

            Storage storage = new Storage(activity);
            if (storage.contains("ADMOB_TEST_DEVICES")) {
                String[] devices = TextUtils.split(storage.getString("ADMOB_TEST_DEVICES"), ", ");
                for (String d : devices) {
                    builder.addTestDevice(d);
                }
            }

            ad.loadAd(builder.build());
        }
    }

    @Override
    public void loadRewardedVideo(Activity activity, TMAdListener listener) {
        //Not available
    }

    @Override
    public void showInterstitial(Activity activity, TMAdListener listener) {
        InterstitialAd ad = (mInterstitialAd.isEmpty() ? null : mInterstitialAd.get(0));
        if (ad != null && ad.isLoaded()) {
            if (listener != null)
                ad.setAdListener(new AdMobInterstitialAdListener(ad, listener, TMAdType.STATIC_INTERSTITIAL));
            ad.show();
        } else
            loadInterstitial(activity, listener);
    }

    @Override
    public void showVideo(Activity activity, TMAdListener listener) {
        InterstitialAd ad = (mVideoInterstitialAd.isEmpty() ? null : mVideoInterstitialAd.get(0));
        if (ad != null && ad.isLoaded()) {
            if (listener != null)
                ad.setAdListener(new AdMobInterstitialAdListener(ad, listener, TMAdType.VIDEO_INTERSTITIAL));
            ad.show();
        } else
            loadVideo(activity, listener);
    }

    @Override
    public void showRewardedVideo(Activity activity, TMRewardAdListener listener) {
    }

    @Override
    public void destroy() {

    }

    private TMAdError buildError(int code) {
        switch (code) {
            case 0:
                return new TMAdError(code, ERROR_CODE_INTERNAL_ERROR);
            case 1:
                return new TMAdError(code, ERROR_CODE_INVALID_REQUEST);
            case 2:
                return new TMAdError(code, ERROR_CODE_NETWORK_ERROR);
            case 3:
                return new TMAdError(code, ERROR_CODE_NO_FILL);
            default:
                return new TMAdError(code, ERROR_UNKNOWN);
        }
    }

    private class AdMobAdListener extends AdListener
    {
        private final TMAdListener mAdListener;

        AdMobAdListener(TMAdListener listener) {
            mAdListener = listener;
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            TLog.debug("onAdClosed");

            if (mAdListener != null)
                mAdListener.onAdClosed();
        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
            TLog.debug("onAdFailedToLoad");

            if (mAdListener != null)
                mAdListener.onAdFailedToLoad(buildError(i));
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            TLog.debug("onAdLeftApplication");

            if (mAdListener != null)
                mAdListener.onAdClick();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            TLog.debug("onAdOpened");

            if (mAdListener != null)
                mAdListener.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            TLog.debug("onAdLoaded");

            if (mAdListener != null)
                mAdListener.onAdLoaded();
        }
    }

    private class AdMobInterstitialAdListener extends AdListener
    {
        private TMAdListener mAdListener;
        private InterstitialAd mAd;
        private int mType;

        AdMobInterstitialAdListener(InterstitialAd ad, TMAdListener listener, int type) {
            mAd = ad;
            mAdListener = listener;
            mType = type;
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            TLog.debug("onAdClosed");

            if (mAdListener != null)
                mAdListener.onAdClosed();

            if (!mInterstitialAd.isEmpty() && mAd == mInterstitialAd.get(0))
                mInterstitialAd.remove(mAd);
            else if(!mVideoInterstitialAd.isEmpty() && mAd == mVideoInterstitialAd.get(0))
                mVideoInterstitialAd.remove(mAd);

            mAd = null;
            mAdListener = null;
        }

        @Override
        public void onAdFailedToLoad(int i) {
            super.onAdFailedToLoad(i);
            TLog.debug("onAdFailedToLoad");

            if (mAdListener != null)
                mAdListener.onAdFailedToLoad(buildError(i));

            if (!mInterstitialAd.isEmpty() && mAd == mInterstitialAd.get(0))
                mInterstitialAd.remove(mAd);
            else if(!mVideoInterstitialAd.isEmpty() && mAd == mVideoInterstitialAd.get(0))
                mVideoInterstitialAd.remove(mAd);

            if (mListener != null)
                mListener.onAdFailedToLoad(mCurrentActivity, TMMediationNetworks.AD_MOB, mType, null, buildError(i));
        }

        @Override
        public void onAdLeftApplication() {
            super.onAdLeftApplication();
            TLog.debug("onAdLeftApplication");

            if (mAdListener != null)
                mAdListener.onAdClick();
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            TLog.debug("onAdOpened");

            if (mAdListener != null)
                mAdListener.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            TLog.debug("onAdLoaded");

            if (mAdListener != null)
                mAdListener.onAdLoaded();
            if (mListener != null)
                mListener.onAdLoad(TMMediationNetworks.AD_MOB, TMAdType.STATIC_INTERSTITIAL, null);
        }
    }

    private class TMAdMobBannerSizes extends TMBannerAdSizes {
        com.google.android.gms.ads.AdSize getSize(TMAdSize size) {
            if (size == STANDARD)
                return com.google.android.gms.ads.AdSize.BANNER;
            else if(size == LARGE)
                return com.google.android.gms.ads.AdSize.LARGE_BANNER;
            else if(size == MEDIUM_RECT)
                return com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE;
            else if(size == FULL)
                return com.google.android.gms.ads.AdSize.FULL_BANNER;
            else if(size == LEADERBOARD)
                return com.google.android.gms.ads.AdSize.LEADERBOARD;
            else if(size == SMART)
                return com.google.android.gms.ads.AdSize.SMART_BANNER;
            TLog.error(String.format(Locale.getDefault(), "No Ad Mob Banner Available for size: %s", size.name));
            return null;
        }
    }

    //Lifecycle Events
    @Override
    public void onCreate(Activity activity) {

    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {
        mCurrentActivity = activity;
    }

    @Override
    public void onPaused(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {
        if (activity == mCurrentActivity)
            mCurrentActivity = null;
    }
}
