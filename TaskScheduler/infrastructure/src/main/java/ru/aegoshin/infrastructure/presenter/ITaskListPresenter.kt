package ru.aegoshin.infrastructure.presenter

import ru.aegoshin.infrastructure.list.ITaskList
import ru.aegoshin.infrastructure.task.TaskStatus

interface ITaskListPresenter {
    fun subscribe(subscriber: ITaskList)
    fun unsubscribe(subscriber: ITaskList)
    fun updateDateInterval(from: Long, to: Long)
    fun updateStatuses(statuses: List<TaskStatus>?)
    fun searchTasks(search: String?)
}