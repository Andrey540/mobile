package io.github.psgroup.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.psgroup.R
import io.github.psgroup.application.PizzaMakerApplication
import io.github.psgroup.model.CookModel
import io.github.psgroup.model.CookingState
import kotlinx.android.synthetic.main.activity_cook.*

class CookActivity : AppCompatActivity(), CookModel.IPresenter {

    private val mModel by lazy { PizzaMakerApplication.cookModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook)

        buttonStart.setOnClickListener {
            mModel.start(pizzaNameInput.text.toString())
        }

        buttonStop.setOnClickListener {
            mModel.stop()
        }

        buttonDelete.setOnClickListener {
            mModel.delete()
        }
    }

    override fun update(cookingState: CookingState) = when (cookingState) {
        CookingState.NotStarted -> {
            progress.max = 0
            progress.progress = 0
            progressValue.text = "0%"
            buttonStart.visibility = View.VISIBLE
            buttonStop.visibility = View.GONE
            buttonDelete.visibility = View.GONE
            pizzaNameLayout.error = ""
        }
        is CookingState.InProgress -> {
            progress.max = cookingState.max
            progress.progress = cookingState.current
            progressValue.text = "${cookingState.current}%"
            buttonStart.visibility = View.GONE
            buttonStop.visibility = View.VISIBLE
            buttonDelete.visibility = View.GONE
            pizzaNameLayout.error = ""
        }
        is CookingState.Completed -> {
            progress.max = CookModel.MAX_PROGRESS
            progress.progress = CookModel.MAX_PROGRESS
            progressValue.text = "${CookModel.MAX_PROGRESS}%"
            buttonStart.visibility = View.GONE
            buttonStop.visibility = View.GONE
            buttonDelete.visibility = View.VISIBLE
            pizzaNameLayout.error = ""
        }
        is CookingState.Error -> {
            progress.max = 0
            progress.progress = 0
            progressValue.text = "0%"
            buttonStart.visibility = View.VISIBLE
            buttonStop.visibility = View.GONE
            buttonDelete.visibility = View.GONE
            pizzaNameLayout.error = cookingState.error.name
        }
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