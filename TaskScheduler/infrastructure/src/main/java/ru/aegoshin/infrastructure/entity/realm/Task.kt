package ru.aegoshin.infrastructure.entity.realm

import io.realm.RealmObject
import ru.aegoshin.domain.model.task.Task as DomainTask

open class Task(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var scheduledTime: Long? = null,
    var status: Int = 0,
    var isNotificationEnabled: Boolean = false,
    var notificationOffset: Long = 0
) : RealmObject()