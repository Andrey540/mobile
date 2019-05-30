package ru.aegoshin.taskscheduler.view.model

import ru.aegoshin.application.presenter.ITaskListPresenter

class UnscheduledTaskListViewModel(
    taskListPresenter: ITaskListPresenter
) : TaskListViewModel(taskListPresenter) {
}