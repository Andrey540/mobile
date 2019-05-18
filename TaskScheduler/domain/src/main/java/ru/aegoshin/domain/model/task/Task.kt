package ru.aegoshin.domain.model.task

open class Task(
    private var id: TaskId,
    private var title: String,
    private var description: String,
    private var scheduledTime: Long?,
    private var status: TaskStatus,
    private var isNotificationEnabled: Boolean,
    private var notificationOffset: Long
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

    fun setTitle(newTitle: String) {
        validateTitle(newTitle)
        title = newTitle
        onUpdated()
    }

    fun setDescription(newDescription: String) {
        description = newDescription
        onUpdated()
    }

    fun updateScheduledTimeAndStatus(newScheduledTime: Long?, newStatus: TaskStatus) {
        validateScheduledTimeAndStatus(newScheduledTime, newStatus)
        scheduledTime = newScheduledTime
        status = newStatus
        onUpdated()
    }

    fun setIsNotificationEnabled(enabled: Boolean) {
        isNotificationEnabled = enabled
        onUpdated()
    }

    fun setNotificationOffset(offset: Long) {
        notificationOffset = offset
        onUpdated()
    }

    fun setCompleted() {
        status = TaskStatus.Completed
        onUpdated()
    }

    fun setUncompleted() {
        status = if (scheduledTime == null) TaskStatus.Unscheduled else TaskStatus.Scheduled
        onUpdated()
    }

    open fun onUpdated() {}

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