package io.github.psgroup.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.psgroup.R
import android.widget.ProgressBar
import io.github.psgroup.application.PizzaMakerApplication
import io.github.psgroup.model.TaskModel
import kotlinx.android.synthetic.main.activity_multi_thread.*

class MultiThreadActivity : AppCompatActivity() {

    companion object {
        private const val TASK_1_ID = "1"
        private const val TASK_2_ID = "2"
        private const val TASK_3_ID = "3"
    }

    private val mModel by lazy { PizzaMakerApplication.taskModel }
    private val mTaskPresenterMap = mutableMapOf<String, TaskModel.IPresenter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_thread)

        mTaskPresenterMap[TASK_1_ID] = ProgressBarPresenter(progressTask1)
        mTaskPresenterMap[TASK_2_ID] = ProgressBarPresenter(progressTask2)
        mTaskPresenterMap[TASK_3_ID] = ProgressBarPresenter(progressTask3)

        btnRunTask1.setOnClickListener {
            val presenter = mTaskPresenterMap[TASK_1_ID]
            if (presenter !== null) {
                mModel.start(TASK_1_ID, presenter)
            }
        }
        btnRunTask2.setOnClickListener {
            val presenter = mTaskPresenterMap[TASK_2_ID]
            if (presenter !== null) {
                mModel.start(TASK_2_ID, presenter)
            }
        }
        btnRunTask3.setOnClickListener {
            val presenter = mTaskPresenterMap[TASK_3_ID]
            if (presenter !== null) {
                mModel.start(TASK_3_ID, presenter)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        mTaskPresenterMap.entries.forEach { entry ->
            mModel.subscribe(entry.key, entry.value)
        }
    }

    override fun onPause() {
        super.onPause()

        mTaskPresenterMap.entries.forEach { entry ->
            mModel.unsubscribe(entry.key)
        }
    }

    inner class ProgressBarPresenter(
        private val progressbar: ProgressBar
    ) : TaskModel.IPresenter {
        override fun update(progress: Int) {
            progressbar.max = TaskModel.MAX_PROGRESS
            progressbar.progress = progress
        }
    }
}
