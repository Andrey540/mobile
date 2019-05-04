package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.task.IImmutableTask

class TasksUpdatedEvent(private val tasks: List<IImmutableTask>) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getTasks(): List<IImmutableTask> {
        return tasks
    }

    companion object {
        const val TYPE = "tasks_updated"
    }
}