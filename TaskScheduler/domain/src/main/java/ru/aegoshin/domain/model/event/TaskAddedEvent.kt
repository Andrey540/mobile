package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.task.Task

class TaskAddedEvent(private val task: Task) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getTask(): Task {
        return task
    }

    companion object {
        const val TYPE = "task_added"
    }
}