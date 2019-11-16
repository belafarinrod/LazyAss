package de.blho.lazyass


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*

import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class UITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)


    @Before
    @After
    fun clearNotifications() {
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        closeNotifications(uiDevice)
    }

    private fun closeNotifications(uiDevice: UiDevice) {
        Thread.sleep(150)
        //swipe up
        var isSuccessful = uiDevice.swipe(50, uiDevice.displayHeight, 50, 2, 15);
        Thread.sleep(150)
        assertTrue("automated closing of notifications failed", isSuccessful)
    }


    @Test
    fun timedNotificationsShouldAppear() {

        clearNotifications()
        val appCompatEditText = onView(withId(R.id.editText))
        appCompatEditText.perform(replaceText("2"))

        val appCompatButton = onView(withId(R.id.button))
        appCompatButton.perform(click())

        val uiDevice = UiDevice.getInstance(getInstrumentation())
        restartScreenWith1SecondBreak(uiDevice)
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.text(NOTIFICATION_TITLE)), 5000)
        var notificationTitleUIElement: UiObject2? =
            uiDevice.findObject(By.text(NOTIFICATION_TITLE))
        assertNotNull("no notification found", notificationTitleUIElement)
        clearNotifications()
    }


    @Test
    fun lockScreenShouldClearPendingNotification() {
        clearNotifications()


        val appCompatEditText = onView(withId(R.id.editText))
        appCompatEditText.perform(replaceText("4"))

        val appCompatButton = onView(withId(R.id.button))
        appCompatButton.perform(click())


        val uiDevice = UiDevice.getInstance(getInstrumentation())
        restartScreenWith1SecondBreak(uiDevice)
        //timer activated for 4 seconds from now


        Thread.sleep(2000)
        //we waited 2 secs, the notification would appear in 2 secs

        restartScreenWith1SecondBreak(uiDevice)
        //the restart(screen of) should have cleared the notification
        // 3 seconds have passed since the first notification was scheduled(1 left,if it was not deleted)



        uiDevice.openNotification()
        uiDevice.wait(
            Until.hasObject(By.text(NOTIFICATION_TITLE)),
            //3 seconds since the original timer was set passed
            // we wait 2 more seconds to be sure the timer was reset and to make sure the timer since the lates screen on won't notify us
            2000
        )
         var notificationTitleUIElement: UiObject2? =
            uiDevice.findObject(By.text(NOTIFICATION_TITLE))
         clearNotifications()
         assertNull("notification should be cleared", notificationTitleUIElement)

    }

    @Ignore("not implemented yet")
    @Test
    fun notificationsWhichAppearWhenScreenIsLockedShouldBeRemovedOnUnlock(){
        clearNotifications()


        val appCompatEditText = onView(withId(R.id.editText))
        appCompatEditText.perform(replaceText("4"))

        val appCompatButton = onView(withId(R.id.button))
        appCompatButton.perform(click())


        val uiDevice = UiDevice.getInstance(getInstrumentation())
        restartScreenWith1SecondBreak(uiDevice)

        uiDevice.sleep()
        Thread.sleep(4000)//notification wil be fired during locked screen

        uiDevice.wakeUp()
        Thread.sleep(100)//give the service a little time to remove the notification

        uiDevice.openNotification()
        uiDevice.wait(
            Until.hasObject(By.text(NOTIFICATION_TITLE)),
            //3 seconds since the original timer was set passed
            // we wait 2 more seconds to be sure the timer was reset and to make sure the timer since the lates screen on won't notify us
            2000
        )
        var notificationTitleUIElement: UiObject2? =
            uiDevice.findObject(By.text(NOTIFICATION_TITLE))
        clearNotifications()
        assertNull("notification should be cleared", notificationTitleUIElement)
    }
    private fun restartScreenWith1SecondBreak(uiDevice: UiDevice) {
        uiDevice.sleep()
        Thread.sleep(1000)
        uiDevice.wakeUp()
    }


}
