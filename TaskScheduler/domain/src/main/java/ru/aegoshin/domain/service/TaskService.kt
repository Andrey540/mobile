package ru.aegoshin.domain.service

import ru.aegoshin.domain.exception.TaskNotFoundException
import ru.aegoshin.domain.model.event.IEventDispather
import ru.aegoshin.domain.model.event.TaskAddedEvent
import ru.aegoshin.domain.model.event.TasksRemovedEvent
import ru.aegoshin.domain.model.event.TasksUpdatedEvent
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

class TaskService(
    private val repository: ITaskRepository,
    private val eventDispatcher: IEventDispather
) : ITaskService {
    override fun addTask(
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long
    ) {
        val task = Task(repository.nextId(), title, description, scheduledTime, status, isNotificationEnabled, notificationOffset)
        repository.addTask(task)
        eventDispatcher.dispatch(TaskAddedEvent(task.getId()))
    }

    override fun updateTask(
        taskId: TaskId,
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long
    ) {
        val task = repository.findTaskById(taskId)
        task ?: throw TaskNotFoundException(taskId)
        task.setTitle(title)
        task.setDescription(description)
        task.updateScheduledTimeAndStatus(scheduledTime, status)
        task.setIsNotificationEnabled(isNotificationEnabled)
        task.setNotificationOffset(notificationOffset)
        repository.updateTasks(listOf(task))
        eventDispatcher.dispatch(TasksUpdatedEvent(listOf(task.getId())))
    }

    override fun removeTasks(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        repository.removeTasks(taskIds)
        eventDispatcher.dispatch(TasksRemovedEvent(taskIds))
    }

    override fun changeTasksStatusToCompleted(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = repository.findTasksByIds(taskIds)
        tasks.forEach { it.setCompleted() }
        repository.updateTasks(tasks)
        eventDispatcher.dispatch(TasksUpdatedEvent(tasks.map { it.getId() }))
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = repository.findTasksByIds(taskIds)
        tasks.forEach { it.setUncompleted() }
        repository.updateTasks(tasks)
        eventDispatcher.dispatch(TasksUpdatedEvent(tasks.map { it.getId() }))
    }
}