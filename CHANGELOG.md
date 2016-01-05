# Tapdaq Android SDK Changelog

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
