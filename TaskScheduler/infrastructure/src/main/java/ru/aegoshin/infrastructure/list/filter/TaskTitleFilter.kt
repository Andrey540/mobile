package ru.aegoshin.infrastructure.list.filter

import ru.aegoshin.infrastructure.task.Task

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