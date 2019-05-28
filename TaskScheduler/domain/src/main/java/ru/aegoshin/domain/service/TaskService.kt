package ru.aegoshin.domain.service

import ru.aegoshin.domain.exception.CategoryNotFoundException
import ru.aegoshin.domain.exception.TaskNotFoundException
import ru.aegoshin.domain.model.category.Category
import ru.aegoshin.domain.model.category.CategoryId
import ru.aegoshin.domain.model.event.*
import ru.aegoshin.domain.model.repository.ICategoryRepository
import ru.aegoshin.domain.model.repository.ITaskRepository
import ru.aegoshin.domain.model.task.Task
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.domain.model.task.TaskStatus

class TaskService(
    private val taskRepository: ITaskRepository,
    private val categoryRepository: ICategoryRepository,
    private val eventDispatcher: IEventDispather
) : ITaskService {
    override fun addTask(
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long,
        categoryId: CategoryId?
    ) {
        val category = getCategoryById(categoryId)
        val task = Task(
            taskRepository.nextId(),
            title,
            description,
            scheduledTime,
            status,
            isNotificationEnabled,
            notificationOffset,
            category
        )
        taskRepository.addTask(task)
        eventDispatcher.dispatch(TaskAddedEvent(task.getId()))
    }

    override fun updateTask(
        taskId: TaskId,
        title: String,
        description: String,
        scheduledTime: Long?,
        status: TaskStatus,
        isNotificationEnabled: Boolean,
        notificationOffset: Long,
        categoryId: CategoryId?
    ) {
        val task = taskRepository.findTaskById(taskId)
        task ?: throw TaskNotFoundException(taskId)
        task.setTitle(title)
        task.setDescription(description)
        task.updateScheduledTimeAndStatus(scheduledTime, status)
        task.setIsNotificationEnabled(isNotificationEnabled)
        task.setNotificationOffset(notificationOffset)

        val taskCategoryId = task.getCategory()?.getId()
        val categoryChanged = (categoryId == null && taskCategoryId != null) ||
                (categoryId != null && taskCategoryId == null) ||
                (categoryId != null && taskCategoryId != null && !categoryId.equalTo(taskCategoryId))

        val category = getCategoryById(categoryId)
        task.updateCategory(category)

        taskRepository.updateTasks(listOf(task))
        eventDispatcher.dispatch(TasksUpdatedEvent(listOf(task.getId())))
        if (categoryChanged) {
            eventDispatcher.dispatch(TasksCategoryChangedEvent(listOf(task.getId()), categoryId))
        }
    }

    override fun removeTasks(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        taskRepository.removeTasks(taskIds)
        eventDispatcher.dispatch(TasksRemovedEvent(taskIds))
    }

    override fun changeTasksStatusToCompleted(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = taskRepository.findTasksByIds(taskIds)
        tasks.forEach { it.setCompleted() }
        taskRepository.updateTasks(tasks)
        eventDispatcher.dispatch(TasksUpdatedEvent(taskIds))
    }

    override fun changeTasksStatusToUncompleted(taskIds: List<TaskId>) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = taskRepository.findTasksByIds(taskIds)
        tasks.forEach { it.setUncompleted() }
        taskRepository.updateTasks(tasks)
        eventDispatcher.dispatch(TasksUpdatedEvent(taskIds))
    }

    override fun changeTasksCategory(taskIds: List<TaskId>, categoryId: CategoryId?) {
        if (taskIds.isEmpty()) {
            return
        }
        val tasks = taskRepository.findTasksByIds(taskIds)
        val category = getCategoryById(categoryId)
        tasks.forEach { it.updateCategory(category) }
        taskRepository.updateTasks(tasks)
        eventDispatcher.dispatch(TasksCategoryChangedEvent(taskIds, categoryId))
    }

    private fun getCategoryById(categoryId: CategoryId?): Category? {
        var result: Category? = null
        if (categoryId != null) {
            result = categoryRepository.findCategoryById(categoryId)
            result ?: throw CategoryNotFoundException(categoryId)
        }
        return result
    }
}