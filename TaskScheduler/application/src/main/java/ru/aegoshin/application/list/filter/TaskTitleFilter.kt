package ru.aegoshin.application.list.filter

import ru.aegoshin.application.task.Task

class TaskTitleFilter : ITaskListFilter {
    private var mTitle: String? = null

    fun updateTitle(search: String?) {
        mTitle = search?.toLowerCase()
    }

    override fun filterTask(task: Task): Boolean {
        if (mTitle == null) {
            return true
        }
        return task.title.toLowerCase().indexOf(mTitle!!) != -1
    }
}