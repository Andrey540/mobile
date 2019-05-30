package ru.aegoshin.application.list.filter

import ru.aegoshin.application.task.Task
import ru.aegoshin.application.task.TaskStatus

class TaskStatusFilter : ITaskListFilter {
    private var mStatuses: List<TaskStatus>? = null

    fun updateStatuses(statuses: List<TaskStatus>?) {
        mStatuses = statuses
    }

    override fun filterTask(task: Task): Boolean {
        if (mStatuses == null) {
            return true
        }
        return mStatuses!!.indexOf(task.status) != -1
    }
}