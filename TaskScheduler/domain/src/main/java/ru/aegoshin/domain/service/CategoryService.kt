package ru.aegoshin.domain.service

import ru.aegoshin.domain.exception.CategoryNotFoundException
import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.event.*
import ru.aegoshin.domain.model.repository.ICategoryRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.TaskId

class CategoryService(
    private val categoryRepository: ICategoryRepository,
    private val taskRepository: ITaskRepository,
    private val taskService: ITaskService,
    private val eventDispatcher: IEventDispather
) : ICategoryService {
    override fun addCategory(
        title: String,
        taskIds: List<TaskId>
    ) {
        val category = Category(categoryRepository.nextId(), title)
        categoryRepository.addCategory(category)
        eventDispatcher.dispatch(CategoryAddedEvent(category.getId()))
    }

    override fun updateCategory(
        categoryId: CategoryId,
        title: String
    ) {
        val category = categoryRepository.findCategoryById(categoryId)
        category ?: throw CategoryNotFoundException(categoryId)
        if (category.getTitle() == title) {
            return
        }
        category.setTitle(title)
        categoryRepository.updateCategories(listOf(category))
        eventDispatcher.dispatch(CategoriesUpdatedEvent(listOf(category.getId())))
    }

    override fun removeCategories(categoryIds: List<CategoryId>) {
        if (categoryIds.isEmpty()) {
            return
        }
        categoryIds.forEach {
            val tasks = taskRepository.findTasksByCategory(it)
            taskService.changeTasksCategory(tasks.map { task -> task.getId() }, null)
        }
        categoryRepository.removeCategories(categoryIds)
        eventDispatcher.dispatch(CategoriesRemovedEvent(categoryIds))
    }
}