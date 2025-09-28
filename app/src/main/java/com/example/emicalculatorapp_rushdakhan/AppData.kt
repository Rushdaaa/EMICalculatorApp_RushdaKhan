package com.example.emicalculatorapp_rushdakhan

/**
 * A singleton object to store application-wide data.
 *
 * This object holds information about the user's Equated Monthly Installment (EMI),
 * their income, and a list of their expenses. It also provides a function to
 * calculate the total monthly expenses.
 */
object AppData {
    var emi: Double = 0.0
    var income: Double = 0.0
    val expenseList = mutableListOf<Expense>()

    fun totalExpensesMonthly(): Double = expenseList.sumOf { it.toMonthly() }
}
