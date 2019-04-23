package io.github.psgroup.model

import android.os.AsyncTask
import java.util.concurrent.ExecutorService

class TaskModel {

    interface IPresenter {
        fun update(progress: Int)
    }

    private var mPresenter: IPresenter? = null
    private var mProgress: Int = 0
    private var mTask: AsyncTask<*, *, *>? = null

    fun start(executor: ExecutorService, presenter: IPresenter) {
        mPresenter = presenter
        mTask = Task().executeOnExecutor(executor)
    }

    fun subscribe(presenter: IPresenter) {
        mPresenter = presenter
        mPresenter?.update(mProgress)
    }

    fun unsubscribe() {
        mPresenter = null
    }

    fun stop() {
        mTask?.cancel(false)
        mProgress = 0
        mPresenter?.update(mProgress)
    }

    fun delete() {
        mProgress = 0
        mPresenter?.update(mProgress)
    }

    fun isReadyToRun(): Boolean {
        return if (mTask !== null) (mTask!!.status === AsyncTask.Status.FINISHED) else true
    }

    inner class Task : AsyncTask<Int, Int, Int>() {
        override fun onPreExecute() {
            super.onPreExecute()
            mProgress = 0
            mPresenter?.update(mProgress)
        }

        override fun doInBackground(vararg params: Int?): Int {
            var progress = MIN_PROGRESS
            Thread.sleep(1000)

            while (progress <= MAX_PROGRESS) {
                Thread.sleep(1000)
                progress += PROGRESS_STEP

                if (isCancelled) return 0

                publishProgress(progress)
            }

            return progress
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            val progress = values.getOrNull(0) ?: 0
            mPresenter?.update(progress)
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            mProgress = result ?: return
            mPresenter?.update(mProgress)
        }
    }

    companion object {
        const val MIN_PROGRESS = 0
        const val MAX_PROGRESS = 100

        private const val PROGRESS_STEP = 10
    }
}