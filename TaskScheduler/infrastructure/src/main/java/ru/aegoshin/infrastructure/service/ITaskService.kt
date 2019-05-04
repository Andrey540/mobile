package ru.aegoshin.infrastructure.service

import ru.aegoshin.infrastructure.task.TaskData

interface ITaskService {
    fun addTask(taskData: TaskData)
    fun updateTask(taskId: String, taskData: TaskData)
    fun removeTasks(taskIds: List<String>)
    fun changeTasksStatusToCompleted(taskIds: List<String>)
    fun changeTasksStatusToUncompleted(taskIds: List<String>)
}