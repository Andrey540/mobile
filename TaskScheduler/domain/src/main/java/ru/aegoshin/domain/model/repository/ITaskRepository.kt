package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import java.util.*

interface ITaskRepository {
    fun nextId(): TaskId
    fun addTask(task: Task)
    fun removeTasks(taskIds: List<TaskId>)
    fun findTasksByIds(taskIds: List<TaskId>):  List<Task>
    fun findTaskById(taskId: TaskId): Task?
    fun findByDateInterval(from: Long?, to: Long?): List<Task>
}