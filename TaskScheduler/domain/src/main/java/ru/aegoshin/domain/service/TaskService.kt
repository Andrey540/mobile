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
        needNotify: Boolean,
        notifyBefore: Long
    ) {
        val task = Task(repository.nextId(), title, description, scheduledTime, status, needNotify, notifyBefore)
        repository.addTask(task)
        eventDispatcher.dispatch(TaskAddedEvent(task))
    }

    override fun updateTask(
        taskId: TaskId,
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        needNotify: Boolean,
        notifyBefore: Long
    ) {
        val task = repository.findTaskById(taskId)
        task ?: throw TaskNotFoundException(taskId)
        task.setTitle(title)
        task.setDescription(description)
        task.updateScheduledTimeAndStatus(scheduledTime, status)
        task.setNeedNotify(needNotify)
        task.setNotifyBefore(notifyBefore)
        eventDispatcher.dispatch(TasksUpdatedEvent(listOf(task)))
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
        eventDispatcher.dispatch(TasksUpdatedEvent(tasks))
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = repository.findTasksByIds(taskIds)
        tasks.forEach { it.setUncompleted() }
        eventDispatcher.dispatch(TasksUpdatedEvent(tasks))
    }
}