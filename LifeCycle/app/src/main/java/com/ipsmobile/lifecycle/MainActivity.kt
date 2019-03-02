package com.ipsmobile.lifecycle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var mResult: Int = 0

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("STATE", mResult)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mResult = savedInstanceState?.getInt("STATE")?: 0
        ++mResult
        printToast()
        Log.d("onCreate", mResult.toString())
    }

    override fun onStart() {
        super.onStart()
        ++mResult
        printToast()
        Log.d("onStart", mResult.toString())
    }
    override fun onResume() {
        super.onResume()
        ++mResult
        printToast()
        Log.d("onResume", mResult.toString())
    }
    override fun onPause() {
        super.onPause()
        --mResult
        printToast()
        Log.d("onPause", mResult.toString())
    }
    override fun onStop() {
        super.onStop()
        --mResult
        printToast()
        Log.d("onStop", mResult.toString())
    }
    override fun onDestroy() {
        super.onDestroy()
        --mResult
        printToast()
        Log.d("onDestroy", mResult.toString())
    }
    override fun onRestart() {
        super.onRestart()
        mResult += 2
        printToast()
        Log.d("onRestart", mResult.toString())
    }

    fun printToast() {
        val toast: Toast = Toast.makeText(this, mResult.toString(), Toast.LENGTH_SHORT)
        toast.show()
    }
}
