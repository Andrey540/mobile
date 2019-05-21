package ru.aegoshin.infrastructure.repository.realm

import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmQuery
import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.infrastructure.entity.Task as TaskEntity
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import java.lang.RuntimeException
import java.util.*

class TaskRepository(
    private val realm: DynamicRealm
) : ITaskRepository, IImmutableTaskRepository {
    override fun nextId(): TaskId {
        return TaskId(UUID.randomUUID())
    }

    override fun addTask(task: Task) {
        val entity = realm.createObject(TaskEntity.SCHEMA, task.getId().toString())
        entity.setString(TaskEntity.TITLE_FIELD, task.getTitle())
        entity.setString(TaskEntity.DESCRIPTION_FIELD, task.getDescription())
        if (task.getScheduledTime() != null) {
            entity.setLong(TaskEntity.SCHEDULED_TIME_FIELD, task.getScheduledTime()!!)
        }
        entity.setInt(TaskEntity.STATUS_FIELD, task.getStatus().status)
        entity.setBoolean(TaskEntity.IS_NOTIFICATION_ENABLED_FIELD, task.isNotificationEnabled())
        entity.setLong(TaskEntity.NOTIFICATION_OFFSET_FIELD, task.getNotificationOffset())
    }

    override fun removeTasks(taskIds: List<TaskId>) {
        val tasksObj = realm.where(TaskEntity.SCHEMA)
            .`in`(TaskEntity.ID_FIELD, taskIds.map { it.toString() }.toTypedArray())
            .findAll()
        tasksObj.deleteAllFromRealm()
    }

    override fun updateTasks(tasks: List<Task>) {
        tasks.forEach { updateTask(it) }
    }

    override fun findTasksByIds(taskIds: List<TaskId>): List<Task> {
        val tasksObj = realm.where(TaskEntity.SCHEMA)
            .`in`(TaskEntity.ID_FIELD, taskIds.map { it.toString() }.toTypedArray())
            .findAll()
        return tasksObj.toArray().map { populateTask(it as DynamicRealmObject) }
    }

    override fun findTaskById(taskId: TaskId): Task? {
        val taskObj = realm.where(TaskEntity.SCHEMA)
            .equalTo(TaskEntity.ID_FIELD, taskId.toString())
            .findFirst()
        return if (taskObj != null) populateTask(taskObj) else null
    }

    override fun findTasksByStatus(status: TaskStatus): List<Task> {
        val tasksObj = realm.where(TaskEntity.SCHEMA)
            .equalTo(TaskEntity.STATUS_FIELD, status.status)
            .findAll()
        return tasksObj.toArray().map { populateTask(it as DynamicRealmObject) }
    }

    override fun findByDateInterval(from: Long?, to: Long?): List<Task> {
        val tasksObj = createDateIntervalQuery(from, to).findAll()
        return tasksObj.toArray().map { populateTask(it as DynamicRealmObject) }
    }

    override fun findNotifiableTasksByInterval(from: Long, to: Long): List<IImmutableTask> {
        val tasksObj = createDateIntervalQuery(from, to + NOTIFICATION_MAX_OFFSET)
            .equalTo(TaskEntity.IS_NOTIFICATION_ENABLED_FIELD, true)
            .equalTo(TaskEntity.STATUS_FIELD, TaskStatus.Scheduled.status)
            .findAll()
        val tasks = tasksObj.toArray().map { populateTask(it as DynamicRealmObject) }
        return tasks.filter { it.getScheduledTime()!! - it.getNotificationOffset() in from..to }
    }

    private fun createDateIntervalQuery(from: Long?, to: Long?): RealmQuery<DynamicRealmObject> {
        var taskQuery = realm.where(TaskEntity.SCHEMA)
        if (from == null && to != null) {
            taskQuery = taskQuery.lessThan(TaskEntity.SCHEDULED_TIME_FIELD, to)
        } else if (from != null && to == null) {
            taskQuery = taskQuery.greaterThan(TaskEntity.SCHEDULED_TIME_FIELD, from)
        } else if (from != null && to != null) {
            taskQuery = taskQuery.between(TaskEntity.SCHEDULED_TIME_FIELD, from, to)
        }
        return taskQuery
    }

    private fun updateTask(task: Task) {
        val taskId = task.getId().toString()
        val taskObj = realm.where(TaskEntity.SCHEMA).equalTo(TaskEntity.ID_FIELD, taskId).findFirst()
        taskObj ?: throw RuntimeException("Cannot find task data by id: $taskId")

        taskObj.setString(ru.aegoshin.infrastructure.entity.Task.TITLE_FIELD, task.getTitle())
        taskObj.setString(ru.aegoshin.infrastructure.entity.Task.DESCRIPTION_FIELD, task.getDescription())
        if (task.getScheduledTime() == null) {
            taskObj.setNull(ru.aegoshin.infrastructure.entity.Task.SCHEDULED_TIME_FIELD)
        } else {
            taskObj.setLong(ru.aegoshin.infrastructure.entity.Task.SCHEDULED_TIME_FIELD, task.getScheduledTime()!!)
        }
        taskObj.setInt(ru.aegoshin.infrastructure.entity.Task.STATUS_FIELD, task.getStatus().status)
        taskObj.setBoolean(
            ru.aegoshin.infrastructure.entity.Task.IS_NOTIFICATION_ENABLED_FIELD,
            task.isNotificationEnabled()
        )
        taskObj.setLong(ru.aegoshin.infrastructure.entity.Task.NOTIFICATION_OFFSET_FIELD, task.getNotificationOffset())
    }

    private fun populateTask(obj: DynamicRealmObject): Task {
        return Task(
            TaskId(TaskId.stringToUuid(obj.getString(ru.aegoshin.infrastructure.entity.Task.ID_FIELD))),
            obj.getString(ru.aegoshin.infrastructure.entity.Task.TITLE_FIELD),
            obj.getString(ru.aegoshin.infrastructure.entity.Task.DESCRIPTION_FIELD),
            if (obj.isNull(ru.aegoshin.infrastructure.entity.Task.SCHEDULED_TIME_FIELD)) null else obj.getLong(ru.aegoshin.infrastructure.entity.Task.SCHEDULED_TIME_FIELD),
            TaskStatus.fromInt(obj.getInt(ru.aegoshin.infrastructure.entity.Task.STATUS_FIELD)),
            obj.getBoolean(ru.aegoshin.infrastructure.entity.Task.IS_NOTIFICATION_ENABLED_FIELD),
            obj.getLong(ru.aegoshin.infrastructure.entity.Task.NOTIFICATION_OFFSET_FIELD)
        )
    }

    companion object {
        private const val NOTIFICATION_MAX_OFFSET = 24 * 60 * 60 * 1000
    }
}