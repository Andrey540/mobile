package ru.aegoshin.infrastructure.entity

class Task {
    companion object {
        const val SCHEMA = "Task"
        const val ID_FIELD = "id"
        const val TITLE_FIELD = "title"
        const val DESCRIPTION_FIELD = "description"
        const val SCHEDULED_TIME_FIELD = "scheduled_time"
        const val STATUS_FIELD = "status"
        const val IS_NOTIFICATION_ENABLED_FIELD = "is_notification_enabled"
        const val NOTIFICATION_OFFSET_FIELD = "notification_offset"
    }
}