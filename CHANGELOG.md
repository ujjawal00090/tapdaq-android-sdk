# Tapdaq Android SDK Changelog

## 3.2.0 (2016-12-08)

- Removed AppCompat dependency. Increased minimum API Level to 11.
- Renamed TMAppCompatActivity to TMLifecycleActivity.

## 3.1.0 (2016-11-30)

- Bug fixes
-- Displays all networks in debug view (even if not used)
-- Chartboost occasionally display static interstitial rather than rewarded
-- Improved Chartboost error handling & logs
-- Other minor improvements / internal fixes

## 3.0.0 (2016-11-23)

- Mediation Services Added

## 2.5.5 (2016-09-23)

- Initialisation methods have been update, please review your code and the documentation
- Bug fixes/Stablity improvements

## 2.5.4 (2016-08-30)

- Fixed adqueue/callback error on bootup
- Prevented crash when clicking ad in emulator & devices without the Play Store installed

## 2.5.3 (2016-08-23)

- Fixed split string null exception

## 2.5.2 (2016-08-22)


## 2.5.1 (2016-08-10)

- Fixed test mode

## 2.5.0 (2016-08-09)

- Added placement tags
- Improved HTTP client
- Minor bug fixes

## 2.4.4 (2016-07-08)

- Fixed crash when app is backgrounded
- Fixed out of memory crash

## 2.4.3 (2016-05-28)

- Improved handling of HTTP status codes. 
- Fixed bug where cancelling Interstitial causes didCloseInterstitial to be called twice.

## 2.4.2 (2016-04-04)

- Improved error handling.
- Fixed bug where Android Version was incorrectly formatted. 

## 2.4.1 (2016-01-25)

- Fixed concurrency issue when ad is removed from the queue.

## 2.4.0 (2016-01-20)

- Deeplinking support
- Fixed bug where purged images from cache were not being handled correctly
- Fixed bug where ad removed from queue that is currently being displayed

## 2.3.1 (2016-01-12)

- Improved retrying mechanism on 5xx responses
- Improved cached image handling to prevent OutOfMemory runtime errors
- Icon image is now auto-fetched for native ads

## 2.3.0 (2016-01-05)

- Moved storage of cached data from shared preferences to SQLite
- Introduced frequency cap functionality on a promotion level
- Updated accessor modifiers on classes
- Updated .withCreativeTypesSupport() to take an empty parameter, which results in no adverts being fetched
- Fixed crash some users experienced when app goes into airplane mode

## 2.2.0 (2015-12-14)

- Improved persistence of preferences between sessions
- Fixed a number of issues with analytics data

## 2.1.0 (2015-12-02)

- Resolved issue where SDK was not activating correctly
- Improved formatting of bootup, impression and click data
- Temporarily removed deep link support
- Native ads: handle optional fields with null values
- Updated callbacks to include didFailToReachServer()
