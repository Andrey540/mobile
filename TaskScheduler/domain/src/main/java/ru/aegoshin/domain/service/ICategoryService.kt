package ru.aegoshin.domain.service

import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.task.TaskId

interface ICategoryService {
    fun addCategory(
        title: String,
        taskIds: List<TaskId>
    )
    fun updateCategory(
        categoryId: CategoryId,
        title: String
    )
    fun removeCategories(categoryIds: List<CategoryId>)
}