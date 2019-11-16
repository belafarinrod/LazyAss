package de.blho.lazyass

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat

const val  NOTIFICATION_TITLE = "Stop being lazy"
class NotificationCreator {
    companion object {
        fun createNotification(context: Context): Notification {

            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.lazy)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText("Time to do stuff")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("You sat on your lazy ass long enough..")
                )
                .setPriority(NotificationCompat.PRIORITY_MAX).build()

        }
    }
}