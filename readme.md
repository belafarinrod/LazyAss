# Lazy Ass

## Purpose

App to show Notifications after X Seconds, after u unlocked your phone the last time.

     My personal usage: Don't forget the time when browsing your phone between your workout sets.

## Backlog
- Make a click on the Notification open the app
- Clean up the mess around the use of the `AlarmManager` (it seems to not work with Extras in Intents, which lead to copied/unused code)
- app shows seconds to the next notification
- app has countdown to next notification
- Write Unit/Integration tests
- Clean up the rest
- Change from input field to sth that only provides Integers
- add minutes/hours
- technical stuff
    - drive towards this architecture:https://developer.android.com/jetpack/docs/guide
    - remove data from activities and recievers
    - use a ViewModel and Databinding(and or expressions): 
    - use room to add a repository with live data that pushed changes to the ViewModel: 
      
## Architecture Overview/How to Conribute
- TODO

# Changelog
## v0.1
## v0.2
- fixes bug, that causes notifications to appear on locked screens when the phone is unlocked and locked before the next notification was scheuled
- notifications are scheduled by alammanager#setExact instead of set, so the notification cannot be rescheduled later by the OS
## v0.3
- add feature to remove existing notifications when unlocking the phone