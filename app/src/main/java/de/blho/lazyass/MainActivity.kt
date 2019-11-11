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
import kotlinx.android.synthetic.main.activity_main.view.*


const val CHANNEL_ID="10001"

class MainActivity : AppCompatActivity() {
    //var notificationId=0

    private val br: BroadcastReceiver = UnlockBroadcastReceiver()
    var registerd=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View){
        createNotificationChannel()
        registerUnblockReceiver()
    }

    private fun unRegister(){
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
