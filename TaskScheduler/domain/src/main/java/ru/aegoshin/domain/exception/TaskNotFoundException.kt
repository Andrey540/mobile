package ru.aegoshin.domain.exception

import ru.aegoshin.domain.model.task.TaskId

class TaskNotFoundException(taskId: TaskId) : DomainException("Can not find task with id $taskId")