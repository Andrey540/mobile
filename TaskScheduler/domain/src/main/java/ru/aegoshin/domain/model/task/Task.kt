package ru.aegoshin.domain.model.task

import ru.aegoshin.domain.model.category.Category

class Task(
    private var id: TaskId,
    private var title: String,
    private var description: String,
    private var scheduledTime: Long?,
    private var status: TaskStatus,
    private var isNotificationEnabled: Boolean,
    private var notificationOffset: Long,
    private var category: Category?
) : IImmutableTask {
    init {
        validateTitle(title)
        validateScheduledTimeAndStatus(scheduledTime, status)
    }

    override fun getId(): TaskId {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    override fun getDescription(): String {
        return description
    }

    override fun getScheduledTime(): Long? {
        return scheduledTime
    }

    override fun getStatus(): TaskStatus {
        return status
    }

    override fun isNotificationEnabled(): Boolean {
        return isNotificationEnabled
    }

    override fun getNotificationOffset(): Long {
        return notificationOffset
    }

    override fun getCategory(): Category? {
        return category
    }

    fun setTitle(newTitle: String) {
        validateTitle(newTitle)
        title = newTitle
    }

    fun setDescription(newDescription: String) {
        description = newDescription
    }

    fun updateScheduledTimeAndStatus(newScheduledTime: Long?, newStatus: TaskStatus) {
        validateScheduledTimeAndStatus(newScheduledTime, newStatus)
        scheduledTime = newScheduledTime
        status = newStatus
    }

    fun updateCategory(newCategory: Category?) {
        category = newCategory
    }

    fun setIsNotificationEnabled(enabled: Boolean) {
        isNotificationEnabled = enabled
    }

    fun setNotificationOffset(offset: Long) {
        notificationOffset = offset
    }

    fun setCompleted() {
        status = TaskStatus.Completed
    }

    fun setUncompleted() {
        status = if (scheduledTime == null) TaskStatus.Unscheduled else TaskStatus.Scheduled
    }

    private fun validateScheduledTimeAndStatus(scheduledTime: Long?, status: TaskStatus) {
        if (scheduledTime == null && status == TaskStatus.Scheduled) {
            throw IllegalArgumentException("Task without scheduledTime cannot be scheduled")
        }
        if (scheduledTime != null && status == TaskStatus.Unscheduled) {
            throw IllegalArgumentException("Task with scheduledTime cannot be unscheduled")
        }
    }

    private fun validateTitle(title: String) {
        if (title.isEmpty()) {
            throw IllegalArgumentException("Task title cannot be empty")
        }
    }
}