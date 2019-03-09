package com.example.androiduinavigation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }

    fun onGetResultButtonClick(view: View) {
        startActivityForResult(Intent(this, ResultScreenActivity::class.java), 101)
    }

    fun onNextButtonClick(view: View) {
        val result = findViewById<EditText>(R.id.resultEditText).text.toString()

        val intent = Intent(this, NextScreenActivity::class.java)
        intent.putExtra(NextScreenActivity.RESULT, result)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        findViewById<EditText>(R.id.resultEditText).setText(data.getStringExtra(ResultScreenActivity.RESULT))
    }
}
