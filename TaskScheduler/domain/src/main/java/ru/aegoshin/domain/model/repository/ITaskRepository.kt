package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

interface ITaskRepository {
    fun nextId(): TaskId
    fun addTask(task: Task)
    fun removeTasks(taskIds: List<TaskId>)
    fun updateTasks(tasks: List<Task>)
    fun findTasksByIds(taskIds: List<TaskId>):  List<Task>
    fun findTaskById(taskId: TaskId): Task?
    fun findTasksByStatus(status: TaskStatus): List<Task>
    fun findByDateInterval(from: Long?, to: Long?): List<Task>
}