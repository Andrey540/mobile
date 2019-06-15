package ru.aegoshin.taskscheduler.view.model

import ru.aegoshin.application.task.TaskStatus

class TaskViewModel (
    var selected: Boolean,
    var swiped: Boolean,
    val id: String,
    val title: String,
    val scheduledTime: Long?,
    val status: TaskStatus
)