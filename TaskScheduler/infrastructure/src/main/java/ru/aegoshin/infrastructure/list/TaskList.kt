package ru.aegoshin.infrastructure.list

import ru.aegoshin.infrastructure.list.filter.ITaskListFilter
import ru.aegoshin.infrastructure.task.Task

class TaskList : ITaskList {
    private var mTasks = mutableListOf<Task>()
    private var mFilters = mutableListOf<ITaskListFilter>()

    override fun updateList(list: List<Task>) {
        mTasks = list.toMutableList()
        mTasks.sortBy { it.scheduledTime }
    }

    fun addFilter(filter: ITaskListFilter) {
        mFilters.add(filter)
    }

    fun getList(): List<Task> {
        return filterList(mTasks)
    }

    fun addTask(task: Task) {
        mTasks.add(task)
        mTasks.sortBy { it.scheduledTime }
    }

    fun removeTasks(taskIds: List<String>) {
        val removingTasks = mTasks.filter { taskIds.indexOf(it.id) != -1 }
        mTasks.removeAll(removingTasks)
    }

    fun updateTasks(tasks: List<Task>) {
        tasks.forEach { newTask ->
            val oldTask = mTasks.find { it.id == newTask.id }
            if (oldTask !== null) {
                val index = mTasks.indexOf(oldTask)
                mTasks[index] = newTask
            }
        }
        val taskIds = mTasks.map { it.id }
        val newTasks = tasks.filter { taskIds.indexOf(it.id) == -1 }
        mTasks.addAll(newTasks)
    }

    private fun filterList(tasks: List<Task>): List<Task> {
        return tasks.filter { filterTask(it) }
    }

    private fun filterTask(task: Task): Boolean {
        mFilters.forEach {
            if (!it.filterTask(task)) {
                return false
            }
        }
        return true
    }
}