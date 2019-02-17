package com.example.equationsolver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast

class SolveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val equationExample = findViewById<TextView>(R.id.equationExample)
        equationExample.text = Html.fromHtml("A * x<sup>2</sup> + B * x + C = 0")
        val solveButton = findViewById<Button>(R.id.solve)

        solveButton.setOnClickListener {
            this.onSolveButtonClicked()
        }
    }

    private fun onSolveButtonClicked() {
        val factorAView = findViewById<EditText>(R.id.factorA)
        val factorBView = findViewById<EditText>(R.id.factorB)
        val factorCView = findViewById<EditText>(R.id.factorC)

        val factorA = factorAView.text.toString().toFloatOrNull()
        val factorB = factorBView.text.toString().toFloatOrNull()
        val factorC = factorCView.text.toString().toFloatOrNull()

        if (factorA == null) {
            this.showMessage(resources.getString(R.string.enterA))
            return
        }
        if (factorB == null) {
            this.showMessage(resources.getString(R.string.enterB))
            return
        }
        if (factorC == null) {
            this.showMessage(resources.getString(R.string.enterC))
            return
        }
        if (factorA.toInt() == 0) {
            this.showMessage(resources.getString(R.string.emptyA))
            return
        }

        val discriminant = factorB * factorB - 4 * factorA * factorC
        val discriminantView = findViewById<TextView>(R.id.discriminant)
        discriminantView.text = (resources.getString(R.string.discriminant) + discriminant)

        if (discriminant < 0) {
            this.showMessage(resources.getString(R.string.negativeDiscriminant))
            return
        }
        val x1 = (-factorB + kotlin.math.sqrt(discriminant)) / (2 * factorA)
        val x2 = (-factorB - kotlin.math.sqrt(discriminant)) / (2 * factorA)

        val valueX1View = findViewById<TextView>(R.id.valueX1)
        val valueX2View = findViewById<TextView>(R.id.valueX2)

        val x2Text: String = if (discriminant.toInt() == 0) "" else x2.toString()
        valueX1View.text = (resources.getString(R.string.valueX1) + x1)
        valueX2View.text = (resources.getString(R.string.valueX2) + x2Text)
    }

    private fun showMessage(text: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this, text, duration)
        toast.setGravity(Gravity.BOTTOM, 0, 30)
        toast.show()
    }
}
