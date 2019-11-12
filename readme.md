# Lazy Ass

## Purpose

App to show Notifications after X Seconds, after u unlocked your phone the last time.

     My personal usage: Don't forget the time when browsing your phone between your workout sets.

## Backlog
- Make a click on the Notification open the app
- Clean up the mess around the use of the `AlarmManager` (it seems to not work with Extras in Intents, which lead to copied/unused code)
- Scheduled Notifications get canceled when the phone gets locked/turnd off
- app shows seconds to the next alarm
- Write Unit/Integration/UI tests
  - UI Tests: 
    - set time, lock,unlock,expect notification
    - set time, lock,unock,lock, wait, expect no notification(tests the cancel operation of alarmmanager)
- Clean up the rest
- Change from input field to sth that only provides Integers
- add minutes/hours
- remove data from acticies and recievers, as https://developer.android.com/jetpack/docs/guide suggests


## Architecture Overview/How to Conribute
- TODO

# Changelog
## v0.1
## v0.2
- fixes bug, that causes notifications to appear on locked screens when the phone is unlocked and locked before the next notification was scheuled
- notifications are scheduled by alammanager#setExact instead of set, so the notification cannot be rescheduled later by the OS
