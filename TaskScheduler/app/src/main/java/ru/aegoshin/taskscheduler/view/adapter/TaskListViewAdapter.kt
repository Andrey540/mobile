package ru.aegoshin.taskscheduler.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.task_item.view.*
import ru.aegoshin.application.task.TaskStatus
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.view.inflate
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class TaskListViewRecyclerAdapter(
    private val clickListener: (task: TaskViewModel) -> Unit,
    private val touchListener: View.OnTouchListener?
) : RecyclerView.Adapter<TaskListViewRecyclerAdapter.TaskHolder>(), AutoUpdatableAdapter {

    private var mTasks: List<TaskViewModel> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(
            oldValue,
            newValue,
            compare = { task1, task2 ->
                task1.id == task2.id
            })
    }

    fun updateItems(taskList: List<TaskViewModel>) {
        mTasks = taskList
    }

    fun selectAll() {
        mTasks.forEach { it.selected = true }
        notifyDataSetChanged()
    }

    fun unSelectAll() {
        mTasks.forEach { it.selected = false }
        notifyDataSetChanged()
    }

    override fun getItemCount() = mTasks.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val itemTask = mTasks[position]
        holder.bindTask(itemTask, clickListener, touchListener)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder,position, payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflatedView = parent.inflate(R.layout.task_item, false)
        return TaskHolder(inflatedView)
    }

    class TaskHolder (v: View) : RecyclerView.ViewHolder(v) {
        private val statusView: ImageView = v.taskStatusView
        private var view: View = v
        private lateinit var task: TaskViewModel

        fun bindTask(task: TaskViewModel, clickListener: (task: TaskViewModel) -> Unit, touchListener: View.OnTouchListener?) {
            this.task = task
            view.taskViewTitle.text = task.title

            updateBackgroundColor(task.selected)
            updateTime(task.scheduledTime)
            updateStatus(task.status)
            view.setOnLongClickListener{
                clickListener(task)
                return@setOnLongClickListener true
            }
            statusView.setOnClickListener{
                setChecked(!this.task.selected)
            }
            if (touchListener != null) {
                view.setOnTouchListener(touchListener)
            }
        }

        private fun updateBackgroundColor(selected: Boolean) {
            val backgroundColorRes = if (selected) R.color.selectedListItemColor else R.color.white
            view.setBackgroundColor(ContextCompat.getColor(view.context, backgroundColorRes))
        }

        private fun updateStatus(status: TaskStatus) {
            when (status) {
                TaskStatus.Scheduled -> {
                    statusView.setImageResource(R.drawable.ic_uncompleted)
                }
                TaskStatus.Completed -> {
                    statusView.setImageResource(R.drawable.ic_completed)
                }
                TaskStatus.Unscheduled -> {
                    statusView.setImageResource(R.drawable.ic_unscheduled)
                }
            }
        }

        private fun updateTime(time: Long?) {
            val pattern = "dd.MM HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            view.taskScheduledDateTime.text = ""
            if (time != null) {
                view.taskScheduledDateTime.text = simpleDateFormat.format(Date(time))
            }
        }

        private fun setChecked(checked: Boolean) {
            this.task.selected = checked
            updateBackgroundColor(checked)
        }
    }
}