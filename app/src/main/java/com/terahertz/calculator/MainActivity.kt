package com.terahertz.calculator


import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.terahertz.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityMainBinding

    // Calculator states
    private var firstNumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""

    // Constants
    companion object {
        const val OPERATOR_PLUS = "+"
        const val OPERATOR_MINUS = "-"
        const val OPERATOR_MULTIPLY = "*"
        const val OPERATOR_DIVIDE = "/"
        const val CLEAR = "C"
        const val EQUALS = "="
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // No limit screen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        initViews()
    }

    private fun initViews() {
        binding.layoutMain.children.filterIsInstance<Button>().forEach { button ->
            button.setOnClickListener { handleButtonClick(button.text.toString()) }
        }
    }

    private fun handleButtonClick(buttonText: String) {
        when {
            buttonText.matches(Regex("[0-9]")) -> handleNumberClick(buttonText)
            buttonText in listOf(OPERATOR_PLUS, OPERATOR_MINUS, OPERATOR_MULTIPLY, OPERATOR_DIVIDE) -> handleOperatorClick(buttonText)
            buttonText == EQUALS -> handleEqualsClick()
            buttonText == "." -> handleDecimalClick()
            buttonText == CLEAR -> handleClearClick()
        }
    }

    private fun handleNumberClick(number: String) {
        if (currentOperator.isEmpty()) {
            firstNumber += number
            binding.calcOutput.text = firstNumber
        } else {
            currentNumber += number
            binding.calcOutput.text = currentNumber
        }
    }

    private fun handleOperatorClick(operator: String) {
        if (binding.calcOutput.text.toString().isNotEmpty()) {
            currentOperator = operator
            binding.calcOutput.text = "0"
            currentNumber = ""
        }
    }

    private fun handleEqualsClick() {
        if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty()) {
            binding.calcInput.text = "$firstNumber $currentOperator $currentNumber"
            result = evaluateExpression(firstNumber, currentNumber, currentOperator)
            firstNumber = result
            binding.calcOutput.text = result
            currentOperator = ""
            currentNumber = ""
        }
    }

    private fun handleDecimalClick() {
        if (currentOperator.isEmpty()) {
            if (!firstNumber.contains(".")) {
                firstNumber += if (firstNumber.isEmpty()) "0." else "."
                binding.calcOutput.text = firstNumber
            }
        } else {
            if (!currentNumber.contains(".")) {
                currentNumber += if (currentNumber.isEmpty()) "0." else "."
                binding.calcOutput.text = currentNumber
            }
        }
    }

    private fun handleClearClick() {
        firstNumber = ""
        currentNumber = ""
        currentOperator = ""
        result = ""
        binding.calcOutput.text = "0"
        binding.calcInput.text = ""
    }

    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        val num1 = firstNumber.toDouble()
        val num2 = secondNumber.toDouble()
        return when (operator) {
            OPERATOR_PLUS -> (num1 + num2).toString()
            OPERATOR_MINUS -> (num1 - num2).toString()
            OPERATOR_MULTIPLY -> (num1 * num2).toString()
            OPERATOR_DIVIDE -> (num1 / num2).toString()
            else -> ""
        }
    }
}
