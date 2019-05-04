package ru.aegoshin.taskscheduler.view.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import ru.aegoshin.taskscheduler.R
import android.app.NotificationChannel
import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.taskscheduler.view.activity.TaskActivity
import java.text.SimpleDateFormat
import java.util.*


/**
 * Helper class for showing and canceling task
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object TaskNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private const val NOTIFICATION_TAG = "Task"
    private const val NOTIFICATION_CHANNEL_ID = "task_channel"
    private const val NOTIFICATION_CHANNEL_NAME = "task_channel"

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     *
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     *
     *
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of task notifications. Make
     * sure to follow the
     * [
 * Notification design guidelines](https://developer.android.com/design/patterns/notifications.html) when doing so.
     *
     * @see .cancel
     */
    fun notify(
        context: Context,
        task: Task,
        number: Int
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        val title = task.title + " " + context.getString(R.string.at) + SimpleDateFormat(
            " d MMMM HH:mm",
            Locale.getDefault()
        ).format(task.scheduledTime)
        val text = task.description

        val intent = Intent(context, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_stat_task)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setNumber(number)
            .setTicker(text)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        if (task.scheduledTime != null) {
            builder.setWhen(task.scheduledTime!!)
        }

        notify(context, builder.build(), number)
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification, id: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_TAG, id, notification)
    }

    /**
     * Cancels any notifications of this type previously shown using
     * [.notify].
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context, id: Int) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIFICATION_TAG.hashCode() + id)
    }
}
