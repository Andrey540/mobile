package ru.aegoshin.infrastructure.repository.realm

import io.realm.Realm
import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.category.IImmutableCategory
import ru.aegoshin.domain.model.repository.ICategoryRepository
import ru.aegoshin.domain.model.repository.IImmutableCategoryRepository
import ru.aegoshin.infrastructure.entity.realm.Task as TaskEntity
import java.util.*

class CategoryRepository(
    private val realm: Realm
) : ICategoryRepository, IImmutableCategoryRepository {
    override fun nextId(): CategoryId {
        return CategoryId(UUID.randomUUID())
    }

    override fun addCategory(category: Category) {
        //TODO
    }

    override fun removeCategories(categoryIds: List<CategoryId>) {
        //TODO
    }

    override fun updateCategories(categoryIds: List<Category>) {
        //TODO
    }

    override fun findCategoryById(categoryId: CategoryId): Category? {
        //TODO
        return null
    }

    override fun findAll(): List<IImmutableCategory> {
        //TODO
        return listOf()
    }
}