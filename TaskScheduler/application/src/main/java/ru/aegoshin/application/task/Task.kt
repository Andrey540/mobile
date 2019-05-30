package ru.aegoshin.application.task

class Task (
    val id: String,
    val title: String,
    val description: String,
    val scheduledTime: Long?,
    val status: TaskStatus,
    val isNotificationEnabled: Boolean,
    val notificationOffset: Long
)