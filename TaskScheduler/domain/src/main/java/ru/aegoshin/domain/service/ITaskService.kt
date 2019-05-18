package ru.aegoshin.domain.service

import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

interface ITaskService {
    fun addTask(
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long
    )
    fun updateTask(
        taskId: TaskId,
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long
    )
    fun removeTasks(taskIds: List<TaskId>)
    fun changeTasksStatusToCompleted(taskIds: List<TaskId>)
    fun changeTasksStatusToUncompleted(taskIds: List<TaskId>)
}