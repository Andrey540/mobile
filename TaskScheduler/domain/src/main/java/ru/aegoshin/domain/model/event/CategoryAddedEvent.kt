package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.category.CategoryId

class CategoryAddedEvent(private val categoryId: CategoryId) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getCategoryId(): CategoryId {
        return categoryId
    }

    companion object {
        const val TYPE = "category_added"
    }
}