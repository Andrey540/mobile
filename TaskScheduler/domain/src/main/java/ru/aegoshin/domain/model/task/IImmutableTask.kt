package ru.aegoshin.domain.model.task

interface IImmutableTask {
    fun getId(): TaskId
    fun getTitle(): String
    fun getDescription(): String
    fun getScheduledTime(): Long?
    fun getStatus(): TaskStatus
    fun isNotificationEnabled(): Boolean
    fun getNotificationOffset(): Long
}