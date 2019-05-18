package ru.aegoshin.taskscheduler.view.fragment

interface ITaskListFragment {
    fun selectAllItems()
    fun unSelectAllItems()
    fun getSelectedIds(): List<String>
    fun searchTasks(search: String?)
}