package ru.aegoshin.infrastructure.migration.realm

import io.realm.*
import ru.aegoshin.infrastructure.repository.realm.TaskRepository

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
            val taskSchema = schema.create(TaskRepository.SCHEMA)

            taskSchema.addField(TaskRepository.ID_FIELD, String::class.java, FieldAttribute.PRIMARY_KEY)
            taskSchema.addField(TaskRepository.TITLE_FIELD, String::class.java, FieldAttribute.REQUIRED)
            taskSchema.addField(TaskRepository.DESCRIPTION_FIELD, String::class.java, FieldAttribute.REQUIRED)
            taskSchema.addField(TaskRepository.SCHEDULED_TIME_FIELD, Long::class.java, FieldAttribute.INDEXED).setNullable(TaskRepository.SCHEDULED_TIME_FIELD, true)
            taskSchema.addField(TaskRepository.STATUS_FIELD, Int::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
            taskSchema.addField(TaskRepository.IS_NOTIFICATION_ENABLED_FIELD, Boolean::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
            taskSchema.addField(TaskRepository.NOTIFICATION_OFFSET_FIELD, Long::class.java, FieldAttribute.REQUIRED, FieldAttribute.INDEXED)
        }
    }
}