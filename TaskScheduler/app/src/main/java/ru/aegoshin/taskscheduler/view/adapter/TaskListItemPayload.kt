package ru.aegoshin.taskscheduler.view.adapter

import ru.aegoshin.infrastructure.task.TaskStatus

data class TaskListItemPayload(
    val selected: Boolean?,
    val title: String?,
    val scheduledTime: Long?,
    val status: TaskStatus?
)