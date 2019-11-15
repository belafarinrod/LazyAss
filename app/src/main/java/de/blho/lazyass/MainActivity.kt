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
import android.widget.TextView
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import java.util.*


const val CHANNEL_ID="10001"

class MainActivity : AppCompatActivity() {
    private val br: BroadcastReceiver = UnlockBroadcastReceiver()
    var registerd=false
    private val nextAlarmTime=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nextNotificationTimeObserver = Observer<Long> { newNotificationTime->
            // Update the UI, in this case, a TextView.
           updateNextAlarmTime(newNotificationTime)
        }

        RepositoryFake.timeForNextAlarm.observe(this,nextNotificationTimeObserver)
    }

    override fun onStart() {
        super.onStart()
        updateNextAlarmTime(RepositoryFake.timeForNextAlarm.value?:-1)
    }


    fun onClick(view: View){
        createNotificationChannel()
        registerUnblockReceiver()
    }

    private fun updateNextAlarmTime(newNotificationTime: Long) {
        var nextAlarmInMillisString:String=findNextAlarm(newNotificationTime)
        findViewById<TextView>(R.id.textView).text=nextAlarmInMillisString


    }

    private fun findNextAlarm(newNotificationTime: Long):String {
        return if(newNotificationTime<0){
            "No Pending Notification"
        }else{
            val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
            SimpleDateFormat("HH:mm:ss", currentLocale).format(newNotificationTime).toString()
        }
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
        val editText:String = findViewById<EditText>(R.id.waitTimeInput).waitTimeInput.text.toString()
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
