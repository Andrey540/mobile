package ru.aegoshin.domain.model.task

import ru.aegoshin.domain.model.category.Category

interface IImmutableTask {
    fun getId(): TaskId
    fun getTitle(): String
    fun getDescription(): String
    fun getScheduledTime(): Long?
    fun getStatus(): TaskStatus
    fun isNotificationEnabled(): Boolean
    fun getNotificationOffset(): Long
    fun getCategory(): Category?
}