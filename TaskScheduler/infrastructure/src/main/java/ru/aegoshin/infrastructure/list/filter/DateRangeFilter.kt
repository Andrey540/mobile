package ru.aegoshin.infrastructure.list.filter

import ru.aegoshin.infrastructure.task.Task
import java.util.*

class DateRangeFilter : ITaskListFilter {
    private var mFrom: Long? = null
    private var mTo: Long? = null

    fun updateDataRange(from: Long?, to: Long?) {
        mFrom = from
        mTo = to
    }

    override fun filterTask(task: Task): Boolean {
        if ((mFrom != null || mTo != null) && (task.scheduledTime === null)) {
            return false
        }
        if ((mFrom != null && task.scheduledTime != null && task.scheduledTime < mFrom!!)) {
            return false
        }
        if ((mTo != null && task.scheduledTime != null && task.scheduledTime > mTo!!)) {
            return false
        }
        return true
    }
}