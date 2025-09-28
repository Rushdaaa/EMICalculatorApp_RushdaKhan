package com.example.emicalculatorapp_rushdakhan

// Necessary imports
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.pow
import android.view.View
import java.util.Locale

/**
 * MainActivity is the main screen of the application.
 * It allows users to calculate EMI (Equated Monthly Installment) for loans,
 * manage their income, track expenses, and view their overall budget.
 *
 * This activity handles:
 * - EMI calculation based on loan amount, interest rate, and tenure.
 * - Displaying EMI results in monthly, bi-weekly, or weekly frequencies.
 * - Saving and editing user's income.
 * - Adding, displaying, and deleting expenses.
 * - Calculating and displaying the remaining budget (savings or deficit)
 *   after accounting for EMI and total expenses.
 *
 * It uses [AppData] to store and retrieve shared application data like income,
 * EMI, and the list of expenses.
 * It launches [AddExpenseActivity] to allow users to add new expenses.
 */
class MainActivity : AppCompatActivity() {

    // EMI
    private lateinit var etLoan: EditText
    private lateinit var etRate: EditText
    private lateinit var etTenure: EditText
    private lateinit var rgFrequency: RadioGroup
    private lateinit var btnCalculate: Button
    private lateinit var btnReset: Button
    private lateinit var tvEmiValue: TextView

    // Income
    private lateinit var etIncome: EditText
    private lateinit var btnSaveIncome: Button
    private lateinit var btnEditIncome: Button
    private lateinit var tvIncomeSaved: TextView

    // Expenses
    private lateinit var expensesContainer: LinearLayout
    private lateinit var tvExpensesTotal: TextView
    private lateinit var btnAddExpense: Button

    // Budget
    // Budget
    private lateinit var tvBudget: TextView

    private lateinit var topAppBar: MaterialToolbar
    private var lastEmiMonthly: Double? = null

    /**
     * ActivityResultLauncher for the [AddExpenseActivity].
     * This is used to start the activity for adding a new expense and
     * to refresh the expenses UI when the activity returns.
     */
    private val addExpenseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { refreshExpensesUI() }

    // onCreate is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // EMI views
        etLoan = findViewById(R.id.etLoan)
        etRate = findViewById(R.id.etRate)
        etTenure = findViewById(R.id.etTenure)
        rgFrequency = findViewById(R.id.rgFrequency)
        btnCalculate = findViewById(R.id.btnCalculate)
        btnReset = findViewById(R.id.btnReset)
        tvEmiValue = findViewById(R.id.tvEmiValue)

        // Income views
        etIncome = findViewById(R.id.etIncome)
        btnSaveIncome = findViewById(R.id.btnSaveIncome)
        btnEditIncome = findViewById(R.id.btnEditIncome)
        tvIncomeSaved = findViewById(R.id.tvIncomeSaved)

        // Expenses views
        expensesContainer = findViewById(R.id.expensesContainer)
        tvExpensesTotal = findViewById(R.id.tvExpensesTotal)
        btnAddExpense = findViewById(R.id.btnAddExpense)

        // Budget views
        tvBudget = findViewById(R.id.tvBudget)

        topAppBar = findViewById(R.id.topAppBar)

        // Initial income state (if already saved earlier)
        if (AppData.income > 0) {
            etIncome.setText(AppData.income.toString())
            setIncomeLocked(true)
            updateIncomeDisplay()
        } else {
            setIncomeLocked(false)
            tvIncomeSaved.text = "—"
        }

        // EMI handlers
        btnCalculate.setOnClickListener { calculateEMI() }
        btnReset.setOnClickListener { resetFields() }
        rgFrequency.setOnCheckedChangeListener { _, _ ->
            lastEmiMonthly?.let { updateEmiDisplay(it) }
        }

        // Income save/edit
        btnSaveIncome.setOnClickListener {
            val income = etIncome.text.toString().toDoubleOrNull()
            if (income != null && income > 0) {
                AppData.income = income
                setIncomeLocked(true)
                updateIncomeDisplay()
                updateBudget()
            } else {
                Toast.makeText(this, "Enter a valid income", Toast.LENGTH_SHORT).show()
            }
        }
        btnEditIncome.setOnClickListener {
            setIncomeLocked(false)
        }

        // Add expense
        btnAddExpense.setOnClickListener {
            addExpenseLauncher.launch(Intent(this, AddExpenseActivity::class.java))
        }

        // First draw of expenses + budget
        refreshExpensesUI()
        updateBudget()
    }

    // ==== EMI ====
    /**
     * Calculates the Equated Monthly Installment (EMI) based on the loan amount,
     * annual interest rate, and loan tenure entered by the user.
     * It validates the input fields and displays an error message if any field is invalid.
     * Otherwise, it calculates the EMI, stores it in [AppData], updates the EMI display, and refreshes the budget.
     */
    private fun calculateEMI() {
        val loan = etLoan.text.toString().toDoubleOrNull()
        val annualRate = etRate.text.toString().toDoubleOrNull()
        val tenureYears = etTenure.text.toString().toIntOrNull()

        if (loan == null || loan <= 0 ||
            annualRate == null || annualRate <= 0 ||
            tenureYears == null || tenureYears <= 0
        ) {
            tvEmiValue.text = "Please fill in all fields with valid values"
            return
        }

        val monthlyRate = annualRate / 12 / 100
        val tenureMonths = tenureYears * 12

        val emiMonthly = (loan * monthlyRate * (1 + monthlyRate).pow(tenureMonths)) /
                ((1 + monthlyRate).pow(tenureMonths) - 1)

        lastEmiMonthly = emiMonthly
        AppData.emi = emiMonthly
        updateEmiDisplay(emiMonthly)
        updateBudget()
    }

    /**
     * Updates the displayed EMI value based on the selected frequency (monthly, bi-weekly, or weekly).
     *
     * @param emiMonthly The calculated monthly EMI.
     */
    private fun updateEmiDisplay(emiMonthly: Double) {
        val (emi, frequencyText) = when (rgFrequency.checkedRadioButtonId) {
            R.id.rbBiWeekly -> emiMonthly / 2 to "Bi-weekly"
            R.id.rbWeekly -> emiMonthly / 4 to "Weekly"
            else -> emiMonthly to "Monthly"
        }
        tvEmiValue.text = String.format(Locale.CANADA, "$%.2f (%s)", emi, frequencyText)
    }

    /**
     * Resets all EMI input fields to their default values, clears the displayed EMI,
     * resets the stored EMI in [AppData], and updates the budget.
     */
    private fun resetFields() {
        etLoan.text.clear()
        etRate.text.clear()
        etTenure.text.clear()
        rgFrequency.check(R.id.rbMonthly)
        tvEmiValue.text = "—"
        lastEmiMonthly = null
        AppData.emi = 0.0
        updateBudget()
    }

    // ==== Income Helpers ====
    /**
     * Controls the enabled state of the income input field and save/edit buttons.
     *
     * @param locked If true, the income field and save button are disabled, and the edit button is enabled.
     *               If false, the income field and save button are enabled, and the edit button is disabled.
     */
    private fun setIncomeLocked(locked: Boolean) {
        etIncome.isEnabled = !locked
        btnSaveIncome.isEnabled = !locked
        btnEditIncome.isEnabled = locked
    }

    /**
     * Updates the text view that displays the saved income.
     * Formats the income value as currency.
     */
    private fun updateIncomeDisplay() {
        tvIncomeSaved.text = "$${"%.2f".format(AppData.income)}"
    }

    // ==== Expenses UI ====
    // Refreshes the display of expenses in the UI.
    private fun refreshExpensesUI() {
        expensesContainer.removeAllViews()
        AppData.expenseList.forEachIndexed { index, exp ->
            expensesContainer.addView(makeExpenseBubble(index, exp))
        }
        tvExpensesTotal.text = getString(
            R.string.total_monthly_expenses,
            String.format("$%.2f", AppData.totalExpensesMonthly())
        )

        updateBudget()
    }

    /**
     * Creates a View representing a single expense item (expense bubble).
     * This view includes the expense name, type, amount, and a delete button.
     *
     * @param index The index of the expense in the [AppData.expenseList].
     * @param exp The [Expense] object to display.
     * @return The inflated and populated View for the expense item.
     */
    private fun makeExpenseBubble(index: Int, exp: Expense): View {
        // Inflate from XML
        val view = layoutInflater.inflate(R.layout.item_expense, expensesContainer, false)

        val tvName = view.findViewById<TextView>(R.id.tvExpenseName)
        val tvType = view.findViewById<TextView>(R.id.tvExpenseType)
        val tvAmount = view.findViewById<TextView>(R.id.tvExpenseAmount)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        tvName.text = exp.name
        tvType.text = exp.type
        tvAmount.text = when (exp.type) {
            "daily" -> "$${"%.2f".format(exp.amount)} × 30"
            "recurring" -> "$${"%.2f".format(exp.amount)} × ${exp.times}"
            else -> "$${"%.2f".format(exp.amount)}"
        }

        btnDelete.setOnClickListener {
            AppData.expenseList.removeAt(index)
            refreshExpensesUI()
        }

        return view
    }

    // ==== Budget ====
    /**
     * Calculates and updates the displayed budget information (monthly savings or deficit).
     * The budget is calculated as: Income - (EMI + Total Monthly Expenses).
     */
    private fun updateBudget() {
        val total = AppData.emi + AppData.totalExpensesMonthly()
        val remaining = AppData.income - total

        tvBudget.text = if (remaining >= 0) {
            "Monthly Savings: $${"%.2f".format(remaining)}"
        } else {
            "Monthly Deficit: -$${"%.2f".format(-remaining)}"
        }

    }
}
