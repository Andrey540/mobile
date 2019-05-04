package ru.aegoshin.infrastructure.task

class Task (
    val id: String,
    val title: String,
    val description: String,
    val scheduledTime: Long?,
    val status: TaskStatus,
    val needNotify: Boolean,
    val notifyBefore: Long
)