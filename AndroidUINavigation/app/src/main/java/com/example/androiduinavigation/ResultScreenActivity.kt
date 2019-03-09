package com.example.androiduinavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class ResultScreenActivity : AppCompatActivity() {
    companion object {
        const val RESULT = "result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_screen)
    }

    fun onReturnResultButtonClick(view: View) {
        val result = findViewById<EditText>(R.id.resultEditText).text.toString()

        val intent = Intent(this, ResultScreenActivity::class.java)
        intent.putExtra(RESULT, result)
        setResult(RESULT_OK, intent)
        this.finish()
    }
}
