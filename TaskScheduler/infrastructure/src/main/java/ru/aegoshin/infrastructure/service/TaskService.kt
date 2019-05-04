package ru.aegoshin.infrastructure.service

import ru.aegoshin.domain.service.ITaskService as IDomainTaskService
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import ru.aegoshin.infrastructure.task.TaskData

class TaskService(
    private val taskService: IDomainTaskService
) : ITaskService {

    override fun addTask(taskData: TaskData) {
        taskService.addTask(
            taskData.title,
            taskData.description,
            taskData.scheduledTime,
            TaskStatus.fromInt(taskData.status.status),
            taskData.needNotify,
            taskData.notifyBefore
        )
    }

    override fun updateTask(taskId: String, taskData: TaskData) {
        taskService.updateTask(
            TaskId(TaskId.stringToUuid(taskId)),
            taskData.title,
            taskData.description,
            taskData.scheduledTime,
            TaskStatus.fromInt(taskData.status.status),
            taskData.needNotify,
            taskData.notifyBefore
        )
    }

    override fun removeTasks(taskIds: List<String>) {
        taskService.removeTasks(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
    }

    override fun changeTasksStatusToCompleted(taskIds: List<String>) {
        taskService.changeTasksStatusToCompleted(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<String>) {
        taskService.changeTasksStatusToUncompleted(
            taskIds.map { TaskId(TaskId.stringToUuid(it)) }
        )
    }
}