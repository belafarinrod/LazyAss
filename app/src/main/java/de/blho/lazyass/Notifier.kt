package de.blho.lazyass

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat


public const val NOTIFICATION_ID = "notification-id"
public const val NOTIFICATION = "notification"

class Notifier : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Mies: der Alarmmanager kann mit extras nicht umgehen-->https://commonsware.com/blog/2016/07/22/be-careful-where-you-use-custom-parcelables.html und https://stackoverflow.com/questions/18000093/how-to-marshall-and-unmarshall-a-parcelable-to-a-byte-array-with-help-of-parcel/18000094#18000094

      //da die Umgehungslösung auch nicht geklappt hat:wohl oder übel erstmal nochmal die notification hier doppelt erstellen statt auslesen
      //  val notification =
      //      ParcableUtil.ParcelableUtil.unmarshall(intent.getByteArrayExtra("n-byte")) as Notification
        //val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
           notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        notificationManager.notify(id, buildNotification(context))
    }


    fun buildNotification(context: Context): Notification? {
        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.lazy)
            .setContentTitle("Stop beeing lazy")
            .setContentText("Time to do stuff")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("You sat on your lazy ass long enough..")
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)


       return builder.build()
    }
}