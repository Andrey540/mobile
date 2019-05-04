package ru.aegoshin.domain.model.task

import java.util.*

class TaskId(private val id: UUID) {
    override fun toString(): String {
        return id.toString()
    }

    fun equalTo(taskId: TaskId): Boolean {
        return toString() == taskId.toString()
    }

    companion object {
        fun stringToUuid(id: String): UUID {
            return UUID.fromString(id)
        }
    }
}