package ru.aegoshin.domain.service

import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

interface ITaskService {
    fun addTask(
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long,
        categoryId: CategoryId?
    )
    fun updateTask(
        taskId: TaskId,
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long,
        categoryId: CategoryId?
    )
    fun removeTasks(taskIds: List<TaskId>)
    fun changeTasksStatusToCompleted(taskIds: List<TaskId>)
    fun changeTasksStatusToUncompleted(taskIds: List<TaskId>)
    fun changeTasksCategory(taskIds: List<TaskId>, categoryId: CategoryId?)
}