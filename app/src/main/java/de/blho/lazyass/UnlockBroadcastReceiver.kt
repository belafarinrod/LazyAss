package de.blho.lazyass

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.KeyguardManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.R.attr.delay
import android.app.Notification
import android.os.SystemClock
import androidx.annotation.RequiresApi
import android.app.PendingIntent




class UnlockBroadcastReceiver : BroadcastReceiver() {
    companion object {
        var screenTimeToNotification: Int =60000
    }

    private  var notifficactionId=0
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_USER_PRESENT) {
            val keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val isLocked = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                keyguardManager.isDeviceLocked
            } else {
                keyguardManager.isKeyguardLocked
            }
            if (!isLocked) {
                shownotification(context)
            }
        } else if (intent.action == Intent.ACTION_SCREEN_OFF) {
            print("test")
        }
    }

    private var notificationId = 0
    @RequiresApi(Build.VERSION_CODES.M)
    private fun shownotification(context: Context) {
        val alarmManager: AlarmManager = context.getSystemService(AlarmManager::class.java)
        val notificationIntent = Intent(context, Notifier::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.lazy)
            .setContentTitle("My notification")
            .setContentText("Notification from broadcastreciever")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer Notification from broadcastreciever...")
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)


        var notification: Notification = builder.build()

      //  with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define

        //    notify(notificationId++, notification)
        //}
        notificationIntent.putExtra(NOTIFICATION , notification) ;
        notificationIntent.putExtra(NOTIFICATION_ID , notifficactionId) ;
        notificationIntent.putExtra("n-byte",ParcableUtil.ParcelableUtil.marshall(notification))




        val futureInMillis = SystemClock.elapsedRealtime() + screenTimeToNotification//1 sekunde in zukunft
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis,pendingIntent)
    }


}
