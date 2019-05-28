package ru.aegoshin.infrastructure.repository.realm

import io.realm.DynamicRealm
import io.realm.DynamicRealmObject
import io.realm.RealmQuery
import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.category.IImmutableCategory
import ru.aegoshin.domain.model.repository.ICategoryRepository
import ru.aegoshin.domain.model.repository.IImmutableCategoryRepository
import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.infrastructure.entity.Task as TaskEntity
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus
import java.lang.RuntimeException
import java.util.*

class CategoryRepository(
    private val realm: DynamicRealm
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