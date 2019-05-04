package ru.aegoshin.infrastructure.task

data class TaskData(
    val title: String,
    val description: String,
    val scheduledTime: Long?,
    val status: TaskStatus,
    val needNotify: Boolean,
    val notifyBefore: Long
)