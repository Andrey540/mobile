package ru.aegoshin.application.task

data class TaskData(
    val title: String,
    val description: String,
    val scheduledTime: Long?,
    val status: TaskStatus,
    val isNotificationEnabled: Boolean,
    val notificationOffset: Long
)