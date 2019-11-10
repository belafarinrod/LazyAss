package de.blho.lazyass

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.view.*


const val CHANNEL_ID="10001";
const val default_notification_channel_id = "default"

class MainActivity : AppCompatActivity() {
    var notificationId=0

    val br: BroadcastReceiver = UnlockBroadcastReceiver()
    var registerd=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View){
        createNotificationChannel()
       // shownotification()
        registerUnblockReceiver()
    }

    fun unRegister(){
        if(registerd) {
            unregisterReceiver(br)
            registerd=false
        }
    }
    fun unRegister(view: View){
        unRegister()
    }

    private fun registerUnblockReceiver() {
        unRegister()
        val editText:String = findViewById<EditText>(R.id.editText).editText.text.toString()
        val timeInMillis =  editText.toInt()*1000
        UnlockBroadcastReceiver.screenTimeToNotification=timeInMillis
        registerd=true
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF).apply {
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(br, filter)
    }

    private fun shownotification() {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.lazy)
            .setContentTitle("My notification")
            .setContentText("Notification from button")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Longeeeeeeeeeer Notification from button  ............")
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId++, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
