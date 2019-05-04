package ru.aegoshin.taskscheduler.application

import android.app.*
import android.content.Context
import android.content.Intent
import ru.aegoshin.infrastructure.repository.TaskInMemoryRepository
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskStatus
import ru.aegoshin.infrastructure.list.TaskList
import ru.aegoshin.domain.service.TaskService as DomainTaskService
import ru.aegoshin.infrastructure.event.EventDispatcher
import ru.aegoshin.infrastructure.presenter.ITaskListPresenter
import ru.aegoshin.infrastructure.presenter.TaskListPresenter
import ru.aegoshin.infrastructure.provider.ITaskDataProvider
import ru.aegoshin.infrastructure.provider.TaskDataProvider
import ru.aegoshin.infrastructure.service.ITaskService
import ru.aegoshin.infrastructure.service.TaskService
import ru.aegoshin.taskscheduler.application.receiver.TaskNotificationReceiver
import java.util.*

class TaskSchedulerApplication : Application() {
    private lateinit var mTaskListPresenter: TaskListPresenter
    private lateinit var mTaskService: TaskService
    private lateinit var mTaskDataProvider: TaskDataProvider

    override fun onCreate() {
        super.onCreate()

        val repository = TaskInMemoryRepository()
        val calendar = Calendar.getInstance()
        val notificationOffset: Long = 150 * 60000
        calendar.add(Calendar.MILLISECOND, notificationOffset.toInt() + 60000)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 1",
                "Description 1",
                null,
                TaskStatus.Unscheduled,
                false,
                notificationOffset
            )
        )
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 2",
                "Description 2",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 3",
                "Description 3",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 4",
                "Description 4",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 5",
                "Description 5",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 6",
                "Description 6",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        repository.addTask(
            Task(
                repository.nextId(),
                "Title 7",
                "Description 7",
                calendar.timeInMillis,
                TaskStatus.Scheduled,
                true,
                notificationOffset
            )
        )

        val eventDispatcher = EventDispatcher.instance
        instance = this
        instance.mTaskService = TaskService(DomainTaskService(repository, eventDispatcher))
        instance.mTaskDataProvider = TaskDataProvider(repository)
        instance.mTaskListPresenter = TaskListPresenter(repository, TaskList(), eventDispatcher)

        initAlarmManager()
    }

    private fun initAlarmManager() {
        val alarmIntent = Intent(this, TaskNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), NOTIFICATION_INTERVAL.toLong(), pendingIntent)
    }

    companion object {
        const val NOTIFICATION_INTERVAL = 60000
        private lateinit var instance: TaskSchedulerApplication

        fun getTaskListPresenter(): ITaskListPresenter {
            return instance.mTaskListPresenter
        }

        fun getTaskDataProvider(): ITaskDataProvider {
            return instance.mTaskDataProvider
        }

        fun getTaskService(): ITaskService {
            return instance.mTaskService
        }
    }
}