package ru.aegoshin.taskscheduler.view.model

import ru.aegoshin.infrastructure.presenter.ITaskListPresenter

class UnscheduledTaskListViewModel(
    taskListPresenter: ITaskListPresenter
) : TaskListViewModel(taskListPresenter) {
}