package ru.aegoshin.taskscheduler.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment

import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.activity_task_list.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.SearchView
import ru.aegoshin.daterangecalendar.DateRangeCalendarDialog
import ru.aegoshin.taskscheduler.view.fragment.UnscheduledTaskListFragment
import ru.aegoshin.taskscheduler.view.fragment.ITaskListFragment
import ru.aegoshin.taskscheduler.view.model.DateIntervalViewModel
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import java.util.*
import ru.aegoshin.taskscheduler.view.fragment.ScheduledTaskListFragment

class TaskListActivity : LocalisedActivity(), ScheduledTaskListFragment.OnScheduledTaskListFragmentInteractionListener,
    UnscheduledTaskListFragment.OnUnscheduledTaskListFragmentInteractionListener {
    companion object {
        private const val FRAGMENT = "fragment"
    }

    private val mTaskService = TaskSchedulerApplication.getTaskService()
    private var mActiveListFragment: ITaskListFragment? = null
    private val mScheduledTaskListFragment = ScheduledTaskListFragment()
    private val mUnscheduledTaskListFragment = UnscheduledTaskListFragment()
    private val mFragments = listOf<ITaskListFragment>(mScheduledTaskListFragment, mUnscheduledTaskListFragment)
    private var mMenu: Menu? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar!!.title = getString(R.string.title_home)
                switchToFragment(mScheduledTaskListFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportActionBar!!.title = getString(R.string.title_inbox)
                switchToFragment(mUnscheduledTaskListFragment)
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
            if (mActiveListFragment === mScheduledTaskListFragment) {
                intent.putExtra(TaskActivity.DATE, Calendar.getInstance().timeInMillis)
            }
            startActivity(intent)
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.task_list, mScheduledTaskListFragment)
        ft.commit()
        mActiveListFragment = mScheduledTaskListFragment

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putInt(FRAGMENT, mFragments.indexOf(mActiveListFragment))
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val fragmentIndex = savedInstanceState?.getInt(FRAGMENT)
        if (fragmentIndex != null) {
            switchToFragment(mFragments[fragmentIndex])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        menuInflater.inflate(R.menu.search, menu)

        mMenu = menu

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(getSearchListener())
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val isUnschedulerFragmentActive = mActiveListFragment === mUnscheduledTaskListFragment
        val notEmptySelection = mActiveListFragment!!.getSelectedIds().isNotEmpty()
        menu.findItem(R.id.task_list_item_remove).setVisible(notEmptySelection)
        menu.findItem(R.id.task_list_item_mark_as_completed).setVisible(notEmptySelection && !isUnschedulerFragmentActive)
        menu.findItem(R.id.task_list_item_mark_as_uncompleted).setVisible(notEmptySelection && !isUnschedulerFragmentActive)
        menu.findItem(R.id.task_list_unselect_all).setVisible(notEmptySelection)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.task_list_item_remove -> {
                mTaskService.removeTasks(mActiveListFragment!!.getSelectedIds())
            }
            R.id.task_list_item_mark_as_completed -> {
                mTaskService.changeTasksStatusToCompleted(mActiveListFragment!!.getSelectedIds())
            }
            R.id.task_list_item_mark_as_uncompleted -> {
                mTaskService.changeTasksStatusToUncompleted(mActiveListFragment!!.getSelectedIds())
            }
            R.id.task_list_select_all -> {
                mActiveListFragment!!.selectAllItems()
            }
            R.id.task_list_unselect_all -> {
                mActiveListFragment!!.unSelectAllItems()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onShowDateRangeCalendar(from: Long, to: Long) {
        DateRangeCalendarDialog()
            .setSingle(false)
            .setCallback(getOnDateChangedCallback())
            .setStartDate(Date(from))
            .setEndDate(Date(to))
            .show(supportFragmentManager, "date_range_fragment")
    }

    override fun onEditTaskListItem(task: TaskViewModel) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)
        startActivity(intent)
    }

    override fun onDeleteTaskListItem(task: TaskViewModel) {
        mTaskService.removeTasks(listOf(task.id))
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

    private fun getSearchListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mActiveListFragment?.searchTasks(newText)
                return true
            }
        }
    }

    private fun updateDate(dateInterval: DateIntervalViewModel) {
        if (mActiveListFragment is ScheduledTaskListFragment) {
            (mActiveListFragment as ScheduledTaskListFragment).updateDateInterval(dateInterval)
        }
    }

    private fun resetSearch() {
        if (mMenu != null) {
            val suggestWord = intent.dataString
            val menuItem = mMenu!!.findItem(R.id.search)
            val searchView = menuItem.actionView as SearchView
            searchView.setQuery(suggestWord, false)
            searchView.clearFocus()
            menuItem.collapseActionView()
        }
    }

    private fun switchToFragment(fragment: ITaskListFragment) {
        resetSearch()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.task_list, fragment as Fragment)
        mActiveListFragment = fragment
        ft.commit()
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
}
