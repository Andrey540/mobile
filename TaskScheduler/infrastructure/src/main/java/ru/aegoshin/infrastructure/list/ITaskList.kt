package ru.aegoshin.infrastructure.list

import ru.aegoshin.infrastructure.task.Task

interface ITaskList {
    fun updateList(list: List<Task>)
}