package de.blho.lazyass

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

import de.blho.lazyass.NotificationCreator.Companion.createNotification

const val NOTIFICATION_ID = "notification-id"
const val NOTIFICATION = "notification"

class Notifier : BroadcastReceiver() {
    private var notificationId = 0
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            createNotificationChannel(context)
        //Shitty: alarmmanager cannot handle Intents as extra
        // -->https://commonsware.com/blog/2016/07/22/be-careful-where-you-use-custom-parcelables.html
        // and https://stackoverflow.com/questions/18000093/how-to-marshall-and-unmarshall-a-parcelable-to-a-byte-array-with-help-of-parcel/18000094#18000094
        //so we need to create the Notification here instead of putting it in the intent as extra
        notificationManager.notify(notificationId++, createNotification(context))
    }

    private fun createNotificationChannel(context: Context): NotificationManager {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            //it is ok to create the channel multiple times, the 2. time won't do anything
            //TODO still,there must be a better place to create this
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return notificationManager
    }


}