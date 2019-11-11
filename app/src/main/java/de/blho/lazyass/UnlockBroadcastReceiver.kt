package de.blho.lazyass

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.KeyguardManager
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import de.blho.lazyass.NotificationCreator.Companion.createNotification


private const val NOTIFICATION_REQUEST_CODE = 0//read up: is this really supposed to be the same number for each notification? How else would i cancel pending alerts?

/**
 *
 */
class UnlockBroadcastReceiver : BroadcastReceiver() {
    companion object {
        var screenTimeToNotification: Int =60000
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_USER_PRESENT) {
          if (!isPhoneLocked(context)) {
              scheduleNotification(context)
          }
        } else if (intent.action == Intent.ACTION_SCREEN_OFF) {
            cancelScheduledAlarms(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun cancelScheduledAlarms(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)!!
        alarmManager.cancel(createPendingIntent(context,Intent(context, Notifier::class.java)) )
    }

    private fun isPhoneLocked(context: Context): Boolean {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            keyguardManager.isDeviceLocked
        } else {
            keyguardManager.isKeyguardLocked
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(context: Context) {
        val notificationIntent = Intent(context, Notifier::class.java)
        val pendingIntent = createPendingIntent(context, notificationIntent)

        val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)!!
        val futureInMillis = SystemClock.elapsedRealtime() + screenTimeToNotification
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis,pendingIntent)
    }

    private fun createPendingIntent(
        context: Context,
        notificationIntent: Intent
    ): PendingIntent? {
        return PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }



}
