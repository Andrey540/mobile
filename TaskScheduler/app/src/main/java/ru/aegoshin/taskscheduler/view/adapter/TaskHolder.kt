package ru.aegoshin.taskscheduler.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.task_item.view.*
import ru.aegoshin.infrastructure.task.TaskStatus
import ru.aegoshin.taskscheduler.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.CheckBox
import ru.aegoshin.taskscheduler.view.model.TaskViewModel

class TaskHolder (v: View) : RecyclerView.ViewHolder(v) {
    private val checkbox: CheckBox = v.selectTask
    private var view: View = v
    private lateinit var task: TaskViewModel

    fun bindTask(task: TaskViewModel, clickListener: (task: TaskViewModel) -> Unit, touchListener: View.OnTouchListener) {
        this.task = task
        view.taskViewTitle.text = task.title
        val pattern = "dd.MM HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        view.taskScheduledDateTime.text = ""
        if (task.scheduledTime != null) {
            view.taskScheduledDateTime.text = simpleDateFormat.format(Date(task.scheduledTime))
        }

        checkbox.isChecked = task.selected

        when (task.status) {
            TaskStatus.Scheduled -> {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
            }
            TaskStatus.Completed -> {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.taskDone))
            }
            TaskStatus.Unscheduled -> {
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.taskNotScheduled))
            }
        }
        view.setOnLongClickListener{
            clickListener(task)
            return@setOnLongClickListener true
        }
        view.selectTaskClickArea.setOnClickListener{
            setChecked(!checkbox.isChecked)
        }
        checkbox.setOnCheckedChangeListener{_, isChecked ->
            setChecked(isChecked)
        }
        view.setOnTouchListener(touchListener)
    }

    private fun setChecked(checked: Boolean) {
        this.task.selected = checked
        checkbox.isChecked = checked
    }
}