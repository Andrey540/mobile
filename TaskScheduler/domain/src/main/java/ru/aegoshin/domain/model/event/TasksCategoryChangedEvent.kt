package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.task.TaskId

class TasksCategoryChangedEvent(private val taskIds: List<TaskId>, private val categoryId: CategoryId?) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getTaskIds(): List<TaskId> {
        return taskIds
    }

    fun getCategoryId(): CategoryId? {
        return categoryId
    }

    companion object {
        const val TYPE = "tasks_category_changed"
    }
}