package io.github.psgroup.model

import android.os.AsyncTask
import java.util.concurrent.Executors

class CookModel {

    interface IPresenter {
        fun update(cookingState: CookingState)
    }

    private var mPresenter: IPresenter? = null
    private var mState: CookingState = CookingState.NotStarted
    private var mTask: AsyncTask<*, *, *>? = null

    fun start(pizza: String) {
        mTask = OrderPizzaTask().execute(pizza)
    }

    fun stop() {
        mTask?.cancel(false)
        mState = CookingState.NotStarted
        mPresenter?.update(mState)
    }

    fun delete() {
        mState = CookingState.NotStarted
        mPresenter?.update(mState)
    }

    fun subscribe(presenter: IPresenter) {
        mPresenter = presenter
        mPresenter?.update(mState)
    }

    fun unsubscribe() {
        mPresenter = null
    }

    inner class OrderPizzaTask : AsyncTask<String, Int, CookingState>() {
        override fun onPreExecute() {
            super.onPreExecute()
            mState = CookingState.InProgress(MAX_PROGRESS, 0)
            mPresenter?.update(mState)
        }

        override fun doInBackground(vararg params: String?): CookingState? {
            val pizza = params.getOrNull(0) ?: ""
            var progress = MIN_PROGRESS
            Thread.sleep(1000)

            if (pizza !in AVAILABLE_PIZZA) {
                return CookingState.Error(CookingError.INVALID_PIZZA_NAME)
            }

            while (progress <= MAX_PROGRESS) {
                Thread.sleep(1000)
                progress += PROGRESS_STEP

                if (isCancelled) return null

                publishProgress(progress)
            }

            return CookingState.Completed
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            val progress = values.getOrNull(0) ?: 0
            mState = CookingState.InProgress(MAX_PROGRESS, progress)
            mPresenter?.update(mState)
        }

        override fun onPostExecute(result: CookingState?) {
            super.onPostExecute(result)
            mState = result ?: return
            mPresenter?.update(mState)
        }
    }

    companion object {
        const val MIN_PROGRESS = 0
        const val MAX_PROGRESS = 100

        private const val PROGRESS_STEP = 20
        private val AVAILABLE_PIZZA = arrayOf(
                "margarita",
                "venezia",
                "salami"
        )
    }

}
