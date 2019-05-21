package ru.aegoshin.infrastructure.repository.inmemory

import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import java.util.*

class TaskRepository : ITaskRepository, IImmutableTaskRepository {
    private val mTasks = mutableListOf<Task>()

    override fun nextId(): TaskId {
        return TaskId(UUID.randomUUID())
    }

    override fun addTask(task: Task) {
        if (mTasks.indexOf(task) == -1) {
            mTasks.add(task)
        }
    }

    override fun removeTasks(taskIds: List<TaskId>) {
        val removingTasks = findTasksByIds(taskIds)
        mTasks.removeAll(removingTasks)
    }

    override fun updateTasks(tasks: List<Task>) {}

    override fun findTasksByIds(taskIds: List<TaskId>): List<Task> {
        return mTasks.filter { taskIds.find { taskId -> it.getId().equalTo(taskId) } !== null }
    }

    override fun findTaskById(taskId: TaskId): Task? {
        return mTasks.find { it.getId().equalTo(taskId) }
    }

    override fun findTasksByStatus(status: TaskStatus): List<Task> {
        return mTasks.filter { it.getStatus() == status }
    }

    override fun findByDateInterval(from: Long?, to: Long?): List<Task> {
        return mTasks.filter { isTaskInDateInterval(it, from, to) }
    }

    override fun findNotifiableTasksByInterval(from: Long, to: Long): List<IImmutableTask> {
        return mTasks.filter {
            it.isNotificationEnabled() && (it.getStatus() == TaskStatus.Scheduled) && isTaskInDateInterval(
                it,
                from + it.getNotificationOffset(),
                to + it.getNotificationOffset()
            )
        }
    }

    private fun isTaskInDateInterval(task: Task, from: Long?, to: Long?): Boolean {
        if (from == null && to == null) {
            return true
        } else if (from == null && to != null) {
            return if (task.getScheduledTime() == null) false else task.getScheduledTime()!! <= to
        } else if (from != null && to == null) {
            return if (task.getScheduledTime() == null) false else task.getScheduledTime()!! >= from
        } else if (task.getScheduledTime() == null) {
            return false
        }
        return task.getScheduledTime()!! in from!! .. to!!
    }
}