package ru.aegoshin.infrastructure.service

import ru.aegoshin.domain.service.ITaskService as IDomainTaskService
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import ru.aegoshin.infrastructure.task.TaskData
import ru.aegoshin.infrastructure.transaction.ITransaction

class TaskService(
    private val taskService: IDomainTaskService,
    private val transaction: ITransaction
) : ITaskService {

    override fun addTask(taskData: TaskData) {
        transaction.begin()
        taskService.addTask(
            taskData.title,
            taskData.description,
            taskData.scheduledTime,
            TaskStatus.fromInt(taskData.status.status),
            taskData.isNotificationEnabled,
            taskData.notificationOffset
        )
        transaction.commit()
    }

    override fun updateTask(taskId: String, taskData: TaskData) {
        transaction.begin()
        taskService.updateTask(
            TaskId(TaskId.stringToUuid(taskId)),
            taskData.title,
            taskData.description,
            taskData.scheduledTime,
            TaskStatus.fromInt(taskData.status.status),
            taskData.isNotificationEnabled,
            taskData.notificationOffset
        )
        transaction.commit()
    }

    override fun removeTasks(taskIds: List<String>) {
        transaction.begin()
        taskService.removeTasks(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
        transaction.commit()
    }

    override fun changeTasksStatusToCompleted(taskIds: List<String>) {
        transaction.begin()
        taskService.changeTasksStatusToCompleted(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
        transaction.commit()
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<String>) {
        transaction.begin()
        taskService.changeTasksStatusToUncompleted(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
        transaction.commit()
    }
}