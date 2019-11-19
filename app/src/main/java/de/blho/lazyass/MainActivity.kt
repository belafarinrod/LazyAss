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
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.ConfigurationCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.Observer
import de.blho.lazyass.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.SimpleDateFormat
import androidx.databinding.adapters.TextViewBindingAdapter.setText




const val CHANNEL_ID = "10001"

class MainActivity : AppCompatActivity() {
    private val br: BroadcastReceiver = UnlockBroadcastReceiver()
    var registerd: ObservableBoolean = ObservableBoolean(false)
    private val nextAlarmTime = -1
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.registered = registerd

        val nextNotificationTimeObserver = Observer<Long> { newNotificationTime ->
            // Update the UI, in this case, a TextView.
            updateNextAlarmTime(newNotificationTime)
            startCountdownTimer(newNotificationTime)
        }
        RepositoryFake.timeForNextAlarm.observe(this, nextNotificationTimeObserver)
    }

    private fun startCountdownTimer(newNotificationTime: Long) {
        countDownTimer?.cancel()
        countDownTimer  = object : CountDownTimer(newNotificationTime- System.currentTimeMillis(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.countdownField).text = ( millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                findViewById<TextView>(R.id.countdownField).text="0"
            }
        }.start()


    }

    override fun onStart() {
        super.onStart()
        updateNextAlarmTime(RepositoryFake.timeForNextAlarm.value ?: -1)
     }

    override fun onDestroy() {
        super.onDestroy()
       // countDownTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        //countDownTimer?.cancel()
    }


    fun onClick(view: View) {
        createNotificationChannel()
        registerUnblockReceiver()
    }

    private fun updateNextAlarmTime(newNotificationTime: Long) {
        var nextAlarmInMillisString: String = findNextAlarm(newNotificationTime)
        findViewById<TextView>(R.id.textView).text = nextAlarmInMillisString
    }

    private fun findNextAlarm(newNotificationTime: Long): String {
        return if (newNotificationTime < 0) {
            "No Pending Notification"
        } else {
            val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
            SimpleDateFormat("HH:mm:ss", currentLocale).format(newNotificationTime).toString()
        }
    }

    private fun unRegister() {
        if (registerd.get()) {
            unregisterReceiver(br)
            registerd.set(false)
        }
    }

    fun unRegister(view: View) {
        unRegister()
    }

    private fun registerUnblockReceiver() {
        unRegister()
        val editText: String =
            findViewById<EditText>(R.id.waitTimeInput).waitTimeInput.text.toString()
        val timeInMillis = editText.toInt() * 1000
        UnlockBroadcastReceiver.screenTimeToNotification = timeInMillis
        registerd.set(true)
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
