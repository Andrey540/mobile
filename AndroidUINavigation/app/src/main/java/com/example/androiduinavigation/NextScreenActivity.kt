package com.example.androiduinavigation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class NextScreenActivity : AppCompatActivity() {
    companion object {
        const val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_screen)

        val result = intent.extras?.getString(RESULT)
        findViewById<TextView>(R.id.resultTextView).text = result
    }

    fun onReturnToMainButtonClick(view: View) {
        finish()
    }
}
