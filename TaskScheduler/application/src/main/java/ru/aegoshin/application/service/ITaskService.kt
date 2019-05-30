package ru.aegoshin.application.service

import ru.aegoshin.application.task.TaskData

interface ITaskService {
    fun addTask(taskData: TaskData)
    fun updateTask(taskId: String, taskData: TaskData)
    fun removeTasks(taskIds: List<String>)
    fun changeTasksStatusToCompleted(taskIds: List<String>)
    fun changeTasksStatusToUncompleted(taskIds: List<String>)
}