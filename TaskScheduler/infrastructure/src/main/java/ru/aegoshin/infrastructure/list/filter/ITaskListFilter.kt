package ru.aegoshin.infrastructure.list.filter

import ru.aegoshin.infrastructure.task.Task

interface ITaskListFilter {
    fun filterTask(task: Task): Boolean
}