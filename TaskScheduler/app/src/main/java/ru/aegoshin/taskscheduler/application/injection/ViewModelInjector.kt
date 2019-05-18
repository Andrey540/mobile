package ru.aegoshin.taskscheduler.application.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.FragmentActivity
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.view.model.UnscheduledTaskListViewModel
import ru.aegoshin.taskscheduler.view.model.ScheduledTaskListViewModel

object ViewModelInjector {
    private val mScheduledTaskListPresenter = TaskSchedulerApplication.getScheduledTaskListPresenter()
    private val mUnscheduledTaskListPresenter = TaskSchedulerApplication.getUnscheduledTaskListPresenter()

    fun getScheduledTaskListViewModel(fragmentActivity: FragmentActivity): ScheduledTaskListViewModel {
        val provider = ViewModelProvider(fragmentActivity, Factory)
        val viewModel = provider[ScheduledTaskListViewModel::class.java]
        mScheduledTaskListPresenter.subscribe(viewModel)
        return viewModel
    }

    fun getUnscheduledTaskListViewModel(fragmentActivity: FragmentActivity): UnscheduledTaskListViewModel {
        val provider = ViewModelProvider(fragmentActivity, Factory)
        val viewModel = provider[UnscheduledTaskListViewModel::class.java]
        mUnscheduledTaskListPresenter.subscribe(viewModel)
        return viewModel
    }

    private object Factory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == ScheduledTaskListViewModel::class.java) {
                return ScheduledTaskListViewModel(mScheduledTaskListPresenter) as T
            }
            if (modelClass == UnscheduledTaskListViewModel::class.java) {
                return UnscheduledTaskListViewModel(mUnscheduledTaskListPresenter) as T
            }

            throw IllegalArgumentException(
                "ViewModel ${modelClass.canonicalName} not supported")
        }

    }
}