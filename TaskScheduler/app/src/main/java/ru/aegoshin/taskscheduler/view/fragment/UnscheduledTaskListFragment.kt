package ru.aegoshin.taskscheduler.view.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_scheduled_task_list.view.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.injection.ViewModelInjector
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import android.widget.LinearLayout
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.view.listener.OnSwipeRecyclerViewListener
import ru.aegoshin.taskscheduler.view.model.UnscheduledTaskListViewModel

class UnscheduledTaskListFragment : Fragment(), ITaskListFragment {
    private var listener: OnUnscheduledTaskListFragmentInteractionListener? = null
    private val mTaskListPresenter = TaskSchedulerApplication.getUnscheduledTaskListPresenter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mTaskListAdapter: TaskListViewRecyclerAdapter
    private lateinit var mViewModel: UnscheduledTaskListViewModel
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_unscheduled_task_list, container, false)

        mTaskListAdapter = TaskListViewRecyclerAdapter(
            { task -> listener?.onEditTaskListItem(task) },
            { task -> listener?.onDeleteTaskListItem(task) },
            null
        )
        linearLayoutManager = LinearLayoutManager(context)
        mView.taskList.layoutManager = linearLayoutManager
        mView.taskList.adapter = mTaskListAdapter

        val context = activity!!.applicationContext
        val layout = LinearLayout(context)
        layout.addView(mView)

        val swipeRecyclerViewListener =
            OnSwipeRecyclerViewListener(context!!, mTaskListAdapter, 0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
        val itemTouchHelper = ItemTouchHelper(swipeRecyclerViewListener)
        itemTouchHelper.attachToRecyclerView(mView.taskList)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = ViewModelInjector.getUnscheduledTaskListViewModel(activity!!)
        mViewModel.taskList.observe(this, Observer { taskList: List<TaskViewModel>? ->
            if (taskList !== null) {
                mTaskListAdapter.updateItems(taskList.toList())
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUnscheduledTaskListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnScheduledTaskListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun selectAllItems() {
        mTaskListAdapter.selectAll()
    }

    override fun unSelectAllItems() {
        mTaskListAdapter.unSelectAll()
    }

    override fun getSelectedIds(): List<String> {
        return mViewModel.getSelectedIds()
    }

    override fun searchTasks(search: String?) {
        mTaskListPresenter.searchTasks(search)
    }

    interface OnUnscheduledTaskListFragmentInteractionListener {
        fun onEditTaskListItem(task: TaskViewModel)
        fun onDeleteTaskListItem(task: TaskViewModel)
    }
}
