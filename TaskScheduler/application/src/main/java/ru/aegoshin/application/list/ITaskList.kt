package ru.aegoshin.application.list

import ru.aegoshin.application.task.Task

interface ITaskList {
    fun updateList(list: List<Task>)
}