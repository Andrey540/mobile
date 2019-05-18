package ru.aegoshin.taskscheduler.view.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.view.activity.TaskActivity
import ru.aegoshin.taskscheduler.view.publisher.NotificationPublisher
import java.text.SimpleDateFormat
import java.util.*

object NotificationFactory {
    fun createTaskNotification(context: Context, task: Task, number: Int): Notification {
        val title = task.title + " " + context.getString(R.string.at) + SimpleDateFormat(
            " d MMMM HH:mm",
            Locale.getDefault()
        ).format(task.scheduledTime)
        val text = task.description

        val intent = Intent(context, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)

        val builder = NotificationCompat.Builder(context, NotificationPublisher.NOTIFICATION_CHANNEL_ID)
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
        return builder.build()
    }
}