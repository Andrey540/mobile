package ru.aegoshin.infrastructure.provider

import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.infrastructure.task.TaskData
import ru.aegoshin.infrastructure.task.TaskStatus

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
            task.getNeedNotify(),
            task.getNotifyBefore()
        )
    }

    private fun convertTask(task: IImmutableTask): Task {
        return Task(
            task.getId().toString(),
            task.getTitle(),
            task.getDescription(),
            task.getScheduledTime(),
            TaskStatus.fromInt(task.getStatus().status),
            task.getNeedNotify(),
            task.getNotifyBefore()
        )
    }
}