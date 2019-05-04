package ru.aegoshin.taskscheduler.application.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.view.notification.TaskNotification
import java.util.*

class TaskNotificationReceiver : BroadcastReceiver() {
    private val mTaskDataProvider = TaskSchedulerApplication.getTaskDataProvider()

    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()
        val from = calendar.timeInMillis
        calendar.add(Calendar.MILLISECOND, TaskSchedulerApplication.NOTIFICATION_INTERVAL)
        val to = calendar.timeInMillis

        val tasks = mTaskDataProvider.findNotifiableTasksByInterval(from, to)
        tasks.forEachIndexed { index, it -> TaskNotification.notify(context, it, index) }
    }
}
