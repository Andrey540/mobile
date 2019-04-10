package io.github.psgroup.presentation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.psgroup.R
import io.github.psgroup.application.PizzaMakerApplication
import io.github.psgroup.model.AuthorizationModel
import kotlinx.android.synthetic.main.activity_authorization.*

class AuthorizationActivity : AppCompatActivity(), AuthorizationModel.IPresenter {

    private val mModel by lazy { PizzaMakerApplication.authorizationModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        button.setOnClickListener {
            mModel.signIn(loginInput.text.toString(), passwordInput.text.toString())
        }
    }

    override fun waitSignIn(isWait: Boolean) {
        button.visibility = if (!isWait) View.VISIBLE else View.INVISIBLE
        progress.visibility = if (isWait) View.VISIBLE else View.INVISIBLE
    }

    override fun signInCompleted() {
        startActivity(Intent(this, CookActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        mModel.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        mModel.unsubscribe()
    }

}
