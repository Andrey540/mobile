package ru.aegoshin.taskscheduler.view.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_scheduled_task_list.view.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.application.injection.ViewModelInjector
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter
import ru.aegoshin.taskscheduler.view.listener.OnSwipeTouchListener
import ru.aegoshin.taskscheduler.view.model.DateIntervalViewModel
import ru.aegoshin.taskscheduler.view.model.ScheduledTaskListViewModel
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.widget.LinearLayout
import ru.aegoshin.application.task.TaskStatus
import android.support.v4.content.ContextCompat
import ru.aegoshin.taskscheduler.view.model.ScheduledTaskListTab


class ScheduledTaskListFragment : Fragment(), ITaskListFragment {
    private var listener: OnScheduledTaskListFragmentInteractionListener? = null
    private val mTaskListPresenter = TaskSchedulerApplication.getScheduledTaskListPresenter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mTaskListAdapter: TaskListViewRecyclerAdapter
    private lateinit var mViewModel: ScheduledTaskListViewModel
    private lateinit var mView: View

    fun updateDateInterval(dateInterval: DateIntervalViewModel) {
        mViewModel.dateInterval.value = dateInterval
        mTaskListPresenter.updateDateInterval(dateInterval.from, dateInterval.to)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_scheduled_task_list, container, false)

        val swipeTouchListener = getSwipeTouchListener()
        mView.taskList.setOnTouchListener(swipeTouchListener)
        mTaskListAdapter = TaskListViewRecyclerAdapter({ task -> taskListItemClicked(task) }, swipeTouchListener)
        linearLayoutManager = LinearLayoutManager(context)
        mView.taskList.layoutManager = linearLayoutManager
        mView.taskList.adapter = mTaskListAdapter

        val context = activity!!.applicationContext
        val layout = LinearLayout(context)
        layout.addView(mView)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mView.currentDateView.setOnClickListener { onChangeDate() }
        mView.btnPrevInterval.setOnClickListener { onPrevIntervalSelected() }
        mView.btnNextInterval.setOnClickListener { onNextIntervalSelected() }

        initTabView()

        mViewModel = ViewModelInjector.getScheduledTaskListViewModel(activity!!)
        mViewModel.taskList.observe(this, Observer { taskList: List<TaskViewModel>? ->
            if (taskList !== null) {
                mTaskListAdapter.updateItems(taskList.toList())
            }
        })
        mViewModel.dateInterval.observe(this, Observer { dateInterval: DateIntervalViewModel? ->
            if (dateInterval !== null) {
                updateDateIntervalView(dateInterval)
            }
        })
        mViewModel.currentTab.observe(this, Observer { tab: ScheduledTaskListTab? ->
            if (tab !== null) {
                updateSelectedTab(tab)
            }
        })
        if (mViewModel.dateInterval.value == null) {
            initStartDateInterval()
        }
        if (mViewModel.currentTab.value == null) {
            mViewModel.currentTab.value = ScheduledTaskListTab.All
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnScheduledTaskListFragmentInteractionListener) {
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

    private fun initTabView() {
        mView.tabLayoutView.addOnTabSelectedListener(getOnTabSelectedListener())

        val tabCount = mView.tabLayoutView.tabCount
        for (i in 0 until tabCount) {
            val tabView = mView.tabLayoutView.getTabAt(i)
            setUnselectedTabColor(tabView!!)
        }

        val tabView = mView.tabLayoutView.getTabAt(mView.tabLayoutView.selectedTabPosition)!!
        setSelectedTabColor(tabView)
    }

    private fun onChangeDate() {
        listener?.onShowDateRangeCalendar(mViewModel.dateInterval.value!!.from, mViewModel.dateInterval.value!!.to)
    }

    private fun taskListItemClicked(task: TaskViewModel) {
        listener?.onEditTaskListItem(task)
    }

    private fun getOnTabSelectedListener(): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mViewModel.currentTab.value = ScheduledTaskListTab.fromInt(tab.position)
                setSelectedTabColor(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setUnselectedTabColor(tab)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        }
    }

    private fun setSelectedTabColor(tab: TabLayout.Tab) {
        val tabIconColor = ContextCompat.getColor(context!!, R.color.selectedTabColor)
        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
    }

    private fun setUnselectedTabColor(tab: TabLayout.Tab) {
        val tabIconColor = ContextCompat.getColor(context!!, R.color.unselectedTabColor)
        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
    }

    private fun getSwipeTouchListener(): OnSwipeTouchListener {
        return object : OnSwipeTouchListener(context!!) {
            override fun onSwipeLeft() {
                onNextIntervalSelected()
            }

            override fun onSwipeRight() {
                onPrevIntervalSelected()
            }
        }
    }

    private fun onNextIntervalSelected() {
        val interval = mViewModel.dateInterval.value!!
        updateDate(shiftDateInterval(interval, getDateIntervalDiff(interval)))
    }

    private fun onPrevIntervalSelected() {
        val interval = mViewModel.dateInterval.value!!
        updateDate(shiftDateInterval(interval, -getDateIntervalDiff(interval)))
    }

    private fun initStartDateInterval() {
        val calendar = Calendar.getInstance()
        val from = getDayFromDate(calendar.timeInMillis)
        calendar.timeInMillis = from
        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MINUTE, -1)
        updateDate(DateIntervalViewModel(from, calendar.timeInMillis))
    }

    private fun shiftDateInterval(dateInterval: DateIntervalViewModel, amount: Int): DateIntervalViewModel {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInterval.from
        calendar.add(Calendar.DATE, amount)

        val from = calendar.timeInMillis

        calendar.timeInMillis = dateInterval.to
        calendar.add(Calendar.DATE, amount)

        return DateIntervalViewModel(from, calendar.timeInMillis)
    }

    private fun updateDate(dateInterval: DateIntervalViewModel) {
        mViewModel.dateInterval.value = dateInterval
        mTaskListPresenter.updateDateInterval(dateInterval.from, dateInterval.to)
    }

    private fun updateSelectedTab(tab: ScheduledTaskListTab) {
        when (tab) {
            ScheduledTaskListTab.All -> mTaskListPresenter.updateStatuses(null)
            ScheduledTaskListTab.Completed -> mTaskListPresenter.updateStatuses(listOf(TaskStatus.Completed))
            ScheduledTaskListTab.Uncompleted -> mTaskListPresenter.updateStatuses(listOf(TaskStatus.Scheduled))
        }
        linearLayoutManager.scrollToPosition(0)
        val tabView = mView.tabLayoutView.getTabAt(tab.tab)
        tabView!!.select()
    }

    private fun updateDateIntervalView(dateInterval: DateIntervalViewModel) {
        var text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date(dateInterval.from))

        val fromDay = getDayFromDate(dateInterval.from)
        val toDay = getDayFromDate(dateInterval.to)
        if (fromDay != toDay) {
            text += SimpleDateFormat(" - d MMMM", Locale.getDefault()).format(Date(dateInterval.to))
        }
        mView.currentDateView.text = text
    }

    private fun getDayFromDate(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getDateIntervalDiff(dateInterval: DateIntervalViewModel): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInterval.from
        val from = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = dateInterval.to
        val to = calendar.get(Calendar.DAY_OF_YEAR)
        return to - from + 1
    }

    interface OnScheduledTaskListFragmentInteractionListener {
        fun onShowDateRangeCalendar(from: Long, to: Long)
        fun onEditTaskListItem(task: TaskViewModel)
    }
}
