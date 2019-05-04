package ru.aegoshin.taskscheduler.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.view.inflate
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import kotlin.properties.Delegates

class TaskListViewRecyclerAdapter(
    private val clickListener: (task: TaskViewModel) -> Unit,
    private val touchListener: View.OnTouchListener
) : RecyclerView.Adapter<TaskHolder>(), AutoUpdatableAdapter {

    private var mTasks: List<TaskViewModel> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(
            oldValue,
            newValue,
            compare = { task1, task2 ->
                task1.id === task2.id
            })
    }

    fun updateItems(taskList: List<TaskViewModel>) {
        val prevSize = mTasks.size
        mTasks = taskList

        if (prevSize == taskList.size) {
            notifyDataSetChanged()
        }
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
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflatedView = parent.inflate(R.layout.task_item, false)
        return TaskHolder(inflatedView)
    }
}