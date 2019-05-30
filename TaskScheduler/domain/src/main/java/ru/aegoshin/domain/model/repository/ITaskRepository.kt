package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

interface ITaskRepository : IImmutableTaskRepository {
    fun nextId(): TaskId
    fun addTask(task: Task)
    fun removeTasks(taskIds: List<TaskId>)
    fun updateTasks(tasks: List<Task>)
    override fun findTasksByIds(taskIds: List<TaskId>):  List<Task>
    override fun findTaskById(taskId: TaskId): Task?
    fun findTasksByStatus(status: TaskStatus): List<Task>
    override fun findByDateInterval(from: Long?, to: Long?): List<Task>
    fun findTasksByCategory(categoryId: CategoryId?): List<Task>
}