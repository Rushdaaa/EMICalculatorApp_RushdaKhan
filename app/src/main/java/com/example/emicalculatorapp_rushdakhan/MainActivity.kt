package com.example.emicalculatorapp_rushdakhan

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Setup the Toolbar (AppBar) ---
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_title) // force title to show

        // --- Setup Tab Navigation ---
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Add the three main tabs
        tabLayout.addTab(tabLayout.newTab().setText("EMI Calculator"))
        tabLayout.addTab(tabLayout.newTab().setText("Expenses"))
        tabLayout.addTab(tabLayout.newTab().setText("Summary"))

        // Handle tab clicks to switch screens
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { /* Already on EMI Calculator */ }
                    1 -> startActivity(Intent(this@MainActivity, IncomeExpenseActivity::class.java))
                    2 -> startActivity(Intent(this@MainActivity, BudgetBalanceActivity::class.java))
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // --- Connect EMI input fields ---
        val etLoan: EditText = findViewById(R.id.etLoan)
        val etRate: EditText = findViewById(R.id.etRate)
        val etTenure: EditText = findViewById(R.id.etTenure)
        val rgFrequency: RadioGroup = findViewById(R.id.rgFrequency)
        val btnCalculate: Button = findViewById(R.id.btnCalculate)
        val btnReset: Button = findViewById(R.id.btnReset)
        val tvEmiValue: TextView = findViewById(R.id.tvEmiValue)

        // --- Calculate EMI functionality ---
        btnCalculate.setOnClickListener {
            try {
                val loan = etLoan.text.toString().toDouble()
                val rate = etRate.text.toString().toDouble() / 100 / 12 // monthly rate
                val tenureMonths = etTenure.text.toString().toInt() * 12 // years → months

                // EMI formula
                val emi = (loan * rate * (1 + rate).pow(tenureMonths)) /
                        ((1 + rate).pow(tenureMonths) - 1)

                // Adjust EMI for frequency
                val frequencyId = rgFrequency.checkedRadioButtonId
                val finalEmi = when (frequencyId) {
                    R.id.rbMonthly -> emi
                    R.id.rbBiWeekly -> emi * 12 / 26
                    R.id.rbWeekly -> emi * 12 / 52
                    else -> emi
                }

                // Format result in CAD ($)
                val emiFormatted =
                    NumberFormat.getCurrencyInstance(Locale.CANADA).format(finalEmi)
                tvEmiValue.text = emiFormatted

            } catch (e: Exception) {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Reset functionality ---
        btnReset.setOnClickListener {
            etLoan.text.clear()
            etRate.text.clear()
            etTenure.text.clear()
            rgFrequency.check(R.id.rbMonthly) // reset to monthly
            tvEmiValue.text = "—"
        }
    }
}
