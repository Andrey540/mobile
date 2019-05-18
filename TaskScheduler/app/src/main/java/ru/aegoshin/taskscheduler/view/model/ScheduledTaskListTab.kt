package ru.aegoshin.taskscheduler.view.model

enum class ScheduledTaskListTab(val tab: Int) {
    All(0),
    Completed(1),
    Uncompleted(2);

    companion object {
        fun fromInt(tab: Int) = values().first { it.tab == tab }
    }
}