package com.example.emicalculatorapp_rushdakhan

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import kotlin.math.pow
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Declare views
    private lateinit var etLoan: EditText
    private lateinit var etRate: EditText
    private lateinit var etTenure: EditText
    private lateinit var rgFrequency: RadioGroup
    private lateinit var btnCalculate: Button
    private lateinit var btnReset: Button
    private lateinit var tvEmiValue: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var topAppBar: MaterialToolbar

    // Store last EMI calculation so we can re-use when frequency changes
    private var lastEmiMonthly: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link XML to Kotlin
        etLoan = findViewById(R.id.etLoan)
        etRate = findViewById(R.id.etRate)
        etTenure = findViewById(R.id.etTenure)
        rgFrequency = findViewById(R.id.rgFrequency)
        btnCalculate = findViewById(R.id.btnCalculate)
        btnReset = findViewById(R.id.btnReset)
        tvEmiValue = findViewById(R.id.tvEmiValue)
        tabLayout = findViewById(R.id.tabLayout)
        topAppBar = findViewById(R.id.topAppBar)

        // Set up tabs
        tabLayout.addTab(tabLayout.newTab().setText("EMI Calculator"))
        tabLayout.addTab(tabLayout.newTab().setText("Income & Expenses"))
        tabLayout.addTab(tabLayout.newTab().setText("Budget Balance"))

        // Handle Calculate button
        btnCalculate.setOnClickListener {
            calculateEMI()
        }

        // Handle Reset button
        btnReset.setOnClickListener {
            resetFields()
        }

        // Auto-update EMI when payment frequency changes
        rgFrequency.setOnCheckedChangeListener { _, _ ->
            if (lastEmiMonthly != null) {
                updateEmiDisplay(lastEmiMonthly!!)
            }
        }
    }

    /**
     * Calculates the EMI based on loan, rate, tenure and frequency.
     */
    private fun calculateEMI() {
        val loanStr = etLoan.text.toString()
        val rateStr = etRate.text.toString()
        val tenureStr = etTenure.text.toString()

        if (loanStr.isEmpty() || rateStr.isEmpty() || tenureStr.isEmpty()) {
            tvEmiValue.text = "Please fill in all fields"
            return
        }

        val loan = loanStr.toDoubleOrNull()
        val annualRate = rateStr.toDoubleOrNull()
        val tenureYears = tenureStr.toIntOrNull()

        if (loan == null || loan <= 0) {
            tvEmiValue.text = "Enter a valid loan amount > 0"
            return
        }
        if (annualRate == null || annualRate <= 0) {
            tvEmiValue.text = "Enter a valid interest rate > 0"
            return
        }
        if (tenureYears == null || tenureYears <= 0) {
            tvEmiValue.text = "Enter a valid tenure > 0"
            return
        }

        // Convert interest rate to monthly
        val monthlyRate = annualRate / 12 / 100
        val tenureMonths = tenureYears * 12

        // EMI formula: [P * r * (1+r)^n] / [(1+r)^n - 1]
        val emiMonthly = (loan * monthlyRate * (1 + monthlyRate).pow(tenureMonths)) /
                ((1 + monthlyRate).pow(tenureMonths) - 1)

        lastEmiMonthly = emiMonthly

        // Display EMI based on current frequency
        updateEmiDisplay(emiMonthly)
    }

    /**
     * Updates EMI display depending on selected frequency.
     */
    private fun updateEmiDisplay(emiMonthly: Double) {
        val checkedId = rgFrequency.checkedRadioButtonId
        val (emi, frequencyText) = when (checkedId) {
            R.id.rbBiWeekly -> emiMonthly / 2 to "Bi-weekly"
            R.id.rbWeekly -> emiMonthly / 4 to "Weekly"
            else -> emiMonthly to "Monthly"
        }

        val emiFormatted = String.format(Locale.CANADA, "$%.2f (%s)", emi, frequencyText)
        tvEmiValue.text = emiFormatted
    }

    /**
     * Clears all input fields and resets the result.
     */
    private fun resetFields() {
        etLoan.text.clear()
        etRate.text.clear()
        etTenure.text.clear()
        rgFrequency.check(R.id.rbMonthly)
        tvEmiValue.text = "—"
        lastEmiMonthly = null
    }
}
