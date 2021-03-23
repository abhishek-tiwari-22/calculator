package com.example.abhiscalculator

import android.os.Bundle
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

private const val statePendingoperation="PendingOperation"
private const val stateOperand1="Operand1"
private const val stateOperand1Stored="Operand1_Stored"

class MainActivity : AppCompatActivity() {
    //    operands and type of calculations are now given a variable to hold
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        working of number and dot buttons
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }
//        working of operation buttons
        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }
//        clear buttons
        clearAll.setOnClickListener {
            operand1 = null
            pendingOperation = "="
            result.setText("")
            newNumber.setText("")
            operation.text = ""
        }
        clear.setOnClickListener {
            newNumber.setText("")
        }

        negative.setOnClickListener{
            val value=newNumber.text.toString()
            if (value.isEmpty()){
                newNumber.setText("-")
            }else{
                try{
                    var doubleValue=value.toDouble()
                    doubleValue*=-1
                    newNumber.setText(doubleValue.toString())
                }catch (e:NumberFormatException){
//                    new number was "-" or ".",so clear it
                newNumber.setText("")
                }
            }
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        buttonEquals.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonSubtract.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN                                  // divided by zero is handled
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "+" -> operand1 = operand1!! + value
                "-" -> operand1 = operand1!! - value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1!=null){
            outState.putDouble(stateOperand1,operand1!!)
            outState.putBoolean(stateOperand1Stored,true)
        }
        outState.putString(statePendingoperation,pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(stateOperand1Stored,false)) {
            savedInstanceState.getDouble(stateOperand1)
        }else{
            null
        }
        pendingOperation= savedInstanceState.getString(statePendingoperation).toString()
        operation.text=pendingOperation
    }
}
