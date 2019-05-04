package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.TaskId
import java.util.*

interface IImmutableTaskRepository {
    fun findByDateInterval(from: Long?, to: Long?): List<IImmutableTask>
    fun findTaskById(taskId: TaskId): IImmutableTask?
    fun findNotifiableTasksByInterval(from: Long, to: Long): List<IImmutableTask>
}