package ru.aegoshin.application.presenter

import ru.aegoshin.application.list.ITaskList
import ru.aegoshin.application.task.TaskStatus

interface ITaskListPresenter {
    fun subscribe(subscriber: ITaskList)
    fun unsubscribe(subscriber: ITaskList)
    fun updateDateInterval(from: Long, to: Long)
    fun updateStatuses(statuses: List<TaskStatus>?)
    fun searchTasks(search: String?)
}