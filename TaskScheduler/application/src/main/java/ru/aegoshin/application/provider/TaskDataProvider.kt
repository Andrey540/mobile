package ru.aegoshin.application.provider

import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.application.task.Task
import ru.aegoshin.application.task.TaskData
import ru.aegoshin.application.task.TaskStatus
import ru.aegoshin.domain.model.repository.IImmutableTaskRepository

class TaskDataProvider(
    private val repository: IImmutableTaskRepository
) : ITaskDataProvider {
    override fun findTaskDataById(taskId: String): TaskData? {
        val task = repository.findTaskById(TaskId(TaskId.stringToUuid(taskId)))
        return if (task != null) convertTaskData(task) else null
    }

    override fun findTasksByDateInterval(from: Long?, to: Long?): List<Task> {
        return repository.findByDateInterval(from, to).map { convertTask(it) }
    }

    override fun findNotifiableTasksByInterval(from: Long, to: Long): List<Task> {
        return repository.findNotifiableTasksByInterval(from, to).map { convertTask(it) }
    }

    private fun convertTaskData(task: IImmutableTask): TaskData {
        return TaskData(
            task.getTitle(),
            task.getDescription(),
            task.getScheduledTime(),
            TaskStatus.fromInt(task.getStatus().status),
            task.isNotificationEnabled(),
            task.getNotificationOffset()
        )
    }

    private fun convertTask(task: IImmutableTask): Task {
        return Task(
            task.getId().toString(),
            task.getTitle(),
            task.getDescription(),
            task.getScheduledTime(),
            TaskStatus.fromInt(task.getStatus().status),
            task.isNotificationEnabled(),
            task.getNotificationOffset()
        )
    }
}