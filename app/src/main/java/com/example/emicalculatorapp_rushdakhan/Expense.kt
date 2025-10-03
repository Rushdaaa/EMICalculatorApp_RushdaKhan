package com.example.emicalculatorapp_rushdakhan

/**
 * Represents a single expense item.
 *
 * This data class holds information about an expense, including its name, amount,
 * and how often it occurs.
 *
 * @property name The descriptive name of the expense (e.g., "Rent", "Groceries").
 * @property amount The monetary value of a single instance of the expense.
 * @property type The category of the expense, defining its frequency.
 *              Accepted values are "monthly", "daily", or "recurring".
 * @property times Specifies the number of times a "recurring" expense occurs per month.
 *                 This property is only used when `type` is "recurring". It defaults to 1.
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
