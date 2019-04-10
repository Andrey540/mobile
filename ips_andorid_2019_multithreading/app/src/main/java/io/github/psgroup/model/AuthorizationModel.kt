package io.github.psgroup.model

import android.os.Handler
import android.os.Looper

class AuthorizationModel {

    interface IPresenter {
        fun waitSignIn(isWait: Boolean)
        fun signInCompleted()
    }

    private var mPresenter: IPresenter? = null
    private var mIsWait = false

    fun signIn(login: String, password: String) {
        mIsWait = true
        mPresenter?.waitSignIn(mIsWait)

        val longRunningOperation = Runnable {
            Thread.sleep(1000)

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                // run on UI thread
                mIsWait = false
                mPresenter?.waitSignIn(mIsWait)
                mPresenter?.signInCompleted()
            }
        }

        val thread = Thread(longRunningOperation)
        thread.start()
    }

    fun subscribe(presenter: IPresenter) {
        mPresenter = presenter
        mPresenter?.waitSignIn(mIsWait)
    }

    fun unsubscribe() {
        mPresenter = null
    }

}
