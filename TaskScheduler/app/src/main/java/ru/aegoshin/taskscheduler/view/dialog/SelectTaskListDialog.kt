package ru.aegoshin.taskscheduler.view.dialog

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.injection.ViewModelInjector
import ru.aegoshin.taskscheduler.view.model.TaskViewModel


class SelectTaskListDialog : DialogFragment(), DialogCompleteListener {
    private var mCallback: Callback? = null
    private lateinit var mView: SelectTaskListDialogView

    fun setCallback(callback: Callback): SelectTaskListDialog {
        mCallback = callback
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SelectTaskListDialogStyle)
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = activity!!.layoutInflater.inflate(R.layout.select_task_list_dialog, container) as SelectTaskListDialogView
        mView.setCallback(mCallback)
        mView.setCompleteListener(this)
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelInjector.getUnscheduledTaskListViewModel(activity!!)
        viewModel.taskList.observe(this, Observer { taskList: List<TaskViewModel>? ->
            if (taskList !== null) {
                mView.updateItems(taskList.toList())
            }
        })
    }

    override fun complete() {
        mView.resetSearch()
        this.dismiss()
    }

    interface Callback {
        fun onCancelled()

        fun onTaskSelected(taskId: String)
    }
}