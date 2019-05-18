package ru.aegoshin.infrastructure.entity

import io.realm.DynamicRealmObject
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

class Task(
    private val obj: DynamicRealmObject
) : Task(
    TaskId(TaskId.stringToUuid(obj.getString(ID_FIELD))),
    obj.getString(TITLE_FIELD),
    obj.getString(DESCRIPTION_FIELD),
    if (obj.isNull(SCHEDULED_TIME_FIELD)) null else obj.getLong(SCHEDULED_TIME_FIELD),
    TaskStatus.fromInt(obj.getInt(STATUS_FIELD)),
    obj.getBoolean(IS_NOTIFICATION_ENABLED_FIELD),
    obj.getLong(NOTIFICATION_OFFSET_FIELD)
) {
    override fun onUpdated() {
        obj.setString(TITLE_FIELD, getTitle())
        obj.setString(DESCRIPTION_FIELD, getDescription())
        if (getScheduledTime() == null) {
            obj.setNull(SCHEDULED_TIME_FIELD)
        } else {
            obj.setLong(SCHEDULED_TIME_FIELD, getScheduledTime()!!)
        }
        obj.setInt(STATUS_FIELD, getStatus().status)
        obj.setBoolean(IS_NOTIFICATION_ENABLED_FIELD, isNotificationEnabled())
        obj.setLong(NOTIFICATION_OFFSET_FIELD, getNotificationOffset())
    }

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