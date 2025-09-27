package com.example.emicalculatorapp_rushdakhan

data class Expense(
    val name: String,
    val amount: Double,
    val type: String, // "monthly", "daily", "recurring"
    val times: Int = 1
) {
    fun toMonthly(): Double = when (type) {
        "daily" -> amount * 30
        "recurring" -> amount * times
        else -> amount
    }
}
