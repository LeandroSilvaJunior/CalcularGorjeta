package org.senac.calculagorjeta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var amountComponent : EditText
    private lateinit var customComponent : SeekBar
    private lateinit var customValueTipComponent : TextView
    private lateinit var tipComponent : EditText
    private lateinit var customTipComponent : EditText
    private lateinit var totalTipComponent : EditText
    private lateinit var customTotalTipComponent : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()

        customValueTipComponent.setText("${customComponent.progress.toString()}%")

        amountComponent.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                atualizarGorjeta(s)
                atualizarGorjetaCustom(customComponent.progress.toDouble())
            }
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        customComponent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                customValueTipComponent.text = "$progress%"

                try {
                    atualizarGorjetaCustom(progress.toDouble())
                } catch (e : Exception) {
                    Log.e("Calculo", e.toString())
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun atualizarGorjeta(s: CharSequence) {
        try {
            if (s !== null && s.toString() !== "") {
                val tip = calcularGorjeta(15.toDouble(), s.toString().toDouble())
                tipComponent.setText("$${"%.2f".format(tip)}")
                totalTipComponent.setText("$${"%.2f".format(s.toString().toDouble() + tip)}")
            }
        } catch (e: Exception) {
            Log.e("Calculo", e.toString())
        }
    }

    private fun atualizarGorjetaCustom(percent: Double) {
        val amount = amountComponent.text.toString().toDouble()
        val tip = calcularGorjeta(percent, amount)

        customTipComponent.setText("$${"%.2f".format(tip)}")
        customTotalTipComponent.setText("$${"%.2f".format(amount + tip)}")
    }

    private fun initComponents() {
        amountComponent = findViewById(R.id.et_amount)
        customComponent = findViewById(R.id.sb_custom)
        customValueTipComponent = findViewById(R.id.tv_customTip)
        tipComponent = findViewById(R.id.et_tip)
        customTipComponent = findViewById(R.id.et_customTip)
        totalTipComponent = findViewById(R.id.et_totalTip)
        customTotalTipComponent = findViewById(R.id.et_customTotalTip)
    }

    private fun calcularGorjeta(percent : Double, amount : Double) : Double {
        return amount * ( percent / 100.toDouble())
    }

}
