💼 Personal Finance Manager – EMI Calculator App

This Android app helps users calculate mortgage EMI (Equated Monthly Installments), record their income, track expenses, and view their remaining savings or deficit.

It was developed as Assignment 1 for SOFE 4640U – Mobile Application Development (Fall 2025) under Dr. Nasim Beigi-Mohammadi.

✨ Features

Loan EMI Calculator

Enter loan amount, annual interest rate, and tenure.

Supports different payment frequencies (Monthly, Bi-Weekly, Weekly).

Reset clears all input fields.

Income Input & Management

Users can save their monthly income.

Saved values are displayed and editable.

Expense Tracking

Add expenses with categories: Monthly, Daily, Recurring.

Daily and recurring entries are converted into monthly equivalents.

Each expense is shown in its own “bubble” with a delete button.

Scroll support for long expense lists.

Budget Balance Calculation

Computes Monthly Savings (if income > EMI + expenses).

Computes Monthly Deficit (if EMI + expenses > income).

Navigation / Intents

Button-based navigation between screens using Android intents.

Example: Main screen → Add Expense screen → returns data back.

🛠 Tech Stack

Language: Kotlin

Framework: Android SDK with Material Design Components

Build System: Gradle

Persistence: In-memory (singleton AppData)

AppData stores EMI, income, and expenses list.

Expense model handles type conversion (monthly/daily/recurring).

📂 Project Structure

MainActivity.kt – EMI calculator, income/expenses UI, budget logic.

AddExpenseActivity.kt – Collects and saves new expenses.

Expense.kt – Data model with helper method toMonthly().

AppData.kt – Singleton to share EMI, income, and expense list.

res/layout/ – XML layouts (activity_main.xml, activity_add_expense.xml).

AndroidManifest.xml – Activity declarations.

🚀 How to Run

Clone this repository:

git clone https://github.com/<your-username>/EMICalculatorApp_RushdaKhan.git


Open the project in Android Studio.

Sync Gradle and build the project.

Run on an emulator or physical Android device.

🖼 Screenshots

📌 Paste screenshots here once available (main screen, add expense screen, budget balance view, etc.)

🔮 Future Improvements

💾 Persist data locally (e.g., Room database or SharedPreferences).

🎨 Improve UI for expense “bubbles” (use Material Cards/Chips).

📊 Add charts or summaries for spending categories.

📱 Dark mode toggle.

🌐 (Optional) Integrate with online loan/finance APIs.

📘 About EMI

EMI (Equated Monthly Installment) is a fixed payment made by a borrower to a lender each month. It includes both principal and interest. This app estimates EMI based on loan inputs and helps users plan their budget by combining EMI, income, and expenses.

📎 References

TD Mortgage Calculator

Android Studio / Jetpack documentation
