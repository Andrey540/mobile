package ru.aegoshin.infrastructure.provider

import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.infrastructure.task.TaskData

interface ITaskDataProvider {
    fun findTaskDataById(taskId: String): TaskData?
    fun findTasksByDateInterval(from: Long?, to: Long?): List<Task>
    fun findNotifiableTasksByInterval(from: Long, to: Long): List<Task>
}