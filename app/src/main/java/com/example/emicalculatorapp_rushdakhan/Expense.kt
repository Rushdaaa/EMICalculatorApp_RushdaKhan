package com.example.emicalculatorapp_rushdakhan

/**
 * Represents an expense with its name, amount, type, and frequency.
 *
 * @property name The name of the expense (e.g., "Rent", "Groceries").
 * @property amount The monetary value of the expense.
 * @property type The type of expense, indicating its recurrence pattern.
 *              Valid values are "monthly", "daily", or "recurring".
 * @property times Applicable only when `type` is "recurring". It specifies the number of times
 *                 the expense occurs within a month. Defaults to 1 if not specified.
 */
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
