package ru.aegoshin.application.provider

import ru.aegoshin.application.task.Task
import ru.aegoshin.application.task.TaskData

interface ITaskDataProvider {
    fun findTaskDataById(taskId: String): TaskData?
    fun findTasksByDateInterval(from: Long?, to: Long?): List<Task>
    fun findNotifiableTasksByInterval(from: Long, to: Long): List<Task>
}