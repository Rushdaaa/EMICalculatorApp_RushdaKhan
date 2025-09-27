package com.example.emicalculatorapp_rushdakhan

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etExpenseName: EditText
    private lateinit var etExpenseAmount: EditText
    private lateinit var etTimes: EditText
    private lateinit var toggleType: MaterialButtonToggleGroup
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var currentType = "monthly"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        etExpenseName = findViewById(R.id.etExpenseName)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etTimes = findViewById(R.id.etTimes)
        toggleType = findViewById(R.id.toggleType)
        btnSave = findViewById(R.id.btnSaveExpense)
        btnCancel = findViewById(R.id.btnCancelExpense)

        // Handle toggle selection
        toggleType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                currentType = when (checkedId) {
                    R.id.btnDaily -> "daily"
                    R.id.btnRecurring -> "recurring"
                    else -> "monthly"
                }
                etTimes.visibility = if (currentType == "recurring") View.VISIBLE else View.GONE
            }
        }

        // Save expense
        btnSave.setOnClickListener {
            val name = etExpenseName.text.toString()
            val amount = etExpenseAmount.text.toString().toDoubleOrNull()
            val times = if (currentType == "recurring") etTimes.text.toString().toIntOrNull() ?: 1 else 1

            if (name.isNotBlank() && amount != null && amount > 0) {
                val exp = Expense(name, amount, currentType, times)
                AppData.expenseList.add(exp)
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Enter valid expense", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel
        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
