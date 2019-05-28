package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.category.CategoryId

class CategoriesUpdatedEvent(private val categoryIds: List<CategoryId>) : IDomainEvent {
    override fun getType(): String {
        return TYPE
    }

    fun getCategoryIds(): List<CategoryId> {
        return categoryIds
    }

    companion object {
        const val TYPE = "categories_updated"
    }
}