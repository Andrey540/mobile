package ru.aegoshin.taskscheduler.view.model

import ru.aegoshin.infrastructure.task.TaskStatus

class TaskViewModel (
    var selected: Boolean,
    val id: String,
    val title: String,
    val scheduledTime: Long?,
    val status: TaskStatus
)