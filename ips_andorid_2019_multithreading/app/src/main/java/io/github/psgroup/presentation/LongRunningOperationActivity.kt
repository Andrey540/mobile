package io.github.psgroup.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.psgroup.R
import kotlinx.android.synthetic.main.activity_long_running_operation.*

class LongRunningOperationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_long_running_operation)

        button.setOnClickListener {
            Thread.sleep(3000)
        }
    }

}