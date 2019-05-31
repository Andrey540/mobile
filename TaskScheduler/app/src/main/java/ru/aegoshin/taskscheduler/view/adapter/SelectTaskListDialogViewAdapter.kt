package ru.aegoshin.taskscheduler.view.adapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.task_item.view.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.view.inflate
import ru.aegoshin.taskscheduler.view.model.TaskViewModel
import kotlin.properties.Delegates

class SelectTaskListDialogViewAdapter(
    private val clickListener: (task: TaskViewModel) -> Unit
) : RecyclerView.Adapter<SelectTaskListDialogViewAdapter.TaskHolder>(), AutoUpdatableAdapter {

    private var mTasks: List<TaskViewModel> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        autoNotify(
            oldValue,
            newValue,
            compare = { task1, task2 ->
                task1.id === task2.id
            },
            getPayload = null)
    }

    fun updateItems(taskList: List<TaskViewModel>) {
        mTasks = taskList
    }

    override fun getItemCount() = mTasks.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val itemTask = mTasks[position]
        holder.bindTask(itemTask, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflatedView = parent.inflate(R.layout.select_task_item, false)
        return TaskHolder(inflatedView)
    }

    class TaskHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private lateinit var task: TaskViewModel

        fun bindTask(task: TaskViewModel, clickListener: (task: TaskViewModel) -> Unit) {
            this.task = task
            view.taskViewTitle.text = task.title
            view.setOnClickListener{
                clickListener(task)
            }
        }
    }
}