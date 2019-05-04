package ru.aegoshin.taskscheduler.view.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.infrastructure.list.ITaskList
import ru.aegoshin.infrastructure.presenter.ITaskListPresenter

class TaskListViewModel(
    private val taskListPresenter: ITaskListPresenter
) : ViewModel(), ITaskList {
    val taskList = MutableLiveData<List<TaskViewModel>>()
    val dateInterval = MutableLiveData<DateIntervalViewModel>()

    fun getSelectedIds(): List<String> {
        if (taskList.value !== null) {
            return taskList.value!!.filter { it.selected }.map { it.id }
        }
        return listOf()
    }

    override fun updateList(list: List<Task>) {
        val selectedIds = getSelectedIds()
        val newList = createTaskList(list)
        newList.forEach { it.selected = (selectedIds.indexOf(it.id) != -1) }
        taskList.value = newList
    }

    override fun onCleared() {
        taskListPresenter.unsubscribe(this)
    }

    private fun createTaskList(taskList: List<Task>): List<TaskViewModel> {
        return taskList.map { convertTask(it) }
    }

    private fun convertTask(task: Task): TaskViewModel {
        return TaskViewModel(
            false,
            task.id,
            task.title,
            task.description,
            task.scheduledTime,
            task.status,
            task.needNotify,
            task.notifyBefore
        )
    }
}