package ru.aegoshin.infrastructure.presenter

import ru.aegoshin.domain.model.event.*
import ru.aegoshin.domain.model.repository.IImmutableTaskRepository
import ru.aegoshin.domain.model.task.IImmutableTask
import ru.aegoshin.domain.model.task.TaskId
import ru.aegoshin.infrastructure.task.Task
import ru.aegoshin.domain.model.task.TaskStatus as DomainTaskStatus
import ru.aegoshin.infrastructure.task.TaskStatus
import ru.aegoshin.infrastructure.list.ITaskList
import ru.aegoshin.infrastructure.list.TaskList
import ru.aegoshin.infrastructure.list.filter.DateRangeFilter
import ru.aegoshin.infrastructure.list.filter.TaskStatusFilter
import ru.aegoshin.infrastructure.list.filter.TaskTitleFilter
import java.lang.RuntimeException

class TaskListPresenter(
    private val repository: IImmutableTaskRepository,
    private val list: TaskList,
    eventDispatcher: IEventDispather
) : ITaskListPresenter {
    private val mSubscribers = mutableListOf<ITaskList>()
    private val mTaskAddedEventHandler = TaskAddedEventHandler()
    private val mTasksRemovedEventHandler = TasksRemovedEventHandler()
    private val mTasksUpdatedEventHandler = TasksUpdatedEventHandler()
    private val mDateRangeFilter = DateRangeFilter()
    private val mStatusFilter = TaskStatusFilter()
    private val mTitleFilter = TaskTitleFilter()

    init {
        eventDispatcher.subscribe(mTaskAddedEventHandler)
        eventDispatcher.subscribe(mTasksRemovedEventHandler)
        eventDispatcher.subscribe(mTasksUpdatedEventHandler)
        list.addFilter(mDateRangeFilter)
        list.addFilter(mStatusFilter)
        list.addFilter(mTitleFilter)
    }

    override fun subscribe(subscriber: ITaskList) {
        if (mSubscribers.indexOf(subscriber) == -1) {
            mSubscribers.add(subscriber)
        }
        refresh(subscriber)
    }

    override fun unsubscribe(subscriber: ITaskList) {
        mSubscribers.remove(subscriber)
    }

    override fun updateDateInterval(from: Long, to: Long) {
        list.updateList(convertTasks(repository.findByDateInterval(from, to)))
        mDateRangeFilter.updateDataRange(from, to)
        updateListImpl(list.getList())
    }

    override fun updateStatuses(statuses: List<TaskStatus>?) {
        mStatusFilter.updateStatuses(statuses)
        updateListImpl(list.getList())
    }

    override fun searchTasks(search: String?) {
        mTitleFilter.updateTitle(search)
        updateListImpl(list.getList())
    }

    fun updateList(taskList: List<IImmutableTask>) {
        list.updateList(convertTasks(taskList))
        updateListImpl(list.getList())
    }

    private fun updateListImpl(list: List<Task>) {
        mSubscribers.forEach {
            it.updateList(list)
        }
    }

    private fun refresh(subscriber: ITaskList) {
        if (mSubscribers.indexOf(subscriber) != -1) {
            subscriber.updateList(list.getList())
        }
    }

    private fun convertTasks(taskList: List<IImmutableTask>): List<Task> {
        return taskList.map { convertTask(it) }
    }

    private fun convertTask(task: IImmutableTask): Task {
        return Task(
            task.getId().toString(),
            task.getTitle(),
            task.getDescription(),
            task.getScheduledTime(),
            TaskStatus.fromInt(task.getStatus().status),
            task.isNotificationEnabled(),
            task.getNotificationOffset()
        )
    }

    private fun convertTaskIds(taskList: List<TaskId>): List<String> {
        return taskList.map { convertTaskId(it) }
    }

    private fun convertTaskId(taskId: TaskId): String {
        return taskId.toString()
    }

    inner class TaskAddedEventHandler : IDomainEventHandler {
        override fun handle(event: IDomainEvent) {
            val taskId = (event as TaskAddedEvent).getTaskId()
            val task = repository.findTaskById(taskId)
            task ?: throw RuntimeException("Cannot find task data by id: $taskId")
            list.addTask(convertTask(task))
            updateListImpl(list.getList())
        }

        override fun isSubscribedToEvent(event: IDomainEvent): Boolean {
            return event.getType() === TaskAddedEvent.TYPE
        }
    }

    inner class TasksRemovedEventHandler : IDomainEventHandler {
        override fun handle(event: IDomainEvent) {
            list.removeTasks(convertTaskIds((event as TasksRemovedEvent).getTaskIds()))
            updateListImpl(list.getList())
        }

        override fun isSubscribedToEvent(event: IDomainEvent): Boolean {
            return event.getType() === TasksRemovedEvent.TYPE
        }
    }

    inner class TasksUpdatedEventHandler : IDomainEventHandler {
        override fun handle(event: IDomainEvent) {
            val taskIds = (event as TasksUpdatedEvent).getTaskIds()
            val tasks = repository.findTasksByIds(taskIds)
            list.updateTasks(convertTasks(tasks))
            updateListImpl(list.getList())
        }

        override fun isSubscribedToEvent(event: IDomainEvent): Boolean {
            return event.getType() === TasksUpdatedEvent.TYPE
        }
    }
}