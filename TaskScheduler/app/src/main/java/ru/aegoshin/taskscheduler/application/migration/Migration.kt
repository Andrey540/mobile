package ru.aegoshin.taskscheduler.application.migration

import io.realm.*
import ru.aegoshin.infrastructure.entity.Task

class Migration : RealmMigration {

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema

        /************************************************
        // Version 0
        class Task
        @Required
        @PrimaryKey
        private uid: String
        @Required
        private name: String
        private description: String
        private status: Int
        private isNotificationEnabled: Boolean
        private notificationOffset: Long
        @Optional
        private Long? scheduledTime
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion <= (0).toLong()) {
            val taskSchema = schema.create(Task.SCHEMA)

            taskSchema.addField(Task.ID_FIELD, String::class.java, FieldAttribute.PRIMARY_KEY)
            taskSchema.addField(Task.TITLE_FIELD, String::class.java, FieldAttribute.REQUIRED)
            taskSchema.addField(Task.DESCRIPTION_FIELD, String::class.java, FieldAttribute.REQUIRED)
            taskSchema.addField(Task.SCHEDULED_TIME_FIELD, Long::class.java, FieldAttribute.INDEXED).setNullable(Task.SCHEDULED_TIME_FIELD, true)
            taskSchema.addField(Task.STATUS_FIELD, Int::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
            taskSchema.addField(Task.IS_NOTIFICATION_ENABLED_FIELD, Boolean::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
            taskSchema.addField(Task.NOTIFICATION_OFFSET_FIELD, Long::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
        }
    }
}