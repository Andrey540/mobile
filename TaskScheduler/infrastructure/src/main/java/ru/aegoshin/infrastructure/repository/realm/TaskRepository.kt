package ru.aegoshin.infrastructure.repository.realm

import io.realm.Realm
import io.realm.RealmQuery
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.infrastructure.entity.realm.Task as TaskEntity
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import java.lang.RuntimeException
import java.util.*

class TaskRepository(
    private val realm: Realm
) : ITaskRepository, IImmutableTaskRepository {
    companion object {
        private const val NOTIFICATION_MAX_OFFSET = 24 * 60 * 60 * 1000

        const val SCHEMA = "Task"
        const val ID_FIELD = "id"
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val SCHEDULED_TIME_FIELD = "scheduledTime"
        const val STATUS_FIELD = "status"
        const val IS_NOTIFICATION_ENABLED_FIELD = "isNotificationEnabled"
        const val NOTIFICATION_OFFSET_FIELD = "notificationOffset"
    }

    override fun nextId(): TaskId {
        return TaskId(UUID.randomUUID())
    }

    override fun addTask(task: Task) {
        val entity = realm.createObject(TaskEntity::class.java)
        entity.id = task.getId().toString()
        entity.title = task.getTitle()
        entity.description = task.getDescription()
        entity.scheduledTime = task.getScheduledTime()
        entity.status = task.getStatus().status
        entity.isNotificationEnabled = task.isNotificationEnabled()
        entity.notificationOffset = task.getNotificationOffset()
    }

    override fun removeTasks(taskIds: List<TaskId>) {
        val tasksObj = realm.where(TaskEntity::class.java)
            .`in`(ID_FIELD, taskIds.map { it.toString() }.toTypedArray())
            .findAll()
        tasksObj.deleteAllFromRealm()
    }

    override fun updateTasks(tasks: List<Task>) {
        tasks.forEach { updateTask(it) }
    }

    override fun findTasksByIds(taskIds: List<TaskId>): List<Task> {
        val tasksObj = realm.where(TaskEntity::class.java)
            .`in`(ID_FIELD, taskIds.map { it.toString() }.toTypedArray())
            .findAll()
        return tasksObj.toArray().map { populateDomainTask(it as TaskEntity) }
    }

    override fun findTaskById(taskId: TaskId): Task? {
        val taskObj = realm.where(TaskEntity::class.java)
            .equalTo(ID_FIELD, taskId.toString())
            .findFirst()
        return if (taskObj != null) populateDomainTask(taskObj) else null
    }

    override fun findTasksByStatus(status: TaskStatus): List<Task> {
        val tasksObj = realm.where(TaskEntity::class.java)
            .equalTo(STATUS_FIELD, status.status)
            .findAll()
        return tasksObj.toArray().map { populateDomainTask(it as TaskEntity) }
    }

    override fun findByDateInterval(from: Long?, to: Long?): List<Task> {
        val tasksObj = createDateIntervalQuery(from, to).findAll()
        return tasksObj.toArray().map { populateDomainTask(it as TaskEntity) }
    }

    override fun findTasksByCategory(categoryId: CategoryId?): List<Task> {
        // TODO
        return listOf()
    }

    override fun findNotifiableTasksByInterval(from: Long, to: Long): List<IImmutableTask> {
        val tasksObj = createDateIntervalQuery(from, to + NOTIFICATION_MAX_OFFSET)
            .equalTo(IS_NOTIFICATION_ENABLED_FIELD, true)
            .equalTo(STATUS_FIELD, TaskStatus.Scheduled.status)
            .findAll()
        val tasks = tasksObj.toArray().map { populateDomainTask(it as TaskEntity) }
        return tasks.filter { it.getScheduledTime()!! - it.getNotificationOffset() in from..to }
    }

    private fun createDateIntervalQuery(from: Long?, to: Long?): RealmQuery<TaskEntity> {
        var taskQuery = realm.where(TaskEntity::class.java)
        if (from == null && to != null) {
            taskQuery = taskQuery.lessThan(SCHEDULED_TIME_FIELD, to)
        } else if (from != null && to == null) {
            taskQuery = taskQuery.greaterThan(SCHEDULED_TIME_FIELD, from)
        } else if (from != null && to != null) {
            taskQuery = taskQuery.between(SCHEDULED_TIME_FIELD, from, to)
        }
        return taskQuery
    }

    private fun updateTask(task: Task) {
        val taskId = task.getId().toString()
        val taskObj = realm.where(TaskEntity::class.java).equalTo(ID_FIELD, taskId).findFirst()
        taskObj ?: throw RuntimeException("Cannot find task data by id: $taskId")

        updateEntity(taskObj, task)
    }

    private fun updateEntity(taskEntity: TaskEntity, taskDomain: Task): TaskEntity {
        taskEntity.title = taskDomain.getTitle()
        taskEntity.description = taskDomain.getDescription()
        taskEntity.scheduledTime = taskDomain.getScheduledTime()
        taskEntity.status = taskDomain.getStatus().status
        taskEntity.isNotificationEnabled = taskDomain.isNotificationEnabled()
        taskEntity.notificationOffset = taskDomain.getNotificationOffset()
        return taskEntity
    }

    private fun populateDomainTask(taskEntity: TaskEntity): Task {
        return Task(
            TaskId(TaskId.stringToUuid(taskEntity.id)),
            taskEntity.title,
            taskEntity.description,
            taskEntity.scheduledTime,
            TaskStatus.fromInt(taskEntity.status),
            taskEntity.isNotificationEnabled,
            taskEntity.notificationOffset,
            null
        )
    }
}