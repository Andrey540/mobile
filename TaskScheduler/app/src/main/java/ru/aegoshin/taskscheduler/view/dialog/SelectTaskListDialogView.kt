package ru.aegoshin.taskscheduler.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.select_task_list_dialog_frame.view.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import ru.aegoshin.taskscheduler.view.adapter.SelectTaskListDialogViewAdapter
import ru.aegoshin.taskscheduler.view.model.TaskViewModel

class SelectTaskListDialogView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var callback: SelectTaskListDialog.Callback? = null
    private var completeListener: DialogCompleteListener? = null
    private val linearLayoutManager: LinearLayoutManager
    private val mTaskListAdapter: SelectTaskListDialogViewAdapter
    private val mTaskListPresenter = TaskSchedulerApplication.getUnscheduledTaskListPresenter()

    fun setCallback(callback: SelectTaskListDialog.Callback?) {
        this.callback = callback
    }

    fun setCompleteListener(completeListener: DialogCompleteListener?) {
        this.completeListener = completeListener
    }

    fun updateItems(taskList: List<TaskViewModel>) {
        mTaskListAdapter.updateItems(taskList)
    }

    fun resetSearch() {
        selectTaskSearch.setQuery("", false)
        selectTaskSearch.clearFocus()
    }

    init {
        View.inflate(context, R.layout.select_task_list_dialog_frame, this)

        mTaskListAdapter = SelectTaskListDialogViewAdapter { task -> taskListItemClicked(task) }
        linearLayoutManager = LinearLayoutManager(context)
        selectTaskList.layoutManager = linearLayoutManager
        selectTaskList.adapter = mTaskListAdapter

        txtCancel.setOnClickListener {
            callback?.onCancelled()
            completeListener?.complete()
        }

        initSearchView()
    }

    private fun initSearchView() {
        val color = ContextCompat.getColor(context, R.color.white)
        val textView = selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_src_text) as TextView
        textView.setTextColor(color)
        textView.setHintTextColor(color)

        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_close_btn) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_button) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_mag_icon) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_mag_icon) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_voice_btn) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        (selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_go_btn) as ImageView).setColorFilter(
            color,
            PorterDuff.Mode.SRC_IN
        )
        val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val searchPlate = selectTaskSearch.findViewById(android.support.v7.appcompat.R.id.search_plate) as View
        searchPlate.setBackgroundColor(primaryColor)

        selectTaskSearch.setOnQueryTextListener(getSearchListener())
    }

    private fun taskListItemClicked(task: TaskViewModel) {
        callback?.onTaskSelected(task.id)
        completeListener?.complete()
    }

    private fun getSearchListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mTaskListPresenter.searchTasks(newText)
                return true
            }
        }
    }
}