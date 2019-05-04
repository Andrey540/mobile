package ru.aegoshin.taskscheduler.application.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.aegoshin.taskscheduler.view.activity.TaskListActivity
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.view.model.TaskListViewModel

object ViewModelInjector {
    private val mTaskListPresenter = TaskSchedulerApplication.getTaskListPresenter()

    fun getTaskListViewModel(activity: TaskListActivity): TaskListViewModel {
        val provider = ViewModelProvider(activity, Factory)
        val viewModel = provider[TaskListViewModel::class.java]
        mTaskListPresenter.subscribe(viewModel)
        return viewModel
    }

    private object Factory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == TaskListViewModel::class.java) {
                return TaskListViewModel(mTaskListPresenter) as T
            }

            throw IllegalArgumentException(
                "ViewModel ${modelClass.canonicalName} not supported")
        }

    }
}