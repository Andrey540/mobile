package ru.aegoshin.domain.model.task

enum class TaskStatus(val status: Int) {
    Unscheduled(0),
    Scheduled(1),
    Completed(2);

    companion object {
        fun fromInt(status: Int) = TaskStatus.values().first { it.status == status }
    }
}