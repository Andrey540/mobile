package ru.aegoshin.taskscheduler.view.adapter

import ru.aegoshin.application.task.TaskStatus

data class TaskListItemPayload(
    var selected: Boolean?,
    val title: String?,
    val scheduledTime: Long?,
    val status: TaskStatus?
)