package ru.aegoshin.application.list.filter

import ru.aegoshin.application.task.Task

interface ITaskListFilter {
    fun filterTask(task: Task): Boolean
}