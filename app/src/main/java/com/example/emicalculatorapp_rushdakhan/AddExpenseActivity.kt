package com.example.emicalculatorapp_rushdakhan

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup

/**
 * Activity for adding a new expense.
 *
 * This activity allows the user to input details for a new expense,
 * including its name, amount, and type (daily, monthly, or recurring).
 * If the expense is recurring, the user can also specify the number of times it recurs.
 * The entered expense is then saved to the `AppData.expenseList`.
 */
class AddExpenseActivity : AppCompatActivity() {

    // UI elements
    private lateinit var etExpenseName: EditText
    private lateinit var etExpenseAmount: EditText
    private lateinit var etTimes: EditText
    private lateinit var toggleType: MaterialButtonToggleGroup
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    // Variable to store the currently selected expense type, defaulting to "monthly"
    private var currentType = "monthly"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize UI elements by finding them in the layout
        etExpenseName = findViewById(R.id.etExpenseName)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etTimes = findViewById(R.id.etTimes)
        toggleType = findViewById(R.id.toggleType)
        btnSave = findViewById(R.id.btnSaveExpense)
        btnCancel = findViewById(R.id.btnCancelExpense)

        // Listener for the toggle button group to handle changes in expense type selection.
        // Updates `currentType` and shows/hides the "times" input field based on the selection.
        toggleType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                currentType = when (checkedId) {
                    R.id.btnDaily -> "daily"
                    R.id.btnRecurring -> "recurring"
                    else -> "monthly"
                }
                // Show the "times" input field only if the expense type is "recurring"
                etTimes.visibility = if (currentType == "recurring") View.VISIBLE else View.GONE
            }
        }

        // Click listener for the "Save" button.
        // Validates the input fields and, if valid, creates an Expense object,
        // adds it to the `AppData.expenseList`, sets the result to OK, and finishes the activity.
        // Shows a toast message if the input is invalid.
        btnSave.setOnClickListener {
            val name = etExpenseName.text.toString()
            val amount = etExpenseAmount.text.toString().toDoubleOrNull()
            val times = if (currentType == "recurring") etTimes.text.toString().toIntOrNull() ?: 1 else 1
            // Defaults 'times' to 1 if not recurring or if the input is invalid for recurring expenses.

            if (name.isNotBlank() && amount != null && amount > 0) {
                val exp = Expense(name, amount, currentType, times)
                AppData.expenseList.add(exp)
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Enter valid expense", Toast.LENGTH_SHORT).show()
            }
        }

        // Click listener for the "Cancel" button.
        // Sets the result to CANCELED and finishes the activity.
        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
