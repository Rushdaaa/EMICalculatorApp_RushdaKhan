package com.example.emicalculatorapp_rushdakhan

object AppData {
    var emi: Double = 0.0
    var income: Double = 0.0
    val expenseList = mutableListOf<Expense>()

    fun totalExpensesMonthly(): Double = expenseList.sumOf { it.toMonthly() }
}
