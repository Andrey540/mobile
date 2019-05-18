package ru.aegoshin.taskscheduler.view.model

import android.arch.lifecycle.MutableLiveData
import ru.aegoshin.infrastructure.presenter.ITaskListPresenter

class ScheduledTaskListViewModel(
    taskListPresenter: ITaskListPresenter
) : TaskListViewModel(taskListPresenter) {
    val currentTab = MutableLiveData<ScheduledTaskListTab>()
    val dateInterval = MutableLiveData<DateIntervalViewModel>()
}