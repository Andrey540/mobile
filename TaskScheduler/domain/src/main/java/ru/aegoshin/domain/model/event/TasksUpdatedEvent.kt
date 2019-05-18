package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.task.TaskId

class TasksUpdatedEvent(private val taskIds: List<TaskId>) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getTaskIds(): List<TaskId> {
        return taskIds
    }

    companion object {
        const val TYPE = "tasks_updated"
    }
}