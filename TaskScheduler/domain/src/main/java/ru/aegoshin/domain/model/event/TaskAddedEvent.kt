package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.task.TaskId

class TaskAddedEvent(private val taskId: TaskId) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getTaskId(): TaskId {
        return taskId
    }

    companion object {
        const val TYPE = "task_added"
    }
}