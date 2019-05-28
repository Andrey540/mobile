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
        transaction.execute {
            taskService.addTask(
                taskData.title,
                taskData.description,
                taskData.scheduledTime,
                TaskStatus.fromInt(taskData.status.status),
                taskData.isNotificationEnabled,
                taskData.notificationOffset,
                null
            )
        }
    }

    override fun updateTask(taskId: String, taskData: TaskData) {
        transaction.execute{
            taskService.updateTask(
                TaskId(TaskId.stringToUuid(taskId)),
                taskData.title,
                taskData.description,
                taskData.scheduledTime,
                TaskStatus.fromInt(taskData.status.status),
                taskData.isNotificationEnabled,
                taskData.notificationOffset,
                null
            )
        }
    }

    override fun removeTasks(taskIds: List<String>) {
        transaction.execute{
            taskService.removeTasks(
                taskIds.map { TaskId(TaskId.stringToUuid(it)) }
            )
        }
    }

    override fun changeTasksStatusToCompleted(taskIds: List<String>) {
        transaction.execute{
            taskService.changeTasksStatusToCompleted(
                taskIds.map { TaskId(TaskId.stringToUuid(it)) }
            )
        }
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<String>) {
        transaction.execute{
            taskService.changeTasksStatusToUncompleted(
                taskIds.map { TaskId(TaskId.stringToUuid(it)) }
            )
        }
    }
}