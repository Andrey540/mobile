package ru.aegoshin.taskscheduler.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.app.NotificationManager
import android.app.NotificationChannel

import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.content_task_list.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.application.injection.ViewModelInjector
import ru.aegoshin.taskscheduler.view.adapter.TaskListViewRecyclerAdapter
import ru.aegoshin.taskscheduler.view.model.TaskListViewModel
import android.view.Menu
import android.view.MenuItem
import ru.aegoshin.daterangecalendar.DateRangeCalendarDialog
import ru.aegoshin.taskscheduler.view.listener.OnSwipeTouchListener
import ru.aegoshin.taskscheduler.view.model.DateIntervalViewModel
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import ru.aegoshin.taskscheduler.application.receiver.TaskNotificationReceiver


class TaskListActivity : LocaliseActivity() {
    private val mTaskListPresenter = TaskSchedulerApplication.getTaskListPresenter()
    private val mTaskService = TaskSchedulerApplication.getTaskService()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mTaskListAdapter: TaskListViewRecyclerAdapter
    private lateinit var mViewModel: TaskListViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar!!.title = getString(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportActionBar!!.title = getString(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportActionBar!!.title = getString(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.DATE, Calendar.getInstance().timeInMillis)
            startActivity(intent)
        }
        currentDateView.setOnClickListener { onChangeDate() }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val swipeTouchListener = getSwipeTouchListener()
        taskList.setOnTouchListener(swipeTouchListener)
        mTaskListAdapter = TaskListViewRecyclerAdapter({ task -> taskListItemClicked(task) }, swipeTouchListener)
        linearLayoutManager = LinearLayoutManager(this)
        taskList.layoutManager = linearLayoutManager
        taskList.adapter = mTaskListAdapter

        mViewModel = ViewModelInjector.getTaskListViewModel(this)
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
        if (mViewModel.dateInterval.value == null) {
            initStartDateInterval()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val notEmptySelection = mViewModel.getSelectedIds().isNotEmpty()
        menu.findItem(R.id.task_list_item_remove).setVisible(notEmptySelection)
        menu.findItem(R.id.task_list_item_mark_as_completed).setVisible(notEmptySelection)
        menu.findItem(R.id.task_list_item_mark_as_uncompleted).setVisible(notEmptySelection)
        menu.findItem(R.id.task_list_unselect_all).setVisible(notEmptySelection)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.task_list_item_remove -> {
                mTaskService.removeTasks(mViewModel.getSelectedIds())
                return true
            }
            R.id.task_list_change_date -> {
                onChangeDate()
                return true
            }
            R.id.task_list_item_mark_as_completed -> {
                mTaskService.changeTasksStatusToCompleted(mViewModel.getSelectedIds())
                return true
            }
            R.id.task_list_item_mark_as_uncompleted -> {
                mTaskService.changeTasksStatusToUncompleted(mViewModel.getSelectedIds())
                return true
            }
            R.id.task_list_select_all -> {
                mTaskListAdapter.selectAll()
                return true
            }
            R.id.task_list_unselect_all -> {
                mTaskListAdapter.unSelectAll()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSwipeTouchListener(): OnSwipeTouchListener {
        return object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                val interval = mViewModel.dateInterval.value!!
                updateDate(shiftDateInterval(interval, getDateIntervalDiff(interval)))
            }

            override fun onSwipeRight() {
                val interval = mViewModel.dateInterval.value!!
                updateDate(shiftDateInterval(interval, -getDateIntervalDiff(interval)))
            }
        }
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

    private fun taskListItemClicked(task: TaskViewModel) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)
        startActivity(intent)
    }

    private fun getOnDateChangedCallback(): DateRangeCalendarDialog.Callback {
        return object : DateRangeCalendarDialog.Callback {
            override fun onCancelled() {}

            override fun onDataSelected(firstDate: Calendar?, secondDate: Calendar?) {
                if (firstDate != null) {
                    if (secondDate == null) {
                        val from = getDayFromDate(firstDate.timeInMillis)
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = from
                        calendar.add(Calendar.DATE, 1)
                        calendar.add(Calendar.MINUTE, -1)
                        updateDate(DateIntervalViewModel(from, calendar.timeInMillis))
                    } else {
                        val from = getDayFromDate(firstDate.timeInMillis)
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = getDayFromDate(secondDate.timeInMillis)
                        calendar.add(Calendar.DATE, 1)
                        calendar.add(Calendar.MINUTE, -1)
                        updateDate(DateIntervalViewModel(from, calendar.timeInMillis))
                    }
                }
            }
        }
    }

    private fun initStartDateInterval() {
        val calendar = Calendar.getInstance()
        val from = getDayFromDate(calendar.timeInMillis)
        calendar.timeInMillis = from
        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MINUTE, -1)
        updateDate(DateIntervalViewModel(from, calendar.timeInMillis))
    }

    private fun onChangeDate() {
        val dialog = DateRangeCalendarDialog()
            .setSingle(false)
            .setCallback(getOnDateChangedCallback())
            .setStartDate(Date(mViewModel.dateInterval.value!!.from))
            .setEndDate(Date(mViewModel.dateInterval.value!!.to))
        dialog.show(supportFragmentManager, "fragment_alert")
    }

    private fun updateDate(dateInterval: DateIntervalViewModel) {
        mViewModel.dateInterval.value = dateInterval
        mTaskListPresenter.updateDateInterval(dateInterval.from, dateInterval.to)
    }

    private fun updateDateIntervalView(dateInterval: DateIntervalViewModel) {
        var text = SimpleDateFormat("d MMMM", Locale.getDefault()).format(Date(dateInterval.from))

        val fromDay = getDayFromDate(dateInterval.from)
        val toDay = getDayFromDate(dateInterval.to)
        if (fromDay != toDay) {
            text += SimpleDateFormat(" - d MMMM", Locale.getDefault()).format(Date(dateInterval.to))
        }
        currentDateView.text = text
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
}
