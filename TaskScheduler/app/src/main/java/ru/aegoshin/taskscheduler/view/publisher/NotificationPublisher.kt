package ru.aegoshin.taskscheduler.view.publisher

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationPublisher {
    const val NOTIFICATION_CHANNEL_ID = "task_channel"
    private const val NOTIFICATION_TAG = "Task"
    private const val NOTIFICATION_CHANNEL_NAME = "task_channel"

    fun publish(context: Context, notification: Notification, number: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        publishImpl(context, notification, number)
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun publishImpl(context: Context, notification: Notification, id: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_TAG, id, notification)
    }
}