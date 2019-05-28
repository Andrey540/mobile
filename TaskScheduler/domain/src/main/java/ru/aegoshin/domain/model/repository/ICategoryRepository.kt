package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId

interface ICategoryRepository {
    fun nextId(): CategoryId
    fun addCategory(category: Category)
    fun removeCategories(categoryIds: List<CategoryId>)
    fun updateCategories(categoryIds: List<Category>)
    fun findCategoryById(categoryId: CategoryId): Category?
}