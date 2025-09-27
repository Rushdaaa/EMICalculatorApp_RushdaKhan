# Personal Finance Manager – EMI Calculator App

This Android app helps users calculate mortgage EMI (Equated Monthly Installment), track their monthly income and expenses, and view their resulting savings or deficit.  

It was developed as **Assignment 1** for **SOFE 4640U – Mobile Application Development (Fall 2025)** under Dr. Nasim Beigi-Mohammadi.

---

## ✨ Features

- **EMI Calculator**
  - Input: Loan amount, annual interest rate, tenure (years).
  - Output: Monthly EMI value (with options for monthly, bi-weekly, and weekly views).
  - Reset option clears all fields.

- **Income Management**
  - Users can enter and save their monthly income.
  - Saved income is displayed and can be edited later.

- **Expense Tracking**
  - Users can add expenses (with types: monthly, daily, recurring).
  - Each expense is shown as a “bubble” with a delete option.
  - Supports scrolling through multiple expenses.

- **Budget Balance**
  - After subtracting EMI and expenses from monthly income, the app shows:
    - **Monthly Savings** (if positive).
    - **Monthly Deficit** (if negative).

- **Navigation / Intents**
  - Main activity launches sub-activities like **Add Expense**.
  - Add Expense Activity collects expense name, amount, and type, then returns it to the main screen.

---

## 🖼 User Interface

- Uses **Material Toolbar** for headers.
- Clear sectioning:  
  - EMI Calculator  
  - Monthly Income  
  - Monthly Expenses  
  - Budget Balance
- Scrollable views for long lists of expenses.

---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **Framework:** Android (Material Design components)  
- **Data Storage:** In-memory (via singleton `AppData` object).  
  - `AppData` keeps track of income, EMI, and expense list globally.  
  - `Expense` class converts daily/recurring to monthly equivalents.

---

## 📂 Project Structure

- `MainActivity.kt` – EMI, income, expenses, and budget balance logic.  
- `AddExpenseActivity.kt` – UI and logic to add a new expense.  
- `Expense.kt` – Data model for expenses.  
- `AppData.kt` – Singleton to persist income, EMI, and expenses.  
- `res/layout/` – XML layouts for main and add expense screens.  
- `AndroidManifest.xml` – Registers all activities.  

---

## 🚀 How to Run

1. Clone or download the repo.
2. Open the project in **Android Studio**.
3. Run on an emulator or physical device.
4. Use the **Main Screen** to:
   - Calculate EMI.
   - Enter monthly income.
   - Add expenses.
   - View savings/deficit under **Budget Balance**.

---

## 📎 References

- [TD Mortgage Calculator](https://ix0.apps.td.com/en/mortgage-payment-calculator)  
- Android Studio / Jetpack documentation.

---
