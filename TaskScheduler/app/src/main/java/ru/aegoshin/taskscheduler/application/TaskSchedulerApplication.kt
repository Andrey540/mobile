package ru.aegoshin.taskscheduler.application

import android.app.*
import android.content.Context
import android.content.Intent
import io.realm.Realm
import ru.aegoshin.infrastructure.repository.inmemory.TaskRepository as TaskInMemoryRepository
import ru.aegoshin.infrastructure.repository.realm.TaskRepository as TaskRealmRepository
import ru.aegoshin.infrastructure.repository.realm.CategoryRepository as CategoryRealmRepository
import ru.aegoshin.domain.model.task.TaskStatus as DomainTaskStatus
import ru.aegoshin.application.list.TaskList
import ru.aegoshin.domain.service.TaskService as DomainTaskService
import ru.aegoshin.application.event.EventDispatcher
import ru.aegoshin.application.presenter.ITaskListPresenter
import ru.aegoshin.application.presenter.TaskListPresenter
import ru.aegoshin.application.provider.ITaskDataProvider
import ru.aegoshin.application.provider.TaskDataProvider
import ru.aegoshin.application.service.ITaskService
import ru.aegoshin.application.service.TaskService
import ru.aegoshin.application.task.TaskStatus
import ru.aegoshin.taskscheduler.framework.receiver.TaskNotificationReceiver
import ru.aegoshin.infrastructure.migration.realm.Migration
import io.realm.RealmConfiguration
import ru.aegoshin.domain.service.CategoryService
import ru.aegoshin.infrastructure.transaction.realm.RealmTransaction
import java.io.File

class TaskSchedulerApplication : Application() {
    private lateinit var mScheduledTaskListPresenter: TaskListPresenter
    private lateinit var mUnscheduledTaskListPresenter: TaskListPresenter
    private lateinit var mTaskService: TaskService
    private lateinit var mCategoryService: CategoryService
    private lateinit var mTaskDataProvider: TaskDataProvider
    private lateinit var mRealm: Realm

    override fun onCreate() {
        super.onCreate()

        val file = File(applicationContext.filesDir, DATABASE_NAME)
        if (!file.exists()) {
            file.createNewFile()
        }

        Realm.init(applicationContext)
        val realmConfig = RealmConfiguration.Builder()
            .name(DATABASE_NAME)
            .deleteRealmIfMigrationNeeded()
            .migration(Migration())
            .schemaVersion(MIGRATION_VERSION)
            .build()
        Realm.migrateRealm(realmConfig)
        Realm.setDefaultConfiguration(realmConfig)
        mRealm = Realm.getInstance(realmConfig)

        val transaction = RealmTransaction(mRealm)
        val taskRepository = TaskRealmRepository(mRealm)
        val categoryRepository = CategoryRealmRepository(mRealm)

        //for debug by emulator
        /*val calendar = Calendar.getInstance()
        val notificationOffset: Long = 150 * 60000
        calendar.add(Calendar.MILLISECOND, notificationOffset.toInt() + 60000)

        transaction.begin()
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 1",
                "Description 1",
                null,
                DomainTaskStatus.Unscheduled,
                false,
                notificationOffset,
                null
            )
        )

        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 2",
                "Description 2",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 3",
                "Description 3",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 4",
                "Description 4",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 5",
                "Description 5",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 6",
                "Description 6",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        calendar.add(Calendar.MINUTE, 1)
        taskRepository.addTask(
            Task(
                taskRepository.nextId(),
                "Title 7",
                "Description 7",
                calendar.timeInMillis,
                DomainTaskStatus.Scheduled,
                true,
                notificationOffset,
                null
            )
        )
        transaction.commit()*/

        val eventDispatcher = EventDispatcher.instance
        instance = this
        instance.mTaskService = TaskService(DomainTaskService(taskRepository, categoryRepository, eventDispatcher), transaction)
        instance.mTaskDataProvider = TaskDataProvider(taskRepository)
        instance.mScheduledTaskListPresenter = TaskListPresenter(taskRepository, TaskList(), eventDispatcher)
        instance.mUnscheduledTaskListPresenter = TaskListPresenter(taskRepository, TaskList(), eventDispatcher)
        instance.mUnscheduledTaskListPresenter.updateStatuses(listOf(TaskStatus.Unscheduled))
        instance.mUnscheduledTaskListPresenter.updateList(taskRepository.findTasksByStatus(DomainTaskStatus.Unscheduled))

        initAlarmManager()
    }

    override fun onTerminate() {
        super.onTerminate()

        mRealm.close()
    }

    private fun initAlarmManager() {
        val alarmIntent = Intent(this, TaskNotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            NOTIFICATION_INTERVAL.toLong(),
            pendingIntent
        )
    }

    companion object {
        const val NOTIFICATION_INTERVAL = 5 * 60 * 1000
        const val MIGRATION_VERSION = 1.toLong()
        private const val DATABASE_NAME = "taskscheduler.realm"
        private lateinit var instance: TaskSchedulerApplication

        fun getScheduledTaskListPresenter(): ITaskListPresenter {
            return instance.mScheduledTaskListPresenter
        }

        fun getUnscheduledTaskListPresenter(): ITaskListPresenter {
            return instance.mUnscheduledTaskListPresenter
        }

        fun getTaskDataProvider(): ITaskDataProvider {
            return instance.mTaskDataProvider
        }

        fun getTaskService(): ITaskService {
            return instance.mTaskService
        }
    }
}