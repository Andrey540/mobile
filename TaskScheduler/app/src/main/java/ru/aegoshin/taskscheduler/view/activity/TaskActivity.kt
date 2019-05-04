package ru.aegoshin.taskscheduler.view.activity

import android.os.Bundle
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task.*
import ru.aegoshin.infrastructure.task.TaskData
import ru.aegoshin.infrastructure.task.TaskStatus
import ru.aegoshin.taskscheduler.R
import ru.aegoshin.taskscheduler.application.TaskSchedulerApplication
import java.text.SimpleDateFormat
import java.util.*

class TaskActivity : LocaliseActivity() {
    companion object {
        const val DATE = "dateInterval"
        const val TASK_ID = "task_id"
        private const val TIME_FORMAT = "%02d:%02d"
    }

    private val mTaskService = TaskSchedulerApplication.getTaskService()
    private val mTaskDataProvider = TaskSchedulerApplication.getTaskDataProvider()
    private var mCalendar = Calendar.getInstance()
    private var mTaskId: String? = null
    private var mDate: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        mDate = intent.extras?.getLong(DATE)
        mTaskId = intent.extras?.getString(TASK_ID)
        var taskData: TaskData? = null
        if (mTaskId != null) {
            taskData = mTaskDataProvider.findTaskDataById(mTaskId!!)
            taskData ?: throw IllegalArgumentException("Cannot find task data by id: $mTaskId")
        }
        initViewFields(taskData)

        clearDateButton.setOnClickListener {
            mDate = null
            dateText.setText("")
            timeText.setText("")
            clearDateButton.visibility = INVISIBLE
        }

        initDateComponent()
        initTimeComponent()
        initEnableNotificationCheckBox()
        initNotifyBeforeTimeText()
        initSaveButton()
    }

    private fun initViewFields(taskData: TaskData?) {
        notifyBeforeTimeText.setText(String.format(TIME_FORMAT, 0, 0))

        if (taskData != null) {
            taskNameText.setText(taskData.title)
            taskDescriptionText.setText(taskData.description)
            mDate = taskData.scheduledTime
            enableNotificationCheckBox.isChecked = taskData.needNotify
            val notifyTime = taskData.notifyBefore / 60000
            val notifyMinutes = notifyTime % 60
            val notifyHours = notifyTime / 60
            val text = String.format(TIME_FORMAT, notifyHours, notifyMinutes)
            notifyBeforeTimeText.setText(text)
            isCompletedCheckBox.isChecked = taskData.status == TaskStatus.Completed
        }

        clearDateButton.visibility = INVISIBLE
        if (mDate != null && mDate!! >= 0) {
            clearDateButton.visibility = VISIBLE
            mCalendar.timeInMillis = mDate!!
            updateDateView()
            updateTimeView()
        }
    }

    private fun initSaveButton() {
        saveButton.setOnClickListener {
            if (taskNameText.text.isEmpty()) {
                Toast.makeText(this, R.string.task_name_is_required, Toast.LENGTH_SHORT).show()
            } else {
                val taskData = TaskData(
                    taskNameText.text.toString(),
                    taskDescriptionText.text.toString(),
                    mDate,
                    getStatus(),
                    enableNotificationCheckBox.isChecked,
                    (getNotifyBeforeTime() * 60000).toLong()
                )
                if (mTaskId != null) mTaskService.updateTask(mTaskId!!, taskData) else mTaskService.addTask(taskData)
                finish()
            }
        }
    }

    private fun initDateComponent() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                mCalendar.set(Calendar.YEAR, year)
                mCalendar.set(Calendar.MONTH, monthOfYear)
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                mDate = mCalendar.time.time
                clearDateButton.visibility = VISIBLE

                updateDateView()
            }
        }
        dateText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun initTimeComponent() {
        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                mCalendar.set(Calendar.MINUTE, minute)
                mDate = mCalendar.time.time
                clearDateButton.visibility = VISIBLE

                updateTimeView()
            }
        }
        timeText.setOnClickListener {
            val hours = mCalendar.get(Calendar.HOUR_OF_DAY)
            val minutes = mCalendar.get(Calendar.MINUTE)
            TimePickerDialog(
                this,
                timeSetListener,
                hours,
                minutes,
                true
            ).show()
        }
    }

    private fun initEnableNotificationCheckBox() {
        enableNotificationCheckBox.setOnCheckedChangeListener{_, _ ->
            updateNotifyBeforeAvailability()
        }
    }

    private fun initNotifyBeforeTimeText() {
        updateNotifyBeforeAvailability()

        val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
                val text = String.format(TIME_FORMAT, hourOfDay, minute)
                notifyBeforeTimeText.setText(text)
            }
        }
        notifyBeforeTimeText.setOnClickListener {
            val time = getNotifyBeforeTime()
            val hours = time / 60
            val minutes = time % 60
            TimePickerDialog(
                this,
                timeSetListener,
                hours,
                minutes,
                true
            ).show()
        }
    }

    private fun updateNotifyBeforeAvailability() {
        notifyBeforeLabel.isEnabled = enableNotificationCheckBox.isChecked
        notifyBeforeTimeText.isEnabled = enableNotificationCheckBox.isChecked
    }

    private fun updateDateView() {
        dateText.setText(SimpleDateFormat("d MMMM", Locale.getDefault()).format(mCalendar.time))
    }

    private fun updateTimeView() {
        timeText.setText(TIME_FORMAT.format(mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE)))
    }

    private fun getStatus(): TaskStatus {
        if (isCompletedCheckBox.isChecked) {
            return TaskStatus.Completed
        }
        return if (mDate != null) TaskStatus.Scheduled else TaskStatus.Unscheduled
    }

    private fun getNotifyBeforeTime(): Int {
        val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = formatter.parse(notifyBeforeTimeText.text.toString())
        mCalendar.timeInMillis = date.time
        return mCalendar.get(Calendar.MINUTE) + mCalendar.get(Calendar.HOUR_OF_DAY) * 60
    }
}
