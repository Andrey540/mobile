package ru.aegoshin.domain.model.repository

import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.category.IImmutableCategory

interface IImmutableCategoryRepository {
    fun findCategoryById(categoryId: CategoryId): IImmutableCategory?
    fun findAll(): List<IImmutableCategory>
}