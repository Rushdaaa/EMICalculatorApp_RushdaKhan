package com.example.emicalculatorapp_rushdakhan

/**
 * A singleton object for managing application-wide data.
 *
 * This object serves as a centralized in-memory store for financial data used throughout the application.
 * It holds the user's Equated Monthly Installment (EMI), total income, and a list of their expenses.
 *
 * @property emi The calculated Equated Monthly Installment (EMI) amount.
 * @property income The user's total income.
 * @property expenseList A mutable list of [Expense] objects representing the user's expenses.
 */
object AppData {
    var emi: Double = 0.0
    var income: Double = 0.0
    val expenseList = mutableListOf<Expense>()

    fun totalExpensesMonthly(): Double = expenseList.sumOf { it.toMonthly() }
}
